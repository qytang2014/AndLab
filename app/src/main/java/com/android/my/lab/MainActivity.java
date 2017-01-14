package com.android.my.lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.my.lab.active_android.ActiveAndroidTestActivity;
import com.android.my.lab.lan.LANTestActivity;
import com.android.my.lab.lan_live.LanLiveTestActivity;
import com.android.my.lab.memory_leak.MemoryLeakIndexActivity;
import com.android.my.lab.wifip2p.WifiP2pTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setView();
        setViewListener();
    }

    private void findView() {

    }

    private void setView() {

    }

    private void setViewListener() {
        findViewById(R.id.btn_goto_message_mechanism).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(com.android.my.lab.message_mechanism.IndexActivity.class);
            }
        });

        findViewById(R.id.btn_goto_memory_leak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(MemoryLeakIndexActivity.class);
            }
        });

        findViewById(R.id.btn_goto_active_android_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(ActiveAndroidTestActivity.class);
            }
        });

        findViewById(R.id.btn_goto_wifip2p_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(WifiP2pTestActivity.class);
            }
        });

        findViewById(R.id.btn_goto_lan_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(LANTestActivity.class);
            }
        });

        findViewById(R.id.btn_goto_lan_live_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(LanLiveTestActivity.class);
            }
        });
    }

    private void gotoXXXTest(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
