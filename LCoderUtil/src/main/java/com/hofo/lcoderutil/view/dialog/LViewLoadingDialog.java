package com.hofo.lcoderutil.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.hofo.lcoderutil.R;

public class LViewLoadingDialog extends Dialog {
    private Context mContext;
    private View mView;

    public LViewLoadingDialog(Context context) {
        super(context, R.style.DialogDefaultStyle);
        mContext = context;
    }

    @Override
    public void show() {
        try {
            super.show();
            Animation rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_rotate);
            LinearInterpolator interpolator = new LinearInterpolator();
            rotateAnimation.setInterpolator(interpolator);
            mView.findViewById(R.id.imageView).startAnimation(rotateAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mView.findViewById(R.id.imageView).clearAnimation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);

        mView = View.inflate(mContext, R.layout.dialog_loading, null);
        setContentView(mView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }
}
