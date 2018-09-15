package com.mpl.GrowthStud.Student.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.ConstomDialog;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private int scope;
    private ImageButton back;
    private LinearLayout ll_fix_password, ll_upgrade, ll_clear, ll_about;
    private TextView tv_version, tv_cache, log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        scope = bundle.getInt("scope", 0);
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
                Intent intent1 = new Intent(this, ChangePasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("scope", scope);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.ll_upgrade:
                //实例化自定义对话框
                final ConstomDialog mdialog = new ConstomDialog(this);
                mdialog.setTv("没有新版本升级");
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
                Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
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
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.TAG_EXIT, true);
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
        mdialog.setTv("清空缓存");
        //对话框中退出按钮事件
        mdialog.setOnExitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果对话框处于显示状态
                if (mdialog.isShowing()) {
                    NetworkUtils.clearAllCache(SettingActivity.this);
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
