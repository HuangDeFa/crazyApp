package com.kenzz.crazyapp.utils;

import android.content.Context;
import android.text.style.ScaleXSpan;

/**
 * Created by huangdefa on 14/09/2017.
 * Version 1.0
 * 各种度量单位之间的转换工具
 */

public class DimensionUtil {
    public static int dp2px(Context context,float dp){
        float scale= context.getResources().getDisplayMetrics().density;
        return (int) (scale*dp+.5f);
    }

    public static int px2dp(Context context,float px){
        float scale= context.getResources().getDisplayMetrics().density;
        return (int) (px/scale-.5f);
    }

    public static int sp2px(Context context,float sp){
        float scale= context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale*sp+.5f);
    }

    public static int px2sp(Context context,float px){
        float scale= context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px/scale-.5f);
    }
}
