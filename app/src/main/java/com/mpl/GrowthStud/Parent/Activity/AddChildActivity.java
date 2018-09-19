package com.mpl.GrowthStud.Parent.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.Parent.View.WheelView;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class AddChildActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back, ll_choose;
    private EditText et_child_name, et_xueji, et_content;
    private TextView tv_choose, tv_add_commit;
    final String[] choose = new String[1];
    private String childname;
    private String schoolnum;
    private int role = 0;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        ll_choose = findViewById(R.id.ll_choose);
        ll_choose.setOnClickListener(this);

        et_child_name = findViewById(R.id.et_child_name);
        et_xueji = findViewById(R.id.et_xueji);
        et_content = findViewById(R.id.et_content);

        tv_choose = findViewById(R.id.tv_choose);

        tv_add_commit = findViewById(R.id.tv_add_commit);
        tv_add_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_choose:
                String[] PLANETS = new String[]{"爸爸", "妈妈", "爷爷", "奶奶", "外公", "外婆"};

                // 声明PopupWindow
                final PopupWindow popupWindow;
                // 声明PopupWindow对应的视图
                View popupView;

                // 声明平移动画
                TranslateAnimation animation;
                final String[] choose_item = new String[1];
                popupView = View.inflate(this, R.layout.wheel_choose, null);
                popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                // 设置点击popupwindow外屏幕其它地方消失
                popupWindow.setOutsideTouchable(true);
                // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
                animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(200);
                // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                popupWindow.showAtLocation(AddChildActivity.this.findViewById(R.id.tv_add_commit), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupView.startAnimation(animation);
                WheelView wva = popupView.findViewById(R.id.wheelview);
                wva.setOffset(1);
                wva.setSeletion(0);
                wva.isSelected();
                wva.setItems(Arrays.asList(PLANETS));
                wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("selectedIndex==>>>", "selectedIndex: " + selectedIndex + ", item: " + item);
                        choose_item[0] = item;
                    }
                });
                TextView tv_quxiao = popupView.findViewById(R.id.tv_quxiao);
                tv_quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                TextView tv_ok = popupView.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (choose_item[0] != null) {
                            choose[0] = choose_item[0];
                        } else {
                            choose[0] = "爸爸";
                        }
                        tv_choose.setText(choose[0]);
                        Log.d("choose==>>", choose[0]);
                        if (choose[0].equals("爸爸")) {
                            role = 1;
                        } else if (choose[0].equals("妈妈")) {
                            role = 2;
                        } else if (choose[0].equals("爷爷")) {
                            role = 3;
                        } else if (choose[0].equals("奶奶")) {
                            role = 4;
                        } else if (choose[0].equals("外公")) {
                            role = 5;
                        } else if (choose[0].equals("外婆")) {
                            role = 6;
                        }
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.tv_add_commit:
                childname = et_child_name.getText().toString().trim();
                schoolnum = et_xueji.getText().toString().trim();
                content = et_content.getText().toString().trim();
                if (childname.equals("")) {
                    Toast.makeText(this, "孩子姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (schoolnum.equals("")) {
                    Toast.makeText(this, "孩子学籍号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (role == 0) {
                    Toast.makeText(this, "请选择您的身份", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content.equals("")) {
                    Toast.makeText(this, "请输入您要说的话", Toast.LENGTH_SHORT).show();
                    return;
                }
                doAdd(childname, schoolnum, role, content);
                break;
        }
    }

    private void doAdd(String childname, String schoolnum, final int role, String content) {
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        final SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/audit/add";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", childname);
        params.put("number", schoolnum);
        params.put("role", role);
        params.put("content", content);
        client.addHeader("X-Api-Token", token);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(AddChildActivity.this, "已发送绑定申请", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AddChildActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(AddChildActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AddChildActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(AddChildActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
