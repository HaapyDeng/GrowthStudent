package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import java.util.List;
import java.util.Map;

public class ChangeBanShiActivity extends Activity {
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private Context context;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ban_shi);
        context = this;
        gridView = (GridView) findViewById(R.id.gridview);
        //初始化数据
        initData();
        String[] from = {"img"};

        int[] to = {R.id.img};

        adapter = new SimpleAdapter(this, dataList, R.layout.banshi_gridview_item, from, to);
        gridView.setAdapter(adapter);
        gridView.setSelection(0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }

        });

    }

    void initData() {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(context)) {
            Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/default/style/{type}/{num}";
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
    }
}
