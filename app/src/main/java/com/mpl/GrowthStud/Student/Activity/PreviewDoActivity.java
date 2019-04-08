package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.Utils;

public class PreviewDoActivity extends Activity implements View.OnClickListener {
    private Context context;
    private View addview;
    private TextView tv_changebanshi, tv_changebg;
    private LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_do);
        int screenHeight = 0;
        int screenWidth = 0;
        context = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        addview = findViewById(R.id.addview);
        //判断手机是否有底部导航栏
        if (Utils.checkDeviceHasNavigationBar(context)) {
            int navigationBarHeight = Utils.getNavigationBarHeight(context);
            screenHeight = Utils.getAndroiodScreenHeight(context) - navigationBarHeight;
        } else {
            screenHeight = Utils.getAndroiodScreenHeight(context);
        }
        screenWidth = Utils.getAndroiodScreenWidth(context);
        Log.d("screenWidth==>>>", "" + screenWidth);
        Log.d("screenHeight==>>>", "" + screenHeight);
        int addviewWidth = (375 * screenWidth) / 375;
        int addviewHeight = (467 * screenHeight) / 667;
        ViewGroup.LayoutParams lp;
        lp = addview.getLayoutParams();
        lp.width = addviewWidth;
        lp.height = addviewHeight;
        addview.setLayoutParams(lp);
        addview.setBackgroundResource(R.drawable.review_default_bg);
        tv_changebanshi = findViewById(R.id.tv_changebanshi);
        tv_changebanshi.setOnClickListener(this);

        tv_changebg = findViewById(R.id.tv_changebg);
        tv_changebg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_changebanshi:
                Intent intent = new Intent(context, ChangeBanShiActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_changebg:
                break;
        }
    }
}
