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
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.StudentInfo;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 设置密码
 */
public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private EditText et_new_psd;
    private TextView tv_save;
    private String num;
    private String newPassword;
    private int scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        num = bundle.getString("num");
        scope = bundle.getInt("scope");
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        et_new_psd = findViewById(R.id.et_new_psd);

        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_save:
                newPassword = et_new_psd.getText().toString().trim();
                doUpdatePassword(newPassword);
                break;
        }
    }

    /**
     * 更新密码
     *
     * @param newPassword
     */
    private void doUpdatePassword(final String newPassword) {
        if (NetworkUtils.checkNetWork(SetPasswordActivity.this) == false) {
            Toast.makeText(SetPasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/update/student/password";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("number", num);
        params.put("newPassword", newPassword);
        client.addHeader("X-Api-Token", token);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        SharedPreferences sp = getSharedPreferences("myinfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password", newPassword);
                        editor.commit();
                        Toast.makeText(SetPasswordActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SetPasswordActivity.this, SettingActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("scope", scope);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        return;
                    } else {
                        Toast.makeText(SetPasswordActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SetPasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(SetPasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SetPasswordActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
