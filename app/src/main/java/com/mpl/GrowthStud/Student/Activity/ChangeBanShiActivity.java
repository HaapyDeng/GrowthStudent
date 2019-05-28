package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.GridViewBanShiAdapter;
import com.mpl.GrowthStud.Student.Bean.BanShiItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 更改版式
 *
 */
public class ChangeBanShiActivity extends Activity {
    private GridView gridView;
    private List<BanShiItem> dataList = new ArrayList<>();
    private GridViewBanShiAdapter adapter;
    private Context context;
    private LoadingDialog loadingDialog;
    private String type, num, banshiId = "", banshiImgeUrl = "";
    private TextView tv_baocun;
    private LinearLayout back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ban_shi);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        type = extras.getString("type");
        num = extras.getString("piccount");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_baocun = findViewById(R.id.tv_baocun);
        tv_baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (banshiId.equals("")) {
                    Toast.makeText(context, "请选择版式", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("banshiImgeUrl", banshiImgeUrl); //将计算的值回传回去
                intent.putExtra("beijingId", banshiId); //将计算的值回传回去
                //setResult(resultCode, data);第一个参数表示结果返回码
                setResult(3, intent);

                finish();
            }
        });
        context = this;
        gridView = (GridView) findViewById(R.id.gridview);
        //初始化数据
        initData();

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
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/default/style/" + type + "/" + num;
        Log.d("url==>>", url);
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
                        final JSONArray data = response.getJSONArray("data");
                        String img;
                        String id;
                        for (int i = 0; i < data.length(); i++) {
                            img = data.getJSONObject(i).getString("image");
                            id = data.getJSONObject(i).getString("id");
                            BanShiItem banShiItem = new BanShiItem(img, id);
                            dataList.add(banShiItem);
                        }
                        Log.d("dataList", "" + dataList.size());
                        adapter = new GridViewBanShiAdapter(context, dataList);
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                //把点击的position传递到adapter里面去
                                adapter.changeState(position);
                                Log.d("点击id==》》》", dataList.get(position).getId());
                                banshiId = dataList.get(position).getId();
                                banshiImgeUrl = dataList.get(position).getImge();
                            }

                        });

                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

}
