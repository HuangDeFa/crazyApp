package com.kenzz.crazyapp.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.kenzz.crazyapp.utils.DimensionUtil;

/**
 * Created by ken.huang on 9/15/2017.
 * 一个好玩的加载动画View
 */

public class LoadingView extends View {

    //浅蓝
    private int color1= Color.parseColor("#967BEF");
    //粉色
    private int color2=Color.parseColor("#F692CB");

    private int color3=Color.parseColor("#8ED1F2");

    private int leftColor,midColor,rightColor;

    private Paint mPaint;
    private final static int RADIUS=30;
    private int mWidth,mHeight;
    private float offset,animOffset;

    private ValueAnimator mAnimator;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        offset= DimensionUtil.dp2px(getContext(),18);
        leftColor=color1;
        midColor=color2;
        rightColor=color3;

        mAnimator=ValueAnimator.ofFloat(0,1);
        mAnimator.setDuration(500);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animOffset=-2*offset*(float)animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                int mid=midColor;
                midColor=rightColor;
                rightColor=leftColor;
                leftColor=mid;
            }
        });
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimator.start();
            }
        },1000);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(midColor);
        canvas.drawCircle(mWidth/2,mHeight/2,RADIUS,mPaint);
        mPaint.setColor(leftColor);
        canvas.drawCircle(mWidth/2-2*RADIUS-offset-animOffset,mHeight/2,RADIUS,mPaint);
        mPaint.setColor(rightColor);
        canvas.drawCircle(mWidth/2+2*RADIUS+ offset+animOffset,mHeight/2,RADIUS,mPaint);
    }
}
