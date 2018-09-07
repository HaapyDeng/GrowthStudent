package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;

import com.mpl.GrowthStud.Student.Bean.AchieveToDoItem;
import com.mpl.GrowthStud.Student.Tools.DownImage;

import java.util.List;


public class AchieveToDoListViewAdapter extends ListViewAdapter<AchieveToDoItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public AchieveToDoListViewAdapter(Context context, List<AchieveToDoItem> datas) {
        super(context, datas, R.layout.todo_achieve_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, AchieveToDoItem bean) {
        ((TextView) holder.getView(R.id.tv_title)).setText(bean.getName());
        ((TextView) holder.getView(R.id.tv_1)).setText(bean.getCategory_name());
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
        if (bean.getType().equals("1")) {
            Drawable drawable = context1.getResources().getDrawable(R.mipmap.little_wenzi_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable);
        } else if (bean.getType().equals("2")) {
            Drawable drawable2 = context1.getResources().getDrawable(R.mipmap.little_tuwen_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable2);
        } else if (bean.getType().equals("3")) {
            Drawable drawable3 = context1.getResources().getDrawable(R.mipmap.little_video_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable3);
        } else if (bean.getType().equals("4")) {
            Drawable drawable4 = context1.getResources().getDrawable(R.mipmap.little_pingjia_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable4);
        } else if (bean.getType().equals("5")) {
            Drawable drawable5 = context1.getResources().getDrawable(R.mipmap.little_duocai_img);
            ((ImageView) holder.getView(R.id.iv_1)).setBackground(drawable5);
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
//        ((TextView) holder.getView(R.id.titleTv)).setText(bean.getTitle());
//        ((TextView) holder.getView(R.id.descTv)).setText(bean.getDesc());
//        ((TextView) holder.getView(R.id.timeTv)).setText(bean.getTime());
//        ((TextView) holder.getView(R.id.phoneTv)).setText(bean.getPhone());

/*
        TextView tv = holder.getView(R.id.titleTv);
        tv.setText(...);

       ImageView view = getView(viewId);
       Imageloader.getInstance().loadImag(view.url);
*/
    }
}