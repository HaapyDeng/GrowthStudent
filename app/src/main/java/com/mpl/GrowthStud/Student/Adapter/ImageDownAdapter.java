package com.mpl.GrowthStud.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mpl.GrowthStud.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageDownAdapter extends BaseAdapter {
    private List<String> data;
    private Context context;

    public ImageDownAdapter(Context context, List<String> image) {
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
        View view;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
//            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                    android.view.ViewGroup.LayoutParams.FILL_PARENT,
//                    120);//传入自己需要的宽高
//            convertView.setLayoutParams(param);
            view = LayoutInflater.from(context).inflate(R.layout.image_grid_view, null);
            holder.iv = (ImageView) view.findViewById(R.id.iv_image);
//            DownImgeJson downImgeJson = null;
//            downImgeJson = new DownImgeJson(data.get(position));
//            downImgeJson.loadImage(new DownImgeJson.ImageCallBack() {
//
//                @SuppressLint("NewApi")
//                @Override
//                public void getDrawable(Drawable drawable) {
//                    (holder.iv).setBackground(drawable);
//                }
//            });
            Picasso.with(context).load(data.get(position).toString().trim()).into(holder.iv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder {
        ImageView iv;
    }
}
