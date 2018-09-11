package com.mpl.GrowthStud.Student.View;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mpl.GrowthStud.R;

import java.util.ArrayList;

/**
 * Class: ViewPager 页码圆点指示器UI组件.
 * explain:直接在XML文件中调用,需要在Activity中绑定 ViewPager 对象,使用addViewPager()方法.
 */
public class PageNumberPoint extends LinearLayout {
    private Context context;
    private PagerAdapter adapter;
    private int countPoint = 0;
    private ArrayList<Circle> point;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;

    public PageNumberPoint(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public PageNumberPoint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public PageNumberPoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }


    private void initView() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        this.setClickable(false);
    }

    /**
     * 绑定页码
     *
     * @param pager
     */
    public void addViewPager(ViewPager pager) {
        this.adapter = pager.getAdapter();
        addPagerPoint();
        //设置监听器
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setSelectPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void addPagerPoint() {
        countPoint = adapter.getCount();
        point = new ArrayList<>();

        for (int i = 0; i < countPoint; i++) {
            Circle circle = new Circle(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
            params.setMargins(20, 0, 0, 0); //设置外边距
            circle.setLayoutParams(params);
            this.addView(circle);
            point.add(circle);
        }

        point.get(0).setChecked(true);  //默认第一个是选中的
    }

    /**
     * 缩放动画效果
     *
     * @param view
     */
    private void playAnimator(View view) {
        scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.0f);
        scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.0f);
        //通过动画集合组合动画
        AnimatorSet animatorSet = new AnimatorSet();
        //这两个动画同时执行 绑定起来
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    private void setSelectPoint(int position) {
        point.get(position).setChecked(true);
        //开启动画
        playAnimator(point.get(position));
        for (int i = 0; i < point.size(); i++) {
            if (i == position)
                continue;
            point.get(i).setChecked(false);
        }
    }

    /***************************自定义的小圆点UI组件******************************************/
    public class Circle extends View {
        private float circleRadius = 6.8f;   //默认的圆的半径
        private boolean checked = false;

        public Circle(Context context) {
            super(context);
            initViewSize();
        }

        public Circle(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initViewSize();
        }

        public Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initViewSize();
        }

        private void initViewSize() {
            //推荐使用 宽高 各50dp
            this.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
            this.setClickable(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int verticalCenter = getHeight() / 2;
            int horizontalCenter = getWidth() / 2;
            Paint paint = new Paint();
            paint.setAntiAlias(true);  //防锯齿
            paint.setDither(true);   //防抖动
            paint.setStyle(Paint.Style.FILL_AND_STROKE);

            if (checked) {  //如果是选中状态
                //画圆心填充物
                paint.setColor(context.getResources().getColor(R.color.tab_checked));
                float radius = circleRadius + circleRadius / 2;
                canvas.drawCircle(verticalCenter - (circleRadius / 2), horizontalCenter - (circleRadius / 2), radius, paint);
            } else {
                paint.setColor(context.getResources().getColor(R.color.dot_not_choose));
                canvas.drawCircle(verticalCenter, horizontalCenter, circleRadius, paint);
            }
        }

        /**
         * 设置圆的半径
         *
         * @param radius
         */
        public void setCircleRadius(float radius) {
            this.circleRadius = radius;
            invalidate();//重新绘制组件
        }

        /**
         * 设置选择 还是非选择
         *
         * @param checked
         */
        public void setChecked(boolean checked) {
            this.checked = checked;
            invalidate();
        }
    }

}
