package com.mpl.GrowthStud.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Tools.LineEditText;
import com.mpl.GrowthStud.Tools.NetworkUtils;

public class ActivateKinderStdActivity extends Activity implements View.OnClickListener {
    private ImageButton btn_back;
    private LineEditText et_card_num;
    private Button btn_active;
    private String cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_kinder_std);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        et_card_num = findViewById(R.id.et_card_num);

        btn_active = findViewById(R.id.btn_active);
        btn_active.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_active:
                cardId = et_card_num.getText().toString().trim();
                if (cardId.length() <= 0) {
                    Toast.makeText(this, R.string.cardid_not_null, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!NetworkUtils.checkNetWork(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
                doActive(cardId);
        }

    }

    private void doActive(String cardId) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
