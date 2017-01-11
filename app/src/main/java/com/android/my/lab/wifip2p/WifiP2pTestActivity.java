package com.android.my.lab.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.android.my.lab.R;
import com.android.my.lab.active_android.ActiveAndroidBean;
import com.android.my.lab.active_android.ActiveAndroidBean2;

import java.util.Collection;
import java.util.List;

public class WifiP2pTestActivity extends AppCompatActivity {
    private static final String TAG = "WifiP2pTestActivity";

    private ViewGroup mViewContainer;
    private Button mBtnDiscoverPeers;

    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mWiFiP2PBdReceiver;
    private IntentFilter mWifiP2pIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2p_test);

        findView();
        setViewListener();

        initWifiP2P();
        registerReceiver(mWiFiP2PBdReceiver, mWifiP2pIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mWiFiP2PBdReceiver);
    }

    private void findView() {
        mViewContainer = (ViewGroup) findViewById(R.id.view_content_container);
        mBtnDiscoverPeers = (Button) findViewById(R.id.btn_discover_peers);
    }

    private void setViewListener() {
        mBtnDiscoverPeers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnDiscoverPeers.setText(R.string.text_discovering_peers);
                mBtnDiscoverPeers.setEnabled(false);

                if (mWifiP2pManager != null) {
                    AsyncTask discoverPeersTask = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] params) {
                            Log.d(TAG, "Start to discoverPeers!");
                            mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                                @Override
                                public void onFailure(int reason) {
                                    Log.d(TAG, "discoverPeers fail!");
                                }

                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "discoverPeers success!");
                                }
                            });

                            return null;
                        }
                    };
                    discoverPeersTask.execute();
                } else {
                    Log.d(TAG, "Can not start to discoverPeers because mWifiP2pManager is null!");
                }
            }
        });
    }

    private void initWifiP2P() {
        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mChannel = mWifiP2pManager.initialize(this, getMainLooper(), null);
        } else {
            Log.d(TAG, "Wifi-Direct is only supported above sdk14(Android4.0)");
        }
        mWiFiP2PBdReceiver = new WiFiDirectBroadcastReceiver(mWifiP2pManager, mChannel);

        mWifiP2pIntentFilter = new IntentFilter();
        mWifiP2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mWifiP2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mWifiP2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mWifiP2pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    /**
     * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
     */
    public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

        private WifiP2pManager wifiP2pManager;
        private WifiP2pManager.Channel channel;

        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel) {
            super();
            this.wifiP2pManager = manager;
            this.channel = channel;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                //回调此Action后，搜索peers也停止了
                Log.d(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
                if (wifiP2pManager != null) {
                    wifiP2pManager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            mBtnDiscoverPeers.setText(R.string.discover_peers);
                            mBtnDiscoverPeers.setEnabled(true);

                            Collection<WifiP2pDevice> deviceList = peers.getDeviceList();
                            Log.d(TAG, "deviceList size=" + (deviceList != null ? deviceList.size() : 0));
                            for (final WifiP2pDevice device : deviceList) {
                                Log.d(TAG, device.deviceName + ":" + device.deviceAddress);
                                int childCount = mViewContainer.getChildCount();
                                if (childCount > 1) {
                                    for (int i = 1; i < childCount; i++) {
                                        mViewContainer.removeViewAt(i);
                                    }
                                }
                                Button deviceBtn = new Button(getApplicationContext());
                                deviceBtn.setText(device.deviceName + ":" + device.deviceAddress);
                                mViewContainer.addView(deviceBtn);

                                deviceBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        WifiP2pConfig config = new WifiP2pConfig();
                                        config.deviceAddress = device.deviceAddress;
                                        //这行代码很关键，能够消除连接确认对话框
                                        config.wps.setup = WpsInfo.PBC;
                                        wifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                                            @Override
                                            public void onSuccess() {
                                                Log.d(TAG, "connect " + device.deviceAddress  + " success!");
                                            }

                                            @Override
                                            public void onFailure(int reason) {
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("Connection to " + device.deviceName + " failed: ");
                                                switch (reason){
                                                    case WifiP2pManager.P2P_UNSUPPORTED:
                                                        sb.append("P2P Unsupported");
                                                        break;
                                                    case WifiP2pManager.BUSY:
                                                        sb.append("Framework is busy");
                                                        break;
                                                    case WifiP2pManager.ERROR:
                                                        sb.append("Internal error");
                                                        break;
                                                    default:
                                                        sb.append("Unknown error");
                                                        break;
                                                }
                                                Log.d(TAG, sb.toString());
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    });
                }

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
                Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
                Log.d(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
            }
        }
    }
}
