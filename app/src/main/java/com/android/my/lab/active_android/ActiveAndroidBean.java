package com.android.my.lab.active_android;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by 郑裕集 on 2016/11/4.
 */
@Table(name = "t_test_new_")
public class ActiveAndroidBean extends Model {

    @Column(name = "log_type_new")
    private int logType;

    @Column(name = "atrr1")
    private int atrr1;

    @Column(name = "atrr2")
    private int atrr2;

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public int getAtrr1() {
        return atrr1;
    }

    public void setAtrr1(int atrr1) {
        this.atrr1 = atrr1;
    }

    public int getAtrr2() {
        return atrr2;
    }

    public void setAtrr2(int atrr2) {
        this.atrr2 = atrr2;
    }
}
