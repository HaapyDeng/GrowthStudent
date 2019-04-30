package com.mpl.GrowthStud.Student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mpl.GrowthStud.Student.Activity.TuWenInfoActivity;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.DownImage;
import com.mpl.GrowthStud.Student.Tools.DownImgeJson;
import com.mpl.GrowthStud.Student.View.SquareLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private List<String> data;
    private Context context;

    public ImageAdapter(Context context, List<String> image) {
        this.context = context;
        this.data = image;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {

        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
//            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                    android.view.ViewGroup.LayoutParams.FILL_PARENT,
//                    120);//传入自己需要的宽高
//            convertView.setLayoutParams(param);
            convertView = LayoutInflater.from(context).inflate(R.layout.image_grid_view, null);
            holder.iv = (SquareLayout) convertView.findViewById(R.id.iv_image);
            DownImgeJson downImgeJson = null;
            downImgeJson = new DownImgeJson(data.get(position));
            downImgeJson.loadImage(new DownImgeJson.ImageCallBack() {

                @SuppressLint("NewApi")
                @Override
                public void getDrawable(Drawable drawable) {
                    (holder.iv).setBackground(drawable);
                }
            });

        }
        return convertView;
    }

    class Holder {
        SquareLayout iv;
    }
}
