package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.QuestionInfoItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class QuestionInfoListViewAdapter extends ListViewAdapter<QuestionInfoItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public QuestionInfoListViewAdapter(Context context, List<QuestionInfoItem> datas) {
        super(context, datas, R.layout.question_info_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, QuestionInfoItem bean) {
        if (bean.getType() == 1) {
            ((TextView) holder.getView(R.id.tv_type)).setText("单选题");
        } else if (bean.getType() == 2) {
            ((TextView) holder.getView(R.id.tv_type)).setText("多选题");
        } else {
            ((TextView) holder.getView(R.id.tv_type)).setText("说明");
        }
        ((TextView) holder.getView(R.id.tv_name)).setText(bean.getName());
        if (bean.getOptions() == null) {
            ((LinearLayout) holder.getView(R.id.ll_choose_item)).setVisibility(View.INVISIBLE);
        } else {
            ((LinearLayout) holder.getView(R.id.ll_choose_item)).setVisibility(View.VISIBLE);
            JSONArray array = bean.getOptions();
            try {
                switch (array.length()) {
                    case 1:
                        ((LinearLayout) holder.getView(R.id.ll_a)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_a_content)).setText(array.getJSONObject(0).getString("name"));
                        break;
                    case 2:
                        ((LinearLayout) holder.getView(R.id.ll_a)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_a_content)).setText(array.getJSONObject(0).getString("name"));
                        ((LinearLayout) holder.getView(R.id.ll_b)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_b_content)).setText(array.getJSONObject(0).getString("name"));
                        break;
                    case 3:
                        ((LinearLayout) holder.getView(R.id.ll_a)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_a_content)).setText(array.getJSONObject(0).getString("name"));
                        ((LinearLayout) holder.getView(R.id.ll_b)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_b_content)).setText(array.getJSONObject(0).getString("name"));
                        ((LinearLayout) holder.getView(R.id.ll_c)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_c_content)).setText(array.getJSONObject(0).getString("name"));

                        break;
                    case 4:
                        ((LinearLayout) holder.getView(R.id.ll_a)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_a_content)).setText(array.getJSONObject(0).getString("name"));
                        ((LinearLayout) holder.getView(R.id.ll_b)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_b_content)).setText(array.getJSONObject(0).getString("name"));
                        ((LinearLayout) holder.getView(R.id.ll_c)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_c_content)).setText(array.getJSONObject(0).getString("name"));
                        ((LinearLayout) holder.getView(R.id.ll_d)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.tv_d_content)).setText(array.getJSONObject(0).getString("name"));
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}