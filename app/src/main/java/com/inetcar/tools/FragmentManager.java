package com.inetcar.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import java.util.ArrayList;

/**
 * 管理Fragment，处理点击radiobutton切换不同fragemnt
 */
public class FragmentManager implements RadioGroup.OnCheckedChangeListener{


    private ArrayList<Fragment> fragment_list;
    private RadioGroup rg_tab;
    private FragmentActivity activity;
    private int fragmentId;        // 显示Fragment的容器（布局）的ID
    private int currentPage; // 当前fragment页面index

    //private FragmentTransaction transaction; //commit 不能被同一个FragmentTransaction调用多次

    public FragmentManager(ArrayList<Fragment> fragment_list, RadioGroup rg_tab,
                           FragmentActivity activity, int fragmentId,int currentPage) {

        this.fragment_list = fragment_list;
        this.rg_tab = rg_tab;
        this.activity = activity;
        this.fragmentId = fragmentId;
        this.currentPage = currentPage;

        //默认显示currentPage
        FragmentTransaction transaction= activity.getSupportFragmentManager().beginTransaction();
        transaction.add(fragmentId,fragment_list.get(currentPage));
        transaction.commit();
        this.rg_tab.setOnCheckedChangeListener(this);
    }

    /**
     * @param group     the group in which the checked radio button has changed
     * @param checkedId the unique identifier of the newly checked radio button
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        for(int i=0; i<group.getChildCount(); i++){

            if(group.getChildAt(i).getId()==checkedId){
                Fragment temp = fragment_list.get(i);
                FragmentTransaction transaction= activity.getSupportFragmentManager().beginTransaction();
                //将当前页面隐藏
                fragment_list.get(currentPage).onPause();
                fragment_list.get(currentPage).onStop();

                //显示新页面
                if(temp.isAdded()){

                    temp.onStart();
                    temp.onResume();

                }else{
                    transaction.add(fragmentId,temp);
                }

                transaction.commit();
                showTab(i);
            }
        }

    }

    /**
     * 显示Tab页面
     * @param index  Index of Fragment which will be show
     */
    private void showTab(int index) {

        for(int i=0; i<fragment_list.size(); i++){

            Fragment temp = fragment_list.get(i);
            FragmentTransaction transaction= activity.getSupportFragmentManager().beginTransaction();
            if(i==index){
                transaction.show(temp);
            }else{
                transaction.hide(temp);
            }
            transaction.commit();
        }
        currentPage = index;
    }

    /**
     * 设置显示的页面
     * @param index 页面ID
     */
    public void setPage(int index){

        if(currentPage!=index){
            Fragment temp = fragment_list.get(index);
            FragmentTransaction transaction= activity.getSupportFragmentManager().beginTransaction();
            //将当前页面隐藏
            fragment_list.get(currentPage).onPause();
            fragment_list.get(currentPage).onStop();

            //显示新页面
            if(temp.isAdded()){
                temp.onStart();
                temp.onResume();
            }else{
                transaction.add(fragmentId,temp);
            }
            transaction.commit();
            showTab(index);
        }
    }
}
