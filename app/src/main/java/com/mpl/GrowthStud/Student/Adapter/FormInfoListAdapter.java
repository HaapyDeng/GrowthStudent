package com.mpl.GrowthStud.Student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.FormAnswerBean;
import com.mpl.GrowthStud.Student.Bean.FormInfoListItem;
import com.mpl.GrowthStud.Student.Bean.FormListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormInfoListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<FormInfoListItem> mDatas;
    private Context mContext;
    private List<FormAnswerBean> answerData = new ArrayList<>();// 存储的EditText值

    public FormInfoListAdapter(Context context, List<FormInfoListItem> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if (convertView == null) {
        convertView = mInflater.inflate(R.layout.form_info_list_item, parent, false); //加载布局
        holder = new ViewHolder();
        holder.tv_lable_title = convertView.findViewById(R.id.tv_lable_title);
        holder.et_content = convertView.findViewById(R.id.et_content);
        convertView.setTag(holder);
        holder.et_content.setTag(position);
//        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
//            holder = (ViewHolder) convertView.getTag();
//        }
        FormInfoListItem formInfoListItem = mDatas.get(position);

        holder.tv_lable_title.setText(formInfoListItem.getLabel() + "：");
        JSONObject answer = formInfoListItem.getAnswers();
        String id = formInfoListItem.getId();
        String answerText = "";
        try {
            answerText = answer.getString(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.et_content.setText(answerText);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_lable_title;
        TextView et_content;

    }

}