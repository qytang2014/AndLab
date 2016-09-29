package com.android.my.lab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.my.lab.memory_leak.MemoryLeakIndexActivity;

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
    }

    private void gotoXXXTest(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
