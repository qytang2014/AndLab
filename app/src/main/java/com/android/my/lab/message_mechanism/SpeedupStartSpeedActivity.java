package com.android.my.lab.message_mechanism;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.my.lab.R;

public class SpeedupStartSpeedActivity extends AppCompatActivity {
    private static final String TAG = "Jenwis:" + SpeedupStartSpeedActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedup_start_speed);
        findView();
        setView();
        setViewListener();
    }

    private void findView() {
    }

    private void setView() {

    }

    private void setViewListener() {
        findViewById(R.id.btn_start_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(TempForSpeedupTestActivity.class, false);
            }
        });
        findViewById(R.id.btn_start_speed_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(TempForSpeedupTestActivity.class, true);
            }
        });
    }

    private void gotoXXXTest(Class c, boolean speedUp) {
        Intent intent = new Intent(this, c);
        intent.putExtra(TempForSpeedupTestActivity.KEY_INTENT_EXTRA_SPEED_UP, speedUp);
        startActivity(intent);
    }
}
