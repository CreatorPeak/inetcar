package com.inetcar.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inetcar.meg7.widget.CircleImageView;
import com.inetcar.startup.R;


public class MeFragment extends Fragment implements View.OnClickListener{


    private View view_me;

    private CircleImageView im_photo; //用户头像按钮
    private TextView tv_name;         //用户名称
    private TextView tv_phone;        //用户电话

    private Button btn_login;         //登陆按钮
    private Button btn_register;      //注册按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view_me = inflater.inflate(R.layout.fragment_tab_me, container, false);
        initView();
        return view_me;
    }

    private void initView() {
        im_photo = (CircleImageView) view_me.findViewById(R.id.im_tab_me_user);
        im_photo.setOnClickListener(this);
        tv_name = (TextView) view_me.findViewById(R.id.tv_tab_me_username);
        tv_name.setOnClickListener(this);
        tv_phone = (TextView) view_me.findViewById(R.id.tv_tab_me_userphone);
        tv_phone.setOnClickListener(this);

        btn_login = (Button) view_me.findViewById(R.id.btn_tab_me_login);
        btn_login.setOnClickListener(this);

        btn_register = (Button) view_me.findViewById(R.id.btn_tab_me_register);
        btn_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.im_tab_me_user:  //点击头像按钮
            {
                break;
            }
            case R.id.tv_tab_me_username:  //点击姓名
            case R.id.tv_tab_me_userphone: //点击电话
            {

                break;
            }
            case R.id.btn_tab_me_login: //登陆
            {

                break;
            }
            case R.id.btn_tab_me_register: //注册
            {

                break;
            }
            default:
                break;
        }
    }
}
