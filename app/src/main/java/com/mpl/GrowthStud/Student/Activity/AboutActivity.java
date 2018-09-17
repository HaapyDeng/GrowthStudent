package com.mpl.GrowthStud.Student.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

public class AboutActivity extends AppCompatActivity {
    private LinearLayout back;
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_version = findViewById(R.id.tv_version);
        try {
            tv_version.setText(NetworkUtils.getVersionName(AboutActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
