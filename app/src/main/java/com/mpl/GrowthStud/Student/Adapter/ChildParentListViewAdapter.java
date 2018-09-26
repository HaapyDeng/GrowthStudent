package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.ChildParentListItem;
import com.mpl.GrowthStud.Student.View.CircleImageView;

import java.util.List;


public class ChildParentListViewAdapter extends ListViewAdapter<ChildParentListItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public ChildParentListViewAdapter(Context context, List<ChildParentListItem> datas) {
        super(context, datas, R.layout.child_parent_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, ChildParentListItem bean) {
        if (bean.getGender() == 1) {
            ((ImageView) holder.getView(R.id.iv_head)).setImageDrawable(context1.getResources().getDrawable(R.mipmap.head_parent_man));
        } else {
            ((ImageView) holder.getView(R.id.iv_head)).setImageDrawable(context1.getResources().getDrawable(R.mipmap.head_parent_woman));
        }
        ((TextView) holder.getView(R.id.tv_name)).setText(bean.getParent_username());
        ((TextView) holder.getView(R.id.tv_phone)).setText(bean.getMobile());
        ((TextView) holder.getView(R.id.tv_status)).setText("已绑定");
    }
}