package com.android.my.lab.message_mechanism;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.my.lab.R;

import java.util.List;

public class ChangeCallbackOrderActivity extends AppCompatActivity {
    private static final String TAG = "Jenwis:" + ChangeCallbackOrderActivity.class.getSimpleName();

    private TextView mTvCallbackOrderNormal;
    private TextView mTvCallbackOrderChanged;
    private TextView mTvTopActivityNormal;
    private TextView mTvTopActivityChanged;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_callback_order);

        findView();
        setView();
        setViewListener();

        mTvCallbackOrderNormal.setText("正常调用顺序:\n" + "onCreate");
        mTvCallbackOrderChanged.setText("让onStart在onResume后调用:\n" + "onCreate");
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
        mTvCallbackOrderNormal.setText(mTvCallbackOrderNormal.getText().toString() + "-->" + "onRestart");
        mTvCallbackOrderChanged.setText(mTvCallbackOrderChanged.getText().toString() + "-->" + "onRestart");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        mTvCallbackOrderNormal.setText(mTvCallbackOrderNormal.getText().toString() + "-->" + "onStart");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mTvCallbackOrderChanged.setText(mTvCallbackOrderChanged.getText().toString() + "-->" + "onStart");
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        mTvCallbackOrderNormal.setText(mTvCallbackOrderNormal.getText().toString() + "-->" + "onPause");
        mTvCallbackOrderChanged.setText(mTvCallbackOrderChanged.getText().toString() + "-->" + "onPause");
        mTvTopActivityNormal.setText(getString(R.string.current_activity_normal) + "（当前界面的Activity为：ChangeCallbackOrderActivity）\n" + topActivity());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onPause changed");
                mTvTopActivityChanged.setText(getString(R.string.current_activity_changed) + "（启动的Activity为：TempForCallbackOrderTestActivity）\n" + topActivity());
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        mTvCallbackOrderNormal.setText(mTvCallbackOrderNormal.getText().toString() + "-->" + "onResume");
        mTvCallbackOrderChanged.setText(mTvCallbackOrderChanged.getText().toString() + "-->" + "onResume");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        mTvCallbackOrderNormal.setText(mTvCallbackOrderNormal.getText().toString() + "-->" + "onStop");
        mTvCallbackOrderChanged.setText(mTvCallbackOrderChanged.getText().toString() + "-->" + "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mTvCallbackOrderNormal.setText(mTvCallbackOrderNormal.getText().toString() + "-->" + "onDestroy");
        mTvCallbackOrderChanged.setText(mTvCallbackOrderChanged.getText().toString() + "-->" + "onDestroy");
    }

    private void findView() {
        mTvCallbackOrderNormal = (TextView) findViewById(R.id.tv_callback_order_normal);
        mTvCallbackOrderChanged = (TextView) findViewById(R.id.tv_callback_order_changed);
        mTvTopActivityNormal = (TextView) findViewById(R.id.tv_current_activity_normal);
        mTvTopActivityChanged = (TextView) findViewById(R.id.tv_current_activity_changed);
    }

    private void setView() {

    }

    private void setViewListener() {
        findViewById(R.id.btn_new_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(TempForCallbackOrderTestActivity.class);
            }
        });
    }

    private void gotoXXXTest(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private String topActivity() {
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = am.getRunningTasks(1);
        if (runningTaskInfoList != null && runningTaskInfoList.size() > 0) {
            ComponentName cn = runningTaskInfoList.get(0).topActivity;
            return cn.getClassName();
        }

        return "";
    }
}
