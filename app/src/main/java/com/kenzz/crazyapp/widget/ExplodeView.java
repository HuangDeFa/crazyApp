package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.kenzz.crazyapp.utils.DimensionUtil;

/**
 * Created by huangdefa on 14/09/2017.
 * Version 1.0
 * 拖拽爆炸
 * 要点：贝塞尔曲线
 * Path:主要方法
 *   mPath.moveTo(10,10); --移动到(10,10)且作为起点，影响起点
     mPath.lineTo(20,20);
     mPath.setLastPoint(10,60);

     mPath.set(new Path()); --启动重置作用
     mPath.reset();         --清空数据
     mPath.offset(23,56);   --将路径进行偏移
     mPath.close()          --路径闭合
     mPath.isEmpty()        --是否为空
     mPath.isRect(Rect rect) --是否为矩形并将矩形返回

     addXXX() 系列方法，其中 Path.Direction.CW/ Path.Direction.CCW 表示绘制的方向前者顺时针后者逆时针
     方向不一致导致终点会不一样，影响接下去的路径的连接
     mPath.addCircle(2,3,80, Path.Direction.CW);

     arcTo()方法 是添加圆弧，参数forceMoveTo表示绘制圆弧的起点是否与上一次的点连接，true即系强制当前点(圆弧绘制起点)为新的
     起点所以不连接，否则设为false则表示连接。
     RectF rect=new RectF(23,23,125,125);
     mPath.arcTo(rect,0,180,true);

     贝塞尔曲线：任何曲线都可以用贝塞尔曲线表示

 */

public class ExplodeView extends View {
    private Paint mPaint;
    //固定小圆半径
    private int mSmallRadius;
    //拖拽出去的大圆半径
    private int mBigRadius;
    //最大的连接距离
    private int mMaxDistance;

    private final int SMALLRADIUS_MIN=DimensionUtil.dp2px(getContext(),3);
    private final int SMALLRADIUS_MAX=DimensionUtil.dp2px(getContext(),7);

    //分别表示1,2,3,4象限
    private final static int QUADRANT_1=1;
    private final static int QUADRANT_2=2;
    private final static int QUADRANT_3=3;
    private final static int QUADRANT_4=4;

    private Path mPath;

    private int touchSlop;

    public ExplodeView(Context context) {
        this(context,null);
    }

    public ExplodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ExplodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);

        mSmallRadius=SMALLRADIUS_MIN;
        mBigRadius=DimensionUtil.dp2px(getContext(),10);
        mMaxDistance= DimensionUtil.dp2px(getContext(),50f);
        mPath=new Path();

        touchSlop= ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mCircleOneX=500;
        mCircleOneY=300;
        mCircleTwoX=mCircleOneX-10;//+mMaxDistance;
        mCircleTwoY=mCircleOneY+mMaxDistance;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // float dis= (float) calcDistance();
        drawCircle(canvas);
        //if(dis<mMaxDistance)
        drawMetaBall2(canvas);
    }

    private int downX,downY;
    private float mCircleOneX,mCircleOneY;
    private float mCircleTwoX,mCircleTwoY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX= (int) event.getX();
                downY= (int) event.getY();
              //  mPath.reset();
                //mPath.moveTo(downX,downY);
                mCircleOneX=event.getX();
                mCircleOneY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
               /* int x= (int) event.getX();
                int y= (int) event.getY();

                int dx=Math.abs(x-downX);
                int dy=Math.abs(y-downY);
                if(dx>10 || dy>10){
                    int cx=(x+downX)/2;
                    int cy=(y+downY)/2;
                    mPath.quadTo(downX,downY,cx,cy);
                   // mPath.lineTo(x,y);
                    invalidate();
                    downX=x;
                    downY=y;
                }*/
                mCircleTwoX=event.getX();
                mCircleTwoY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }


    private void drawCircle(Canvas canvas){
        canvas.drawCircle(mCircleOneX,mCircleOneY,mSmallRadius,mPaint);
        canvas.drawCircle(mCircleTwoX,mCircleTwoY,mBigRadius,mPaint);
    }

    private double calcDistance(){
        float dx=mCircleTwoX-mCircleOneX;
        float dy=mCircleTwoY-mCircleOneY;
        double dis = Math.sqrt(dx * dx + dy * dy);
        if(dis<=mMaxDistance){
            mSmallRadius= (int) ((dis/mMaxDistance)*(SMALLRADIUS_MAX-SMALLRADIUS_MIN)+SMALLRADIUS_MIN);
        }else {
            mSmallRadius=0;
        }
        return dis;
    }

    //粗略计算
    private void drawMetaBall(Canvas canvas){

        //控制点
        float controlX=(mCircleTwoX+mCircleOneX)/2;
        float controlY=(mCircleTwoY+mCircleOneY)/2;

        //反切角
        double arcTan=Math.atan((mCircleTwoY-mCircleOneY)/(mCircleTwoX-mCircleOneX));

        double offsetX=mSmallRadius*Math.sin(arcTan);
        double offsetY=mSmallRadius*Math.cos(arcTan);

        float x1= (float) (mCircleOneX+offsetX);
        float y1= (float) (mCircleOneY-offsetY);

        float x3= (float) (mCircleOneX-offsetX);
        float y3= (float) (mCircleOneY+offsetY);

        offsetX=mBigRadius*Math.sin(arcTan);
        offsetY=mBigRadius*Math.cos(arcTan);

        float x2= (float) (mCircleTwoX+offsetX);
        float y2= (float) (mCircleTwoY-offsetY);

        float x4= (float) (mCircleTwoX-offsetX);
        float y4= (float) (mCircleTwoY+offsetY);

        mPath.reset();
        mPath.moveTo(x1,y1);
        mPath.quadTo(controlX,controlY,x2,y2);
        mPath.lineTo(x4,y4);
        mPath.quadTo(controlX,controlY,x3,y3);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }

    //根据动态的点获取所在的象限，相对于固定的圆
    private int getQuadrant(){
       float dx=mCircleTwoX-mCircleOneX;
        float dy=mCircleTwoY-mCircleOneY;
        int Quadrant=QUADRANT_4;
        if(dx>0 && dy>0){
            Quadrant=QUADRANT_4;
        }else if(dx>0 && dy<0){
            Quadrant=QUADRANT_1;
        }else if(dx<0 && dy<0){
            Quadrant=QUADRANT_2;
        }else if(dx<0 &&dy>0){
            Quadrant= QUADRANT_3;
        }
        return Quadrant;
    }

    //精确计算
    private void drawMetaBall2(Canvas canvas){
        float x = mCircleTwoX;
        float y = mCircleTwoY;
        float startX = mCircleOneX;
        float startY = mCircleOneY;
        float controlX = (startX + x) / 2;
        float controlY = (startY + y) / 2;

        float distance = (float) Math.sqrt((controlX - startX) * (controlX - startX) + (controlY - startY) * (controlY - startY));
        double a = Math.acos(mSmallRadius / distance);

        double b = Math.acos((controlX - startX) / distance);
        float offsetX1 = (float) (mSmallRadius * Math.cos(a - b));
        float offsetY1 = (float) (mSmallRadius * Math.sin(a - b));
        float tanX1 = startX + offsetX1;
        float tanY1 = startY - offsetY1;

        double c = Math.acos((controlY - startY) / distance);
        float offsetX2 = (float) (mSmallRadius * Math.sin(a - c));
        float offsetY2 = (float) (mSmallRadius * Math.cos(a - c));
        float tanX2 = startX - offsetX2;
        float tanY2 = startY + offsetY2;

        double d = Math.acos((y - controlY) / distance);
        float offsetX3 = (float) (mBigRadius * Math.sin(a - d));
        float offsetY3 = (float) (mBigRadius * Math.cos(a - d));
        float tanX3 = x + offsetX3;
        float tanY3 = y - offsetY3;

        double e = Math.acos((x - controlX) / distance);
        float offsetX4 = (float) (mBigRadius * Math.cos(a - e));
        float offsetY4 = (float) (mBigRadius * Math.sin(a - e));
        float tanX4 = x - offsetX4;
        float tanY4 = y + offsetY4;

        mPath.reset();
        mPath.moveTo(tanX1, tanY1);
        mPath.quadTo(controlX, controlY, tanX3, tanY3);
        mPath.lineTo(tanX4, tanY4);
        mPath.quadTo(controlX, controlY, tanX2, tanY2);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawMetaBall3(Canvas canvas){
        mPath.reset();
        float distance= (float) Math.sqrt(Math.pow(mCircleOneX-mCircleTwoX,2)+Math.pow(mCircleOneY-mCircleTwoY,2));
        float sina=(mCircleTwoY-mCircleOneY)/distance;
        float cosa=(mCircleTwoX-mCircleOneX)/distance;
        //控制点
        float controlX=(mCircleTwoX+mCircleOneX)/2;
        float controlY=(mCircleTwoY+mCircleOneY)/2;

        mPath.moveTo(mCircleOneX-sina*mSmallRadius,mCircleOneY-cosa*mSmallRadius);
        mPath.lineTo(mCircleOneX+sina*mSmallRadius,mCircleOneY+cosa*mSmallRadius);
        mPath.quadTo(controlX,controlY,mCircleTwoX+sina*mBigRadius,mCircleTwoY+cosa*mBigRadius);
        mPath.lineTo(mCircleTwoX-sina*mBigRadius,mCircleTwoY-cosa*mBigRadius);
        mPath.quadTo(controlX,controlY,mCircleOneX-sina*mSmallRadius,mCircleOneY-cosa*mSmallRadius);
        canvas.drawPath(mPath,mPaint);
    }
}
