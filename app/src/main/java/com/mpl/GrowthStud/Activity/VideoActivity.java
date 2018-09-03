package com.mpl.GrowthStud.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mpl.GrowthStud.Adapter.GridViewAdapter;
import com.mpl.GrowthStud.Bean.MainConstant;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Tools.PictureSelectorConfig;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ImageButton back;
    private TextView tv_title, tv_commit;
    private EditText et_wenzi;
    private String wenzi;
    private String achieveId;
    private String headTitle;
    private ImageView iv_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mContext = this;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);
        tv_commit = findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);

        et_wenzi = findViewById(R.id.et_wenzi);

        iv_button = findViewById(R.id.iv_button);
        iv_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_commit:
                et_wenzi = findViewById(R.id.et_wenzi);
                wenzi = et_wenzi.getText().toString().trim();
                if (wenzi.length() < 100) {
                    Toast.makeText(this, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
                    break;
                }
                doUploadVideo(wenzi);
                break;
            case R.id.iv_button:
                break;
        }
    }

    private void doUploadVideo(String wenzi) {
    }
}
