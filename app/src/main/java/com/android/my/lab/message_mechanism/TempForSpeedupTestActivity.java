package com.android.my.lab.message_mechanism;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.my.lab.R;

public class TempForSpeedupTestActivity extends AppCompatActivity {
    public static final String KEY_INTENT_EXTRA_SPEED_UP = "is_start_speed_up";

    private long mOnCreateTime;

    private TextView mTvTimeToken;
    private ViewGroup mViewContainer;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mOnCreateTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_for_speedup_test);
        findView();

        Intent intent = getIntent();
        if (intent.getBooleanExtra(KEY_INTENT_EXTRA_SPEED_UP, false)) {
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setView();
                        }
                    });
                }
            });
        } else {
            setView();
        }


        setViewListener();
    }

    private void findView() {
        mTvTimeToken = (TextView) findViewById(R.id.tv_time_token);
        mViewContainer = (ViewGroup) findViewById(R.id.view_container);
    }

    private void setView() {

        for (int i = 1; i <= 1000; i++) {
            Button btn = new Button(this);
            btn.setText(i + "");
            mViewContainer.addView(btn);
        }

    }

    private void setViewListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvTimeToken.setText("Time taken between onCreate and onResume:" + (System.currentTimeMillis() - mOnCreateTime));
    }
}
