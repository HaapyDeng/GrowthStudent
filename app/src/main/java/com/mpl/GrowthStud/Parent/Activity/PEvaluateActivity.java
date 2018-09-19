package com.mpl.GrowthStud.Parent.Activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.EvaluateActivity;
import com.mpl.GrowthStud.Student.Activity.GetScoreInfoFragment;
import com.mpl.GrowthStud.Student.Activity.GetStarInfoFragment;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PEvaluateActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView get_star_info, get_score_info, pingjia_point, star_point, total_point, star_count;
    private GetStarInfoFragment fragment1;// 第一个操作界面

    private GetScoreInfoFragment fragment2;// 第二个操作界面

    private android.app.FragmentManager fm;//管理器
    private LinearLayout back;
    /*
    one_star_point (integer, optional): 一颗星等于多少分 ,
    starCount (integer, optional): 获得星 ,
    starPoint (integer, optional): 成就星分 ,
    pingjiaPoint (integer, optional): 问卷分 ,
    totalPoint (integer, optional): 总分
     */
    private String one_star_point, starCount, starPoint, pingjiaPoint, totalPoint;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pevaluate);
        initviews();
        initData();
        selectFragment(0);
    }

    private void initviews() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        get_star_info = findViewById(R.id.get_star_info);
        get_star_info.setOnClickListener(this);
        get_score_info = findViewById(R.id.get_score_info);
        get_score_info.setOnClickListener(this);
        pingjia_point = findViewById(R.id.pingjia_point);
        star_point = findViewById(R.id.star_point);
        total_point = findViewById(R.id.total_point);
        star_count = findViewById(R.id.star_count);
    }

    private void initData() {
        SharedPreferences sharedPreferences3 = getSharedPreferences("userid", MODE_PRIVATE);
        if (!sharedPreferences3.getBoolean("have", false)) {
            Toast.makeText(this, "请先绑定你的孩子", Toast.LENGTH_LONG).show();
            return;
        } else {
            cid = sharedPreferences3.getString("id", "");
        }
        Log.d("cid==>>", cid);
        if (NetworkUtils.checkNetWork(PEvaluateActivity.this) == false) {
            Toast.makeText(PEvaluateActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String uid = sharedPreferences.getString("userid", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/statistical/" + cid;
        Log.d("url==>", url);
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
                        JSONObject data = response.getJSONObject("data");
                        starPoint = data.getString("star_point");
                        pingjiaPoint = data.getString("point");
                        totalPoint = data.getString("total_point");
                        starCount = data.getString("star");
                        one_star_point = data.getString("one_star_point");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(PEvaluateActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PEvaluateActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(PEvaluateActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PEvaluateActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //保存一颗星为多少分
                    SharedPreferences sp = getSharedPreferences("myinfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("one_star_point", one_star_point);
                    editor.commit();
                    total_point.setText(totalPoint);
                    star_point.setText(starPoint);
                    pingjia_point.setText(pingjiaPoint);
                    star_count.setText(starCount);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.get_star_info:

                selectFragment(0);

                break;

            case R.id.get_score_info:

                selectFragment(1);

                break;

            default:

                break;

        }
    }
// 切换Fragment

    private void selectFragment(int i) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        hideFragment(transaction);

        changeView(i);// 设置选项颜色

        switch (i) {

            case 0:

                if (fragment1 == null) {

                    fragment1 = new GetStarInfoFragment();

                    transaction.add(R.id.fragment, fragment1);

                }

                transaction.show(fragment1);

                break;


            case 1:

                if (fragment2 == null) {

                    fragment2 = new GetScoreInfoFragment();

                    transaction.add(R.id.fragment, fragment2);

                }

                transaction.show(fragment2);

                break;

        }

        transaction.commit();

    }
    // 隐藏fragment

    private void hideFragment(FragmentTransaction transaction) {

        if (fragment1 != null) {

            transaction.hide(fragment1);

        }

        if (fragment2 != null) {

            transaction.hide(fragment2);

        }


    }
    //改变字体和背景色状态

    @SuppressLint("NewApi")

    private void changeView(int i) {

        if (i == 0) {

            //设置背景色及字体颜色

            get_star_info.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            get_star_info.setTextColor(getResources().getColor(R.color.text));

            get_score_info.setBackground(getDrawable(R.drawable.textview_border));

            get_score_info.setTextColor(getResources().getColor(R.color.getstarinfo));

        } else if (i == 1) {

            get_score_info.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            get_score_info.setTextColor(getResources().getColor(R.color.text));

            get_star_info.setBackground(getDrawable(R.drawable.textview_border));

            get_star_info.setTextColor(getResources().getColor(R.color.getstarinfo));

        }

    }
}
