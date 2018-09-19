package com.mpl.GrowthStud.Parent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.EchievementActivity;
import com.mpl.GrowthStud.Student.Activity.EvaluateActivity;
import com.mpl.GrowthStud.Student.Activity.MessageActivity;
import com.mpl.GrowthStud.Student.Activity.RoadActivity;


public class PFoundFragment extends Fragment implements View.OnClickListener {
    private TextView tv_evaluate, tv_echievement, tv_road, tv_jiliance;
    private ImageButton ib_message;

    public PFoundFragment() {
        // Required empty public constructor
    }

    public static PFoundFragment newInstance(String param1) {
        PFoundFragment fragment = new PFoundFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pfound, container, false);
        tv_evaluate = root.findViewById(R.id.tv_evaluate);
        tv_evaluate.setOnClickListener(this);

        ib_message = root.findViewById(R.id.ib_message);
        ib_message.setOnClickListener(this);

        tv_jiliance = root.findViewById(R.id.tv_jiliance);
        tv_jiliance.setOnClickListener(this);

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
                Intent intent = new Intent(getActivity(), PEvaluateActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_jiliance:
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
