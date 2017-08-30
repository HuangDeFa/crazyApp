package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

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
