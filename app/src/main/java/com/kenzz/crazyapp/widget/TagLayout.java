package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangdefa on 03/09/2017.
 * 自动换行的，流式布局；
 * 自定义ViewGroup:主要实现onMeasure和onLayout两个方法
 */

public class TagLayout extends ViewGroup {
    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int width=0;
        int maxLineHeight = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int currentWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int currentHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            width+=currentWidth;
            maxLineHeight = Math.max(maxLineHeight, currentHeight);
            //换行
            if(width>getWidth()){
                left=getPaddingLeft();
                top+=maxLineHeight;
                maxLineHeight=currentHeight;
                width=currentWidth;
            }
            child.layout(left,top,left+currentWidth,top+currentHeight);
            left+=currentWidth;
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getPaddingTop() + getPaddingBottom();
        int lineWidth = getPaddingLeft() + getPaddingRight();
        //1.获取所以子view，测量宽高
        final int childCount = getChildCount();
        int maxLineHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int currentWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int currentHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            maxLineHeight = Math.max(maxLineHeight, currentHeight);
            lineWidth += currentWidth;
            //换行
            if (lineWidth > width) {
                height += maxLineHeight;
                lineWidth = currentWidth + getPaddingLeft() + getPaddingRight();
                maxLineHeight = currentHeight;
            }
        }
        height += maxLineHeight;
        //最终设置宽高
        setMeasuredDimension(width, height);
    }

}
