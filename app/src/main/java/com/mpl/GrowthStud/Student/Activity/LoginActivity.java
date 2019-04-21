package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.Parent.Activity.ActiveParentActivity;
import com.mpl.GrowthStud.Parent.Activity.PMainActivity;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.CancelOrOkDialog;
import com.mpl.GrowthStud.Student.Tools.ConstomDialog;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText et_user, et_psd;
    private Button btn_login;
    private String userName, password;
    private TextView tv_forget_psd;
    private LoadingDialog loadingDialog;
    private TextView tv_pregist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();


    }

    private void initView() {
        TextView app_version = findViewById(R.id.app_version);
        try {
            app_version.setText( NetworkUtils.getVersionName(this)+"家庭端");
        } catch (Exception e) {
            e.printStackTrace();
        }
        et_user = findViewById(R.id.et_user);
        et_psd = findViewById(R.id.et_psd);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_forget_psd = findViewById(R.id.tv_forget_psd);
        tv_forget_psd.setOnClickListener(this);

        tv_pregist = findViewById(R.id.tv_pregist);
        tv_pregist.setOnClickListener(this);
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
//                Intent intent = new Intent(this, ChooseGradeActivity.class);
//                startActivity(intent);
                doLogin(userName, password);
                break;
            case R.id.tv_forget_psd:
                final ConstomDialog dialog = new ConstomDialog(LoginActivity.this);
                dialog.setTv("请联系班主任老师修改重置密码");
                dialog.setOnCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setOnExitListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.tv_pregist:
                Intent intent = new Intent(this, ParentRegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void doLogin(final String userName, final String password) {
        loadingDialog = new LoadingDialog(this, "正在登录...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
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
                        int scope;
                        if (role.equals("student")) {
                            scope = data.getJSONArray("scope").getInt(0);
                        } else {
                            scope = 0;
                        }
                        doGetAlia(token, userName, password, schoolId, schoolName, role, isActive, userId, scope);

                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }
        });
    }

    private void doGetAlia(final String token, final String userName, final String password, final int schoolId, final String schoolName, final String role, final int isActive, final String userId, final int scope) {
        final String[] registrationID = new String[1];

        final String[] jpregistrationID = new String[1];
        registrationID[0] = NetworkUtils.getIMEI(this);
        Log.d("IMEI==>>", NetworkUtils.getIMEI(LoginActivity.this));
        JPushInterface.setAlias(LoginActivity.this, registrationID[0], new TagAliasCallback() {

            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    jpregistrationID[0] = JPushInterface.getRegistrationID(LoginActivity.this);
                    Log.d("registrationID==>>", jpregistrationID[0]);
                    doSetAlia(token, userName, password, schoolId, schoolName, role, isActive, userId, scope, jpregistrationID[0]);
                } else {
                    Toast.makeText(LoginActivity.this, "请检查网络稍后再试", Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                    return;
                }
            }
        });
    }

    private void doSetAlia(final String token, final String userName, final String password, final int schoolId,
                           final String schoolName, final String role, final int isActive, final String userId, final int scope, String jpregistrationID) {
        String url = getResources().getString(R.string.local_url) + "/user/jpush/set/" + jpregistrationID;
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
                        SharedPreferences sp = getSharedPreferences("myinfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", token);
                        editor.putInt("scope", scope);
                        editor.putString("username", userName);
                        editor.putString("password", password);
                        editor.putString("userid", userId);
                        editor.putInt("schoolid", schoolId);
                        editor.putString("schoolname", schoolName);
                        editor.commit();
                        SharedPreferences sp2 = getSharedPreferences("tag", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sp2.edit();
                        editor2.putInt("tag", 1);
                        editor2.commit();
                        switch (isActive) {
                            case 0:
                                loadingDialog.dismiss();
                                if (role.equals("student")) {
                                    if (scope == 1) {
                                        //跳转到身份证激活页面
                                        Intent intent = new Intent(LoginActivity.this, ActivateKinderStdActivity.class);
                                        startActivity(intent);
                                    } else {
                                        //跳转到学籍号激活页面
                                        Intent intent = new Intent(LoginActivity.this, ActivateOtherStdActivity.class);
                                        startActivity(intent);
                                    }

                                } else if (role.equals("parent")) {
//                                                跳转到家长激活界面
                                    Intent intent2 = new Intent(LoginActivity.this, ActiveParentActivity.class);
                                    startActivity(intent2);
//                                                Toast.makeText(LoginActivity.this, "家长账号敬请期待", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                break;
                            case 1:
                                if (role.equals("student")) {
                                    loadingDialog.dismiss();
                                    Intent intent3 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent3);
                                    finish();
                                    //跳转到学生主页面
                                } else if (role.equals("parent")) {
                                    SharedPreferences sharedPreferences3 = getSharedPreferences("userid", MODE_PRIVATE);
                                    if (!sharedPreferences3.getBoolean("have", false)) {
                                        doGetChild(); //初次进入app获取第一个孩子的uid
                                    } else {
                                        loadingDialog.dismiss();
                                        //跳转到家长主界面
                                        Intent intent4 = new Intent(LoginActivity.this, PMainActivity.class);
                                        startActivity(intent4);
                                        finish();
                                    }

//                                                Toast.makeText(LoginActivity.this, "家长账号敬请期待", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                break;
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }
        });

    }


    private void doGetChild() {
        Log.d("cid==>>>", "GetChild");
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/parent-info";
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
                        loadingDialog.dismiss();
                        JSONObject data = response.getJSONObject("data");
                        if (data.has("student")) {
                            if (data.getJSONArray("student").length() > 0) {
                                JSONArray student = data.getJSONArray("student");
                                JSONObject object = student.getJSONObject(0);
                                String student_user_id = object.getString("user_id");
                                SharedPreferences sharedPreferences = getSharedPreferences("userid", Context.MODE_PRIVATE);
                                //获取editor对象
                                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                                editor.putString("id", student_user_id);
                                editor.putBoolean("have", true);
                                editor.commit();
                                //跳转到家长主界面
                                Intent intent4 = new Intent(LoginActivity.this, PMainActivity.class);
                                startActivity(intent4);
                                finish();
                            } else {
                                //跳转到家长主界面
                                Intent intent4 = new Intent(LoginActivity.this, PMainActivity.class);
                                startActivity(intent4);
                                finish();
                            }
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
