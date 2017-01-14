package com.android.my.lab.lan_live;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.my.lab.R;

public class LanLiveTestActivity extends AppCompatActivity {
    private Button mBtnServer;
    private Button mBtnLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_live_test);

        findView();
        setViewListener();
    }

    private void findView() {
        mBtnServer = (Button) findViewById(R.id.btn_server);
        mBtnLive = (Button) findViewById(R.id.btn_client);
    }

    private void setViewListener() {
        mBtnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(ServerActivity.class);
            }
        });

        mBtnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoXXXTest(ClientActivity.class);
            }
        });
    }

    private void gotoXXXTest(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}
