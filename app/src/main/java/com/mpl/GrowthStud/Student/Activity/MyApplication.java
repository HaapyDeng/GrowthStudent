package com.mpl.GrowthStud.Student.Activity;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.luck.picture.lib.tools.Constant;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

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
        /**
         * picasso设置图片缓存路径和配置
         */
        File file = new File(getApplicationContext().getExternalCacheDir().getPath());
        if (!file.exists()) {
            file.mkdirs();
        }

        long maxSize = Runtime.getRuntime().maxMemory() / 8;//设置图片缓存大小为运行时缓存的八分之一
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(new Cache(file, maxSize))
                .build();

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))//注意此处替换为 OkHttp3Downloader
                .build();


    }
}
