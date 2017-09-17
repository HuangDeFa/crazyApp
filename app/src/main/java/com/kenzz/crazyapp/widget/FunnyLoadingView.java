package com.kenzz.crazyapp.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.kenzz.crazyapp.utils.DimensionUtil;

/**
 * Created by ken.huang on 9/15/2017.
 * 弹跳加载动画
 * 主要点：
 *  1.ValueAnimator的使用
 *  2.Canvas的使用 例如旋转，剪切画布等
 *  3.DrawText的理解，基线，FontMetricsInt。
 */

public class FunnyLoadingView extends View {

    private int color1 = Color.parseColor("#967BEF");

    private int color2 = Color.parseColor("#F692CB");

    private int color3 = Color.parseColor("#8ED1F2");

    private Paint mTextPaint;
    private Paint mShadePaint;
    private Paint mShapePaint;

    private int mWith, mHeight;
    private int mAnimationDistance;
    private Path mPath;

    private final static int DRAW_TYPE_CIRCLE = 1;
    private final static int DRAW_TYPE_TRIANGLE = 2;
    private final static int DRAW_TYPE_RECTANGLE = 3;

    private int mDrawType = DRAW_TYPE_CIRCLE;

    public FunnyLoadingView(Context context) {
        this(context, null);
    }

    public FunnyLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunnyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWith = w;
        mHeight = h;
    }

    private int repeatCount;

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(Color.parseColor("#B8C3C9"));
        mTextPaint.setTextSize(50);

        mShadePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadePaint.setColor(Color.parseColor("#D0D9DE"));
        mShadePaint.setStyle(Paint.Style.FILL);

        mShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShapePaint.setStyle(Paint.Style.FILL);
        mShapePaint.setDither(true);
        mShapePaint.setColor(color1);

        mPath = new Path();

        mAnimationDistance = DimensionUtil.dp2px(getContext(), 50);
        mValueAnimator = ValueAnimator.ofInt(0, mAnimationDistance);
        mValueAnimator.setDuration(500);
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                repeatCount += 1;
                if (repeatCount == 2) {
                    if (mDrawType == DRAW_TYPE_CIRCLE) {
                        mDrawType = DRAW_TYPE_TRIANGLE;
                        mShapePaint.setColor(color2);
                    } else if (mDrawType == DRAW_TYPE_TRIANGLE) {
                        mDrawType = DRAW_TYPE_RECTANGLE;
                        mShapePaint.setColor(color3);
                    } else {
                        mDrawType = DRAW_TYPE_CIRCLE;
                        mShapePaint.setColor(color1);
                    }
                    repeatCount = 0;
                }
            }
        });
        mValueAnimator.start();
    }

    private ValueAnimator mValueAnimator;
    private int offset;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShape(canvas);
        drawShader(canvas);
        drawText(canvas);
    }

    private void drawShape(Canvas canvas) {
        float top = mHeight / 2 - DimensionUtil.dp2px(getContext(), 13);
        int radius = 30;
        if (mDrawType == DRAW_TYPE_CIRCLE) {
            canvas.drawCircle(mWith / 2, top - radius - 5 - offset, radius, mShapePaint);
        } else if (mDrawType == DRAW_TYPE_TRIANGLE) {
            top = top - 30 - 5 - offset;
            mPath.reset();
            mPath.moveTo(mWith / 2 - radius, top);
            mPath.lineTo(mWith / 2, top - radius);
            mPath.lineTo(mWith / 2 + radius, top);
            mPath.close();
            float rota = offset * 1.0f / mAnimationDistance;
            float degree = 120 * rota;
            canvas.save();
            canvas.rotate(degree, mWith / 2, top - radius / 2);
            canvas.drawPath(mPath, mShapePaint);
            canvas.restore();
        } else {
            top = top - 30 - 5 - offset;
            float rota = offset * 1.0f / mAnimationDistance;
            float degree = 120 * rota;
            canvas.save();
            canvas.rotate(degree, mWith / 2, top - radius);
            canvas.drawRect(mWith / 2 - radius, top - 2 * radius, mWith / 2 + radius, top, mShapePaint);
            canvas.restore();
        }
    }

    private void drawShader(Canvas canvas) {

        float rota = offset * 1.0f / mAnimationDistance;

        int offsetX = (int) (rota * 10);
        int offsetY = (int) (rota * 3);

        float top = mHeight / 2 - DimensionUtil.dp2px(getContext(), 13 - offsetY / 2);
        float bottom = mHeight / 2 - DimensionUtil.dp2px(getContext(), 8 + offsetY / 2);
        float left = mWith / 2 - DimensionUtil.dp2px(getContext(), 30 - offsetX);
        float right = mWith / 2 + DimensionUtil.dp2px(getContext(), 30 - offsetX);
        RectF oval = new RectF(left, top, right, bottom);
        canvas.drawOval(oval, mShadePaint);
    }

    private String mLoadingText = "正在拼命加载...";

    private void drawText(Canvas canvas) {
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        float baseLineY = mHeight / 2 + (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        baseLineY += DimensionUtil.dp2px(getContext(), 30);
        //当动画开始重复，则offset的百分比就要反过来计算
        if (repeatCount == 1) offset = 1 - offset;
        float rota = offset * 1.0f / mAnimationDistance;
        float length = mTextPaint.measureText(mLoadingText);
        mTextPaint.setColor(color3);
        canvas.drawText(mLoadingText, mWith / 2 - length / 2, baseLineY, mTextPaint);

        mTextPaint.setColor(color2);
        canvas.save();
        canvas.clipRect(mWith / 2 - length / 2, baseLineY + fontMetricsInt.top,
                mWith / 2 - length / 2 + rota * length, baseLineY + fontMetricsInt.bottom);
        canvas.drawText(mLoadingText, mWith / 2 - length / 2, baseLineY, mTextPaint);
        canvas.restore();
    }
}
