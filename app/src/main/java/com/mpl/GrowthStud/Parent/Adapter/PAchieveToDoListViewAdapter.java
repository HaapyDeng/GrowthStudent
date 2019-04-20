package com.mpl.GrowthStud.Parent.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.Parent.Bean.PAchieveToDoItem;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.ListViewAdapter;
import com.mpl.GrowthStud.Student.Adapter.ViewHolder;
import com.mpl.GrowthStud.Student.Bean.AchieveToDoItem;
import com.mpl.GrowthStud.Student.Tools.DownImage;

import java.util.List;


public class PAchieveToDoListViewAdapter extends ListViewAdapter<PAchieveToDoItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public PAchieveToDoListViewAdapter(Context context, List<PAchieveToDoItem> datas) {
        super(context, datas, R.layout.ptodo_achieve_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, PAchieveToDoItem bean) {
        ((TextView) holder.getView(R.id.tv_title)).setText(bean.getName());
        Log.d("qqqqqqqqqqq", "" + bean.getType());
        if (bean.getImage().length() == 0) {
            Drawable drawable0 = context1.getResources().getDrawable(R.mipmap.achieve_default);
            ((ImageView) holder.getView(R.id.head_img)).setImageDrawable(drawable0);
        } else {
            DownImage downImage = new DownImage(bean.getImage());
            downImage.loadImage(new DownImage.ImageCallBack() {

                @Override
                public void getDrawable(Drawable drawable) {
                    ((ImageView) holder.getView(R.id.head_img)).setImageDrawable(drawable);
                }
            });
        }
        if (bean.getStatus().equals("0")) {
            ((TextView) holder.getView(R.id.tv_state)).setText("驳回");
        } else if (bean.getStatus().equals("1")) {
            ((TextView) holder.getView(R.id.tv_state)).setText("待完成");
        } else if (bean.getStatus().equals("2")) {
            ((TextView) holder.getView(R.id.tv_state)).setText("家长审核");
        } else if (bean.getStatus().equals("3")) {
            ((TextView) holder.getView(R.id.tv_state)).setText("老师审核");
        } else if (bean.getStatus().equals("4")) {
            ((TextView) holder.getView(R.id.tv_state)).setText("待评星");
        } else if (bean.getStatus().equals("5")) {
            ((TextView) holder.getView(R.id.tv_state)).setText("完成");
        }
        if (bean.getType().equals("1")) {
            Drawable drawable = context1.getResources().getDrawable(R.mipmap.little_wenzi_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable);
            ((TextView) holder.getView(R.id.tv_1)).setText("文字");
        } else if (bean.getType().equals("2")) {
            Drawable drawable2 = context1.getResources().getDrawable(R.mipmap.little_tuwen_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable2);
            ((TextView) holder.getView(R.id.tv_1)).setText("图文");
        } else if (bean.getType().equals("3")) {
            Drawable drawable3 = context1.getResources().getDrawable(R.mipmap.little_video_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable3);
            ((TextView) holder.getView(R.id.tv_1)).setText("视频");
        } else if (bean.getType().equals("4")) {
            Drawable drawable4 = context1.getResources().getDrawable(R.mipmap.little_pingjia_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable4);
            ((TextView) holder.getView(R.id.tv_1)).setText("问卷");
        } else if (bean.getType().equals("5")) {
            Drawable drawable5 = context1.getResources().getDrawable(R.mipmap.little_form);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable5);
            ((TextView) holder.getView(R.id.tv_1)).setText("系统");
        } else if (bean.getType().equals("6")) {
            Drawable drawable5 = context1.getResources().getDrawable(R.mipmap.little_system);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable5);
            ((TextView) holder.getView(R.id.tv_1)).setText("表单");
        } else if (bean.getType().equals("7")) {
            Drawable drawable5 = context1.getResources().getDrawable(R.mipmap.little_mix);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable5);
            ((TextView) holder.getView(R.id.tv_1)).setText("混合");
        }
        ((TextView) holder.getView(R.id.tv_2)).setText(bean.getLabel_name());
        if (bean.getTask_star().equals("0")) {

        } else if (bean.getTask_star().equals("1")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("2")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("3")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("4")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("5")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.VISIBLE);
        }

    }
}