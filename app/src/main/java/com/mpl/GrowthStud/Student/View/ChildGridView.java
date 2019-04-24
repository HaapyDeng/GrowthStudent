package com.mpl.GrowthStud.Student.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ChildGridView extends GridView {
    public ChildGridView(Context context) {
        super(context);
    }

    public ChildGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //改变测量模式

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //需要改变的只是高度的测量模式
        int heightMes = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMes);
    }
}