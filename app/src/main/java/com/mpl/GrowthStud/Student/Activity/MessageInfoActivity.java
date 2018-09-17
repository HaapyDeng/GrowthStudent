package com.mpl.GrowthStud.Student.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;

public class MessageInfoActivity extends AppCompatActivity {
    private String title, time, content;
    private LinearLayout back;
    private TextView tv_title, tv_time, tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        time = bundle.getString("time");
        content = bundle.getString("content");
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);

        tv_time = findViewById(R.id.tv_time);
        tv_time.setText(time);

        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(content);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
