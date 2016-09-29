package com.android.my.lab.memory_leak;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by 郑裕集 on 2016/9/28.
 * <p>
 * 单例中造成的内存泄漏
 */

public class SingleInstance {
    private static SingleInstance sInstance;

    private Context context;

    private WeakReference<Activity> activityWeakReference;

    private SingleInstance(Context context) {
        this.context = context;
    }


    /*
    * 如果Context不保存在成员变量中的话则不会泄漏
    * */
    /*public static SingleInstance getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SingleInstance(context);
        }

        return sInstance;
    }*/


    private SingleInstance(Activity activity) {
        activityWeakReference = new WeakReference<Activity>(activity);
    }

    public static SingleInstance getInstance(Activity activity) {
        if (sInstance == null) {
            sInstance = new SingleInstance(activity);
        }

        return sInstance;
    }

    private void useActivityToDoSomething() {
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            //...
        }
    }
}
