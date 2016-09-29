package com.android.my.lab.memory_leak;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.my.lab.AndLabBaseActivity;
import com.android.my.lab.R;

import java.util.ArrayList;

public class MemoryLeakIndexActivity extends AndLabBaseActivity {

    private ViewGroup mViewContainer;
    private ImageView mIvBmp;
    private Bitmap bmp;

    //集合中的对象要及时remove
    //private ArrayList<Entity> mEntityList = new ArrayList();

    //静态的对象会常驻内存,生命周期跟整个进程一直,会导致集合的泄漏
    private static ArrayList<Entity> mEntityList = new ArrayList();

    //private NoStaticInnerClass mNoStaticInnerClass;

    //非静态内部类实例,如果设置为static,则长期引用外部类造成泄漏
    //private static NoStaticInnerClass mNoStaticInnerClass;

    //Handler、Runnable两个都必须用静态内部类,不能用匿名内部类
    static class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

        }
    }


    static class MyRunnable implements Runnable {

        @Override
        public void run() {

        }
    }

    /*
    * 内存泄漏
    * */
    /*private MainHandler mMainHandler = new MainHandler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_memory_leak_index);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findView() {
        mViewContainer = (ViewGroup) findViewById(R.id.view_container);
        mIvBmp = (ImageView) findViewById(R.id.iv_bmp);
    }

    @Override
    public void setView() {
        setBmp();
    }

    @Override
    public void setViewListener() {
        findViewById(R.id.btn_entity_in_collection_leak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEntityList.add(new Entity());
                Toast.makeText(MemoryLeakIndexActivity.this, "add entity", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_single_instance_leak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SingleInstance.getInstance(getApplicationContext());
                //SingleInstance.getInstance(MemoryLeakIndexActivity.this);
                Toast.makeText(MemoryLeakIndexActivity.this, "get SingleInstance", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_handler_leak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Runnable(),匿名内部类,注意内存泄漏
                new MainHandler().postDelayed(new MyRunnable(), 1000 * 60 * 10);
                Toast.makeText(MemoryLeakIndexActivity.this, "Handler", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_no_static_inner_class_leak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mNoStaticInnerClass = new NoStaticInnerClass();
                Toast.makeText(MemoryLeakIndexActivity.this, "new NoStaticInnerClass", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_bitmap_leak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewContainer.removeView(mIvBmp);
                bmp.recycle();
                bmp = null;
                Toast.makeText(MemoryLeakIndexActivity.this, "Bitmap", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    * 真正回收Bitmap的方式
    * */
    private void setBmp() {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.leak_canary_icon);
        mIvBmp.setImageBitmap(bmp);
    }

    /*
    * 非静态内部类,默认会持有对外部类的引用;把它设置为static或者独立成一个外部类,则不会引用外部类
    * */
    class NoStaticInnerClass {

    }
}
