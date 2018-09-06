package com.mpl.GrowthStud.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.Adapter.ImageAdapter;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Tools.DownImage;
import com.mpl.GrowthStud.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TuWenInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back;
    private TextView tv_title;
    private String achieveId;
    private String headTitle;
    private TextView tv_start_time, tv_prompt, tv_answer;
    private String achieve_name, username, complete_name, complete_role, start_time, prompt, answer, write_time;
    private int task_star, star;
    private JSONArray audit;
    private String[] image = new String[]{};
    private LinearLayout ll_addView;
    private GridView gridview;
    private List<String> listImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_wen_info);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        tv_prompt = findViewById(R.id.tv_prompt);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_answer = findViewById(R.id.tv_answer);

        ll_addView = findViewById(R.id.ll_addView);

        gridview = findViewById(R.id.gridview);
        initData();
    }

    private void initData() {
        if (!NetworkUtils.checkNetWork(TuWenInfoActivity.this)) {
            Toast.makeText(TuWenInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
                        achieve_name = data.getString("name");
                        username = data.getString("username");
                        complete_name = data.getString("complete_name");
                        complete_role = data.getString("complete_role");
                        start_time = data.getString("start");
                        task_star = data.getInt("task_star");
                        star = data.getInt("star");
                        audit = data.getJSONArray("audit");
                        prompt = data.getString("prompt");
                        answer = data.getString("answer");
                        write_time = data.getString("write_time");
                        image = new String[data.getJSONArray("image").length()];
                        for (int i = 0; i < data.getJSONArray("image").length(); i++) {
                            image[i] = data.getJSONArray("image").getString(i);
                            Log.d(" image[i] ==>>>", image[i]);
                        }
                        data.getJSONArray("image");
                        Message message = new Message();
                        message.what = 1;
                        message.obj = image;
                        handler.sendMessage(message);

                    } else {
                        Toast.makeText(TuWenInfoActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(TuWenInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(TuWenInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(TuWenInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_prompt.setText(prompt);
                    tv_start_time.setText(start_time);
                    tv_answer.setText(answer);

                    for (int i = 0; i < image.length; i++) {
                        Log.d("length==>>>", "" + image[i]);
                        listImage.add(image[i]);
                    }
                    Log.d("listimage==>>>", listImage.toString());
                    gridview = findViewById(R.id.gridview);
                    gridview.setAdapter(new ImageAdapter(TuWenInfoActivity.this, listImage));
                    //判断是谁评价了
                    if (audit.length() == 0) {
                        View view = View.inflate(TuWenInfoActivity.this, R.layout.no_evaluate_item, null);
                        ll_addView.addView(view);
                    } else if (audit.length() == 1) {
                        try {
                            JSONObject object = audit.getJSONObject(0);
                            String role = object.getString("role");
                            String comment = object.getString("comment");
                            View view = View.inflate(TuWenInfoActivity.this, R.layout.parent_evaluate_item, null);
                            ll_addView.addView(view);
                            TextView tv_comment = view.findViewById(R.id.tv_comment);
                            tv_comment.setText(comment);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            JSONObject object1 = audit.getJSONObject(0);
                            String role1 = object1.getString("role");
                            String comment1 = object1.getString("comment");
                            JSONObject object2 = audit.getJSONObject(1);
                            String role2 = object1.getString("role");
                            String comment2 = object1.getString("comment");
                            View view2 = View.inflate(TuWenInfoActivity.this, R.layout.teacher_evaluate_item, null);
                            ll_addView.addView(view2);
                            TextView tv_omment1 = view2.findViewById(R.id.comment1);
                            tv_omment1.setText(comment1);
                            TextView tv_omment2 = view2.findViewById(R.id.comment2);
                            tv_omment2.setText(comment2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    break;

            }
        }
    };

}
