package com.mpl.GrowthStud.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.Student.Bean.AchieveCompletItem;
import com.mpl.GrowthStud.Student.Bean.AchieveToDoItem;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.DownImage;

import java.util.List;


public class AchieveCompletListViewAdapter extends ListViewAdapter<AchieveCompletItem> {
    private Context context1;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public AchieveCompletListViewAdapter(Context context, List<AchieveCompletItem> datas) {
        super(context, datas, R.layout.complet_achieve_item);
        context1 = context;
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, AchieveCompletItem bean) {
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
            ((TextView) holder.getView(R.id.status)).setText("待学生重做");
        } else if (bean.getStatus().equals("1")) {
            ((TextView) holder.getView(R.id.status)).setText("待完成");
        } else if (bean.getStatus().equals("2")) {
            ((TextView) holder.getView(R.id.status)).setText("待家长审核");
        } else if (bean.getStatus().equals("3")) {
            ((TextView) holder.getView(R.id.status)).setText("待老师审核");
        } else if (bean.getStatus().equals("4")) {
            ((TextView) holder.getView(R.id.status)).setText("待评星");
        } else if (bean.getStatus().equals("5")) {
            ((TextView) holder.getView(R.id.status)).setText("已评星");
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
        if (bean.getStar().equals("5")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star1)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star3)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star4)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star5)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
        } else if (bean.getStar().equals("4")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star1)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star3)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star4)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
        } else if (bean.getStar().equals("3")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star1)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star3)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.INVISIBLE);
            ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
        } else if (bean.getStar().equals("2")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star1)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star2)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.INVISIBLE);
            ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.INVISIBLE);
            ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
        } else if (bean.getStar().equals("1")) {
            ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_star1)).setBackground(context1.getResources().getDrawable(R.mipmap.star_big));
            ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.INVISIBLE);
            ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.INVISIBLE);
            ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.INVISIBLE);
            ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
        } else if (bean.getStar().equals("0")) {
            if (bean.getTask_star().equals("0")) {
                ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
            } else if (bean.getTask_star().equals("1")) {
                ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
            } else if (bean.getTask_star().equals("2")) {
                ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
            } else if (bean.getTask_star().equals("3")) {
                ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.INVISIBLE);
                ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
            } else if (bean.getTask_star().equals("4")) {
                ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.INVISIBLE);
            } else if (bean.getTask_star().equals("5")) {
                ((ImageView) holder.getView(R.id.iv_star1)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star2)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star3)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star4)).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_star5)).setVisibility(View.VISIBLE);
            }
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