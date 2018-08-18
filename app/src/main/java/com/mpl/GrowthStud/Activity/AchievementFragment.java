package com.mpl.GrowthStud.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpl.GrowthStud.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementFragment extends Fragment {


    public AchievementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievement, container, false);
    }

    public static Fragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        AchievementFragment fragment = new AchievementFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
