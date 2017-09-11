package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by huangdefa on 10/09/2017.
 * Version 1.0
 * 圆形ImageView
 * 主要采用 BitmapShader
 */

public class CircleImageView extends ImageView {
    private Paint mPaint;
    private BitmapShader mBitmapShader;
    private int mSize;
    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmapShader=null;
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmapShader=null;
    }

    private void createCircleDrawable(){
        Drawable drawable=getDrawable();
        if(drawable==null) return;
        if(drawable instanceof BitmapDrawable){
            final Bitmap bitmap=((BitmapDrawable) drawable).getBitmap();
            Matrix matrix=new Matrix();
            mBitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            //设置适当比例
            float scale=Math.min(bitmap.getHeight()*1.0f/getHeight(),bitmap.getWidth()*1.0f/getWidth());
            matrix.postScale(1/scale,1/scale);
            mBitmapShader.setLocalMatrix(matrix);
            mPaint.setShader(mBitmapShader);
        }else {
            throw new IllegalArgumentException("must be a bitmap drawable");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSize=Math.min(w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmapShader==null){
            createCircleDrawable();
        }
        canvas.drawCircle(mSize/2,mSize/2,mSize/2,mPaint);
    }
}
