package com.android.my.lab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 郑裕集 on 2016/9/28.
 */

public abstract class AndLabBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        setView();
        setViewListener();
    }

    public abstract void findView();

    public abstract void setView();

    public abstract void setViewListener();
}
