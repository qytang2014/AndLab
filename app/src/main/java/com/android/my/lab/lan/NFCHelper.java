package com.android.my.lab.lan;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NFCHelper {
    private static final String TAG = "NFCHelper";
    private static final int SERVER_PORT = 9527;
    private static final String MSG_NFC_IDENTIFY_REQUEST = "XP://NFC/IDENTIFY/%s";
    private static final String MSG_NFC_IDENTIFY_SUCCESS = "XP://NFC/IDENTIFY/SUCCESS";
    private static final String MSG_NFC_IDENTIFY_FAIL = "XP://NFC/IDENTIFY/FAIL";

    private String locAddress;//存储本机ip，例：本地ip ：192.168.1.
    private Runtime run = Runtime.getRuntime();//获取当前运行环境，来执行ping，相当于windows的cmd
    private Process proc = null;
    private String ping = "ping -c 1 -w 0.5 ";//其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
    private int mLastAddrOfIp;//存放ip最后一位地址 0-255
    private Context mContext;
    private Socket mClientSocket;
    private String mIdentifyFlag;
    private OnReceiveListener mOnReceiveListener;
    private OnConnectListener mOnConnectListener;

    public NFCHelper(Context ctx) {
        this.mContext = ctx;
    }

    public void connectNFCServer(String id) {
        mIdentifyFlag = String.format(MSG_NFC_IDENTIFY_REQUEST, id);
        final String serverIp = SharedPreferenceHelper.getInstance(mContext).getNFCServerIp();
        Log.d(TAG, "serverIp-->" + serverIp);
        if (TextUtils.isEmpty(serverIp)) {
            scan();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    identifyAndReadMsgFromServer(true, serverIp, mIdentifyFlag);
                }
            }).start();
        }
    }

    /**
     * 扫描局域网内ip，找到对应服务器
     */
    private void scan() {
        locAddress = getLocAddrIndex();//获取本地ip前缀
        if (locAddress.equals("")) {
            Toast.makeText(mContext, "扫描失败，请检查wifi网络", Toast.LENGTH_LONG).show();
            return;
        }

        for (int i = 0; i < 256; i++) {//创建256个线程分别去ping
            mLastAddrOfIp = i;
            new Thread(new Runnable() {
                public void run() {
                    String p = NFCHelper.this.ping + locAddress + NFCHelper.this.mLastAddrOfIp;
                    String currentIp = locAddress + NFCHelper.this.mLastAddrOfIp;
                    try {
                        proc = run.exec(p);
                        int result = proc.waitFor();
                        if (result == 0) {
                            Log.d(TAG, "Ping成功：" + currentIp);
                            // 向服务器发送验证信息
                            identifyAndReadMsgFromServer(false, currentIp, mIdentifyFlag);
                        } else {
                            Log.d(TAG, "Ping失败：" + currentIp);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Log.d(TAG, "Ping异常" + e1.getMessage());
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                        Log.d(TAG, "Ping异常" + e2.getMessage());
                    } finally {
                        proc.destroy();
                    }
                }
            }).start();

        }

    }

    //向serversocket发送消息
    private void identifyAndReadMsgFromServer(boolean isRestore, String ip, String msg) {
        Socket socket = null;
        try {
            socket = new Socket(ip, SERVER_PORT);
            //向服务器发送消息
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            os.println(msg);
            os.flush();// 刷新输出流，使Server马上收到该字符串
            Log.d(TAG, "identifyAndReadMsgFromServer to server(ip:" + ip + ")" + " msg-->" + msg);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                Log.d(TAG, "Reading from Server");
                String msgFromServer = in.readLine();
                Log.d(TAG, "msgFromServer-->" + msgFromServer);
                if (!TextUtils.isEmpty(msgFromServer)) {
                    if (msgFromServer.equals(MSG_NFC_IDENTIFY_SUCCESS)) {
                        mClientSocket = socket;
                        SharedPreferenceHelper.getInstance(mContext).setNFCServerIp(ip);
                        if (mOnConnectListener != null) {
                            mOnConnectListener.onConnect();
                        }
                    } else if (msgFromServer.equals(MSG_NFC_IDENTIFY_FAIL)) {
                        break;
                    } else {
                        if (mOnReceiveListener != null) {
                            mOnReceiveListener.onReceive(msgFromServer);
                        }
                    }
                }

            }
            Log.d(TAG, "Client Thread Over!");
            //socket.close();
        } catch (UnknownHostException unknownHost) {
            Log.d(TAG, "UnknownHostException Cause-->"
                    + (unknownHost.getCause() != null ? unknownHost.getCause() : "NULL")
                    + "； Message:" + unknownHost.getMessage() + "; LocalizedMessage:" + unknownHost.getLocalizedMessage());
            //使用上次保留的ip连接服务器失败，清空保存，并从新scan
            if (isRestore) {
                SharedPreferenceHelper.getInstance(mContext).setNFCServerIp(null);
                scan();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException Cause-->"
                    + (e.getCause() != null ? e.getCause() : "NULL")
                    + "； Message:" + e.getMessage() + "; LocalizedMessage:" + e.getLocalizedMessage());
            Log.d(TAG, "Client Thread Over!");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void sendMsg2Server(String msg) {
        if (mClientSocket != null) {
            PrintWriter os = null;
            try {
                os = new PrintWriter(mClientSocket.getOutputStream());
                os.println(msg);
                os.flush();// 刷新输出流，使Server马上收到该字符串
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "ClientSocket Send msg Exception e-->" + e.getMessage());
            }
        }
    }

    public void closeNFC() {
        try {
            if (mClientSocket != null) {
                mClientSocket.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //获取本地ip地址
    public static String getDeviceIp() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    //获取IP前缀
    public String getLocAddrIndex() {
        String str = getDeviceIp();
        if (!str.equals("")) {
            return str.substring(0, str.lastIndexOf(".") + 1);
        }

        return null;
    }

    //获取本机设备名称
    public String getLocDeviceName() {
        return android.os.Build.MODEL;
    }

    public OnReceiveListener getOnReceiveListener() {
        return mOnReceiveListener;
    }

    public void setOnReceiveListener(OnReceiveListener onReceiveListener) {
        mOnReceiveListener = onReceiveListener;
    }

    public OnConnectListener getOnConnectListener() {
        return mOnConnectListener;
    }

    public void setOnConnectListener(OnConnectListener onConnectListener) {
        mOnConnectListener = onConnectListener;
    }

    public interface OnConnectListener {
        void onConnect();
    }

    public interface OnReceiveListener {
        void onReceive(String msg);
    }

}
