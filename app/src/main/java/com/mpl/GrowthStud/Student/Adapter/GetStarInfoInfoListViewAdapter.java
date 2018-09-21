package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.GetStarInfoInfoItem;
import com.mpl.GrowthStud.Student.Bean.GetStarInfoItem;

import java.util.List;


public class GetStarInfoInfoListViewAdapter extends ListViewAdapter<GetStarInfoInfoItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public GetStarInfoInfoListViewAdapter(Context context, List<GetStarInfoInfoItem> datas) {
        super(context, datas, R.layout.get_start_info_info_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, GetStarInfoInfoItem bean) {
        if (bean.getType().equals("1")) {
            ((TextView) holder.getView(R.id.tv_tpye)).setText("文本");
        } else if (bean.getType().equals("2")) {
            ((TextView) holder.getView(R.id.tv_tpye)).setText("图文");
        } else if (bean.getType().equals("3")) {
            ((TextView) holder.getView(R.id.tv_tpye)).setText("视频");
        } else if (bean.getType().equals("4")) {
            ((TextView) holder.getView(R.id.tv_tpye)).setText("问卷");
        } else if (bean.getType().equals("5")) {
            ((TextView) holder.getView(R.id.tv_tpye)).setText("系统");
        }
        ((TextView) holder.getView(R.id.tv_time)).setText(bean.getUpdated_at());

        ((TextView) holder.getView(R.id.tv_title)).setText(bean.getName());

        ((TextView) holder.getView(R.id.tv_score)).setText(bean.getTotal_point());

        ((TextView) holder.getView(R.id.tv_name)).setText(bean.getComplete_name());
        if (bean.getTask_star().equals("5")) {
            ((ImageView) holder.getView(R.id.star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star3)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star4)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star5)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("4")) {
            ((ImageView) holder.getView(R.id.star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star3)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star4)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("3")) {
            ((ImageView) holder.getView(R.id.star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star3)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("2")) {
            ((ImageView) holder.getView(R.id.star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.star2)).setVisibility(View.VISIBLE);
        } else if (bean.getTask_star().equals("1")) {
            ((ImageView) holder.getView(R.id.star1)).setVisibility(View.VISIBLE);
        }

        if (bean.getStar().equals("5")) {
            ((ImageView) holder.getView(R.id.star1)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star2)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star3)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star4)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star5)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
        } else if (bean.getStar().equals("4")) {
            ((ImageView) holder.getView(R.id.star1)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star2)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star3)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star4)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
        } else if (bean.getStar().equals("3")) {
            ((ImageView) holder.getView(R.id.star1)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star2)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star3)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
        } else if (bean.getStar().equals("2")) {
            ((ImageView) holder.getView(R.id.star1)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.star2)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
        } else if (bean.getStar().equals("1")) {
            ((ImageView) holder.getView(R.id.star1)).setBackgroundDrawable(context1.getResources().getDrawable(R.mipmap.star_big));
        }


    }
}