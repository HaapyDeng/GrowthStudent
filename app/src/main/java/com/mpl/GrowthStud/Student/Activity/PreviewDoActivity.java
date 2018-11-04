package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.Utils;

public class PreviewDoActivity extends Activity {
    private Context context;
    private View addview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_do);
        context = this;
        addview = findViewById(R.id.addview);
        int screenWidth = Utils.getAndroiodScreenWidth(context);
        int screenHeight = Utils.getAndroiodScreenHeight(context);
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
    }
}
