package com.mpl.GrowthStud.Student.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.QuestionInfoListViewAdapter;
import com.mpl.GrowthStud.Student.Bean.QuestionInfoItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 问卷详情
 */
public class QuestionInfoActivity extends AppCompatActivity {
    private String achieveId;
    private String headTitle;
    private TextView tv_title;
    private int question_count;
    private int total_point;
    private QuestionInfoItem questionInfoItem;
    private List<QuestionInfoItem> questionItemList;
    private QuestionInfoListViewAdapter questionInfoListViewAdapter;
    private ListView listView;
    private LinearLayout ll_addView;
    private View cv;
    private JSONArray audit;
    private LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_info);
        cv = getWindow().getDecorView();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);
        listView = findViewById(R.id.listview);
        ll_addView = findViewById(R.id.ll_addView);
        doGetData(achieveId);
    }

    private void doGetData(String achieveId) {
        if (!NetworkUtils.checkNetWork(QuestionInfoActivity.this)) {
            Toast.makeText(QuestionInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        String getUrl = getResources().getString(R.string.local_url) + "/v1/achievement/question/show/" + achieveId;
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Log.d("url==>>", getUrl);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(getUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                int code = 0;
                try {
                    code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject question = data.getJSONObject("question");
                        question_count = question.getInt("item_number");
                        total_point = question.getInt("total_point");
                        JSONArray item = data.getJSONArray("item");
                        if (item.length() == 0) {
                            return;
                        }
                        questionItemList = new ArrayList<>();
                        for (int i = 0; i < item.length(); i++) {
                            JSONArray options = null;
                            JSONArray answers = null;
                            JSONObject object = item.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("name");
                            int point = object.getInt("point");
                            int type = object.getInt("type");
                            if (object.has("options")) {
                                options = object.getJSONArray("options");
                            }
                            if (object.has("answers")) {
                                answers = object.getJSONArray("answers");
                            }
                            if (object.has("audit")) {
                                audit = object.getJSONArray("audit");
                            }
                            questionInfoItem = new QuestionInfoItem(id, name, point, type, options, answers, audit);
                            questionItemList.add(questionInfoItem);
                        }

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(QuestionInfoActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(QuestionInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String
                    responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(QuestionInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(QuestionInfoActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
                    questionInfoListViewAdapter = new QuestionInfoListViewAdapter(QuestionInfoActivity.this, questionItemList);
                    listView.setAdapter(questionInfoListViewAdapter);
                    //判断是谁评价了
                    if (audit.length() == 0) {
                        View view = View.inflate(QuestionInfoActivity.this, R.layout.no_evaluate_item, null);
                        ll_addView.addView(view);
                    } else if (audit.length() == 1) {
                        try {
                            JSONObject object = audit.getJSONObject(0);
                            String role = object.getString("role");
                            String comment = object.getString("comment");
                            View view = View.inflate(QuestionInfoActivity.this, R.layout.parent_evaluate_item, null);
                            ll_addView.addView(view);
                            TextView tv_comment = view.findViewById(R.id.tv_comment);
                            tv_comment.setText(comment);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            JSONObject object1 = audit.getJSONObject(0);
                            String role1 = object1.getString("role");
                            String comment1 = object1.getString("comment");
                            JSONObject object2 = audit.getJSONObject(1);
                            String role2 = object1.getString("role");
                            String comment2 = object1.getString("comment");
                            View view2 = View.inflate(QuestionInfoActivity.this, R.layout.teacher_evaluate_item, null);
                            ll_addView.addView(view2);
                            TextView tv_omment1 = view2.findViewById(R.id.comment1);
                            tv_omment1.setText(comment1);
                            TextView tv_omment2 = view2.findViewById(R.id.comment2);
                            tv_omment2.setText(comment2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };
}
