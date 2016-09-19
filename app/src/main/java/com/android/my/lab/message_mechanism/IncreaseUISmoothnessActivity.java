package com.android.my.lab.message_mechanism;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.my.lab.R;

/*
* 运用消息机制提高ui流畅度
* */
public class IncreaseUISmoothnessActivity extends AppCompatActivity {
    private static final String TAG = "Jenwis";
    private static final int AMOUNT_WIDGET_TO_ADD = 2000;
    private static final int MSG_WHAT_ADD_WIDGET = 1;

    private ViewGroup mViewContainer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_ADD_WIDGET:
                    Button button = new Button(IncreaseUISmoothnessActivity.this);
                    button.setText(msg.obj + "");
                    mViewContainer.addView(button);

                    if (!mQuitAddWidgetByHandler && (Integer) msg.obj < AMOUNT_WIDGET_TO_ADD) {
                        Message addWidgetMsg = Message.obtain();
                        addWidgetMsg.what = MSG_WHAT_ADD_WIDGET;
                        addWidgetMsg.obj = ((Integer) msg.obj + 1);
                        mHandler.sendMessage(addWidgetMsg);
                    }
                    break;
            }

        }
    };
    private boolean mQuitAddWidgetDirectly;
    private boolean mQuitAddWidgetByHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increase_ui_smoothness);

        findView();
        setView();
        setViewListener();

    }

    private void findView() {
        mViewContainer = (ViewGroup) findViewById(R.id.view_container);
    }

    private void setView() {

    }

    private void setViewListener() {
        findViewById(R.id.btn_add_directly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuitAddWidgetByHandler = true;
                mQuitAddWidgetDirectly = false;
                mViewContainer.removeAllViews();
                addWidgetDirectly();
            }
        });
        findViewById(R.id.btn_add_by_handler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuitAddWidgetDirectly = true;
                mQuitAddWidgetByHandler = false;
                mViewContainer.removeAllViews();
                addWidgetByHandler();
            }
        });
    }

    private void addWidgetDirectly() {
        for (int i = 0; i < AMOUNT_WIDGET_TO_ADD; i++) {
            if (!mQuitAddWidgetDirectly) {
                Button button = new Button(this);
                button.setText((i + 1) + "");
                mViewContainer.addView(button);
            } else {
                break;
            }
        }
    }

    /*
    * 不能在for循环中mHandler连续send msg，因为“for循环连续send msg”虽是一个msg事件，但是for循环很耗时
    * */
    private void addWidgetByHandler() {
        if (mHandler.hasMessages(MSG_WHAT_ADD_WIDGET)) {
            mHandler.removeMessages(MSG_WHAT_ADD_WIDGET);
        }
        if (!mQuitAddWidgetByHandler) {
            Message msg = Message.obtain();
            msg.what = MSG_WHAT_ADD_WIDGET;
            msg.obj = 1;
            mHandler.sendMessage(msg);
        }
    }
}
