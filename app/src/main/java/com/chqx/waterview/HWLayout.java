package com.chqx.waterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * author  ChenQiXin
 * date    2019-11-14
 * 描述   : 按比例放大的线性控件
 * 修订版本:
 */
public class HWLayout extends LinearLayout {
    private int mProportion;  //宽和高的比例
    public HWLayout(Context context){
        this(context, null);
    }

    public HWLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public HWLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        //获取比例值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HWLayout);
        mProportion = a.getInt(R.styleable.HWLayout_proportion, 11);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        String s = ""+mProportion;
        int width_pro = Integer.parseInt(s.substring(0, s.length() / 2));  //得到宽的比例
        int height_pro = Integer.parseInt(s.substring(s.length()/2));       //得到高的比例
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);//设置宽度
        /**
         * 按照比例改变高度值
         */
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)(childWidthSize*height_pro*1.0)/width_pro, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
