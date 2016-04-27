package com.inetcar.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inetcar.map.MapFragment;
import com.inetcar.me.MeFragment;
import com.inetcar.startup.R;
import com.inetcar.tools.FragmentManager;
import com.inetcar.tools.WindowTranslucent;

import java.util.ArrayList;

public class MainCarActivity extends FragmentActivity implements MapFragment.MyLocationCallback,
        View.OnClickListener{

    private RelativeLayout left_titlebar;  //titlebar左边的定位按钮容器
    private TextView tv_adress;            //titlebar左侧定位地址

    private ImageView iv_menu;   //titlebar右侧菜单键

    private FrameLayout frame_email; //titlebar右侧邮箱按钮容器
    private TextView tv_unread;      //titlebar未读邮件

    private RadioGroup rg_tab; //底部tab

    private ArrayList<Fragment> fragment_list; //封装三个tab页面对应的Fragment

    private FragmentManager fragment_manager;

    private PopupWindow mMenuPopWindow; //菜单弹出框
    private View view_menu;             //菜单布局
    private TextView tv_scan;           //扫一扫

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowTranslucent.setWindowStatusColor(this, "#0f6ad5");
        setContentView(R.layout.activity_main_car);
        loadFragment();
        initView();
    }


    public void initView() {

        left_titlebar = (RelativeLayout) findViewById(R.id.leftlayout_main_title_bar);
        left_titlebar.setOnClickListener(this);
        tv_adress = (TextView) findViewById(R.id.tv_left_main_title_bar);

        iv_menu = (ImageView) findViewById(R.id.im_menu_main_title_bar);
        iv_menu.setOnClickListener(this);

        frame_email = (FrameLayout) findViewById(R.id.frame_msg_main_title_bar);
        frame_email.setOnClickListener(this);
        tv_unread = (TextView) findViewById(R.id.tv_unread_main_title_bar);

        rg_tab = (RadioGroup) findViewById(R.id.rg_tab_maincar);
        fragment_manager = new FragmentManager(fragment_list, rg_tab, this,
                R.id.frame_container,0);

        view_menu = this.getLayoutInflater().inflate(R.layout.menu_popwindow,null);

        mMenuPopWindow = new PopupWindow(view_menu, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mMenuPopWindow.setFocusable(true);
        mMenuPopWindow.setBackgroundDrawable(new ColorDrawable(00000000));
        mMenuPopWindow.setOutsideTouchable(true); //点击外部弹出框会消失

        tv_scan = (TextView) view_menu.findViewById(R.id.tv_menu_scan);
        tv_scan.setOnClickListener(this);
    }


    /**
     * 加载加油、周边、我三个tab页面
     */
    public void loadFragment() {

        fragment_list = new ArrayList<Fragment>(2);

        MapFragment mapfragment = new MapFragment();
//        mapfragment.setArguments(bundle);  //传递bundle数据
        fragment_list.add(mapfragment);


        MeFragment mefragment = new MeFragment();
//      mefragment.setArguments();
        fragment_list.add(mefragment);
    }


    /**
     * @param city     城市
     * @param district 城区
     */
    @Override
    public void locationCallback(String city, String district) {
        if(city==null || city.isEmpty()){
            //tv_adress.setText("定位失败,请连接网络");
        }else if(district==null || district.isEmpty()){
            //没有城区信息，只显示到城市
            tv_adress.setText(city);
        }else{
            tv_adress.setText(city+district);
        }

       // Log.d("location","locationCallback:"+city+" "+district);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.im_menu_main_title_bar: //菜单键
            {
                mMenuPopWindow.showAsDropDown(iv_menu,0,40);
                break;
            }
            case R.id.leftlayout_main_title_bar: //定位按钮
            {
                break;
            }
            case R.id.frame_msg_main_title_bar: //信箱按钮
            {
                break;
            }
            case R.id.tv_menu_scan: //扫一扫按钮
            {
                break;
            }
            default:
                break;
        }
    }
}