package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.GetStarInfoItem;

import java.util.List;


public class GetScoreInfoListViewAdapter extends ListViewAdapter<GetStarInfoItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public GetScoreInfoListViewAdapter(Context context, List<GetStarInfoItem> datas) {
        super(context, datas, R.layout.get_pingjia_info_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, GetStarInfoItem bean) {
        ((TextView) holder.getView(R.id.tv_title)).setText(bean.getCategory_name());
        ((TextView) holder.getView(R.id.tv_score)).setText(bean.getTotal_point() + "分");

    }
}