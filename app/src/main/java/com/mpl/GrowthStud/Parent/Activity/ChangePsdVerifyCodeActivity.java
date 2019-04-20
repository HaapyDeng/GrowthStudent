package com.mpl.GrowthStud.Parent.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.SetPasswordActivity;
import com.mpl.GrowthStud.Student.Tools.LineEditText;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChangePsdVerifyCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btn_back;
    private EditText et_card_num;
    private Button btn_active;
    private String code;
    private TextView tv_getcode;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psd_verify_code);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        phoneNum = bundle.getString("phonenum");

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        et_card_num = findViewById(R.id.et_card_num);

        btn_active = findViewById(R.id.btn_active);
        btn_active.setOnClickListener(this);

        tv_getcode = findViewById(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_active:
                code = et_card_num.getText().toString().trim();
                if (code.length() <= 0) {
                    Toast.makeText(this, R.string.phone_not_null, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!NetworkUtils.checkNetWork(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
                doActive(code);
                break;
            case R.id.tv_getcode:
                doGetVerifyCode(phoneNum);
                break;
        }
    }

    private void doActive(String code) {
        String url = getResources().getString(R.string.local_url) + "/user/validate-code/" + code;
        Log.d("url==>>", url);
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
//        RequestParams params = new RequestParams();
//        params.put("code", code);
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
                        Toast.makeText(ChangePsdVerifyCodeActivity.this, "验证成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ChangePsdVerifyCodeActivity.this, PSetPasswordActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    } else {
                        Toast.makeText(ChangePsdVerifyCodeActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChangePsdVerifyCodeActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChangePsdVerifyCodeActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChangePsdVerifyCodeActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void doGetVerifyCode(String phoneNum) {
        String url = getResources().getString(R.string.local_url) + "/send-code";
        Log.d("url==>>", url);
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("mobile", phoneNum);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(ChangePsdVerifyCodeActivity.this, R.string.send_code_success, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Toast.makeText(ChangePsdVerifyCodeActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChangePsdVerifyCodeActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChangePsdVerifyCodeActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChangePsdVerifyCodeActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });

    }
}
