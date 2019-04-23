package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.Parent.Activity.ActiveParentActivity;
import com.mpl.GrowthStud.Parent.Activity.PMainActivity;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;

public class SplashActivity extends Activity {
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    boolean isFirstIn = false;
    boolean update = true;
    private String updateUrl, updateVersion, content, mandatory, app_code;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView app_version = findViewById(R.id.app_version);
        try {
            app_version.setText("家庭端" + NetworkUtils.getVersionName(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkUpdate();


    }

    private void checkUpdate() {
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        String version = "";
        try {
            version = NetworkUtils.getVersionName(SplashActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = getResources().getString(R.string.local_url) + "/app-upgrade/" + 2 + "/" + version;
        Log.d("url++>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response++..>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        int hasUpgrade = data.getInt("hasUpgrade");
                        if (hasUpgrade == 0) {
                            update = false;
                            init();//进入app
                        } else {
                            update = true;
                            updateUrl = data.getString("url");
                            updateVersion = data.getString("version");
                            content = data.getString("content");
                            mandatory = data.getString("mandatory");
                            app_code = data.getString("app_code");
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);

                        }
                    } else {
                        Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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

    private void init() {
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);

        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("isFirstIn", true);

        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (!isFirstIn) {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
    }

    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showDialogUpdate();
                    break;
                case GO_HOME:
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
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
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
                        String scope;
//                        if (role.equals("student")) {
                        scope = data.getJSONArray("scope").getString(0);
//                        } else {
//                            scope = 0;
//                        }
                        doGetAlia(token, userName, password, schoolId, schoolName, role, isActive, userId, scope);
                    } else {
                        Toast.makeText(SplashActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
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
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }
        });
    }

    private void doGetAlia(final String token, final String userName, final String password, final int schoolId, final String schoolName, final String role, final int isActive, final String userId, final String scope) {
        final String[] registrationID = new String[1];

        final String[] jpregistrationID = new String[1];
        registrationID[0] = NetworkUtils.getIMEI(this);
        Log.d("IMEI==>>", NetworkUtils.getIMEI(SplashActivity.this));
        JPushInterface.setAlias(SplashActivity.this, registrationID[0], new TagAliasCallback() {

            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    jpregistrationID[0] = JPushInterface.getRegistrationID(SplashActivity.this);
                    Log.d("registrationID==>>", jpregistrationID[0]);
                    doSetAlia(token, userName, password, schoolId, schoolName, role, isActive, userId, scope, jpregistrationID[0]);
                } else {
                    Toast.makeText(SplashActivity.this, "请检查网络稍后再试", Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                    return;
                }
            }
        });
    }

    private void doSetAlia(final String token, final String userName, final String password, final int schoolId, final String schoolName,
                           final String role, final int isActive, final String userId, final String scope, String registrationID) {
        String url = getResources().getString(R.string.local_url) + "/user/jpush/set/" + registrationID;
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
                        SharedPreferences sp = getSharedPreferences("myinfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", token);
                        editor.putString("scope", scope);
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
                                if (role.equals("student")) {
                                    //跳转到学生激活页面
                                    Intent intent = new Intent(SplashActivity.this, ChooseGradeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (role.equals("parent")) {
                                    //跳转到家长激活界面
                                    Intent intent2 = new Intent(SplashActivity.this, ActiveParentActivity.class);
                                    startActivity(intent2);
                                    finish();
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
                                    Intent intent4 = new Intent(SplashActivity.this, PMainActivity.class);
                                    startActivity(intent4);
                                    finish();
                                }
                                break;
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
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
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SplashActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }
        });
    }


    /**
     * 提示版本更新的对话框
     */
    private void showDialogUpdate() {
        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置提示框的标题
        builder.setTitle("版本升级").
                setCancelable(false).
                // 设置提示框的图标
                        setIcon(R.drawable.android_family_icon).
                // 设置要显示的信息
                        setMessage(content).
                // 设置确定按钮
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this, "选择确定哦", 0).show();
                        loadNewVersionProgress();//下载最新的版本程序
                    }
                }).
                // 设置取消按钮,null是什么都不做，并关闭对话框
                        setNegativeButton("", null);
        // 生产对话框
        AlertDialog alertDialog = builder.create();
        // 显示对话框
        alertDialog.show();


    }

    /**
     * 下载新版本程序，需要子线程
     */
    private void loadNewVersionProgress() {
        final String uri = updateUrl;
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        //启动子线程下载任务
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(uri, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    public static File getFileFromServer(String uri, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            long time = System.currentTimeMillis();//当前时间的毫秒数
            File file = new File(Environment.getExternalStorageDirectory(), time + "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * 安装apk
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
