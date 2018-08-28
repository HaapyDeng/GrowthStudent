package com.mpl.GrowthStud.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.View.RingView;

import java.util.ArrayList;
import java.util.List;

public class EchievementActivity extends FragmentActivity implements View.OnClickListener {
    private AchieveCompletFragment fragment1;
    private AchieveUnderwayFragment fragment2;
    private AchieveTodoFragment fragment3;
    private android.app.FragmentManager fm;//管理器
    private TextView completed, underway, todo;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echievement);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        RingView ringView = (RingView) findViewById(R.id.ringView);
        ringView.setProgress(500, 500 - 350);
        ringView.setReminderColor(Color.parseColor("#3699ED"));
        ringView.setProgressColor(Color.parseColor("#EEEEEE"));
        ringView.setCircleWidth(40);
        completed = findViewById(R.id.completed);
        completed.setOnClickListener(this);

        underway = findViewById(R.id.underway);
        underway.setOnClickListener(this);

        todo = findViewById(R.id.todo);
        todo.setOnClickListener(this);
        selectFragment(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.completed:
                selectFragment(0);
                break;
            case R.id.underway:
                selectFragment(1);
                break;
            case R.id.todo:
                selectFragment(2);
                break;
        }
    }

    // 切换Fragment

    private void selectFragment(int i) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        hideFragment(transaction);

        changeView(i);// 设置选项颜色

        switch (i) {

            case 0:

                if (fragment1 == null) {

                    fragment1 = new AchieveCompletFragment();

                    transaction.add(R.id.fragment, fragment1);

                }

                transaction.show(fragment1);

                break;


            case 1:

                if (fragment2 == null) {

                    fragment2 = new AchieveUnderwayFragment();

                    transaction.add(R.id.fragment, fragment2);

                }

                transaction.show(fragment2);

                break;
            case 2:

                if (fragment3 == null) {

                    fragment3 = new AchieveTodoFragment();

                    transaction.add(R.id.fragment, fragment3);

                }

                transaction.show(fragment3);

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
        if (fragment3 != null) {

            transaction.hide(fragment3);

        }


    }
    //改变字体和背景色状态

    @SuppressLint("NewApi")

    private void changeView(int i) {

        if (i == 0) {

            //设置背景色及字体颜色

            completed.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            completed.setTextColor(getResources().getColor(R.color.text));

            underway.setBackground(getDrawable(R.drawable.textview_border));

            underway.setTextColor(getResources().getColor(R.color.getstarinfo));

            todo.setBackground(getDrawable(R.drawable.textview_border));

            todo.setTextColor(getResources().getColor(R.color.getstarinfo));

        } else if (i == 1) {

            underway.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            underway.setTextColor(getResources().getColor(R.color.text));

            completed.setBackground(getDrawable(R.drawable.textview_border));

            completed.setTextColor(getResources().getColor(R.color.getstarinfo));

            todo.setBackground(getDrawable(R.drawable.textview_border));

            todo.setTextColor(getResources().getColor(R.color.getstarinfo));

        } else if (i == 2) {
            todo.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            todo.setTextColor(getResources().getColor(R.color.text));

            completed.setBackground(getDrawable(R.drawable.textview_border));

            completed.setTextColor(getResources().getColor(R.color.getstarinfo));

            underway.setBackground(getDrawable(R.drawable.textview_border));

            underway.setTextColor(getResources().getColor(R.color.getstarinfo));
        }

    }
}
