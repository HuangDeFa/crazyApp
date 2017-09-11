package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.kenzz.crazyapp.R;

import java.io.PrintWriter;

/**
 * Created by huangdefa on 01/09/2017.
 */

public class QQRunView extends View {
    private Paint mPaint;
    private int mBackGroundColor;
    private int mProgressColor;
    private int mRunCount;
    private int mMaxCount=1;
    private int mDefaultSize;
    private int mRealSize;
    private RectF mDrawRect;
    private Paint.FontMetricsInt mFontMetricsInt;

    public QQRunView(Context context) {
        this(context, null);
    }

    public QQRunView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQRunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultSize, mDefaultSize);
            mRealSize = mDefaultSize;
        } else {
            final int size = Math.max(mDefaultSize, Math.min(widthSize, heightSize));
            setMeasuredDimension(size, size);
            mRealSize = size;
        }
    }

    private void init(Context context,AttributeSet attributeSet) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setStrokeWidth(10);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(35);
        mDefaultSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                context.getResources().getDisplayMetrics()) + 0.5f);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.QQRunView);
        mBackGroundColor = typedArray.getColor(R.styleable.QQRunView_bgColor, Color.BLUE);
        mProgressColor = typedArray.getColor(R.styleable.QQRunView_progressColor, Color.RED);
        mRunCount = typedArray.getInt(R.styleable.QQRunView_runCount, 0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawRect == null) {
            float offset = mPaint.getStrokeWidth();
            mDrawRect = new RectF(offset/2, offset/2, mRealSize - offset/2, mRealSize - offset/2);
        }
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mBackGroundColor);
        canvas.drawArc(mDrawRect, 120, 300, false, mPaint);
        mPaint.setColor(mProgressColor);

        canvas.drawArc(mDrawRect, 120, mRunCount*1.0f/mMaxCount *300, false, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mFontMetricsInt = mPaint.getFontMetricsInt();
        float baseLineY = mRealSize / 2 + (mFontMetricsInt.bottom - mFontMetricsInt.top) / 2 - mFontMetricsInt.bottom;
        canvas.drawText(mRunCount + "", mRealSize / 2, baseLineY, mPaint);
    }


    public synchronized void setRunCount(int count){
        this.mRunCount=count;
        invalidate();
    }

    public synchronized void setMaxCount(int maxCount){
        this.mMaxCount=maxCount;
    }
}
