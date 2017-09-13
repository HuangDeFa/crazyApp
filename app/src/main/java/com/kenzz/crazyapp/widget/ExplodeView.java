package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huangdefa on 14/09/2017.
 * Version 1.0
 * 拖拽爆炸
 * 要点：贝塞尔曲线
 */

public class ExplodeView extends View {
    private Paint mPaint;
    //固定小圆半径
    private int mSmallRadius;
    //拖拽出去的大圆半径
    private int mBigRadius;
    //最大的连接距离
    private int mMaxDistance;

    public ExplodeView(Context context) {
        super(context);

    }

    public ExplodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExplodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
