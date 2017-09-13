package com.kenzz.crazyapp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by ken.huang on 9/13/2017.
 * 扩展ViewPager
 * 1.ViewPager 使用Adapter模式将子View添加进来
 * 2.PageAdapter 适配器基类，FragmentPageAdapter/FragmentStatePageAdapter
 * 3.OnPageChangeListener 滑动监听器
 * 4.滑动时的过度动画 setPageTransformer
 * 5.利用clipChildren/clipToPadding属性实现一屏显示多个page
 *   ：clipChildren--表示是否裁剪子View，默认是true 则超过ViewGroup边界的子View的部分会被裁剪
 *   ：clipToPadding--表示是否能够在padding区域绘制，默认是true，即系子View超过ViewGroup的padding区域
 *   范围的部分将被裁剪。
 *
 *   这里设置clipChildren为false，同时如果viewPager外层的parent也要设置clipChildren为false。然后设置
 *   viewpager的左右margin。
 *   如果设置clipToPadding的，自要在viewPager中设置为false，接着设置padding即可。
 */

public class SuperViewPager extends ViewPager {
    private SuperViewPagerScroll mScroller;

    public SuperViewPager(Context context) {
        this(context,null);
    }

    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        try {
            mScroller=new SuperViewPagerScroll(getContext());
            Field field=ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this,mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        super.setPageTransformer(reverseDrawingOrder, transformer);
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer, int pageLayerType) {
        super.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType);
    }


   public void setSmoothScrollDulation(int duration){
       mScroller.setScrollDuration(duration);
    }

    /**
     *
     */
    private class SuperViewPagerScroll extends Scroller{

        private int mDuration=250;
        public SuperViewPagerScroll(Context context) {
            super(context);
        }

        public SuperViewPagerScroll(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public SuperViewPagerScroll(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setScrollDuration(int duration){
            mDuration=duration;
        }
    }

}
