package com.mpl.GrowthStud.Parent.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.AboutActivity;
import com.mpl.GrowthStud.Student.Activity.MainActivity;
import com.mpl.GrowthStud.Student.Tools.ConstomDialog;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

public class PSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private LinearLayout ll_fix_password, ll_upgrade, ll_clear, ll_about;
    private TextView tv_version, tv_cache, log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        ll_fix_password = findViewById(R.id.ll_fix_password);
        ll_fix_password.setOnClickListener(this);

        ll_upgrade = findViewById(R.id.ll_upgrade);
        ll_upgrade.setOnClickListener(this);

        ll_clear = findViewById(R.id.ll_clear);
        ll_clear.setOnClickListener(this);

        ll_about = findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);

        tv_version = findViewById(R.id.tv_version);

        tv_cache = findViewById(R.id.tv_cache);

        try {
            tv_version.setText(NetworkUtils.getVersionName(this));
            tv_cache.setText(NetworkUtils.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log_out = findViewById(R.id.log_out);
        log_out.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_fix_password:
                Intent intent1 = new Intent(this, PChangePasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_upgrade:
                //实例化自定义对话框
                final ConstomDialog mdialog = new ConstomDialog(this);
                mdialog.setTv("已经是最新版本");
                //对话框中确认按钮事件
                mdialog.setOnExitListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果对话框处于显示状态
                        if (mdialog.isShowing()) {
                            mdialog.dismiss();
                        }

                    }
                });
                //对话框中取消按钮事件
                mdialog.setOnCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mdialog != null && mdialog.isShowing()) {
                            //关闭对话框
                            mdialog.dismiss();
                        }
                    }
                });
                mdialog.show();
                break;
            case R.id.ll_clear:
                showDialog();
                break;
            case R.id.ll_about:
                Intent intent = new Intent(PSettingActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                doLoginOut();
                break;
        }

    }

    private void doLoginOut() {
        //实例化自定义对话框
        final ConstomDialog mdialog = new ConstomDialog(this);
        mdialog.setTv("确认退出");
        //对话框中确认按钮事件
        mdialog.setOnExitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果对话框处于显示状态
                if (mdialog.isShowing()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    SharedPreferences sp2 = getSharedPreferences("tag", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sp2.edit();
                    editor2.putInt("tag", 0);
                    editor2.commit();
//                    SharedPreferences sharedPreferences3 = getSharedPreferences("userid", MODE_PRIVATE);
//                    if (sharedPreferences3.getBoolean("have", false)) {
//                        SharedPreferences.Editor editor3 = sharedPreferences3.edit();
//                        editor3.clear();
//                        editor3.commit();
//                    }
                    Intent intent = new Intent(PSettingActivity.this, PMainActivity.class);
                    intent.putExtra(PMainActivity.TAG_EXIT, true);
                    startActivity(intent);
                    //关闭对话框
                    mdialog.dismiss();
                }

            }
        });
        //对话框中取消按钮事件
        mdialog.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdialog != null && mdialog.isShowing()) {
                    //关闭对话框
                    mdialog.dismiss();
                }
            }
        });
        mdialog.show();

    }

    private void showDialog() {
        //实例化自定义对话框
        final ConstomDialog mdialog = new ConstomDialog(this);
        mdialog.setTv("是否清除缓存？");
        //对话框中退出按钮事件
        mdialog.setOnExitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果对话框处于显示状态
                if (mdialog.isShowing()) {
                    NetworkUtils.clearAllCache(PSettingActivity.this);
                    //关闭对话框
                    mdialog.dismiss();
                }

            }
        });
        //对话框中取消按钮事件
        mdialog.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdialog != null && mdialog.isShowing()) {
                    //关闭对话框
                    mdialog.dismiss();
                }
            }
        });
        mdialog.show();

    }
}
