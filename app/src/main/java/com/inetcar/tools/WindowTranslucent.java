package com.inetcar.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * android 4.4 以上实现状态栏沉浸
 */
public class WindowTranslucent {

    //设置状态栏透明

    public static void setWindowTranslucent(Activity ac){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            Window window = ac.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //设置状态栏沉浸
    public static void setWindowStatusColor(Activity ac,String color){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){

            Window window = ac.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View v = new View(ac);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(ac));
            v.setBackgroundColor(Color.parseColor(color));
            v.setLayoutParams(lParams);
            ViewGroup view = (ViewGroup) ac.getWindow().getDecorView();
            view.addView(v);
        }

    }
    //手机屏幕顶部状态栏高度
    public static int getStatusBarHeight(Context ac) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = ac.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
    // 获取ActionBar的高度
    public static int getActionBarHeight(Activity ac) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (ac.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, ac.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

}
