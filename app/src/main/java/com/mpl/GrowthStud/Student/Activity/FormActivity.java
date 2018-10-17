package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.FormListAdapter;
import com.mpl.GrowthStud.Student.Bean.FormAnswerBean;
import com.mpl.GrowthStud.Student.Bean.FormListItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class FormActivity extends Activity implements View.OnClickListener {
    private LinearLayout back;
    private TextView tv_title, tv_commit;
    private EditText et_username, et_male, et_like, et_birthy;
    private String achieveId;
    private String headTitle;
    private Context mContext;
    private List<FormListItem> mdatas;
    private FormListAdapter formListAdapter;

    private LoadingDialog loadingDialog;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");
        mContext = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);

        listview = findViewById(R.id.listview);

        tv_commit = findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);
        initFormLabel(achieveId);

    }

    private void initFormLabel(String id) {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/form/show/" + id;
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
                        JSONObject data = response.getJSONObject("data");
                        JSONArray item = data.getJSONArray("item");
                        mdatas = new ArrayList<>();
                        for (int i = 0; i < item.length(); i++) {
                            JSONObject object = item.getJSONObject(i);
                            String id = object.getString("id");
                            String label = object.getString("label");
                            String prompt = object.getString("prompt");
                            int type = object.getInt("type");
                            String options = object.getString("options");
                            int order = object.getInt("order");
                            FormListItem formListItem = new FormListItem(id, label, prompt, type, options, order);
                            mdatas.add(formListItem);
                        }
                        formListAdapter = new FormListAdapter(FormActivity.this, mdatas);
                        listview.setAdapter(formListAdapter);


                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_commit:
                int answerLength = 0;
                answerLength = formListAdapter.getData().size();
                Log.d("answerLength==>>>", "" + answerLength);
                JSONObject object = new JSONObject();
                if (answerLength == mdatas.size()) {
                    for (int j = 0; j < answerLength; j++) {
                        String id = "";
                        String answer = "";
                        id = formListAdapter.getData().get(j).getId();
                        answer = formListAdapter.getData().get(j).getAnswer();
                        try {
                            object.put(id, answer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("object==>>>", object.toString());
                } else {
                    Toast.makeText(mContext, "请完善信息再提交", Toast.LENGTH_SHORT).show();
                    return;
                }
                doputInfo(object);
                break;
        }

    }

    private void doputInfo(JSONObject object) {
        loadingDialog = new LoadingDialog(this, "提交中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/form/update/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("content", object);
        client.addHeader("X-Api-Token", token);
        client.put(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, R.string.commit_success, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }


}
