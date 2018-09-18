package com.mpl.GrowthStud.Parent.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mpl.GrowthStud.Parent.View.WheelView;
import com.mpl.GrowthStud.R;

import java.util.Arrays;

public class AddChildActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back, ll_choose;
    private EditText et_child_name, et_xueji, et_content;
    private TextView tv_choose, tv_add_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        ll_choose = findViewById(R.id.ll_choose);
        ll_choose.setOnClickListener(this);

        et_child_name = findViewById(R.id.et_child_name);
        et_xueji = findViewById(R.id.et_xueji);
        et_content = findViewById(R.id.et_content);

        tv_choose = findViewById(R.id.tv_choose);

        tv_add_commit = findViewById(R.id.tv_add_commit);
        tv_add_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_choose:
                String[] PLANETS = new String[]{"爸爸", "妈妈", "爷爷", "奶奶", "外公", "外婆"};
                final String[] choose = new String[1];
                // 声明PopupWindow
                final PopupWindow popupWindow;
                // 声明PopupWindow对应的视图
                View popupView;

                // 声明平移动画
                TranslateAnimation animation;
                final String[] choose_item = new String[1];
                popupView = View.inflate(this, R.layout.wheel_choose, null);
                popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                // 设置点击popupwindow外屏幕其它地方消失
                popupWindow.setOutsideTouchable(true);
                // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
                animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(200);
                // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                popupWindow.showAtLocation(AddChildActivity.this.findViewById(R.id.tv_add_commit), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupView.startAnimation(animation);
                WheelView wva = popupView.findViewById(R.id.wheelview);
                wva.setOffset(1);
                wva.setItems(Arrays.asList(PLANETS));
                wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d("selectedIndex==>>>", "selectedIndex: " + selectedIndex + ", item: " + item);
                        choose_item[0] = item;
                    }
                });
                TextView tv_quxiao = popupView.findViewById(R.id.tv_quxiao);
                tv_quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                TextView tv_ok = popupView.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choose[0] = choose_item[0];
                        Log.d("choose==>>", choose[0]);
                    }
                });
                break;
            case R.id.tv_add_commit:
                break;
        }
    }
}
