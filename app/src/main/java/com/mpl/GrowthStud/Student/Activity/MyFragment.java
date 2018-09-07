package com.mpl.GrowthStud.Student.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpl.GrowthStud.R;

public class MyFragment extends Fragment {

    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        // Inflate the layout for this fragment
        return root;
    }

}
