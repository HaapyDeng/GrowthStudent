package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mpl.GrowthStud.Student.Adapter.GridViewAdapter;
import com.mpl.GrowthStud.Student.Bean.MainConstant;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.Tools.PictureSelectorConfig;
import com.mpl.GrowthStud.Student.Tools.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.mpl.GrowthStud.Student.Tools.Utils.saveBitmap;

public class VideoActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private LinearLayout back;
    private TextView tv_title, tv_commit;
    private EditText et_wenzi;
    private String wenzi;
    private String achieveId;
    private String headTitle;
    private ImageView iv_button, iv_video;
    private ImageButton btn_delete;
    String singUrl;
    String bitmapPath;
    String backBitmap, prompt;

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
        doGetInfo();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    et_wenzi.setHint(prompt);
                    break;
            }

        }
    };

    private void doGetInfo() {
        if (!NetworkUtils.checkNetWork(VideoActivity.this)) {
            Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/image/show/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        prompt = data.getString("prompt");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(VideoActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
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
                if (wenzi.length() == 0) {
                    Toast.makeText(this, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
                    break;
                }
                Log.d("视频存放路径：", path);
                if (path.length() == 0) {
                    Toast.makeText(this, R.string.video_not_null, Toast.LENGTH_LONG).show();
                    break;
                }
                File dF = new File(path);
                FileInputStream fis;
                try {
                    fis = new FileInputStream(dF);
                    int fileLen = fis.available(); //这就是文件大小
                    Log.d("daxiaoshi:::", "" + fileLen);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doUploadPic(bitmapPath);
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

    private void doUploadPic(final String bitmapPath) {
        if (!NetworkUtils.checkNetWork(VideoActivity.this)) {
            Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(bitmapPath);
        String imgUrl = getResources().getString(R.string.uploadFile);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("image", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(imgUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONArray array = response.getJSONArray("data");
                        JSONObject object = array.getJSONObject(0);
                        String sing;
                        sing = object.getString("resource");
                        backBitmap = sing;
                        if (bitmapPath.length() != 0) {
                            doUploadVideo(path);
                        }

                    } else {
                        Toast.makeText(VideoActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("responseString==>>", responseString);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });

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
                    bitmapPath = saveBitmap(VideoActivity.this, bitmap);
                    Log.d("bitmap==>>>", bitmapPath);
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

    //上传视频到视频服务器
    private void doUploadVideo(String path) {
        if (!NetworkUtils.checkNetWork(VideoActivity.this)) {
            Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(path);
        String videoUrl = getResources().getString(R.string.uploadFile);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("video", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(videoUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONArray array = response.getJSONArray("data");
                        JSONObject object = array.getJSONObject(0);

                        singUrl = object.getString("resource");
                        Log.d("singUrl==>>>", singUrl);
                        doUploadAll(wenzi, singUrl, backBitmap);
                    } else {
                        Toast.makeText(VideoActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("responseString==>>", responseString);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void doUploadAll(String wenzi, String singUrl, String backBitmap) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/video/update/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("content", wenzi);
        params.put("video", singUrl);
        params.put("video_image", backBitmap);
        client.addHeader("X-Api-Token", token);
        client.put(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(VideoActivity.this, R.string.commit_success, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(VideoActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
