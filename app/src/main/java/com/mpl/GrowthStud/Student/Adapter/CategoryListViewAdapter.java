package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.CategoryListItem;

import java.util.List;


public class CategoryListViewAdapter extends ListViewAdapter<CategoryListItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public CategoryListViewAdapter(Context context, List<CategoryListItem> datas) {
        super(context, datas, R.layout.category_list_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, CategoryListItem bean) {
        ((TextView) holder.getView(R.id.tv_name)).setText(bean.getName());
    }
}