package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 绑定家长账号
 */
public class BoundParentActivity extends AppCompatActivity implements View.OnClickListener {
    private String parent_name, phone, content;
    private int id, gender, role;
    private LinearLayout back;
    private ImageView head_img, iv_gender;
    private TextView tv_parent_name, tv_phone, tv_type, tv_content, tv_ok, tv_refuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_parent);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        parent_name = bundle.getString("parent_name");
        gender = bundle.getInt("gender");
        phone = bundle.getString("phone");
        role = bundle.getInt("role");
        content = bundle.getString("content");
        initView();
    }

    @SuppressLint("NewApi")
    private void initView() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        iv_gender = findViewById(R.id.iv_gender);

        head_img = findViewById(R.id.head_img);
        tv_parent_name = findViewById(R.id.tv_parent_name);
        tv_parent_name.setText(parent_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_phone.setText(phone);
        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(content);

        if (gender == 1) {
            head_img.setBackground(getResources().getDrawable(R.mipmap.head_parent_man));
            iv_gender.setBackground(getResources().getDrawable(R.mipmap.small_boy_img));
        } else {
            head_img.setBackground(getResources().getDrawable(R.mipmap.head_parent_woman));
            iv_gender.setBackground(getResources().getDrawable(R.mipmap.small_girl_img));
        }
        tv_type = findViewById(R.id.tv_type);
        if (role == 1) {
            tv_type.setText("爸爸");
        } else if (role == 2) {
            tv_type.setText("妈妈");
        } else if (role == 3) {
            tv_type.setText("爷爷");
        } else if (role == 4) {
            tv_type.setText("奶奶");
        } else if (role == 5) {
            tv_type.setText("姥爷");
        } else if (role == 6) {
            tv_type.setText("姥姥");
        }

        tv_ok = findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);

        tv_refuse = findViewById(R.id.tv_refuse);
        tv_refuse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_ok:
                doOk(id);
                break;
            case R.id.tv_refuse:
                doRefuse(id);
                break;
        }
    }

    private void doRefuse(int id) {
        if (NetworkUtils.checkNetWork(BoundParentActivity.this) == false) {
            Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/audit/ok/" + id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(BoundParentActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(BoundParentActivity.this, MyFragment.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(BoundParentActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void doOk(int id) {
        if (NetworkUtils.checkNetWork(BoundParentActivity.this) == false) {
            Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/audit/no/" + id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(BoundParentActivity.this, "拒绝成功", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(BoundParentActivity.this, MainActivity.class);
                        intent.putExtra("flag", 2);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(BoundParentActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(BoundParentActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}
