package com.inetcar.startup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class FirstInActivity extends Activity implements ViewPager.OnPageChangeListener {


    private ViewPager vp_guide;

    private LinearLayout ll_dot;

    private ArrayList<View> pageList; //封装四个页面

    private ImageView im_dot[]; //封装四个页面指示器

    private int currentIndex = 0; //当前页面下标

    private ViewPagerAdapter pagerAdapter; //滑动页面适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_in);
        init();
    }


    private void init(){

        //加载4个页面指示器
        ll_dot = (LinearLayout) findViewById(R.id.linearlayout_dot);
        im_dot = new ImageView[4];
        for(int i=0; i<4; i++){
            im_dot[i] = (ImageView) ll_dot.getChildAt(i);
            im_dot[i].setEnabled(false);
        }
        currentIndex = 0;
        im_dot[currentIndex].setEnabled(true); //默认点亮第一个页面指示器

        //加载滑动页面控制器及4个页面
        vp_guide = (ViewPager) findViewById(R.id.viewpager_firstin);
        LayoutInflater inflater = LayoutInflater.from(this);
        pageList = new ArrayList<View>(4);
        pageList.add(inflater.inflate(R.layout.guide_one,null));
        pageList.add(inflater.inflate(R.layout.guide_two,null));
        pageList.add(inflater.inflate(R.layout.guide_three,null));
        pageList.add(inflater.inflate(R.layout.guide_four,null));

        vp_guide.setOffscreenPageLimit(4);
        pagerAdapter = new ViewPagerAdapter(pageList,this);
        vp_guide.setAdapter(pagerAdapter);
        vp_guide.setOnPageChangeListener(this);
    }



    /**
     * 当页面滑动时调用在滑动被停止之前，此方法回一直得到调用
     * @param position 当前页面，即你点击滑动的页面
     * @param positionOffset 当前页面偏移的百分比
     * @param positionOffsetPixels 当前页面偏移的像素位置
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    /**
     * 当页面跳转后调用
     * @param position 当前页面下标
     */
    @Override
    public void onPageSelected(int position) {

        if(position<0 || position>=pageList.size() || currentIndex==position){
            return;
        }
        im_dot[currentIndex].setEnabled(false);
        currentIndex = position;
        im_dot[currentIndex].setEnabled(true);

    }


    /**
     * 当页面滑动状态改变时调用，其中state这个参数有三种状态
     * state==1  表示正在滑动
     * state==2  表示滑动完毕了
     * state==0  表示什么都没做
     * @param state 页面状态
     */
    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
