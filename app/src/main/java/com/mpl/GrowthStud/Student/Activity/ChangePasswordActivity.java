package com.mpl.GrowthStud.Student.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.StudentInfo;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private TextView tv_text, tv_get_num;
    private EditText et_num;
    private int scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        scope = bundle.getInt("scope");

        tv_text = findViewById(R.id.tv_text);
        if (scope == 2) {
            tv_text.setText("输入您学籍号码");
        } else if (scope == 1) {
            tv_text.setText("输入您身份证号码");
        } else {
            tv_text.setText("输入您学籍号码");
        }
        et_num = findViewById(R.id.et_num);

        tv_get_num = findViewById(R.id.tv_get_num);
        tv_get_num.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_get_num:
                String num = et_num.getText().toString().trim();
                doVerifyNum(num);
                break;
        }
    }

    private void doVerifyNum(final String num) {
        if (NetworkUtils.checkNetWork(ChangePasswordActivity.this) == false) {
            Toast.makeText(ChangePasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = ChangePasswordActivity.this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/verify-number/" + num;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    JSONObject data = response.getJSONObject("data");
                    if (code == 0) {
                        String isVerify = data.getString("isVerify");
                        if (isVerify.equals("true")) {
                            Intent intent = new Intent(ChangePasswordActivity.this, SetPasswordActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("num", num);
                            bundle.putInt("scope", scope);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "学籍号或者身份证错误，请重新输入", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChangePasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChangePasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChangePasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
