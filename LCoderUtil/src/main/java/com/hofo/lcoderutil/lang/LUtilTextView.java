package com.hofo.lcoderutil.lang;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.ref.SoftReference;

public class LUtilTextView {
    private static SoftReference<Activity> sActivitySoftReference;

    public static void setActivity(Activity activity) {
        sActivitySoftReference = new SoftReference<>(activity);
    }

    public static String getString(int id) {
        Activity activity = sActivitySoftReference.get();
        if (sActivitySoftReference != null && activity == null) {
            return "";
        }
        return getString(((TextView) activity.findViewById(id)));
    }

    public static <T extends TextView> String getString(T tv) {
        return tv.getText().toString();
    }

    public static <T extends TextView> String getStringTrim(T tv) {
        return getString(tv).trim();
    }

    public static <T extends TextView> void setTextTrim(T tv, String msg) {
        setText(tv, msg.trim());
    }

    public static <T extends TextView> void setText(T tv, String msg) {
        tv.setText(msg);
    }

    public static void setHint(Activity activity, int id, String msg) {
        ((TextView) activity.findViewById(id)).setHint(msg);
    }

    public static void setHint(int id, String msg) {
        Activity activity = sActivitySoftReference.get();
        if (sActivitySoftReference != null && activity == null) {
            return;
        }
        ((TextView) activity.findViewById(id)).setHint(msg);
    }

    public static void setText(int id, String msg) {
        Activity activity = sActivitySoftReference.get();
        if (sActivitySoftReference != null && activity == null) {
            return;
        }
        setText(activity, id, msg);
    }

    public static void setText(Activity activity, int id, String msg) {
        ((TextView) activity.findViewById(id)).setText(msg);
    }

    public static void setTextColor(int id, @ColorInt int color) {
        Activity activity = sActivitySoftReference.get();
        if (sActivitySoftReference != null && activity == null) {
            return;
        }
        ((TextView) activity.findViewById(id)).setTextColor(color);
    }

    public static void setTextTrim(int id, String msg) {
        Activity activity = sActivitySoftReference.get();
        if (sActivitySoftReference != null && activity == null) {
            return;
        }
        setTextTrim(activity, id, msg);
    }

    public static void setTextTrim(Activity activity, int id, String msg) {
        ((TextView) activity.findViewById(id)).setText(msg.trim());
    }


    /**
     * 获得TextView文本
     *
     * @param v
     * @param <T>
     * @return
     */
    public static <T extends TextView> String getText(T v) {
        if (v != null) {
            return ((TextView) v).getText().toString().trim();
        }
        return null;
    }

    /**
     * 获得TextView文本
     *
     * @param v
     * @param isTrim 是否去除前后空格 true去除 false 不去除
     * @param <T>
     * @return
     */
    public static <T extends TextView> String getText(T v, boolean isTrim) {
        if (v != null) {
            if (isTrim) {
                return ((TextView) v).getText().toString().trim();
            }
            return ((TextView) v).getText().toString();
        }
        return null;
    }

    /**
     * TextView内容是否为空
     *
     * @param v
     * @param <T>
     * @return
     */
    public static <T extends TextView> boolean isEmpty(T v) {
        if (v == null) return true;
        return TextUtils.isEmpty(v.getText().toString().trim());
    }

    /**
     * TextView内容是否为空
     *
     * @param v
     * @param isTrim 是否去除前后空格 true去除 false 不去除
     * @param <T>
     * @return
     */
    public static <T extends TextView> boolean isEmpty(T v, boolean isTrim) {
        if (v == null) return true;

        if (isTrim) return TextUtils.isEmpty(v.getText().toString().trim());
        return TextUtils.isEmpty(v.getText().toString());
    }

}
