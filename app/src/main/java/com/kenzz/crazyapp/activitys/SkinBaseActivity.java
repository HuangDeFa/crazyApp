package com.kenzz.crazyapp.activitys;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatViewInflater;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.kenzz.crazyapp.skin.attr.SkinAttr;
import com.kenzz.crazyapp.skin.attr.SkinView;
import com.kenzz.crazyapp.skin.support.SkinAppCompatViewInflater;
import com.kenzz.crazyapp.skin.support.SkinSupport;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

/**
 * Created by huangdefa on 28/08/2017.
 */

public abstract class SkinBaseActivity extends BaseActivity  implements LayoutInflater.Factory2{
    private SkinAppCompatViewInflater mSkinAppCompatViewInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater,this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

       View view=  createView(parent,name,context,attrs);
        //拦截View进行处理
        if(view!=null){
           final List<SkinAttr> skinAttrs= SkinSupport.getSkinViewAttrs(context,attrs);
            SkinView skinView=new SkinView(view,skinAttrs);
            manageSkinView(skinView);
        }
        return view;
    }

    private void manageSkinView(SkinView skinView) {

    }

    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        //拦截view的创建
        if (mSkinAppCompatViewInflater == null) {
            mSkinAppCompatViewInflater = new SkinAppCompatViewInflater();
        }
        boolean isPreLollipop= Build.VERSION.SDK_INT < 21;
        boolean inheritContext = false;
        if (isPreLollipop) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }
        return mSkinAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPreLollipop, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor =getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}
