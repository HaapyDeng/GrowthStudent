package com.mpl.GrowthStud.Student.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mpl.GrowthStud.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment extends Fragment {

    public PagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        String s = bundle.getString("json");
        View view = null;
        JSONObject object = null;
        try {
            object = new JSONObject(s);
            int t = object.getInt("t");//1：成就    2：表单
            int type = 0;
            bundle.putInt("t", t);
            switch (t) {
                case 1:
                    type = object.getInt("type");//成就{ 1 :文字  2：图文 3：视频}
                    switch (type) {
                        case 1:
                            view = inflater.inflate(R.layout.mix_wenzi_item, container, false);
                            toWenzi(view);
                            break;
                        case 2:
                            view = inflater.inflate(R.layout.mix_tuwen_item, container, false);
                            break;
                        case 3:
                            view = inflater.inflate(R.layout.mix_video_item, container, false);
                            break;
                    }
                    break;
                case 2://表单
                    view = inflater.inflate(R.layout.mix_form_item, container, false);
                    break;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return view;
    }

    private void toWenzi(View view) {

    }

}
