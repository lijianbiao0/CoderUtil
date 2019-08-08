package com.hofo.lcoderutil.lang;

import android.content.Context;
import android.util.DisplayMetrics;

public class LUtilDensity {
    /**
     * px转dp
     *
     * @param context The context
     * @param px      the pixel value
     * @return value in dp
     */
    public static int px2dp(Context context, float px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5f);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }

}
