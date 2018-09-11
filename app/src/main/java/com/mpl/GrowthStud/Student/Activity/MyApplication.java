package com.mpl.GrowthStud.Student.Activity;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        // the following line is important
    }
}
