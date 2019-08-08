package com.hofo.lcoderutil.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import com.hofo.lcoderutil.R;
import com.hofo.lcoderutil.lang.LUtilObjects;

import java.util.HashMap;

/**
 * 未实现宽度测量
 */
public class LViewColumnRadioGroup extends RadioGroup {
    private int columnCount = 3;

    public LViewColumnRadioGroup(Context context) {
        super(context);
    }

    public LViewColumnRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrValues(context, attrs);
    }

    private void getAttrValues(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColumnRadioGroup);
        // 获取布局中列数属性值，默认为3个
        columnCount = typedArray.getInteger(R.styleable.ColumnRadioGroup_columnCount, 3);
        // 一个好的习惯是用完资源要记得回收，就想打开数据库和IO流用完后要记得关闭一样
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;
        int width = 0;
        int count = getChildCount();
        int rowMaxHeight = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            rowMaxHeight = Math.max(rowMaxHeight, childHeight);

            if (i % columnCount == columnCount - 1 || i == count - 1) {
                height += rowMaxHeight;
                rowMaxHeight = 0;
            }

            width = Math.max(childWidth, width);
        }

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        if (count == 0) {
            measureWidth = paddingLeft + paddingRight;
            measureHeight = paddingTop + paddingBottom;
            setMeasuredDimension(measureWidth, measureHeight);
        } else {
            setMeasuredDimension(
                    (measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth : width + paddingLeft + paddingRight,
                    (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight : height + paddingTop + paddingBottom);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int rowTop = getPaddingTop();
        int left;
        int parentWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
        int count = getChildCount();
        int rowMaxHeight = 0;
        int region = parentWidth / columnCount;

        HashMap<Integer, Integer> columnMarginTop = new HashMap<>();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int column = i % columnCount;
            Integer marginTop = columnMarginTop.get(column);
            if (LUtilObjects.isNull(marginTop)) {
                columnMarginTop.put(column, lp.topMargin);
            } else {
                columnMarginTop.put(column, lp.topMargin + marginTop);
            }

            int top = columnMarginTop.get(column);
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            if (column == 0) {
                left = region / 2 - childWidth / 2;
                rowTop += rowMaxHeight;
            } else {
                left = region * column + region / 2 - childWidth / 2;
            }
            left += getPaddingLeft() + lp.leftMargin;
            top += rowTop;


            rowMaxHeight = rowMaxHeight > childHeight ? rowMaxHeight : childHeight;
            child.layout(left, top, left + childWidth, top + childHeight);
        }
    }
}
