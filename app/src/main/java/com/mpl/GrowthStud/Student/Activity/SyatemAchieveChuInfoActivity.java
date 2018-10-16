package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.DownImage;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SyatemAchieveChuInfoActivity extends Activity implements View.OnClickListener {

    private LinearLayout back;
    private TextView tv_title, tv_male, tv_birthy, tv_schoolnum, tv_lable, tv_time, tv_nation, tv_political_status, tv_gostudytime, tv_adress, tv_describe;
    private TextView tv_lable2, tv_lable3, tv_lable4, tv_lable5;
    private String achieveId;
    private String headTitle;
    private TextView tv_username, tv_position;
    private String username, male, birthday, animal, postion, updated_at, name, idcard, nation, imagePath, political_status, gostudytime, schoolnum, adress, describe;
    private LoadingDialog loadingDialog;
    private JSONArray label;
    private ImageView iv_image;

    private JSONArray audit;
    private LinearLayout ll_open;

    // 声明PopupWindow
    PopupWindow popupWindow;

    // 声明PopupWindow
    PopupWindow popupWindow2;
    // 声明PopupWindow
    PopupWindow popupWindow3;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syatem_achieve_chu_info);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");
        mContext = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_nation = findViewById(R.id.tv_nation);
        iv_image = findViewById(R.id.iv_image);
        tv_title = findViewById(R.id.tv_title);
        tv_time = findViewById(R.id.tv_time);

        tv_lable = findViewById(R.id.tv_lable);
        tv_lable2 = findViewById(R.id.tv_lable2);
        tv_lable3 = findViewById(R.id.tv_lable3);
        tv_lable4 = findViewById(R.id.tv_lable4);
        tv_lable5 = findViewById(R.id.tv_lable5);

        tv_username = findViewById(R.id.tv_username);
        tv_male = findViewById(R.id.tv_male);
        tv_birthy = findViewById(R.id.tv_birthy);
        tv_schoolnum = findViewById(R.id.tv_schoolnum);
        tv_position = findViewById(R.id.tv_position);
        ll_open = findViewById(R.id.ll_open);
        ll_open.setOnClickListener(this);
        tv_political_status = findViewById(R.id.tv_political_status);
        tv_gostudytime = findViewById(R.id.tv_gostudytime);
        tv_adress = findViewById(R.id.tv_adress);
        tv_describe = findViewById(R.id.tv_describe);
        initData(achieveId);
    }

    private void initData(String achieveId) {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/system/show/" + achieveId;
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
                        loadingDialog.dismiss();
                        JSONObject data = response.getJSONObject("data");
                        name = data.getString("name");
                        updated_at = data.getString("updated_at");
                        label = data.getJSONArray("label");
                        audit = data.getJSONArray("audit");
                        JSONObject answers = data.getJSONObject("answers");
                        username = answers.getString("name");
                        male = answers.getString("gender");
                        birthday = answers.getString("birthday");
                        nation = answers.getString("nation");
                        imagePath = answers.getString("photo");
                        political_status = answers.getString("political_affiliation");
                        gostudytime = answers.getString("year");
                        schoolnum = answers.getString("number");
                        adress = answers.getString("address");
                        describe = answers.getString("describe");

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
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
                    popupView = View.inflate(mContext, R.layout.no_evaluate_item, null);
                    // 参数2,3：指明popupwindow的宽度和高度
                    popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    // 设置点击popupwindow外屏幕其它地方不消失
                    popupWindow.setOutsideTouchable(false);
                    // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                    popupWindow.showAtLocation(SyatemAchieveChuInfoActivity.this.findViewById(R.id.tv_schoolnum), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    popupView2 = View.inflate(mContext, R.layout.parent_evaluate_item, null);
                    // 参数2,3：指明popupwindow的宽度和高度
                    popupWindow2 = new PopupWindow(popupView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    // 设置点击popupwindow外屏幕其它地方不消失
                    popupWindow2.setOutsideTouchable(false);
                    // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                    popupWindow2.showAtLocation(SyatemAchieveChuInfoActivity.this.findViewById(R.id.tv_schoolnum), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    popupView3 = View.inflate(mContext, R.layout.teacher_evaluate_item, null);
                    // 参数2,3：指明popupwindow的宽度和高度
                    popupWindow3 = new PopupWindow(popupView3, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    // 设置点击popupwindow外屏幕其它地方不消失
                    popupWindow3.setOutsideTouchable(false);
                    // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                    popupWindow3.showAtLocation(SyatemAchieveChuInfoActivity.this.findViewById(R.id.tv_schoolnum), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    try {
                        JSONObject object1 = audit.getJSONObject(0);
                        String role1 = object1.getString("role");
                        String comment1 = object1.getString("comment");
                        JSONObject object2 = audit.getJSONObject(1);
                        String role2 = object1.getString("role");
                        String comment2 = object2.getString("comment");
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
                        ImageView iv1 = popupView3.findViewById(R.id.iv1);
                        ImageView iv2 = popupView3.findViewById(R.id.iv2);
                        ImageView iv3 = popupView3.findViewById(R.id.iv3);
                        ImageView iv4 = popupView3.findViewById(R.id.iv4);
                        ImageView iv5 = popupView3.findViewById(R.id.iv5);
                        int task_star = object2.getInt("task_star");
                        int star = object2.getInt("star");
                        switch (task_star) {
                            case 5:
                                iv1.setVisibility(View.VISIBLE);
                                iv2.setVisibility(View.VISIBLE);
                                iv3.setVisibility(View.VISIBLE);
                                iv4.setVisibility(View.VISIBLE);
                                iv5.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                iv1.setVisibility(View.VISIBLE);
                                iv2.setVisibility(View.VISIBLE);
                                iv3.setVisibility(View.VISIBLE);
                                iv4.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                iv1.setVisibility(View.VISIBLE);
                                iv2.setVisibility(View.VISIBLE);
                                iv3.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                iv1.setVisibility(View.VISIBLE);
                                iv2.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                iv1.setVisibility(View.VISIBLE);
                                break;
                        }
                        switch (star) {
                            case 1:
                                iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                break;
                            case 2:
                                iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                break;
                            case 3:
                                iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv3.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                break;
                            case 4:
                                iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv3.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv4.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                break;
                            case 5:
                                iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv3.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv4.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                iv5.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private Handler handler = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ll_open.setVisibility(View.INVISIBLE);
                    tv_title.setText(name);
                    tv_time.setText(updated_at);
                    tv_nation.setText(nation);
                    tv_political_status.setText(political_status);
                    tv_gostudytime.setText(gostudytime);
                    tv_schoolnum.setText(schoolnum);
                    tv_adress.setText(adress);
                    tv_describe.setText(describe);
                    DownImage downImage = new DownImage(imagePath);
                    downImage.loadImage(new DownImage.ImageCallBack() {
                        @Override
                        public void getDrawable(Drawable drawable) {
                            iv_image.setImageDrawable(drawable);
                        }
                    });

                    for (int i = 0; i < label.length(); i++) {
                        try {
                            String s = label.getString(i);

                            switch (i) {
                                case 0:
                                    tv_lable.setText(s);
                                    tv_lable.setBackgroundColor(getResources().getColor(R.color.label_bak));
                                    break;
                                case 1:
                                    tv_lable2.setText(s);
                                    tv_lable2.setBackgroundColor(getResources().getColor(R.color.label_bak));
                                    break;
                                case 2:
                                    tv_lable3.setText(s);
                                    tv_lable3.setBackgroundColor(getResources().getColor(R.color.label_bak));
                                    break;
                                case 3:
                                    tv_lable4.setText(s);
                                    tv_lable4.setBackgroundColor(getResources().getColor(R.color.label_bak));
                                    break;
                                case 4:
                                    tv_lable5.setText(s);
                                    tv_lable5.setBackgroundColor(getResources().getColor(R.color.label_bak));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    tv_username.setText(username);
                    tv_male.setText(male);
                    tv_birthy.setText(birthday);
                    //判断是谁评价了
                    if (audit.length() == 0) {

                        // 声明PopupWindow对应的视图
                        View popupView;
                        // 声明平移动画
                        popupView = View.inflate(mContext, R.layout.no_evaluate_item, null);
                        // 参数2,3：指明popupwindow的宽度和高度
                        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.WRAP_CONTENT);
                        // 设置点击popupwindow外屏幕其它地方不消失
                        popupWindow.setOutsideTouchable(false);
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow.showAtLocation(SyatemAchieveChuInfoActivity.this.findViewById(R.id.tv_schoolnum), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        LinearLayout ll_close = popupView.findViewById(R.id.ll_close);
                        ll_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                ll_open.setVisibility(View.VISIBLE);
                            }
                        });
                    } else if (audit.length() == 1) {

                        // 声明PopupWindow对应的视图
                        View popupView2;
                        popupView2 = View.inflate(mContext, R.layout.parent_evaluate_item, null);
                        // 参数2,3：指明popupwindow的宽度和高度
                        popupWindow2 = new PopupWindow(popupView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        // 设置点击popupwindow外屏幕其它地方不消失
                        popupWindow2.setOutsideTouchable(false);
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow2.showAtLocation(SyatemAchieveChuInfoActivity.this.findViewById(R.id.tv_schoolnum), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

                        // 声明PopupWindow对应的视图
                        View popupView3;
                        popupView3 = View.inflate(mContext, R.layout.teacher_evaluate_item, null);
                        // 参数2,3：指明popupwindow的宽度和高度
                        popupWindow3 = new PopupWindow(popupView3, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        // 设置点击popupwindow外屏幕其它地方不消失
                        popupWindow3.setOutsideTouchable(false);
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow3.showAtLocation(SyatemAchieveChuInfoActivity.this.findViewById(R.id.tv_schoolnum), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        try {
                            JSONObject object1 = audit.getJSONObject(0);
                            String role1 = object1.getString("role");
                            String comment1 = object1.getString("comment");
                            JSONObject object2 = audit.getJSONObject(1);
                            String role2 = object1.getString("role");
                            String comment2 = object2.getString("comment");
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
                            ImageView iv1 = popupView3.findViewById(R.id.iv1);
                            ImageView iv2 = popupView3.findViewById(R.id.iv2);
                            ImageView iv3 = popupView3.findViewById(R.id.iv3);
                            ImageView iv4 = popupView3.findViewById(R.id.iv4);
                            ImageView iv5 = popupView3.findViewById(R.id.iv5);
                            int task_star = object2.getInt("task_star");
                            int star = object2.getInt("star");
                            switch (task_star) {
                                case 5:
                                    iv1.setVisibility(View.VISIBLE);
                                    iv2.setVisibility(View.VISIBLE);
                                    iv3.setVisibility(View.VISIBLE);
                                    iv4.setVisibility(View.VISIBLE);
                                    iv5.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    iv1.setVisibility(View.VISIBLE);
                                    iv2.setVisibility(View.VISIBLE);
                                    iv3.setVisibility(View.VISIBLE);
                                    iv4.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    iv1.setVisibility(View.VISIBLE);
                                    iv2.setVisibility(View.VISIBLE);
                                    iv3.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    iv1.setVisibility(View.VISIBLE);
                                    iv2.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    iv1.setVisibility(View.VISIBLE);
                                    break;
                            }
                            switch (star) {
                                case 1:
                                    iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    break;
                                case 2:
                                    iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    break;
                                case 3:
                                    iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv3.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    break;
                                case 4:
                                    iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv3.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv4.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    break;
                                case 5:
                                    iv1.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv2.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv3.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv4.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    iv5.setBackground(getResources().getDrawable(R.mipmap.star_little));
                                    break;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        }
    };
}
