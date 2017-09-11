package com.kenzz.crazyapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by huangdefa on 08/09/2017.
 * 实现跑马灯效果的TextView,实现字体变色
 * 控制变色的方向 {@link #LTR,#RTL}
 */

public class HeightLightTextView extends TextView {
    public static final int LTR=0;
    public static final int RTL=1;

    private Paint mPaint;
    private float mCurrentProgress;
    private int mCurrentDirection;

    public HeightLightTextView(Context context) {
        this(context,null);
    }

    public HeightLightTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeightLightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(getTextSize());
    }

    @Override
    protected void onDraw(Canvas canvas) {
     String text= (String) getText();
     if(!TextUtils.isEmpty(text)){
         float progress=mCurrentProgress;
         drawHeightLightText(canvas,text,progress);
         drawOriginalText(canvas,text,progress);
     }
    }

    private void drawOriginalText(Canvas canvas, String text, float progress) {
        if(mCurrentDirection==RTL){
           progress=1.0f-progress;
        }
        final int width= (int) (getWidth()*progress);
        mPaint.setColor(Color.BLACK);
        if(mCurrentDirection==LTR) {
            drawText(canvas, text, width, getWidth(), mPaint);
        }else {
            drawText(canvas, text, 0, width, mPaint);
        }
    }

    private void drawHeightLightText(Canvas canvas, String text, float progress) {
        if(mCurrentDirection==RTL){
            progress=1.0f-progress;
        }
        final int width= (int) (getWidth()*progress);
        mPaint.setColor(Color.RED);
        if(mCurrentDirection==LTR) {
            drawText(canvas, text, 0, width, mPaint);
        }else {
            drawText(canvas, text, width, getWidth(), mPaint);
        }
    }

    private void drawText(Canvas canvas,String text,int startX,int endX,Paint paint){
        canvas.save();
        //通过canvas的clipRect方法 裁剪需要绘制的区域从而实现不同区域绘制的文字不用的颜色
        canvas.clipRect(startX,0,endX,getHeight());
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        Rect bound=new Rect();
        paint.getTextBounds(text,0,text.length(),bound);
        int baseLine=(fontMetricsInt.bottom-fontMetricsInt.top)/2+getHeight()/2-fontMetricsInt.bottom;
        canvas.drawText(text,getWidth()/2-bound.width()/2,baseLine,paint);
        canvas.restore();
    }

    public void setProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }

    public void setDirection(@IntRange(from = LTR,to = RTL) int currentDirection) {
        mCurrentDirection = currentDirection;
    }

    public int getDirection() {
        return mCurrentDirection;
    }
}
