package com.mpl.GrowthStud.Parent.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.WenZiInfoActivity;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PWenZiCheckActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private TextView tv_title, tv_start_time, tv_prompt, tv_answer;
    private String wenzi;
    private String achieveId;
    private String headTitle;
    private String achieve_name, username, complete_name, complete_role, start_time, prompt, answer, write_time;
    private int task_star, star;
    private JSONArray audit;
    private LinearLayout ll_open;
    // 声明PopupWindow
    PopupWindow popupWindow;
    // 声明PopupWindow对应的视图
    View popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwen_zi_check);
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

        ll_open = findViewById(R.id.ll_open);
        ll_open.setOnClickListener(this);
        initData();

    }

    private void initData() {
        if (!NetworkUtils.checkNetWork(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/text/show/" + achieveId;
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
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(PWenZiCheckActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PWenZiCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(PWenZiCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PWenZiCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
            case R.id.ll_open:
                ll_open.setVisibility(View.INVISIBLE);
                ShowPopuWindow();
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
                    tv_prompt.setText(headTitle);
                    tv_start_time.setText(start_time);
                    tv_answer.setText(answer);
                    ShowPopuWindow();
                    break;

            }
        }
    };

    private void ShowPopuWindow() {

        // 声明平移动画
        popupView = View.inflate(PWenZiCheckActivity.this, R.layout.check_evaluate_item, null);
        // 参数2,3：指明popupwindow的宽度和高度
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置点击popupwindow外屏幕其它地方不消失
        popupWindow.setOutsideTouchable(false);
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(PWenZiCheckActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        LinearLayout ll_close = popupView.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                ll_open.setVisibility(View.VISIBLE);
            }
        });
        final CheckBox ck_1, ck_2, ck_3, ck_4, ck_5;
        final StringBuilder[] sb = {new StringBuilder("")};
        final EditText et_content = popupView.findViewById(R.id.et_content);
        ck_1 = popupView.findViewById(R.id.ck_1);
        ck_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    et_content.setText(sb[0].append(ck_1.getText()));
                    ck_1.setBackground(getResources().getDrawable(R.drawable.textview_shape));
                    ck_1.setTextColor(getColor(R.color.white));
                } else {
                    ck_1.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                    ck_1.setTextColor(getColor(R.color.text2));
                }
            }
        });
        ck_2 = popupView.findViewById(R.id.ck_2);
        ck_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    et_content.setText(sb[0].append(ck_2.getText()));
                    ck_2.setBackground(getResources().getDrawable(R.drawable.textview_shape));
                    ck_2.setTextColor(getColor(R.color.white));
                } else {
                    ck_2.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                    ck_2.setTextColor(getColor(R.color.text2));
                }
            }
        });
        ck_3 = popupView.findViewById(R.id.ck_3);
        ck_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    et_content.setText(sb[0].append(ck_3.getText()));
                    ck_3.setBackground(getResources().getDrawable(R.drawable.textview_shape));
                    ck_3.setTextColor(getColor(R.color.white));
                } else {
                    ck_3.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                    ck_3.setTextColor(getColor(R.color.text2));
                }
            }
        });
        ck_4 = popupView.findViewById(R.id.ck_4);
        ck_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    et_content.setText(sb[0].append(ck_4.getText()));
                    ck_4.setBackground(getResources().getDrawable(R.drawable.textview_shape));
                    ck_4.setTextColor(getColor(R.color.white));
                } else {
                    ck_4.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                    ck_4.setTextColor(getColor(R.color.text2));
                }
            }
        });
        ck_5 = popupView.findViewById(R.id.ck_5);
        ck_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    et_content.setText(sb[0].append(ck_5.getText()));
                    ck_5.setBackground(getResources().getDrawable(R.drawable.textview_shape));
                    ck_5.setTextColor(getColor(R.color.white));
                } else {
                    ck_5.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                    ck_5.setTextColor(getColor(R.color.text2));
                }
            }
        });

        final String[] contentText = new String[1];
        final int[] rbText = {1};
        RadioGroup rg = popupView.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                ck_1.setChecked(false);
                ck_2.setChecked(false);
                ck_3.setChecked(false);
                ck_4.setChecked(false);
                ck_5.setChecked(false);
                switch (id) {
                    case R.id.rb_ok:
                        et_content.setText("");
                        sb[0] = new StringBuilder("");
                        rbText[0] = 1;
                        ck_1.setText("图文并茂很生动");
                        ck_1.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_1.setTextColor(getColor(R.color.text2));
                        ck_2.setText("我看好你哦加油");
                        ck_2.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_2.setTextColor(getColor(R.color.text2));
                        ck_3.setText("相互共同进步");
                        ck_3.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_3.setTextColor(getColor(R.color.text2));
                        ck_4.setText("多学多问问题");
                        ck_4.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_4.setTextColor(getColor(R.color.text2));
                        ck_5.setText("很棒很棒");
                        ck_5.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_5.setTextColor(getColor(R.color.text2));
                        break;
                    case R.id.rb_no:
                        et_content.setText("");
                        sb[0] = new StringBuilder("");
                        rbText[0] = 2;
                        ck_1.setText("文字有违规字眼");
                        ck_1.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_1.setTextColor(getColor(R.color.text2));
                        ck_2.setText("图片有违规内容");
                        ck_2.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_2.setTextColor(getColor(R.color.text2));
                        ck_3.setText("未按要求记录");
                        ck_3.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_3.setTextColor(getColor(R.color.text2));
                        ck_4.setText("记录内容过少");
                        ck_4.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_4.setTextColor(getColor(R.color.text2));
                        ck_5.setText("有错别字");
                        ck_5.setBackground(getResources().getDrawable(R.drawable.textview_shape_grey));
                        ck_5.setTextColor(getColor(R.color.text2));
                        break;
                }
            }
        });
        TextView tv_commit = popupView.findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentText[0] = et_content.getText().toString().trim();
                if (contentText[0].equals("")) {
                    Toast.makeText(PWenZiCheckActivity.this, "请添加评审内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                doCommit(rbText[0], achieveId, contentText[0]);

            }
        });
    }

    private void doCommit(int i, String achieveId, String content) {
        if (!NetworkUtils.checkNetWork(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/audit/update/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("status", i);
        params.put("content", content);
        client.addHeader("X-Api-Token", token);
        client.put(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(PWenZiCheckActivity.this, "提交评审完成", Toast.LENGTH_LONG).show();
                        popupWindow.dismiss();
                        finish();
                    } else {
                        Toast.makeText(PWenZiCheckActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PWenZiCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(PWenZiCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PWenZiCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

}
