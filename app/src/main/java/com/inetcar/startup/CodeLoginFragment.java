package com.inetcar.startup;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * 验证码登录Fragment
 */
public class CodeLoginFragment extends Fragment implements View.OnClickListener{

    private View view_codelogin;
    private EditText et_phone;
    private EditText et_code;
    private Button btn_sendcode;
    private Button btn_login;

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

        view_codelogin = inflater.inflate(R.layout.fragment_codelogin,container,false);
        initView();
        return view_codelogin;
    }

    /**
     * 加载并初始化控件
     */
    private void initView() {
        et_phone = (EditText) view_codelogin.findViewById(R.id.et_codelogin_phone);
        et_code = (EditText) view_codelogin.findViewById(R.id.et_codelogin_code);
        btn_sendcode = (Button) view_codelogin.findViewById(R.id.btn_codelogin_send_code);
        btn_sendcode.setOnClickListener(this);
        btn_login = (Button) view_codelogin.findViewById(R.id.btn_codelogin);
        btn_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_codelogin:    //验证码登录
            {
                break;
            }
            case R.id.btn_codelogin_send_code: //获取验证码
            {
                break;
            }
            default:
                break;
        }
    }
}
