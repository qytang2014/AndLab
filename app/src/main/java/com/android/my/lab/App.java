package com.android.my.lab;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by 郑裕集 on 16/9/20.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        //ActiveAndroid.initialize(this);
    }
}
