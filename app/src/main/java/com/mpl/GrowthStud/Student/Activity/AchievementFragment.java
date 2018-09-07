package com.mpl.GrowthStud.Student.Activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpl.GrowthStud.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementFragment extends Fragment implements View.OnClickListener {

    private AchieveTodoFragment fragment1;
    private AchieveCompletFragment fragment2;
    private TextView completed, todo;

    public AchievementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievement, container, false);
        completed = root.findViewById(R.id.tv_complete);
        completed.setOnClickListener(this);

        todo = root.findViewById(R.id.tv_todo);
        todo.setOnClickListener(this);
        selectFragment(0);
        // Inflate the layout for this fragment
        return root;
    }


    public static Fragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        AchievementFragment fragment = new AchievementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complete:
                selectFragment(1);
                break;
            case R.id.tv_todo:
                selectFragment(0);
                break;
        }
    }
    // 切换Fragment

    private void selectFragment(int i) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        hideFragment(transaction);

        changeView(i);// 设置选项颜色

        switch (i) {

            case 0:

                if (fragment1 == null) {

                    fragment1 = new AchieveTodoFragment();

                    transaction.add(R.id.fragment, fragment1);

                }

                transaction.show(fragment1);

                break;


            case 1:

                if (fragment2 == null) {

                    fragment2 = new AchieveCompletFragment();

                    transaction.add(R.id.fragment, fragment2);

                }

                transaction.show(fragment2);

                break;

        }

        transaction.commit();

    }
    // 隐藏fragment

    private void hideFragment(FragmentTransaction transaction) {

        if (fragment1 != null) {

            transaction.hide(fragment1);

        }

        if (fragment2 != null) {

            transaction.hide(fragment2);

        }


    }
    //改变字体和背景色状态

    @SuppressLint("NewApi")

    private void changeView(int i) {

        if (i == 0) {

            //设置背景色及字体颜色


            completed.setTextColor(getResources().getColor(R.color.grey));


            todo.setTextColor(getResources().getColor(R.color.getstarinfo));

        } else if (i == 1) {


            completed.setTextColor(getResources().getColor(R.color.getstarinfo));


            todo.setTextColor(getResources().getColor(R.color.grey));
        }
    }
}
