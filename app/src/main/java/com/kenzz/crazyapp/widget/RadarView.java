package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.kenzz.crazyapp.R;

/**
 * Created by ken.huang on 9/11/2017.
 *  雷达扫描图
 *  知识要点： SweepGradient
 */

public class RadarView extends View {
    private Paint mPaint;
    private float [] radiusRatio={1.0f/3,1.0f/2,2.0f/3,5.0f/6};
    private float mCurrentDegress=0;
    private SweepGradient mShader;
    private BitmapShader mBitmapShader;
    private Bitmap mCentreBitmap;
    private Paint mBitmapPaint;
    private Paint mSolidCirclePaint;
    private int mSolidCirlceRadius;

    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mBitmapPaint=new Paint();
        mSolidCirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mSolidCirclePaint.setStyle(Paint.Style.FILL);
        mSolidCirclePaint.setColor(Color.YELLOW);
        mSolidCirlceRadius=30;

        mCentreBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.funny_icon);
        postDelayed(mRunnable,60);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int radius=Math.min(getWidth(),getHeight())/2;
        mShader = new SweepGradient(radius,radius, Color.TRANSPARENT, Color.parseColor("#84B5CA"));
        mBitmapShader=new BitmapShader(mCentreBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale= Math.min(mCentreBitmap.getWidth()/(2*radius*radiusRatio[0]),mCentreBitmap.getHeight()/(2*radius*radiusRatio[0]));
        Matrix matrix=new Matrix();
        //缩放图片
        matrix.setScale(1/scale,1/scale);
        //移动到中间
        matrix.postTranslate(radius/2+radius*radiusRatio[0]/2,radius/2+radius*radiusRatio[0]/2);
        mBitmapShader.setLocalMatrix(matrix);
        mBitmapPaint.setShader(mBitmapShader);
    }

    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
          mCurrentDegress+=2;
          invalidate();
            if(mCurrentDegress==360){
                mCurrentDegress=0;
            }
            postDelayed(mRunnable,60);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
      final int radius=Math.min(getWidth(),getHeight())/2;
        drawCircles(canvas,radius);
        drawCentreIcon(canvas,radius);
        drawSolidCircle(canvas,radius);
    }

    /**
     * 绘制中心图片
     * @param canvas
     * @param radius
     */
    private void drawCentreIcon(Canvas canvas,int radius) {
        final int r= (int) (radius*radiusRatio[2]);
        canvas.drawCircle(radius,radius,r,mBitmapPaint);
    }

    /**
     * 绘制圆环
     * @param canvas
     * @param radius
     */
    private void drawCircles(Canvas canvas, int radius) {

        canvas.save();
        canvas.rotate(mCurrentDegress,radius,radius);
        mPaint.setShader(mShader);
        mPaint.setStyle(Paint.Style.FILL);
        final int sr= (int) (radius * radiusRatio[3]);
        canvas.drawCircle(radius,radius,sr-mPaint.getStrokeWidth()/2,mPaint);

        mPaint.setShader(null);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        for(int i=0;i<5;i++){
            int r;
            if(i<=3) {
             r = (int) (radius * radiusRatio[i] - mPaint.getStrokeWidth() / 2);
            }else{
                r= (int) (radius-mPaint.getStrokeWidth()/2);
            }
            canvas.drawCircle(radius,radius,r,mPaint);
        }
        canvas.restore();
    }

    /**
     * 绘制实心圆点
     * @param canvas
     * @param radius
     */
    private void drawSolidCircle(Canvas canvas, int radius){
        if(mCurrentDegress>=45){
            double angle=45f/180*Math.PI;
            float dx= (float) (radius*radiusRatio[1]*Math.sin(angle));
            canvas.drawCircle(radius+dx+mSolidCirlceRadius,radius+dx+mSolidCirlceRadius,
                    mSolidCirlceRadius,mSolidCirclePaint);
        }

        if(mCurrentDegress>=80){
            double angle=80f/180*Math.PI;
            float dy= (float) (radius*radiusRatio[1]*Math.sin(angle));
            float dx=(float) (radius*radiusRatio[1]*Math.cos(angle));
            canvas.drawCircle(radius+dx,radius+dy,
                    mSolidCirlceRadius,mSolidCirclePaint);
        }

        if(mCurrentDegress>=100){
            double angle=100f/180*Math.PI;
            float dy= (float) (radius*radiusRatio[2]*Math.sin(angle));
            float dx=(float) (radius*radiusRatio[2]*Math.cos(angle));
            canvas.drawCircle(radius+dx,radius+dy,
                    mSolidCirlceRadius,mSolidCirclePaint);
        }

        if(mCurrentDegress>=130){
            double angle=130f/180*Math.PI;
            float dy= (float) (radius*radiusRatio[1]*Math.sin(angle));
            float dx=(float) (radius*radiusRatio[1]*Math.cos(angle));
            canvas.drawCircle(radius+dx,radius+dy,
                    mSolidCirlceRadius,mSolidCirclePaint);
        }

        if(mCurrentDegress>=170){
            double angle=170f/180*Math.PI;
            float dy= (float) (radius*radiusRatio[2]*Math.sin(angle));
            float dx=(float) (radius*radiusRatio[2]*Math.cos(angle));
            canvas.drawCircle(radius+dx,radius+dy,
                    mSolidCirlceRadius,mSolidCirclePaint);
        }

    }
}
