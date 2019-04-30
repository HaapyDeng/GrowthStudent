package com.mpl.GrowthStud.Student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.CloudPhotoBean;
import com.mpl.GrowthStud.Student.Bean.GetStarInfoItem;
import com.mpl.GrowthStud.Student.Tools.DownImage;
import com.zhy.base.imageloader.ImageLoader;

import java.util.List;


public class CloudPhotoListAdapter extends ListViewAdapter<CloudPhotoBean> {

    //为了让子类访问，于是将属性设置为protected
    protected Context mContext;
    protected LayoutInflater mInflater;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public CloudPhotoListAdapter(Context context, List<CloudPhotoBean> datas) {
        super(context, datas, R.layout.cloud_photo_list_item);
        mContext = context;
    }


    @Override
    public void convert(ViewHolder holder, CloudPhotoBean cloudPhotoBean) {
        final ImageView iv_img = holder.getView(R.id.iv_img);
        CheckBox item_cb = holder.getView(R.id.item_cb);
        DownImage downImage = new DownImage(cloudPhotoBean.getName());
        downImage.loadImage(new DownImage.ImageCallBack() {
            @Override
            public void getDrawable(Drawable drawable) {
                iv_img.setImageDrawable(drawable);
            }
        });
        if (cloudPhotoBean.isChcked()) {
            item_cb.setChecked(true);
        } else {
            item_cb.setChecked(false);
        }

    }


//    private class ViewHolder {
//        ImageView iv_img;
//        CheckBox item_cb;
//
//
//    }

}