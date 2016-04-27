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
 * 手机注册
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private View view_register;
    private EditText et_phone;
    private EditText et_name;
    private EditText et_code;
    private EditText et_pwd;
    private Button btn_send;
    private Button btn_register;

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
        view_register = inflater.inflate(R.layout.fragment_register,container,false);
        initView();
        return view_register;
    }

    /**
     * 加载并初始化控件
     */
    private void initView() {
        et_phone = (EditText) view_register.findViewById(R.id.et_register_phone);
        et_name = (EditText) view_register.findViewById(R.id.et_register_name);
        et_code = (EditText) view_register.findViewById(R.id.et_register_code);
        et_pwd = (EditText) view_register.findViewById(R.id.et_register_pwd);

        btn_send = (Button) view_register.findViewById(R.id.btn_register_send_code);
        btn_send.setOnClickListener(this);
        btn_register = (Button) view_register.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_register_send_code:
            {
                break;
            }
            case R.id.btn_register:
            {
                break;
            }
            default:
                break;
        }
    }
}
