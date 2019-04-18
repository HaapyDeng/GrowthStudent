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
import com.mpl.GrowthStud.Student.Bean.FormListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<FormListItem> mDatas;
    private Context mContext;
    private List<FormAnswerBean> answerData = new ArrayList<>();// 存储的EditText值

    public FormListAdapter(Context context, List<FormListItem> datas) {
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
        convertView = mInflater.inflate(R.layout.form_list_item, parent, false); //加载布局
        holder = new ViewHolder();
        holder.tv_lable_title = convertView.findViewById(R.id.tv_lable_title);
        holder.et_content = convertView.findViewById(R.id.et_content);
        convertView.setTag(holder);
        holder.et_content.setTag(position);
//        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
//            holder = (ViewHolder) convertView.getTag();
//        }
        FormListItem formListItem = mDatas.get(position);

//        if (mDatas.get(position).getId().equals("0")) {
//            return convertView;
//        }
        holder.tv_lable_title.setText(formListItem.getLabel() + "：");
        String prompt = formListItem.getPrompt();
        holder.et_content.setHint(formListItem.getPrompt());
        holder.mTextWatcher = new MyTextChangeListener(holder);
        holder.et_content.addTextChangedListener(holder.mTextWatcher);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_lable_title;
        EditText et_content;
        MyTextChangeListener mTextWatcher;

    }

    private class MyTextChangeListener implements TextWatcher {
        private ViewHolder holder;
        private FormAnswerBean formAnswerBean;

        public MyTextChangeListener(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int position = (Integer) holder.et_content.getTag();
            formAnswerBean = new FormAnswerBean(mDatas.get(position).getId(), holder.et_content.getText().toString().trim());
            if ((position + 1) <= answerData.size()) {
                answerData.set(position, formAnswerBean);
            } else {
                answerData.add(formAnswerBean);
            }

        }


        @Override
        public void afterTextChanged(Editable editable) {
            Log.d("editable", "" + editable);
        }


    }

    public List<FormAnswerBean> getData() {
        return answerData;
    }
}