package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.AchieveCompletItem;
import com.mpl.GrowthStud.Student.Bean.GetStarInfoItem;
import com.mpl.GrowthStud.Student.Tools.DownImage;

import java.util.List;


public class GetStarInfoListViewAdapter extends ListViewAdapter<GetStarInfoItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public GetStarInfoListViewAdapter(Context context, List<GetStarInfoItem> datas) {
        super(context, datas, R.layout.get_start_info_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, GetStarInfoItem bean) {
        ((TextView) holder.getView(R.id.tv_title)).setText(bean.getCategory_name());
        ((TextView) holder.getView(R.id.start)).setText(bean.getStar());
        ((TextView) holder.getView(R.id.total_start)).setText(bean.getTask_star());
        ((TextView) holder.getView(R.id.tv_score)).setText(bean.getPoint());

    }
}