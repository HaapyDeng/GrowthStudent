package com.mpl.GrowthStud.Parent.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpl.GrowthStud.Parent.Bean.PstudentApplyListItem;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.ListViewAdapter;
import com.mpl.GrowthStud.Student.Adapter.ViewHolder;
import com.mpl.GrowthStud.Student.Bean.ChildParentListItem;

import java.util.List;


public class ParentChildApplyListViewAdapter extends ListViewAdapter<PstudentApplyListItem> {
    private Context context1;
    private List<PstudentApplyListItem> mdatas;
    private int mposition;
    private OnClickListener onClickListener;

    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public ParentChildApplyListViewAdapter(Context context, List<PstudentApplyListItem> datas) {
        super(context, datas, R.layout.parent_child_item);
        context1 = context;
        mdatas = datas;
        onClickListener = (OnClickListener) context;

    }

    @SuppressLint("NewApi")
    @Override
    public void convert(final ViewHolder holder, PstudentApplyListItem bean) {
        if (bean.getGender() == 1) {
            ((ImageView) holder.getView(R.id.iv_head)).setBackground(context1.getResources().getDrawable(R.mipmap.head_student_boy));
        } else {
            ((ImageView) holder.getView(R.id.iv_head)).setBackground(context1.getResources().getDrawable(R.mipmap.head_student_girl));
        }
        ((TextView) holder.getView(R.id.tv_name)).setText(bean.getUsername());
        ((TextView) holder.getView(R.id.tv_phone)).setText(bean.getSchool() + bean.getGrade() + bean.getClassroom_name());
        if (((CheckBox) holder.getView(R.id.main_list_item_checkBox)).isChecked()) {//状态选中
            ((CheckBox) holder.getView(R.id.main_list_item_checkBox)).setChecked(true);
        } else {
            ((CheckBox) holder.getView(R.id.main_list_item_checkBox)).setChecked(false);
        }
        if (mposition == 0) {
            ((CheckBox) holder.getView(R.id.main_list_item_checkBox)).setChecked(true);
        }
        ((CheckBox) holder.getView(R.id.main_list_item_checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    onClickListener.setSelectedNum(mposition);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        mposition = position;
        return mposition;
    }

    public interface OnClickListener {
        public void setSelectedNum(int num);
    }
}