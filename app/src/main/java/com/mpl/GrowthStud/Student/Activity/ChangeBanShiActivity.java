package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.mpl.GrowthStud.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeBanShiActivity extends Activity {
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private Context context;


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
        //图标
        int icno[] = {R.mipmap.one_img_1, R.mipmap.one_img_2, R.mipmap.two_img_1,R.mipmap.two_img_2,
                R.mipmap.three_img_1, R.mipmap.three_img_2, R.mipmap.four_img_1, R.mipmap.four_img_2};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icno.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icno[i]);
            dataList.add(map);
        }
    }
}
