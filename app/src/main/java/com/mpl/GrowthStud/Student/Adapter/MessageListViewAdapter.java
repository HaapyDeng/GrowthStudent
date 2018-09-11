package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.MessageItem;
import com.mpl.GrowthStud.Student.View.CircleImageView;

import java.util.List;


public class MessageListViewAdapter extends ListViewAdapter<MessageItem> {
    private Context context1;
    private List<MessageItem> data;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public MessageListViewAdapter(Context context, List<MessageItem> datas) {
        super(context, datas, R.layout.message_item);
        context1 = context;
        data = datas;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, final MessageItem bean) {
        if (bean.getType() == 1) {
            ((ImageView) holder.getView(R.id.iv_head)).setBackground(context1.getResources().getDrawable(R.mipmap.icon_system_info));
        } else {
            ((ImageView) holder.getView(R.id.iv_head)).setBackground(context1.getResources().getDrawable(R.mipmap.icon_evaluation_info));
        }
        ((TextView) holder.getView(R.id.tv_name)).setText(bean.getTitle());
        if (bean.getIs_read() == 0) {
            ((CircleImageView) holder.getView(R.id.iv_dot)).setVisibility(View.VISIBLE);
        } else {
            ((CircleImageView) holder.getView(R.id.iv_dot)).setVisibility(View.INVISIBLE);
        }
        Log.d("getCreated_at==>>>", bean.toString());
        ((TextView) holder.getView(R.id.tv_time)).setText(bean.getCreated_at());
        ((TextView) holder.getView(R.id.tv_content)).setText(bean.getContent());
    }

}