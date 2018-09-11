package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        countDownTimer.start();
    }

    private CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
            SharedPreferences sp2 = getSharedPreferences("tag", MODE_PRIVATE);
            int tag = sp2.getInt("tag", 0);
            Log.d("spuser==?>>", "" + tag);
            if (tag == 0) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                doLogin(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));
            }
        }
    };

    private void doLogin(final String userName, final String password) {
        String url = getResources().getString(R.string.local_url) + "/login";
        Log.d("url==>>", url + "+" + userName + password);
        RequestParams params = new RequestParams();
        params.put("username", userName);
        params.put("password", password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {

                        JSONObject data = response.getJSONObject("data");
                        String token = data.getString("access_token");
                        int schoolId = data.getInt("school_id");
                        String schoolName = data.getString("school_name");
                        String role = data.getString("role");
                        int isActive = data.getInt("is_active");
                        String userId = data.getString("user_id");
                        SharedPreferences sp = getSharedPreferences("myinfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", token);
                        editor.putString("username", userName);
                        editor.putString("password", password);
                        editor.putString("userid", userId);
                        editor.putInt("schoolid", schoolId);
                        editor.putString("schoolname", schoolName);
                        editor.commit();
                        doSetAlia(token, role, isActive, userId);
                    } else {
                        Toast.makeText(SplashActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void doSetAlia(final String token, final String role, final int isActive, String userId) {
        final String[] registrationID = new String[1];
        JPushInterface.setAlias(this, NetworkUtils.getIMEI(this), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    registrationID[0] = JPushInterface.getRegistrationID(SplashActivity.this);
                    if (registrationID[0].equals("")) {
                        Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                        return;
                    }
                    Log.d("IMEI==>>", NetworkUtils.getIMEI(SplashActivity.this));
                    Log.d("registrationID==>>", registrationID[0]);
                    String url = getResources().getString(R.string.local_url) + "/user/jpush/set/" + registrationID[0];
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
                                    switch (isActive) {
                                        case 0:
                                            if (role.equals("student")) {
                                                //跳转到学生激活页面
                                                Intent intent = new Intent(SplashActivity.this, ChooseGradeActivity.class);
                                                startActivity(intent);
                                            } else if (role.equals("parent")) {
                                                //跳转到家长激活界面
                                            }
                                            break;
                                        case 1:
                                            if (role.equals("student")) {
                                                Intent intent3 = new Intent(SplashActivity.this, MainActivity.class);
                                                startActivity(intent3);
                                                finish();
                                                //跳转到学生主页面
                                            } else if (role.equals("parent")) {
                                                //跳转到家长主界面
                                            }
                                            break;
                                    }
                                } else {
                                    Toast.makeText(SplashActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                            return;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                            return;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                } else {
                    Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

    }
}
