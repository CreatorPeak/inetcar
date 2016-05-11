package com.inetcar.startup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inetcar.main.MainCarActivity;
import com.inetcar.tools.FragmentManager;
import com.inetcar.tools.LoadingDialog;
import com.inetcar.tools.ResultCodeUtils;
import com.inetcar.tools.ShowDialogInterface;
import com.inetcar.tools.WindowTranslucent;

import java.util.ArrayList;

public class LoginActivity extends FragmentActivity implements View.OnClickListener,
        ShowDialogInterface{

    private TextView tv_back;       //返回按钮
    private TextView tv_skip;       //跳过按钮
    private RadioGroup rg_login;    //tab按钮

    private ArrayList<Fragment> mFragments;
    private FragmentManager mFragmentManager;
    private LoadingDialog mDialog;

    private int mCurrentPage = 0;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowTranslucent.setWindowStatusColor(this,"#0f6ad5");
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences(ResultCodeUtils.USERINFO,
                Context.MODE_PRIVATE);
        mCurrentPage = this.getIntent().getIntExtra("page",0);

        loadFragments();
        initViews();
    }

    /**
     * 加载并初始化控件
     */
    private void initViews() {

        mDialog = new LoadingDialog(this);

        tv_back = (TextView) findViewById(R.id.tv_login_back);
        tv_back.setOnClickListener(this);
        tv_skip = (TextView) findViewById(R.id.tv_login_skip);
        tv_skip.setOnClickListener(this);
        rg_login = (RadioGroup) findViewById(R.id.rg_login_tab);

        mFragmentManager = new FragmentManager(mFragments,rg_login,this,
                R.id.linear_loginfragnent_container,mCurrentPage);
    }

    /**
     * 加载密码登录、验证码登录、手机注册Fragment
     */
    private void loadFragments() {

        mFragments = new ArrayList<Fragment>(3);
        PwdLoginFragment pwdLoginFragment = new PwdLoginFragment();
        CodeLoginFragment codeLoginFragment = new CodeLoginFragment();

        String temp = sharedPreferences.getString("phone",null);
        if(temp!=null && !temp.trim().isEmpty()){
            Bundle bundle = new Bundle();
            bundle.putString("phone",temp);
            pwdLoginFragment.setArguments(bundle);
            codeLoginFragment.setArguments(bundle);
        }

        mFragments.add(pwdLoginFragment);
        mFragments.add(codeLoginFragment);
        mFragments.add(new RegisterFragment());
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.tv_login_skip: //跳过
            {
                Intent intent = new Intent(LoginActivity.this, MainCarActivity.class);
                startActivity(intent);
                this.finish();
                break;
            }
            case R.id.tv_login_back: //返回
            {
                this.finish();
                break;
            }
           default:
               break;
        }
    }

    @Override
    public void showDialog() {

        if(mDialog!=null && !mDialog.isShowing()){
            mDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if(mDialog!=null && mDialog.isShowing()){
            mDialog.hide();
        }
    }

    /**
     * Dispatch onStop() to all fragments.  Ensure all loaders are stopped.
     */
    @Override
    protected void onStop() {
        if(mDialog!=null){
            mDialog.dismiss();
        }
        super.onStop();
    }
}
