package com.android.my.lab.lan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.my.lab.R;

/*
* 局域网测试：获取局域网中设备的ip地址、mac地址等
* */
public class LANTestActivity extends AppCompatActivity implements NFCHelper.OnConnectListener,
        NFCHelper.OnReceiveListener, NFCHelper.OnFileTransferReadyListener {
    private static final String TAG = "LANTestActivity";

    private ViewGroup mViewContainer;
    private Button mBtnDiscoverIp;
    private TextView mTvState;
    private EditText mEtMsg;
    private Button mBtnSend;
    private Button mBtnClose;
    private Button mBtnTransferFile;

    private NFCHelper mNFCHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_test);

        mNFCHelper = new NFCHelper(getApplication());
        findView();
        setViewListener();
    }

    private void findView() {
        mViewContainer = (ViewGroup) findViewById(R.id.view_content_container);
        mBtnDiscoverIp = (Button) findViewById(R.id.btn_discover_ip);
        mTvState = (TextView) findViewById(R.id.tv_state);
        mEtMsg = (EditText) findViewById(R.id.et_msg);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnClose = (Button) findViewById(R.id.btn_close);
        mBtnTransferFile = (Button) findViewById(R.id.btn_transfer_file);
    }

    private void setViewListener() {
        mBtnDiscoverIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNFCHelper.connectNFCServer("10019");
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNFCHelper.sendMsg2Server(mEtMsg.getText().toString());
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNFCHelper.closeNFC();
            }
        });

        mBtnTransferFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNFCHelper.requestFileTransfer();
            }
        });

        mNFCHelper.setOnConnectListener(this);
        mNFCHelper.setOnReceiveListener(this);
        mNFCHelper.setOnFileTransferReadyListener(this);
    }

    @Override
    public void onConnect() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvState.setText("已连接到服务器");
            }
        });
    }

    @Override
    public void onReceive(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvState.setText(msg + "");
            }
        });
    }

    @Override
    public void onFileTransferReady() {
        Log.d(TAG, "onFileTransferReady");
        mNFCHelper.transferFile("/mnt/sdcard/Music/my_music");
    }
}
