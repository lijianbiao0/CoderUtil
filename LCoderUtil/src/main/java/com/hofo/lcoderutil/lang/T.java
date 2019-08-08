package com.hofo.lcoderutil.lang;

import android.app.Application;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//Toast统一管理类
public class T {

    private static Toast mToast;
    private static Application mApplication;

    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void init(Application app) {
        mApplication = app;
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, "", Toast.LENGTH_SHORT);
        }

        LinearLayout linearLayout = (LinearLayout) mToast.getView();

        TextView messageTextView = (TextView) linearLayout.getChildAt(0);

        messageTextView.setTextSize(25);
    }

    public static void init(Application app, int setTextSize) {
        mApplication = app;
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, "", Toast.LENGTH_SHORT);
        }

        LinearLayout linearLayout = (LinearLayout) mToast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        messageTextView.setTextSize(setTextSize);
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(int message) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, message, Toast.LENGTH_LONG);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, message, Toast.LENGTH_LONG);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, message, duration);
        } else {
            mToast.setText(message);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(int message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, message, duration);
        } else {
            mToast.setText(message);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void makeText(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplication, message, Toast.LENGTH_LONG);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
}