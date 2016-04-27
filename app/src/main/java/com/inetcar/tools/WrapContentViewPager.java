package com.inetcar.tools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 自适应高度的viewpager
 */
public class WrapContentViewPager extends ViewPager{

    public WrapContentViewPager(Context context) {
        super(context);
    }

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        Log.d("child-count",getChildCount()+"");
        for(int i=0; i<getChildCount(); i++){
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
            int h =child.getMeasuredHeight();
            if(h>height){
                height = h;
            }
        }
        Log.d("child-width",height+"");
        //这里由于存在gridview 所以高度乘以gird行数
        //heightMeasureSpec = MeasureSpec.makeMeasureSpec(3*height,MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
