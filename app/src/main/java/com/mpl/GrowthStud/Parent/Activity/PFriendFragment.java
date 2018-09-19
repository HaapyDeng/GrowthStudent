package com.mpl.GrowthStud.Parent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.MessageActivity;


public class PFriendFragment extends Fragment implements View.OnClickListener {
    private ImageButton ib_message;

    public PFriendFragment() {
        // Required empty public constructor
    }


    public static PFriendFragment newInstance(String name) {
        PFriendFragment fragment = new PFriendFragment();
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
        View root = inflater.inflate(R.layout.fragment_pfriend, container, false);
        ib_message = root.findViewById(R.id.ib_message);
        ib_message.setOnClickListener(this);
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_message:
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
        }
    }
}
