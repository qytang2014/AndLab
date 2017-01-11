package com.android.my.lab.lan;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * 这个类主要是将需要存放在软件中的所有设置信息集中起来统一的管理，便于以后的维护；
 */
public class SharedPreferenceHelper {
    private Context mContext;
    private static SharedPreferenceHelper sInstance;
    private static final String SHARED_NFC_SERVER_IP = "nfc_server_ip";

    private SharedPreferenceHelper(Context context) {
        mContext = context;
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceHelper(context);
        }
        return sInstance;
    }


    public void setNFCServerIp(String ip) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(SHARED_NFC_SERVER_IP, ip).commit();
    }

    public String getNFCServerIp() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(SHARED_NFC_SERVER_IP, null);
    }


}

