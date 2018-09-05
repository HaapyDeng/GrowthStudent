package com.mpl.GrowthStud.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.mpl.GrowthStud.R;

public class VideoPlayActivity extends AppCompatActivity {
    private String path;
    private VideoView videoView;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        path = bundle.getString("path");
        videoView = findViewById(R.id.video_view);
        // 播放相应的视频
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
