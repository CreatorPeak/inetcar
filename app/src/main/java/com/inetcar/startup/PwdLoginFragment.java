package com.inetcar.startup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inetcar.main.MainCarActivity;
import com.inetcar.model.User;
import com.inetcar.tools.NetWorkUtils;
import com.inetcar.tools.ResultCodeUtils;
import com.inetcar.tools.ShowDialogInterface;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * 密码登录Fragment
 */
public class PwdLoginFragment extends Fragment implements View.OnClickListener{


    private View view_pwdlogin;
    private Button btn_login;
    private EditText et_phone;
    private EditText et_pwd;
    private TextView tv_forgetpwd;


    private Context mContext;
    private LoginHandler mHandler;
    private Gson mGson;
    private User user;

    private ShowDialogInterface mDialog;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        mDialog = (ShowDialogInterface)this.getActivity();
        super.onAttach(context);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view_pwdlogin = inflater.inflate(R.layout.fragment_pwdlogin,container,false);
        mHandler = new LoginHandler(this);
        mGson = new Gson();
        initView();
        return view_pwdlogin;
    }

    /**
     * 加载控件并初始化
     */
    private void initView() {


        btn_login = (Button) view_pwdlogin.findViewById(R.id.btn_pwdlogin);
        btn_login.setOnClickListener(this);
        tv_forgetpwd = (TextView) view_pwdlogin.findViewById(R.id.tv_pwdlogin_forgetpwd);
        tv_forgetpwd.setOnClickListener(this);
        et_phone = (EditText) view_pwdlogin.findViewById(R.id.et_pwdlogin_phone);
        et_pwd = (EditText) view_pwdlogin.findViewById(R.id.et_pwdlogin_pwd);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_pwdlogin:   //登录
            {
                login();

                break;
            }
            case R.id.tv_pwdlogin_forgetpwd: //忘记密码
            {
                break;
            }
            default:
                break;
        }
    }

    /**
     * 登录
     */
    private void login(){
        String phone = et_phone.getText().toString();
        if(phone==null || phone.trim().isEmpty()){
            Toast.makeText(mContext,"请输入手机号",Toast.LENGTH_LONG).show();
            return;
        }

        String pwd = et_pwd.getText().toString();
        if(pwd==null || pwd.trim().isEmpty()){
            Toast.makeText(mContext,"请输入密码",Toast.LENGTH_LONG).show();
            return;
        }

        if(phone.trim().length()<11){
            Toast.makeText(mContext,"手机号长度不符",Toast.LENGTH_LONG).show();
            return;
        }
        if(pwd.trim().length()<11){
            Toast.makeText(mContext,"密码长度不符",Toast.LENGTH_LONG).show();
            return;
        }

        mDialog.showDialog();

        LoginTask task = new LoginTask(phone.trim(),pwd.trim());
        task.execute();
    }

    private class LoginHandler extends Handler{

        private WeakReference<PwdLoginFragment>mActivity;

        public LoginHandler(PwdLoginFragment activity){
            mActivity = new WeakReference<PwdLoginFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PwdLoginFragment activity = mActivity.get();
            mDialog.hideDialog();
            switch (msg.what){
                case ResultCodeUtils.MSG_SUCCESS:
                {
                    String result = (String) msg.obj;
                    Log.d("user", result);
                    user = mGson.fromJson(result, User.class);
                    if(user!=null){

                        Toast.makeText(mContext,"登录成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(mContext, MainCarActivity.class);
                        intent.putExtra("user",user);
                        mContext.startActivity(intent);
                        activity.finishActivity();
                    }
                    break;
                }
                case ResultCodeUtils.MSG_EXCEPTION:
                {
                    Toast.makeText(mContext,"数据出现异常，请重试",Toast.LENGTH_LONG).show();
                    break;
                }
                case ResultCodeUtils.MSG_EMPTY:
                {
                    Toast.makeText(mContext,"网络连接失败，请检查网络",Toast.LENGTH_LONG).show();
                    break;
                }
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void finishActivity(){
        this.getActivity().finish();
    }
    /**
     * 登录异步类
     */
    class LoginTask extends AsyncTask<Void,Integer,Void>{

        private String phone;
        private String passwd;
        private String result;

        public LoginTask(String phone, String passwd) {
            this.phone = phone;
            this.passwd = passwd;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>(2);
            param.add(new BasicNameValuePair("phone",phone));
            param.add(new BasicNameValuePair("passwd",passwd));
            try {
                result = NetWorkUtils.doTask("/PwdLoginServlet",param);
                if(result!=null){
                    publishProgress(ResultCodeUtils.MSG_SUCCESS);
                }else{
                    publishProgress(ResultCodeUtils.MSG_EMPTY);
                }

            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(ResultCodeUtils.MSG_EXCEPTION);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Message msg = new Message();
            msg.what = values[0];
            msg.obj = result;
            mHandler.sendMessage(msg);
            super.onProgressUpdate(values);
        }

    }

    @Override
    public void onStop() {

        super.onStop();
    }
}
