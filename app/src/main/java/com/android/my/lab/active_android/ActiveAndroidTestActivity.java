package com.android.my.lab.active_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.android.my.lab.R;

import java.util.List;

public class ActiveAndroidTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_android_test);

        doTest();
    }

    private void doTest() {
        findViewById(R.id.btn_modify_table_struct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ActiveAndroidBean> beanList = new Select().from(ActiveAndroidBean2.class).where("atrr2 = ?", 1).execute();

                Toast.makeText(getApplicationContext(), "beanList=" + (beanList != null ? beanList.size() : "null"), Toast.LENGTH_LONG).show();
            }
        });
    }
}
