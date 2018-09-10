package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.ParentApplyListItem;

import java.util.List;


public class ParentApplyListViewAdapter extends ListViewAdapter<ParentApplyListItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public ParentApplyListViewAdapter(Context context, List<ParentApplyListItem> datas) {
        super(context, datas, R.layout.parent_apply_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, ParentApplyListItem bean) {
        if (bean.getGender() == 1) {
            ((ImageView) holder.getView(R.id.iv_head)).setBackground(context1.getResources().getDrawable(R.mipmap.head_parent_man));
        } else {
            ((ImageView) holder.getView(R.id.iv_head)).setBackground(context1.getResources().getDrawable(R.mipmap.head_parent_woman));
        }
        ((TextView) holder.getView(R.id.tv_name)).setText(bean.getParent_username());
        ((TextView) holder.getView(R.id.tv_content)).setText(bean.getContent());
        if (bean.getStatus() == 1) {
            ((TextView) holder.getView(R.id.tv_status)).setText("新的请求");
            ((ImageView) holder.getView(R.id.iv_dot)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_right)).setVisibility(View.VISIBLE);
        } else if (bean.getStatus() == 2) {
            ((TextView) holder.getView(R.id.tv_status)).setText("同意");
        } else {
            ((TextView) holder.getView(R.id.tv_status)).setText("已拒绝");
        }
    }
}