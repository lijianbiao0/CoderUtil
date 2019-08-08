package com.hofo.lcoderutil.view.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hofo.lcoderutil.R;


public class LViewCenterDialog extends Dialog implements View.OnClickListener {
    private int mGravity = -1;
    private Context context;      // 上下文
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id
    private View contentView;
    private boolean onClickIsDismiss = true;
    private OnCenterItemClickListener listener;
    private int mWidth = -1;
    private int mHeight = -2;

    public LViewCenterDialog(Context context, int layoutResID, int... listenedItems) {
        this(context, layoutResID, Gravity.CENTER, listenedItems);
    }

    public LViewCenterDialog(Context context, int layoutResID, int Gravity, int[] listenedItems) {
        super(context, R.style.DialogDefaultStyle); //dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
        mGravity = Gravity;
        this.listenedItems = listenedItems;
        contentView = LayoutInflater.from(context).inflate(layoutResID, null);
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }

    public void setText(int id, String msg) {
        if (getView(id) instanceof TextView) {
            ((TextView) getView(id)).setText(msg);
        }
    }

    public <T extends View> T getView(int id) {
        return contentView.findViewById(id);
    }

    public void onClickIsDismiss(boolean onClickIsDismiss) {
        this.onClickIsDismiss = onClickIsDismiss;
    }

    @Override
    public void onClick(View view) {
        if (onClickIsDismiss)
            dismiss();//注意：我在这里加了这句话，表示只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        if (listener != null) {
            listener.OnCenterItemClick(this, view);
        }
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (mGravity != -1) {
            window.setGravity(mGravity); // 此处可以设置dialog显示的位置为居中
        } else {
            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        }
        window.setWindowAnimations(R.style.bottom_animation); // 添加动画效果
        setContentView(contentView);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (mWidth != -1) {
            lp.width = mWidth; // 设置dialog宽度为屏幕的4/5

        } else {
            lp.width = display.getWidth() * 5 / 6; // 设置dialog宽度为屏幕的4/5
        }
        if (mHeight == -2) {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = mHeight;
        }
        getWindow().setAttributes(lp);

        //遍历控件id,添加点击事件
        if (listenedItems != null) {
            for (int id : listenedItems) {
                findViewById(id).setOnClickListener(this);
            }
        }

        setCanceledOnTouchOutside(false);

    }


    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public interface OnCenterItemClickListener {
        void OnCenterItemClick(LViewCenterDialog dialog, View view);
    }
}
