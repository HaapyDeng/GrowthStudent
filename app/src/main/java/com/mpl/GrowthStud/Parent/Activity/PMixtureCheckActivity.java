package com.mpl.GrowthStud.Parent.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.MixtureInfoActivity;
import com.mpl.GrowthStud.Student.Adapter.MixtureInfoListAdapter;
import com.mpl.GrowthStud.Student.Bean.MixtureInfoListItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PMixtureCheckActivity extends Activity implements View.OnClickListener {

    private LinearLayout back;
    private TextView tv_title, tv_time, tv_lable, tv_lable2, tv_lable3, tv_lable4, tv_lable5;
    private String achieveId;
    private LoadingDialog loadingDialog;
    private JSONArray label;
    private String updated_at, name;

    private JSONArray audit;
    private LinearLayout ll_open;

    private String headTitle;
    private String achieve_name, username, complete_name, complete_role, start_time, prompt, answer, write_time;
    private int task_star, star;
    private String[] image = new String[]{};
    private GridView gridview;
    private List<String> listImage = new ArrayList<>();


    // 声明PopupWindow
    PopupWindow popupWindow;

    // 声明PopupWindow对应的视图
    View popupView;

    private Context mContext;
    private ListView listview;

    private List<MixtureInfoListItem> mDatas = new ArrayList<MixtureInfoListItem>();
    private MixtureInfoListAdapter mixtureInfoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmixture_check);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        mContext = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        tv_time = findViewById(R.id.tv_time);

        tv_lable = findViewById(R.id.tv_lable);
        tv_lable2 = findViewById(R.id.tv_lable2);
        tv_lable3 = findViewById(R.id.tv_lable3);
        tv_lable4 = findViewById(R.id.tv_lable4);
        tv_lable5 = findViewById(R.id.tv_lable5);
        ll_open = findViewById(R.id.ll_open);
        ll_open.setOnClickListener(this);
        listview = findViewById(R.id.listview);

        initData(achieveId);
    }

    private void initData(String achieveId) {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(PMixtureCheckActivity.this)) {
            Toast.makeText(PMixtureCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/assembly/show/" + achieveId;
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
                        achieve_name = data.getString("name");
                        username = data.getString("username");
                        complete_name = data.getString("complete_name");
                        complete_role = data.getString("complete_role");
                        start_time = data.getString("start");
                        task_star = data.getInt("task_star");
                        star = data.getInt("star");
                        audit = data.getJSONArray("audit");
                        write_time = data.getString("write_time");
                        label = data.getJSONArray("label");
                        //获取混合答案及详情
                        JSONArray history = data.getJSONArray("history");
                        for (int i = 0; i < history.length(); i++) {
                            JSONObject object = history.getJSONObject(i);
                            int t = 0;
                            t = object.getInt("t");
                            MixtureInfoListItem mixtureInfoListItem = new MixtureInfoListItem(t, object);
                            mDatas.add(mixtureInfoListItem);
                        }
                        mixtureInfoListAdapter = new MixtureInfoListAdapter(mContext, mDatas);
                        listview.setAdapter(mixtureInfoListAdapter);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = image;
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ll_open.setVisibility(View.INVISIBLE);
                    tv_title.setText(achieve_name);
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
                    ShowPopuWindow();
            }
        }
    };

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

    private void ShowPopuWindow() {

        // 声明平移动画
        popupView = View.inflate(PMixtureCheckActivity.this, R.layout.check_evaluate_item, null);
        // 参数2,3：指明popupwindow的宽度和高度
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置点击popupwindow外屏幕其它地方不消失
        popupWindow.setOutsideTouchable(false);
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(PMixtureCheckActivity.this.findViewById(R.id.tv_answer), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    Toast.makeText(PMixtureCheckActivity.this, "请添加评审内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                doCommit(rbText[0], achieveId, contentText[0]);
            }
        });
    }

    private void doCommit(int i, String achieveId, String content) {
        loadingDialog = new LoadingDialog(mContext, "提交中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
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
                    loadingDialog.dismiss();
                    if (code == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(PMixtureCheckActivity.this, "提交评审完成", Toast.LENGTH_LONG).show();
                        popupWindow.dismiss();
                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                        intent.putExtra("pdata", "refresh");
                        LocalBroadcastManager.getInstance(PMixtureCheckActivity.this).sendBroadcast(intent);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        Toast.makeText(PMixtureCheckActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(PMixtureCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(PMixtureCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(PMixtureCheckActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
