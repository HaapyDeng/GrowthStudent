package com.mpl.GrowthStud.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText et_user, et_psd;
    private Button btn_login;
    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_user = findViewById(R.id.et_user);
        et_psd = findViewById(R.id.et_psd);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userName = et_user.getText().toString().trim();
                password = et_psd.getText().toString().trim();
                if (userName.length() <= 0 || password.length() <= 0) {
                    Toast.makeText(this, R.string.username_not_null, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!NetworkUtils.checkNetWork(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(this, ChooseGradeActivity.class);
                startActivity(intent);
//                doLogin(userName, password);
                break;
        }

    }

    private void doLogin(String userName, String password) {
        String url = "/login";
        RequestParams params = new RequestParams();
        params.put("mobile", new BigInteger(userName));
        params.put("password", password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
