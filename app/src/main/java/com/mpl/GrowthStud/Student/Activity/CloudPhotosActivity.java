package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.CloudPhotoListAdapter;
import com.mpl.GrowthStud.Student.Adapter.ViewHolder;
import com.mpl.GrowthStud.Student.Bean.CloudPhotoBean;
import com.mpl.GrowthStud.Student.Bean.StudentInfo;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CloudPhotosActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout back;
    private LoadingDialog loadingDialog;
    private String class_roomId;
    private TextView tv_ok;

    private Context context;
    private CloudPhotoBean cloudPhotoBean;
    private CloudPhotoListAdapter cloudPhotoListAdapter;
    private List<CloudPhotoBean> cloudPhotoBeanList = new ArrayList<>();
    private ListView listveiw;
    private List<String> imgList = new ArrayList<>();
    private List<String> imgListlocal = new ArrayList<>();
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_photos);
        context = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        tv_ok = findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);

        listveiw = findViewById(R.id.listveiw);
        listveiw.setOnItemClickListener(this);

        getUserInfo();

    }

    private void getUserInfo() {
        if (NetworkUtils.checkNetWork(context) == false) {
            Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/student-info";
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        class_roomId = data.getString("classroom_id");
                        getCloudPhotos();
                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void getCloudPhotos() {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(CloudPhotosActivity.this)) {
            Toast.makeText(CloudPhotosActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String scope = sharedPreferences.getString("scope", "");
        String uid = sharedPreferences.getString("uid", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/album/" + class_roomId + "/" + uid;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                loadingDialog.dismiss();
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    loadingDialog.dismiss();
                    if (code == 0) {
                        JSONArray data = response.getJSONArray("data");
                        cloudPhotoBeanList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            int id = object.getInt("id");
                            String name = object.getString("name");
                            int type = object.getInt("type");
                            cloudPhotoBean = new CloudPhotoBean(id, name, type, false);
                            cloudPhotoBeanList.add(cloudPhotoBean);
                        }
                        cloudPhotoListAdapter = new CloudPhotoListAdapter(context, cloudPhotoBeanList);
                        listveiw.setAdapter(cloudPhotoListAdapter);
                    } else {
                        Toast.makeText(CloudPhotosActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(CloudPhotosActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(CloudPhotosActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(CloudPhotosActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
            case R.id.tv_ok:
                if (imgList.size() == 0) {
                    Toast.makeText(context, "请选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingDialog = new LoadingDialog(context, "提交中...", R.drawable.ic_dialog_loading);
                loadingDialog.show();
                //下载图片到本地获取本地路径保存
                for (int i = 0; i < imgList.size(); i++) {
                    new MyAsyncTask().execute(imgList.get(i));
                    if (imgListlocal.size() == imgList.size()) {
                        loadingDialog.dismiss();
                        Log.d("imgListlocal==>>>", imgListlocal.toString());
                        Intent intentTemp = new Intent();
                        intentTemp.putStringArrayListExtra("imgList", (ArrayList<String>) imgListlocal);
                        setResult(5, intentTemp);
                        finish();
                    }
                }
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x123) {
                String path = SavaImage(bitmap, Environment.getExternalStorageDirectory().getPath() + "/cloud_photo");
                imgListlocal.add(path);
            }
        }

        ;
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (cloudPhotoBeanList.get(i).isChcked()) {
            cloudPhotoBeanList.get(i).setChcked(false);
            imgList.remove(cloudPhotoBeanList.get(i).getName());
        } else {
            if (imgList.size() > 3) {
                Toast.makeText(context, "最多只能选四张图", Toast.LENGTH_SHORT).show();
                return;
            } else {
                cloudPhotoBeanList.get(i).setChcked(true);
                imgList.add(cloudPhotoBeanList.get(i).getName());
            }
        }
        cloudPhotoListAdapter.notifyDataSetChanged();
        Log.d("imgList==>>>", imgList.toString());


    }


    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public String SavaImage(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + "/" + System.currentTimeMillis() + ".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 异步线程下载图片
     */
    class MyAsyncTask extends AsyncTask<String, Integer, Void> {


        protected Void doInBackground(String... params) {
            bitmap = GetImageInputStream((String) params[0]);

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message = new Message();
            message.what = 0x123;
            handler.sendMessage(message);

        }

    }
}
