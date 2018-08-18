package com.mpl.GrowthStud.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mpl.GrowthStud.R;

public class ChooseGradeActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_youeryuan, ll_xiaoxue, ll_chuzhong, ll_gaozhong;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_grade);
        initViews();
    }

    private void initViews() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        ll_youeryuan = findViewById(R.id.ll_youeryuan);
        ll_youeryuan.setOnClickListener(this);

        ll_xiaoxue = findViewById(R.id.ll_xiaoxue);
        ll_xiaoxue.setOnClickListener(this);

        ll_chuzhong = findViewById(R.id.ll_chuzhong);
        ll_chuzhong.setOnClickListener(this);

        ll_gaozhong = findViewById(R.id.ll_gaozhong);
        ll_gaozhong.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_youeryuan:
                Intent intent = new Intent(this, ActivateKinderStdActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_xiaoxue:
                Intent intent2 = new Intent(this, ActivateOtherStdActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_chuzhong:
                Intent intent3 = new Intent(this, ActivateOtherStdActivity.class);
                startActivity(intent3);
                break;
            case R.id.ll_gaozhong:
                Intent intent4 = new Intent(this, ActivateOtherStdActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
