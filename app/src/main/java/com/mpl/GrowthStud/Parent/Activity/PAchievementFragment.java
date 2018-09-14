package com.mpl.GrowthStud.Parent.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpl.GrowthStud.R;


public class PAchievementFragment extends Fragment {
    public PAchievementFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        PAchievementFragment fragment = new PAchievementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pachievement, container, false);
    }
}

