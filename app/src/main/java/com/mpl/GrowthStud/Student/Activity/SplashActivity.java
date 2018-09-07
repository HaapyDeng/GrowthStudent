package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.mpl.GrowthStud.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        countDownTimer.start();
    }

    private CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
