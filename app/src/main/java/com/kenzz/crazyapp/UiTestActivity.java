package com.kenzz.crazyapp;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kenzz.crazyapp.annotations.ViewById;
import com.kenzz.crazyapp.utils.ViewUtils;
import com.kenzz.crazyapp.widget.SuperViewPager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UiTestActivity extends AppCompatActivity {

    @ViewById(R.id.mySuperViewPager)
    private SuperViewPager mSuperViewPager;
    private List<CardView> mCardViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ui_test);
        ViewUtils.inject(this);
        initData();
        setView();
        //mSuperViewPager.postDelayed(mRunnable,2000);
    }

    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            if(mSuperViewPager.getCurrentItem()==mCardViews.size()-1){
                mSuperViewPager.setCurrentItem(0,true);
            }else {
                mSuperViewPager.setCurrentItem(1+mSuperViewPager.getCurrentItem(),true);
            }
            mSuperViewPager.postDelayed(mRunnable,2000);
        }
    };

    private void setView() {
        //设置页面间距
        mSuperViewPager.setPageMargin(40);
        mSuperViewPager.setOffscreenPageLimit(3);
        mSuperViewPager.setSmoothScrollDulation(1000);
        mSuperViewPager.setPageTransformer(false,mPageTransformer);
        mSuperViewPager.setAdapter(mPagerAdapter);
    }

    private PagerAdapter mPagerAdapter=new PagerAdapter() {
        @Override
        public int getCount() {
            return mCardViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view= mCardViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }
    };

    //ViewPager 切换动画
    // 唯一的一个回调方法里面，position代表page的切换系数，范围是[-1,1]
    //如有 A,B两个页面。A页面退出时position->(-1,0],B页面进入position->[1,0);
    private ViewPager.PageTransformer mPageTransformer=new ViewPager.PageTransformer() {
        float basicscale=0.80f;
        float basicalpha=0.50f;
        @Override
        public void transformPage(View page, float position) {
            //不能看见的页面
            if(position<-1||position>1){
              page.setAlpha(basicalpha);
              page.setScaleX(basicscale);
              page.setScaleY(basicscale);
            }else if(position<=1) {
                float scaleFactor = Math.max(basicscale, 1 - Math.abs(position));
                if (position < 0) {
                    float scaleX = 1 + 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                } else {
                    float scaleX = 1 - 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                }
                page.setAlpha(basicalpha + (scaleFactor - basicscale) / (1 - basicscale) * (1 - basicalpha));
            }
        }
    };

    private void initData() {
        mCardViews=new CopyOnWriteArrayList<>();
        for(int i=0;i<6;i++){
          CardView cardView=(CardView) getLayoutInflater().inflate(R.layout.cardview_item,null);
            TextView tv= (TextView) cardView.getChildAt(0);
            tv.setText(i+"页面");
            mCardViews.add(cardView);
        }

        obser
    }


}
