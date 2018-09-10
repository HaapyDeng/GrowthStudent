package com.mpl.GrowthStud.Student.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
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

public class ParentsAccountActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageButton back;
    private ListView already_listview, no_listview;
    private List<ParentApplyListItem> listItems;
    private List<ChildParentListItem> listItems2;
    private ParentApplyListViewAdapter parentApplyListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_account);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        already_listview = findViewById(R.id.already_listview);

        no_listview = findViewById(R.id.no_listview);
        no_listview.setOnItemClickListener(this);
        initData();
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
                        JSONArray array = data.getJSONArray("audit");
                        listItems = new ArrayList<ParentApplyListItem>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            int id = object.getInt("id");
                            String user_id = object.getString("user_id");
                            String parent_id = object.getString("parent_id");
                            String parent_username = object.getString("parent_username");
                            int role = object.getInt("role");
                            int status = object.getInt("status");
                            String content = object.getString("content");
                            String mobile = object.getString("mobile");
                            int gender = object.getInt("gender");
                            ParentApplyListItem parentApplyListItem = new ParentApplyListItem(id, user_id, parent_id, parent_username, role, status, content, mobile, gender);
                            listItems.add(parentApplyListItem);
                        }
                        parentApplyListViewAdapter = new ParentApplyListViewAdapter(ParentsAccountActivity.this, listItems);
                        no_listview.setAdapter(parentApplyListViewAdapter);
                        JSONArray child = data.getJSONArray("child");
                        listItems2 = new ArrayList<ChildParentListItem>();
                        for (int j = 0; j < child.length(); j++) {
                            JSONObject object2 = child.getJSONObject(j);
                            String parent_id = object2.getString("parent_id");
                            String parent_username = object2.getString("parent_username");
                            int role = object2.getInt("role");
                            int gender = object2.getInt("gender");
                            String mobile = object2.getString("mobile");
                            ChildParentListItem childParentListItem = new ChildParentListItem(parent_id, parent_username, role, gender, mobile);
                            listItems2.add(childParentListItem);
                            ChildParentListViewAdapter childParentListViewAdapter = new ChildParentListViewAdapter(ParentsAccountActivity.this, listItems2);
                            already_listview.setAdapter(childParentListViewAdapter);
                        }
                    } else {
                        Toast.makeText(ParentsAccountActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ParentsAccountActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ParentsAccountActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ParentsAccountActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listItems.get(i).getStatus() == 1) {
            Intent intent = new Intent(ParentsAccountActivity.this, BundleParentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", listItems.get(i).getId());
            bundle.putString("parent_name", listItems.get(i).getParent_username());
            bundle.putInt("gender", listItems.get(i).getGender());
            bundle.putString("phone", listItems.get(i).getMobile());
            bundle.putInt("role", listItems.get(i).getRole());
            bundle.putString("content", listItems.get(i).getContent());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
