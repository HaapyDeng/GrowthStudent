package com.mpl.GrowthStud.Student.Tools;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

public class DownImgeJson {
    public String image_path;

    public DownImgeJson(String image_path) {
        this.image_path = image_path;
    }

    public void loadImage(final ImageCallBack callBack) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Drawable drawable = (Drawable) msg.obj;
                callBack.getDrawable(drawable);
            }

        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Log.d("imgePath==>>>", image_path);
                    JSONObject object = new JSONObject(image_path);
                    String path = object.getString("image");
                    Drawable drawable = Drawable.createFromStream(new URL(
                            path).openStream(), "");

                    Message message = Message.obtain();
                    message.obj = drawable;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface ImageCallBack {
        public void getDrawable(Drawable drawable);
    }
}
