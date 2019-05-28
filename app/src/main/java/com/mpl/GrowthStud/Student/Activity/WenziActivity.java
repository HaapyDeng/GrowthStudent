package com.mpl.GrowthStud.Student.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.Tools.Utils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;
import com.mpl.GrowthStud.Student.View.TipsDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 文字成就
 */
public class WenziActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private TextView tv_title, tv_commit;
    private EditText et_wenzi;
    private String wenzi;
    private String achieveId;
    private String headTitle, prompt, tip;
    private TextView tv_text_count;
    private LoadingDialog loadingDialog;
    private ImageButton ib_more;
    private PopupWindow popupWindow;
    private TextView tv_takephoto;
    private TextView tv_preview;
    private Context mContext;
    private ImageButton ib_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenzi);
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

        ib_more = findViewById(R.id.ib_more);
        ib_more.setOnClickListener(this);

        tv_text_count = findViewById(R.id.tv_text_count);

        et_wenzi = findViewById(R.id.et_wenzi);
        et_wenzi.addTextChangedListener(mTextWatcher);

        ib_tips = findViewById(R.id.ib_tips);
        ib_tips.setOnClickListener(this);
        doGetInfo();
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//            et_wenzi.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            tv_text_count.setText("" + temp.length());
        }
    };

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
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(WenziActivity.this)) {
            Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
                        loadingDialog.dismiss();
                        JSONObject data = response.getJSONObject("data");
                        prompt = data.getString("prompt");
                        tip = data.getString("tip");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(WenziActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Utils.hideInput(mContext, v);
                finish();
                break;
            case R.id.tv_commit:
                et_wenzi = findViewById(R.id.et_wenzi);
                wenzi = et_wenzi.getText().toString().trim();
                if (wenzi.length() <= 0) {
                    Toast.makeText(this, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
                    break;
                }
                doUploadWenzi(wenzi);
                break;
            //提示语
            case R.id.ib_tips:
                TipsDialog tipsDialog = new TipsDialog(mContext, tip);
                tipsDialog.show();
                break;
            case R.id.ib_more:
                // 获取自定义的菜单布局文件
                View menu_view = getLayoutInflater().inflate(R.layout.more_menu, null, false);
                // 创建PopupWindow实例,设置菜单宽度和高度为包裹其自身内容
                popupWindow = new PopupWindow(menu_view,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, true);
                //设置菜单显示在按钮的下面
                popupWindow.showAsDropDown(ib_more, 0, 0);
                // 点击其他地方消失
                menu_view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        //如果菜单存在并且为显示状态，就关闭菜单并初始化菜单
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                        return false;
                    }
                });
                tv_takephoto = menu_view.findViewById(R.id.tv_takephoto);
                tv_takephoto.setOnClickListener(this);

                tv_preview = menu_view.findViewById(R.id.tv_preview);
                tv_preview.setOnClickListener(this);

                break;
            //拍照上传
            case R.id.tv_takephoto:
                break;
            //预览
            case R.id.tv_preview:
                Intent intent = new Intent(mContext, TuWenPreviewDoActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void doUploadWenzi(String wenzi) {
        loadingDialog = new LoadingDialog(this, "提交中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(WenziActivity.this)) {
            Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("wenzhi:>>>", wenzi);
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/text/update/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("content", wenzi);
        client.addHeader("X-Api-Token", token);
        client.put(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(WenziActivity.this, R.string.commit_success, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                        intent.putExtra("data", "refresh");
                        LocalBroadcastManager.getInstance(WenziActivity.this).sendBroadcast(intent);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(WenziActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
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
                Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(WenziActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });

    }
}
