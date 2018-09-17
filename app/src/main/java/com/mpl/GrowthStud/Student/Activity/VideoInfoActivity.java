package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.Student.Adapter.ImageAdapter;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.DownImage;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class VideoInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private TextView tv_title;
    private String achieveId;
    private String headTitle;
    private ImageView img, stat_play;
    private TextView tv_start_time, tv_prompt, tv_answer;
    private String achieve_name, username, complete_name, complete_role, start_time, prompt, answer, write_time, video_image, video;
    private int task_star, star;
    private LinearLayout ll_open;
    private JSONArray audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        ll_open = findViewById(R.id.ll_open);
        ll_open.setOnClickListener(this);


        tv_prompt = findViewById(R.id.tv_prompt);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_answer = findViewById(R.id.tv_answer);

        img = findViewById(R.id.video_img);

        stat_play = findViewById(R.id.stat_play);
        stat_play.setOnClickListener(this);

        initData();
    }

    private void initData() {
        if (!NetworkUtils.checkNetWork(VideoInfoActivity.this)) {
            Toast.makeText(VideoInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/video/show/" + achieveId;
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
                        video_image = data.getString("video_image");
                        video = data.getString("video");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);

                    } else {
                        Toast.makeText(VideoInfoActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(VideoInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(VideoInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
            case R.id.stat_play:
                Intent intent1 = new Intent(this, VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("path", video);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.ll_open:
                ll_open.setVisibility(View.INVISIBLE);
                //判断是谁评价了
                if (audit.length() == 0) {
                    // 声明PopupWindow
                    final PopupWindow popupWindow;
                    // 声明PopupWindow对应的视图
                    View popupView;
                    // 声明平移动画
                    popupView = View.inflate(VideoInfoActivity.this, R.layout.no_evaluate_item, null);
                    // 参数2,3：指明popupwindow的宽度和高度
                    popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    // 设置点击popupwindow外屏幕其它地方不消失
                    popupWindow.setOutsideTouchable(false);
                    // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                    popupWindow.showAtLocation(VideoInfoActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    LinearLayout ll_close = popupView.findViewById(R.id.ll_close);
                    ll_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            ll_open.setVisibility(View.VISIBLE);
                        }
                    });
                } else if (audit.length() == 1) {
                    // 声明PopupWindow
                    final PopupWindow popupWindow2;
                    // 声明PopupWindow对应的视图
                    View popupView2;
                    popupView2 = View.inflate(VideoInfoActivity.this, R.layout.parent_evaluate_item, null);
                    // 参数2,3：指明popupwindow的宽度和高度
                    popupWindow2 = new PopupWindow(popupView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    // 设置点击popupwindow外屏幕其它地方不消失
                    popupWindow2.setOutsideTouchable(false);
                    // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                    popupWindow2.showAtLocation(VideoInfoActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    try {
                        JSONObject object = audit.getJSONObject(0);
                        String role = object.getString("role");
                        String comment = object.getString("comment");
                        TextView tv_comment = popupView2.findViewById(R.id.tv_comment);
                        tv_comment.setText(comment);
                        LinearLayout ll_close = popupView2.findViewById(R.id.ll_close);
                        ll_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow2.dismiss();
                                ll_open.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 声明PopupWindow
                    final PopupWindow popupWindow3;
                    // 声明PopupWindow对应的视图
                    View popupView3;
                    popupView3 = View.inflate(VideoInfoActivity.this, R.layout.teacher_evaluate_item, null);
                    // 参数2,3：指明popupwindow的宽度和高度
                    popupWindow3 = new PopupWindow(popupView3, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    // 设置点击popupwindow外屏幕其它地方不消失
                    popupWindow3.setOutsideTouchable(false);
                    // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                    popupWindow3.showAtLocation(VideoInfoActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    try {
                        JSONObject object1 = audit.getJSONObject(0);
                        String role1 = object1.getString("role");
                        String comment1 = object1.getString("comment");
                        JSONObject object2 = audit.getJSONObject(1);
                        String role2 = object1.getString("role");
                        String comment2 = object1.getString("comment");
                        TextView tv_omment1 = popupView3.findViewById(R.id.comment1);
                        tv_omment1.setText(comment1);
                        TextView tv_omment2 = popupView3.findViewById(R.id.comment2);
                        tv_omment2.setText(comment2);
                        LinearLayout ll_close = popupView3.findViewById(R.id.ll_close);
                        ll_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow3.dismiss();
                                ll_open.setVisibility(View.VISIBLE);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

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
                    ll_open.setVisibility(View.INVISIBLE);
                    tv_prompt.setText(prompt);
                    tv_start_time.setText(start_time);
                    tv_answer.setText(answer);
                    DownImage downImage = new DownImage(video_image);
                    downImage.loadImage(new DownImage.ImageCallBack() {

                        @Override
                        public void getDrawable(Drawable drawable) {
                            img.setImageDrawable(drawable);
                        }
                    });
                    //判断是谁评价了
                    if (audit.length() == 0) {
                        // 声明PopupWindow
                        final PopupWindow popupWindow;
                        // 声明PopupWindow对应的视图
                        View popupView;
                        // 声明平移动画
                        popupView = View.inflate(VideoInfoActivity.this, R.layout.no_evaluate_item, null);
                        // 参数2,3：指明popupwindow的宽度和高度
                        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.WRAP_CONTENT);
                        // 设置点击popupwindow外屏幕其它地方不消失
                        popupWindow.setOutsideTouchable(false);
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow.showAtLocation(VideoInfoActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        LinearLayout ll_close = popupView.findViewById(R.id.ll_close);
                        ll_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                ll_open.setVisibility(View.VISIBLE);
                            }
                        });
                    } else if (audit.length() == 1) {
                        // 声明PopupWindow
                        final PopupWindow popupWindow2;
                        // 声明PopupWindow对应的视图
                        View popupView2;
                        popupView2 = View.inflate(VideoInfoActivity.this, R.layout.parent_evaluate_item, null);
                        // 参数2,3：指明popupwindow的宽度和高度
                        popupWindow2 = new PopupWindow(popupView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        // 设置点击popupwindow外屏幕其它地方不消失
                        popupWindow2.setOutsideTouchable(false);
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow2.showAtLocation(VideoInfoActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        try {
                            JSONObject object = audit.getJSONObject(0);
                            String role = object.getString("role");
                            String comment = object.getString("comment");
                            TextView tv_comment = popupView2.findViewById(R.id.tv_comment);
                            tv_comment.setText(comment);
                            LinearLayout ll_close = popupView2.findViewById(R.id.ll_close);
                            ll_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow2.dismiss();
                                    ll_open.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 声明PopupWindow
                        final PopupWindow popupWindow3;
                        // 声明PopupWindow对应的视图
                        View popupView3;
                        popupView3 = View.inflate(VideoInfoActivity.this, R.layout.teacher_evaluate_item, null);
                        // 参数2,3：指明popupwindow的宽度和高度
                        popupWindow3 = new PopupWindow(popupView3, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        // 设置点击popupwindow外屏幕其它地方不消失
                        popupWindow3.setOutsideTouchable(false);
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow3.showAtLocation(VideoInfoActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        try {
                            JSONObject object1 = audit.getJSONObject(0);
                            String role1 = object1.getString("role");
                            String comment1 = object1.getString("comment");
                            JSONObject object2 = audit.getJSONObject(1);
                            String role2 = object1.getString("role");
                            String comment2 = object1.getString("comment");
                            TextView tv_omment1 = popupView3.findViewById(R.id.comment1);
                            tv_omment1.setText(comment1);
                            TextView tv_omment2 = popupView3.findViewById(R.id.comment2);
                            tv_omment2.setText(comment2);
                            LinearLayout ll_close = popupView3.findViewById(R.id.ll_close);
                            ll_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow3.dismiss();
                                    ll_open.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    break;

            }
        }
    };
}
