package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.Utils;

import java.util.ArrayList;

/**
 * 图文加模板预览
 */
public class TuWenPreviewDoActivity extends Activity implements View.OnClickListener {
    private Context context;
    private View addview;
    private TextView tv_changebanshi, tv_changebg;
    private LinearLayout back;
    private ArrayList<String> mPicList; //上传的图片凭证的数据源
    private String achieveid, type;
    private int piccount;
    private final static int REQUESTCODE = 1; // 返回的结果码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_do);
        mPicList = (ArrayList<String>) getIntent().getSerializableExtra("mPicList");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveid = extras.getString("achieveid");
        type = extras.getString("type");
        piccount = extras.getInt("piccount");


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
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("piccount", "" + piccount);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUESTCODE); //REQUESTCODE--->1
                break;
            case R.id.tv_changebg:
                Intent intent1 = new Intent(context, ChangeBasePicActivity.class);
                startActivityForResult(intent1, REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                String banshiId = data.getStringExtra("banshiId");
                String banshiImge = data.getStringExtra("banshiImgeUrl");
                Log.d("result==>>>", banshiId + "/" + banshiImge);
                if (banshiId.equals("20001")) {        // 单图   图片在下，文字在上

                } else if (banshiId.equals("20002")) {// 单图  图片在上，文字在下

                } else if (banshiId.equals("20003")) { // 2张图  图在上，文字在下

                } else if (banshiId.equals("20004")) {// 2张图  图在下，文字在上

                } else if (banshiId.equals("20005")) {// 3张图  图在左边 ，文字右边

                } else if (banshiId.equals("20006")) {// 3张图  图在右边，文字左边

                } else if (banshiId.equals("20007")) {// 4张图  2图上，文字中，2图下

                } else if (banshiId.equals("20008")) {// 4张图  文字上，4图下

                } else if (banshiId.equals("20009")) {// 4张图  4图上，文字下

                }

            }
        }
    }
}
