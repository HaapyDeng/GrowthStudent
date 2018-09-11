package com.mpl.GrowthStud.Student.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mpl.GrowthStud.R;


public class FoundFragment extends Fragment implements View.OnClickListener {


    private TextView tv_evaluate, tv_echievement, tv_road;
    private ImageButton ib_message;

    public static FoundFragment newInstance(String name) {
        FoundFragment fragment = new FoundFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_found, container, false);
        tv_evaluate = root.findViewById(R.id.tv_evaluate);
        tv_evaluate.setOnClickListener(this);

        ib_message = root.findViewById(R.id.ib_message);
        ib_message.setOnClickListener(this);

        tv_echievement = root.findViewById(R.id.tv_echievement);
        tv_echievement.setOnClickListener(this);

        tv_road = root.findViewById(R.id.tv_road);
        tv_road.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_message:
                Intent intent4 = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent4);
                break;
            case R.id.tv_evaluate:
                Intent intent = new Intent(getActivity(), EvaluateActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_echievement:
                Intent intent2 = new Intent(getActivity(), EchievementActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_road:
                Intent intent3 = new Intent(getActivity(), RoadActivity.class);
                startActivity(intent3);
                break;

        }
    }
}
