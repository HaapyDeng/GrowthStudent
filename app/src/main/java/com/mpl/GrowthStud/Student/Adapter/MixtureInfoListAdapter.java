package com.mpl.GrowthStud.Student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.TuWenInfoActivity;
import com.mpl.GrowthStud.Student.Bean.FormInfoListItem;
import com.mpl.GrowthStud.Student.Bean.MixtureInfoListItem;
import com.mpl.GrowthStud.Student.View.ChildGridView;
import com.mpl.GrowthStud.Student.View.ChildListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MixtureInfoListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MixtureInfoListItem> mDatas;
    private Context mContext;
    private List<FormInfoListItem> formInfoListItems = new ArrayList<>();
    private String[] image = new String[]{};
    private List<String> listImage = new ArrayList<>();

    public MixtureInfoListAdapter(Context context, List<MixtureInfoListItem> datas) {
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
        holder = new ViewHolder();

        switch (mDatas.get(position).getT()) {
            case 1:
                convertView = mInflater.inflate(R.layout.mix_tuwen_view, parent, false); //加载布局
                holder.gridview = convertView.findViewById(R.id.gridview);
                holder.tv_answer = convertView.findViewById(R.id.tv_answer);
                JSONObject object1 = mDatas.get(position).getObject();
                try {
                    String answer = object1.getString("answer");
                    holder.tv_answer.setText(answer);
                    image = new String[object1.getJSONArray("image").length()];
                    JSONArray imageArray = object1.getJSONArray("image");
                    for (int i = 0; i < object1.getJSONArray("image").length(); i++) {
                        JSONObject objectoo = imageArray.getJSONObject(i);
                        listImage.add(objectoo.toString());
                    }
                    Log.d("listimage==>>>", listImage.toString());
                    holder.gridview.setAdapter(new ImageAdapter(mContext, listImage));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 2:
                convertView = mInflater.inflate(R.layout.mix_form_listview, parent, false); //加载布局
                holder.listView = convertView.findViewById(R.id.listview);
                JSONObject object = mDatas.get(position).getObject();
                try {
                    JSONObject answers = object.getJSONObject("answers");
                    JSONArray itemArray = object.getJSONArray("item");
                    formInfoListItems = new ArrayList<>();
                    for (int i = 0; i < itemArray.length(); i++) {
                        JSONObject object2 = null;
                        try {
                            object2 = itemArray.getJSONObject(i);
                            String id = object2.getString("id");
                            String label = object2.getString("label");
                            String prompt = object2.getString("prompt");
                            int type = object2.getInt("type");
                            String options = object2.getString("options");
                            int order = object2.getInt("order");
                            FormInfoListItem formInfoListItem = new FormInfoListItem(id, label, prompt, type, options, order, answers);
                            formInfoListItems.add(formInfoListItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("mdata===>>>", "" + formInfoListItems.size());
                    FormInfoListAdapter formInfoListAdapter = new FormInfoListAdapter(mContext, formInfoListItems);
                    holder.listView.setAdapter(formInfoListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }


        return convertView;
    }

    private class ViewHolder {
        ChildListView listView;
        TextView tv_answer;
        ChildGridView gridview;

    }

}