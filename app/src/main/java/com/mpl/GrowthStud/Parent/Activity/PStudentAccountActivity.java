package com.mpl.GrowthStud.Parent.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.Parent.Adapter.ParentChildApplyListViewAdapter;
import com.mpl.GrowthStud.Parent.Bean.PstudentApplyListItem;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.ParentsAccountActivity;
import com.mpl.GrowthStud.Student.Adapter.ChildParentListViewAdapter;
import com.mpl.GrowthStud.Student.Adapter.ParentApplyListViewAdapter;
import com.mpl.GrowthStud.Student.Bean.ChildParentListItem;
import com.mpl.GrowthStud.Student.Bean.ParentApplyListItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PStudentAccountActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private LinearLayout back;
    private ListView already_listview, no_listview;
    private TextView tv_add;
    private List<PstudentApplyListItem> listItems;
    private PstudentApplyListItem pstudentApplyListItem;
    private ParentChildApplyListViewAdapter parentChildApplyListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pstudent_account);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        tv_add = findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);

        already_listview = findViewById(R.id.already_listview);
        already_listview.setOnItemClickListener(this);

        no_listview = findViewById(R.id.no_listview);
        no_listview.setOnItemClickListener(this);
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_add:
                break;
        }
    }

    private void initData() {
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        final SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/get-audit";
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
                        JSONArray array = data.getJSONArray("child");
                        listItems = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String user_id = object.getString("user_id");
                            String username = object.getString("username");
                            String classroom_name = object.getString("classroom_name");
                            String grade = object.getString("grade");
                            String school = object.getString("school");
                            int gender = object.getInt("gender");
                            pstudentApplyListItem = new PstudentApplyListItem(user_id, username, classroom_name, grade, school, gender);
                            listItems.add(pstudentApplyListItem);
                        }
                        parentChildApplyListViewAdapter = new ParentChildApplyListViewAdapter(PStudentAccountActivity.this, listItems);
                        already_listview.setAdapter(parentChildApplyListViewAdapter);

                    } else {
                        Toast.makeText(PStudentAccountActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PStudentAccountActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(PStudentAccountActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PStudentAccountActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()) {
            case R.id.already_listview:
                break;
        }
    }
}
