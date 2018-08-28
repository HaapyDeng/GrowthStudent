package com.mpl.GrowthStud.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mpl.GrowthStud.R;

public class EvaluateActivity extends FragmentActivity implements View.OnClickListener {

    private TextView get_star_info, get_score_info;
    private GetStarInfoFragment fragment1;// 第一个操作界面

    private GetScoreInfoFragment fragment2;// 第二个操作界面

    private android.app.FragmentManager fm;//管理器
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        initviews();
    }

    private void initviews() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        get_star_info = findViewById(R.id.get_star_info);
        get_star_info.setOnClickListener(this);
        get_score_info = findViewById(R.id.get_score_info);
        get_score_info.setOnClickListener(this);
        selectFragment(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.get_star_info:

                selectFragment(0);

                break;

            case R.id.get_score_info:

                selectFragment(1);

                break;

            default:

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

                    fragment1 = new GetStarInfoFragment();

                    transaction.add(R.id.fragment, fragment1);

                }

                transaction.show(fragment1);

                break;


            case 1:

                if (fragment2 == null) {

                    fragment2 = new GetScoreInfoFragment();

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

            get_star_info.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            get_star_info.setTextColor(getResources().getColor(R.color.text));

            get_score_info.setBackground(getDrawable(R.drawable.textview_border));

            get_score_info.setTextColor(getResources().getColor(R.color.getstarinfo));

        } else if (i == 1) {

            get_score_info.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            get_score_info.setTextColor(getResources().getColor(R.color.text));

            get_star_info.setBackground(getDrawable(R.drawable.textview_border));

            get_star_info.setTextColor(getResources().getColor(R.color.getstarinfo));

        }

    }
}
