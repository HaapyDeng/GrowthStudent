package com.mpl.GrowthStud.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mpl.GrowthStud.Student.Activity.TuWenActivity;
import com.mpl.GrowthStud.Student.Bean.MainConstant;
import com.mpl.GrowthStud.R;

import java.util.List;

public class GridViewAdapter extends android.widget.BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;

    public GridViewAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        //return mList.size() + 1;//因为最后多了一个添加图片的ImageView
        int count = mList == null ? 1 : mList.size() + 1;
        if (count > MainConstant.MAX_SELECT_PIC_NUM) {
            return mList.size();
        } else {
            return count;
        }
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
        convertView = inflater.inflate(R.layout.grid_item, parent, false);
        ImageView iv = (ImageView) convertView.findViewById(R.id.pic_iv);
//动态设置GridView图片的宽高,间距是10，每行3列，计算每个图片的宽度，高度与宽度一致
        int width = (TuWenActivity.screenWidth - (3 * Dp2Px(mContext, 10))) / 3;
        //width = px2dip(getActivity(),width);
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(width, width);
        iv.setLayoutParams(param);

        if (position < mList.size()) {
            //代表+号之前的需要正常显示图片
            String picUrl = mList.get(position); //图片路径
            Glide.with(mContext).load(picUrl).into(iv);
        } else {
            iv.setImageResource(R.mipmap.add_camera);//最后一个显示加号图片
        }
        return convertView;
    }
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

