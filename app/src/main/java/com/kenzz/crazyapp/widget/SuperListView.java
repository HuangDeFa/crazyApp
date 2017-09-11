package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.TextView;

import com.kenzz.crazyapp.R;

/**
 * Created by huangdefa on 30/08/2017.
 *  超级ListView，在scrollView中嵌套使用 {@link ListView }
 *  会出现ListView显示不全的问题，这是因为{@link android.widget.ScrollView} 进行测量子View
 *  的时候构建了{@link android.view.View.MeasureSpec} 的mode为 MeasureSpec.UNSPECIFIED
 */

public class SuperListView extends ListView {
    public SuperListView(Context context) {
        super(context);
    }

    public SuperListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.SuperListView);
        typedArray.getDimensionPixelSize(R.styleable.SuperListView_textSize,15);
        typedArray.getString(R.styleable.SuperListView_text);
        typedArray.getDrawable(R.styleable.SuperListView_src);
        typedArray.getInt(R.styleable.SuperListView_inputType,1);
        typedArray.recycle();

        Paint paint=new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10);
        //设置ColorFilter,使用矩阵操作RGB颜色值，可以用着图片上实现出滤镜胶片等效果
        paint.setColorFilter(new ColorMatrixColorFilter(new float[]{1,0,0,0,1,0,0,0,1}));
        //绘制文本有效
        paint.setTextSize(12);
        //绘制文本 字体对齐方式（相对于x轴）canvas.drawText(x,y,text,paint),
        //x表示文字绘制时水平对齐的坐标，如设置左对齐则表示x是水平绘制的开始，中间对齐则x代表文字绘制边框的中间位置，
        //右对齐时x表示文字绘制边框的右边的坐标
        //y表示绘制文字的基线(baseLine)
        paint.setTextAlign(Paint.Align.CENTER);

        //创建BitmapShader作为Paint的背景，然后通过这个画笔绘制不同形状就能得到不同边框的Image了
        //比如经典的圆形头像
        BitmapShader shader=new BitmapShader(Bitmap.createBitmap(100,100, Bitmap.Config.RGB_565),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置不同的缩放比例，让图片绘制处理不失真
        Matrix matrix=new Matrix();
        matrix.postScale(1.5f,1.5f);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        if(heightMode==MeasureSpec.UNSPECIFIED){
           heightMeasureSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
