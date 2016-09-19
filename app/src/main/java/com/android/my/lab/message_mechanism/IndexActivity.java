package com.android.my.lab.message_mechanism;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.my.lab.R;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_mechanism_index);

        findView();
        setView();
        setViewListener();
    }

    private void findView() {

    }

    private void setView() {

    }

    private void setViewListener() {
        findViewById(R.id.btn_goto_ui_smoothness_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(IncreaseUISmoothnessActivity.class);
            }
        });
        findViewById(R.id.btn_change_callback_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(ChangeCallbackOrderActivity.class);
            }
        });
        findViewById(R.id.btn_speed_up_start_speed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(SpeedupStartSpeedActivity.class);
            }
        });
    }

    private void gotoXXXTest(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
