package com.inetcar.startup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.inetcar.main.MainCarActivity;
import java.lang.ref.WeakReference;


public class StartUpActivity extends Activity {


    private static boolean isFirstIn = false;   //第一次进入APP

    private static final String FIRSTIN = "firstin";

    private static int MSG_FIRST = 0x0001;
    private static int MSG_MAIN = 0x0002;

    private MyHandler handler;

    private static class MyHandler extends Handler{

        private WeakReference<StartUpActivity> m_Activity;

        public MyHandler(StartUpActivity activity){
            m_Activity = new WeakReference<StartUpActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            StartUpActivity activity = m_Activity.get();

            if(activity==null){
                return;
            }

            if(msg.what==MSG_FIRST){
                activity.gotoFisrtInActivity();

            }else if(msg.what==MSG_MAIN){

                activity.gotoMainCarActivity();
            }

            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        init();
    }



    private void init(){

        handler = new MyHandler(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences(FIRSTIN,
                Context.MODE_PRIVATE);
        isFirstIn = sharedPreferences.getBoolean(FIRSTIN,true);

        if(isFirstIn){
            //第一次进入APP，跳转到FirstInActivity界面
            Editor editor = sharedPreferences.edit();
            editor.putBoolean(FIRSTIN,false);
            editor.commit();

            handler.sendEmptyMessageDelayed(MSG_FIRST,1000);

        }else {
            //跳转到Main界面
            handler.sendEmptyMessageDelayed(MSG_MAIN,1000);
        }
    }


    /**
     * 跳转到FirstInActivity
     */
    public void gotoFisrtInActivity(){

        Intent intent = new Intent(StartUpActivity.this,FirstInActivity.class);
        startActivity(intent);
        StartUpActivity.this.finish();
    }


    /**
     * 跳转到Main界面
     */
    public void gotoMainCarActivity(){

        Intent intent = new Intent(StartUpActivity.this,MainCarActivity.class);
        startActivity(intent);
        StartUpActivity.this.finish();
    }
}
