package com.mpl.GrowthStud.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.mpl.GrowthStud.Tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private ImageButton back;
    private TextView tv_title, tv_commit;
    private EditText et_wenzi;
    private String wenzi;
    private String achieveId;
    private String headTitle;
    private ImageView iv_button, iv_video;
    private ImageButton btn_delete;

    // 文件路径
    private String path = "";

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

        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        iv_button = findViewById(R.id.iv_button);
        iv_button.setOnClickListener(this);

        iv_video = findViewById(R.id.iv_video);
        iv_video.setOnClickListener(this);

        File filePathFile = new File("/storage/emulated/0/im/video/");
        if (filePathFile != null && filePathFile.listFiles() != null) {
            if (filePathFile.listFiles().length > 0) {
                path = filePathFile.listFiles()[0].getPath();
                Bitmap bitmap = Utils.createVideoThumbnail(path);
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
//                drawable.setTileModeXY(Shader.TileMode.REPEAT,
//                        Shader.TileMode.REPEAT);
                drawable.setDither(true);
//                btnPlay.setBackgroundDrawable(drawable);
                iv_button.setVisibility(View.GONE);
                iv_video.setVisibility(View.VISIBLE);
                iv_video.setBackgroundDrawable(drawable);
                btn_delete.setVisibility(View.VISIBLE);
            }
        }
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
                Log.d("视频存放路径：", path);
                if (path.length() == 0) {
                    Toast.makeText(this, R.string.video_not_null, Toast.LENGTH_LONG).show();
                    break;
                }
                doUploadVideo(wenzi);
                break;
            case R.id.iv_button:
                // 启动拍摄的Activity
                Intent intent = new Intent(VideoActivity.this, VideoRecorderActivity.class);
                VideoActivity.this.startActivityForResult(intent, 200);
                break;
            case R.id.btn_delete:
                iv_video.setVisibility(View.GONE);
                iv_button.setVisibility(View.VISIBLE);
                path = "";
                btn_delete.setVisibility(View.GONE);
                break;
            case R.id.iv_video:
                if (path != null && !path.equalsIgnoreCase("")) {
                    Intent intent1 = new Intent(this, VideoPlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", path);
                    intent1.putExtras(bundle);
                    startActivity(intent1);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    // 成功
                    path = data.getStringExtra("path");
//                    Toast.makeText(VideoActivity.this, "存储路径为:" + path, Toast.LENGTH_SHORT).show();
                    Log.d("视频路径path==>>>", path);
                    // 通过路径获取第一帧的缩略图并显示
                    Bitmap bitmap = Utils.createVideoThumbnail(path);
                    BitmapDrawable drawable = new BitmapDrawable(bitmap);
//                    drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    drawable.setDither(true);
//                    btnPlay.setBackgroundDrawable(drawable);
                    iv_button.setVisibility(View.GONE);
                    iv_video.setVisibility(View.VISIBLE);
                    iv_video.setBackgroundDrawable(drawable);
                    btn_delete.setVisibility(View.VISIBLE);
                } else {
                    // 失败
                }
                break;

        }
    }

    private void doUploadVideo(String wenzi) {
    }
}
