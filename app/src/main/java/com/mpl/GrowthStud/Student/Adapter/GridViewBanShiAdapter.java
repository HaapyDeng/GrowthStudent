package com.mpl.GrowthStud.Student.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.TuWenActivity;
import com.mpl.GrowthStud.Student.Bean.BanShiItem;
import com.mpl.GrowthStud.Student.Tools.DownImage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GridViewBanShiAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private List<BanShiItem> mList;
    private LayoutInflater inflater;
    private int selectorPosition;

    public GridViewBanShiAdapter(Context mContext, List<BanShiItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.banshi_gridview_item, parent, false);
        final ImageView iv = (ImageView) convertView.findViewById(R.id.pic_iv);
        LinearLayout ll = convertView.findViewById(R.id.ll);
        ImageView iv_bg = convertView.findViewById(R.id.iv_bg);
//动态设置GridView图片的宽高,间距是10，每行3列，计算每个图片的宽度，高度与宽度一致
        int width = (TuWenActivity.screenWidth - (3 * Dp2Px(mContext, 10))) / 3;
//        width = px2dip(getActivity(), width);
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(width, width);
        ll.setLayoutParams(param);
        String imgUrl = mList.get(position).getImge();
        Log.d("imgUrl==>>", imgUrl);
        DownImage downImage = new DownImage(imgUrl);
        downImage.loadImage(new DownImage.ImageCallBack() {
            @Override
            public void getDrawable(Drawable drawable) {
                iv.setImageDrawable(drawable);
            }
        });
        if (selectorPosition == position) {
            iv_bg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.banshi_bg));

        } else {
            //其他的恢复原来的状态
//            iv_bg.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.white));
        }
        return convertView;
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }
}
