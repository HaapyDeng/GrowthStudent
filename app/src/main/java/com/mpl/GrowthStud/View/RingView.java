package com.mpl.GrowthStud.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mpl.GrowthStud.R;

import java.text.NumberFormat;

/**
 * 　　class explain:      圆环统计图
 */


public class RingView extends View {


    /**
     * 第一圈的颜色
     */
    private int mFirstColor = getResources().getColor(R.color.getstarinfo);
    /**
     * 第二圈的颜色
     */
    private int mSecondColor = getResources().getColor(R.color.grey);
    /**
     * 圈的宽度
     */
    private float mCircleWidth = 40;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前进度
     */
    private int mProgress = 40;
    /**
     * 剩余文字
     */
    private String reminderText = "";
    /**
     * 进度文字
     */
    private String progressText = "";

    /**
     * 中心x坐标
     */
    int centreX;
    /**
     * 中心y坐标
     */
    int centerY;

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RingView(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint = new Paint();
        centreX = getWidth() / 2; // 获取圆心的x坐标
        centerY = getHeight() / 2;
        int radius = centreX / 2;// 半径
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        RectF oval = new RectF(centreX - radius, centerY - radius, centreX + radius, centerY + radius); // 用于定义的圆弧的形状和大小的界限
        mPaint.setColor(mFirstColor); // 设置圆环的颜色
        canvas.drawCircle(centreX, centerY, radius, mPaint); // 画出圆环
        mPaint.setColor(mSecondColor); // 设置圆弧的颜色
        canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧

        //获取进度圆弧的中心点
        float textX1 = (float) (centreX + (radius + mCircleWidth / 2) * Math.cos(2 * Math.PI * (mProgress / 2 - 90) / 360));
        float textY1 = (float) (centerY + (radius + mCircleWidth / 2) * Math.sin(2 * Math.PI * (mProgress / 2 - 90) / 360));
        Paint paint = new Paint();
        paint.setStrokeWidth(2); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        paint.setColor(mSecondColor);
        paint.setTextSize(30);
//        drawText(canvas, progressText + getPer(mProgress, 360), textX1, textY1, paint);
        float textX2 = (float) (centreX + (radius + mCircleWidth / 2) * Math.cos(2 * Math.PI * ((mProgress - 360) / 2 - 90) / 360));
        float textY2 = (float) (centerY + (radius + mCircleWidth / 2) * Math.sin(2 * Math.PI * ((mProgress - 360) / 2 - 90) / 360));
        paint.setColor(mFirstColor);
//        drawText(canvas, reminderText + getPer(360 - mProgress, 360), textX2, textY2, paint);
    }

    /**
     * 计算百分比
     *
     * @param now
     * @param total
     * @return
     */
    private String getPer(int now, int total) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);

        String result = numberFormat.format((float) now / (float) total * 100);

        return result + "%";
    }

    /**
     * 设置剩余进度颜色
     *
     * @param color
     */
    public void setReminderColor(int color) {
        mFirstColor = color;
        invalidate();
    }

    /**
     * 设置当前进度颜色
     *
     * @param color
     */
    public void setProgressColor(int color) {
        mSecondColor = color;
        invalidate();
    }

    /**
     * 设置剩余进度文字
     *
     * @param text
     */
    public void setReminderText(String text) {
        reminderText = text;
        invalidate();
    }

    /**
     * 设置当前进度文字
     *
     * @param text
     */
    public void setProgressText(String text) {
        progressText = text;
        invalidate();
    }

    /**
     * 设置圆环宽度
     *
     * @param width
     */
    public void setCircleWidth(int width) {
        mCircleWidth = width;
        invalidate();
    }

    /**
     * 设置当前进度
     *
     * @param total 总量
     * @param now   当前进度量
     */
    public void setProgress(float total, float now) {
        mProgress = (int) ((now / total) * 360);
        postInvalidate();
    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param string
     * @param firstX
     * @param firstY
     * @param paint
     */
    private void drawText(Canvas canvas, String string, float firstX, float firstY, Paint paint) {
        float endX1 = 0;
        float endY1 = 0;
        float endX2 = 0;
        float endY2 = 0;
        float textX = 0;
        float textY = 0;
        //初始点位于第四象限
        if (firstX <= centreX && firstY <= centerY) {
            endX1 = firstX - 50;
            endY1 = firstY - 50;
            endX2 = firstX - 200;
            endY2 = firstY - 50;
            textX = endX2;
            textY = endY2 - 10;
        }
        //初始点位于第三象限
        if (firstX < centreX && firstY > centerY) {
            endX1 = firstX - 50;
            endY1 = firstY + 50;
            endX2 = firstX - 200;
            endY2 = firstY + 50;
            textX = endX2;
            textY = endY2 + 40;
        }
        //初始点位于第一象限
        if (firstX > centreX && firstY < centerY) {
            endX1 = firstX + 50;
            endY1 = firstY - 50;
            endX2 = firstX + 200;
            endY2 = firstY - 50;
            textX = endX1;
            textY = endY2 - 10;
        }
        //初始点位于第二象限
        if (firstX >= centreX && firstY >= centerY) {
            endX1 = firstX + 50;
            endY1 = firstY + 50;
            endX2 = firstX + 200;
            endY2 = firstY + 50;
            textX = endX1;
            textY = endY2 + 40;
        }
        canvas.drawLine(firstX, firstY, endX1, endY1, paint);
        canvas.drawLine(endX1, endY1, endX2, endY2, paint);
        canvas.drawText(string, textX, textY, paint);
    }

}