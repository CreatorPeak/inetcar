package com.inetcar.startup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 密码登录Fragment
 */
public class PwdLoginFragment extends Fragment implements View.OnClickListener{


    private View view_pwdlogin;
    private Button btn_login;
    private EditText et_phone;
    private EditText et_pwd;
    private TextView tv_forgetpwd;

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
}
