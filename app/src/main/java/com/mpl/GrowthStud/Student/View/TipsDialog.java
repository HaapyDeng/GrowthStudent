package com.mpl.GrowthStud.Student.View;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;


public class TipsDialog extends Dialog {

    private static final String TAG = "LoadingDialog";

    private String mMessage;
    private int mImageId;
    private boolean mCancelable;
    private RotateAnimation mRotateAnimation;

    public TipsDialog(@NonNull Context context, String message) {
        this(context, R.style.TipsDialog, message, true);
    }

    public TipsDialog(@NonNull Context context, int themeResId, String message, boolean cancelable) {
        super(context, themeResId);
        mMessage = message;
        mCancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_tips);
        // 设置窗口大小
        WindowManager windowManager = getWindow().getWindowManager();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 1f;
//        attributes.width = screenWidth;
//        attributes.height = attributes.width;
        getWindow().setAttributes(attributes);
        setCancelable(mCancelable);

        TextView tv_content = findViewById(R.id.tv_content);
        ImageButton ib_exit = findViewById(R.id.ib_exit);
        RelativeLayout rl = findViewById(R.id.rl);
        tv_content.setText(mMessage);
        ib_exit.setImageResource(mImageId);
        ib_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
//        rl.measure(0, 0);
//        mRotateAnimation = new RotateAnimation(0, 360, rl.getMeasuredWidth() / 2, rl.getMeasuredHeight() / 2);
//        mRotateAnimation.setInterpolator(new LinearInterpolator());
//        mRotateAnimation.setDuration(1000);
//        mRotateAnimation.setRepeatCount(-1);
//        rl.startAnimation(mRotateAnimation);
    }

    @Override
    public void dismiss() {
//        mRotateAnimation.cancel();
        super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 屏蔽返回键
            return mCancelable;
        }
        return super.onKeyDown(keyCode, event);
    }
}