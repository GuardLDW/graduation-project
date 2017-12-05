package com.bjut.cyl.kfyrip.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Che on 15/9/14.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;

    public static MyApplication getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

}