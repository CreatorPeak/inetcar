package com.inetcar.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inetcar.meg7.widget.CircleImageView;
import com.inetcar.model.User;
import com.inetcar.startup.LoginActivity;
import com.inetcar.startup.R;
import com.inetcar.tools.HandlerPicture;
import com.inetcar.tools.MyResult;
import com.inetcar.tools.NativeImageLoader;
import com.inetcar.tools.NetWorkUtils;
import com.inetcar.tools.ResultCodeUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MeFragment extends Fragment implements View.OnClickListener{


    private View view_me;

    private LinearLayout linear_user;
    private CircleImageView im_photo; //用户头像按钮
    private TextView tv_name;         //用户名称
    private TextView tv_phone;        //用户电话

    private LinearLayout linear_login;
    private Button btn_login;         //登陆按钮
    private Button btn_register;      //注册按钮

    private Context mContext;
    private User mUser = null;         //当前登录用户

    private MyHandler mHandler;
    private boolean isLogin = false;  //用户是否登录
    private Gson mGson;
    private SharedPreferences sharedPreferences;

    private PopupWindow mPhotoWindow;
    private View view_photoWindow;
    private TextView tv_camera;  //拍照
    private TextView tv_album;  //从相册选取图片

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        mContext = context;
        if(this.getArguments()!=null){
            mUser = (User) this.getArguments().getSerializable("user");
            isLogin = this.getArguments().getBoolean("isLogin",false);
        }

        mGson = new Gson();
        sharedPreferences = mContext.getSharedPreferences(ResultCodeUtils.USERINFO,
                Context.MODE_PRIVATE);
        mHandler = new MyHandler(this);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view_me = inflater.inflate(R.layout.fragment_tab_me, container, false);
        //加载用户点击头像的弹窗的View
        view_photoWindow = inflater.inflate(R.layout.photodialog_fragment_me,container,false);
        initView();
        return view_me;
    }

    private void initView() {

        linear_user = (LinearLayout) view_me.findViewById(R.id.linear_tab_me_user);
        im_photo = (CircleImageView) view_me.findViewById(R.id.im_tab_me_user);
        im_photo.setOnClickListener(this);
        tv_name = (TextView) view_me.findViewById(R.id.tv_tab_me_username);
        tv_name.setOnClickListener(this);
        tv_phone = (TextView) view_me.findViewById(R.id.tv_tab_me_userphone);
        tv_phone.setOnClickListener(this);

        linear_login = (LinearLayout) view_me.findViewById(R.id.linear_tab_me_login);
        btn_login = (Button) view_me.findViewById(R.id.btn_tab_me_login);
        btn_login.setOnClickListener(this);
        btn_register = (Button) view_me.findViewById(R.id.btn_tab_me_register);
        btn_register.setOnClickListener(this);

        if(mUser!=null && isLogin){
            linear_login.setVisibility(View.GONE);
            linear_user.setVisibility(View.VISIBLE);
            tv_name.setText(mUser.getUname());
            tv_phone.setText(mUser.getPhone());

        }else{
            linear_user.setVisibility(View.GONE);
            linear_login.setVisibility(View.VISIBLE);
        }

        mPhotoWindow = new PopupWindow(view_photoWindow, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPhotoWindow.setFocusable(true);
        mPhotoWindow.setBackgroundDrawable(new ColorDrawable(00000000));
        mPhotoWindow.setOutsideTouchable(true); //点击外部弹出框会消失

        tv_camera = (TextView) view_photoWindow.findViewById(R.id.tv_camera_photodialog);
        tv_camera.setOnClickListener(this);
        tv_album = (TextView) view_photoWindow.findViewById(R.id.tv_album_photodialog);
        tv_album.setOnClickListener(this);

    }

    public boolean getLoginStatus(){
        return isLogin;
    }
    public User getLoginUser() {return mUser;}

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.im_tab_me_user:  //点击头像按钮
            {
                if(!isLogin){
                    Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(mPhotoWindow!=null && !mPhotoWindow.isShowing()){
                    mPhotoWindow.showAtLocation(view_me, Gravity.CENTER,0,0);
                }
                break;
            }
            case R.id.tv_tab_me_username:  //点击姓名
            case R.id.tv_tab_me_userphone: //点击电话
            {

                break;
            }
            case R.id.btn_tab_me_login: //登陆
            {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("page",0);
                startActivity(intent);
                this.getActivity().finish();
                break;
            }
            case R.id.btn_tab_me_register: //注册
            {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("page",2);
                startActivity(intent);
                this.getActivity().finish();
                break;
            }
            case R.id.tv_camera_photodialog: //点击拍照按钮
            {
                if(mPhotoWindow!=null && mPhotoWindow.isShowing()){
                    mPhotoWindow.dismiss();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用相机拍照
                startActivityForResult(intent,ResultCodeUtils.USE_CAMERA);
                break;
            }
            case R.id.tv_album_photodialog: //点击相册按钮
            {
                if(mPhotoWindow!=null && mPhotoWindow.isShowing()){
                    mPhotoWindow.dismiss();
                }
                Intent intent = new Intent(mContext,AlbumActivity.class);
                startActivityForResult(intent,ResultCodeUtils.SELECT_IMAGE);
                break;
            }
            default:
                break;
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link @Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        if(mUser!=null && !isLogin){
            LoginTask task = new LoginTask(mUser.getPhone(),mUser.getPasswd());
            task.execute();
        }
    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ResultCodeUtils.SELECT_IMAGE:
            {
                if(resultCode== Activity.RESULT_OK){

                    String path = data.getStringExtra("path");
                    //显示图片到头像控件
                    Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,
                            new NativeImageLoader.NativeImageCallBack() {
                        @Override
                        public void onImageLoader(Bitmap bitmap, String path) {
                            if(bitmap!=null){
                                im_photo.setImageBitmap(bitmap);
                                savePhoto(bitmap);
                            }
                        }
                    });
                    if(bitmap!=null){
                        im_photo.setImageBitmap(bitmap);
                        savePhoto(bitmap);
                    }else{
                        im_photo.setImageResource(R.mipmap.user_no_login);
                    }
                    //upLoadPhoto(path);
                   // Log.d("path", "root path: "+mContext.getFilesDir());
                }else{
                    Toast.makeText(mContext,"未选择任何图片",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case ResultCodeUtils.USE_CAMERA:
            {
                if(resultCode== Activity.RESULT_OK){

                    Toast.makeText(mContext,"拍照成功",Toast.LENGTH_SHORT).show();
                    //不设定Uri，data中返回缩略图；设定uri，data==null
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    Log.d("path", "缩略图：width:"+thumbnail.getWidth()+" height:"+
                            thumbnail.getHeight());
                    im_photo.setImageBitmap(thumbnail);
                    //保存图片
                    savePhoto(thumbnail);
                }else{
                    Toast.makeText(mContext,"取消拍照",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存用户头像到内部存储
     * @param bitmap
     */
    private void savePhoto(final Bitmap bitmap){

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(mContext.getFilesDir(),mUser.getPhone()+".png");
                if(file.exists()){
                    file.delete();
                }
                FileOutputStream out;
                try {
                    out = new FileOutputStream(file);
                    if(bitmap.compress(Bitmap.CompressFormat.PNG,100,out)){
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message msg = mHandler.obtainMessage();
                msg.what = ResultCodeUtils.SAVE_SUCCESS;
                msg.obj = file.getAbsolutePath();
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    /**
     * 上传文件到服务器
     * @param @path  文件路径
     */

    /*
    private void upLoadPhoto(String path){

        try {
            final FileBody filebody = new FileBody(new File(path));
            final StringBody strBody = new StringBody(mUser.getPhone());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String result = NetWorkUtils.upLoad("/UpLoadPhotoServlet",
                                filebody,strBody);
                        if(result!=null){
                            Message msg = mHandler.obtainMessage();
                            msg.what = ResultCodeUtils.UPLOAD_SUCCESS;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }else{
                            mHandler.sendEmptyMessage(ResultCodeUtils.UPLOAD_FAILED);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }*/
    private  class MyHandler extends Handler{
        private WeakReference<MeFragment>mFragment;

        public MyHandler(MeFragment fragment) {
            this.mFragment =  new WeakReference<MeFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case ResultCodeUtils.MSG_SUCCESS:
                {

                    MyResult tempMyResult = mGson.fromJson((String)msg.obj, MyResult.class);
                    if(tempMyResult!=null){
                        switch (tempMyResult.getStatus()){
                            case 302:    //用户不存在
                            {
                               // Toast.makeText(mContext,"该手机号未注册",Toast.LENGTH_SHORT).show();
                                linear_user.setVisibility(View.GONE);
                                linear_login.setVisibility(View.VISIBLE);
                                isLogin = false;
                                delSharedData();
                                break;
                            }
                            case 303:    //手机号与密码不匹配
                            {
                                Toast.makeText(mContext,"手机号与密码不匹配，请重新输入",
                                        Toast.LENGTH_SHORT).show();
                                linear_user.setVisibility(View.GONE);
                                linear_login.setVisibility(View.VISIBLE);
                                isLogin = false;
                                delSharedData();
                                break;
                            }
                            case 304:    //登录成功，返回User信息
                            {
                                mUser = mGson.fromJson(tempMyResult.getMsg(), User.class);
                                if(mUser!=null){

                                    Toast.makeText(mContext,"登录成功",Toast.LENGTH_SHORT).show();
                                    linear_login.setVisibility(View.GONE);
                                    linear_user.setVisibility(View.VISIBLE);
                                    tv_name.setText(mUser.getUname());
                                    tv_phone.setText(mUser.getPhone());
                                    isLogin = true;

                                    mUser.setPhoto(sharedPreferences.getString("photo",null));

                                    if(mUser.getPhoto()!=null && !mUser.getPhoto().isEmpty()){
                                        File file = new File(mUser.getPhoto());
                                        if(file.exists()){
                                            Bitmap bitmap = HandlerPicture.cutPicture(mUser.getPhoto(),1);
                                            if(bitmap!=null){
                                                im_photo.setImageBitmap(bitmap);
                                            }
                                        }
                                    }else{
                                        Log.d("path", "user photo is null");
                                    }

                                }else{
                                    Toast.makeText(mContext,"数据解析异常",Toast.LENGTH_SHORT).show();
                                    linear_user.setVisibility(View.GONE);
                                    linear_login.setVisibility(View.VISIBLE);
                                    isLogin = false;
                                }
                                break;
                            }
                            default:
                            {
                                linear_user.setVisibility(View.GONE);
                                linear_login.setVisibility(View.VISIBLE);
                                isLogin = false;
                                break;
                            }
                        }
                    }else{
                        Toast.makeText(mContext,"数据解析异常",Toast.LENGTH_SHORT).show();
                        linear_user.setVisibility(View.GONE);
                        linear_login.setVisibility(View.VISIBLE);
                        isLogin = false;
                    }
                    break;
                }
                case ResultCodeUtils.MSG_EMPTY:
                case ResultCodeUtils.MSG_EXCEPTION:
                {
                    Toast.makeText(mContext,"网络连接失败，请检查网络",Toast.LENGTH_LONG).show();
                    linear_user.setVisibility(View.GONE);
                    linear_login.setVisibility(View.VISIBLE);
                    isLogin = false;
                    break;
                }
                case ResultCodeUtils.SAVE_SUCCESS:  //文件保存成功
                {
                    mUser.setPhoto((String)msg.obj);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("photo",(String)msg.obj);
                    editor.commit();
                    Log.d("path","save success: " +(String)msg.obj);
                    //头像保存成功才上传
                    //upLoadPhoto((String)msg.obj);
                    break;
                }
                case ResultCodeUtils.UPLOAD_FAILED:
                {
                    Toast.makeText(mContext,"头像上传失败，请联网",
                            Toast.LENGTH_SHORT).show();
                }
                case ResultCodeUtils.UPLOAD_SUCCESS:
                {
                    String result = (String) msg.obj;
                    MyResult myResult = mGson.fromJson(result,MyResult.class);
                    if(myResult.getStatus()==306){
                        Toast.makeText(mContext,"头像上传成功",
                                Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(mContext,myResult.getMsg(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 删除缓存的User信息
     */
    private void delSharedData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("uid");
        editor.remove("uname");
        editor.remove("passwd");
        editor.remove("photo");
        editor.commit();
    }

    /**
     * 登录异步类
     */
    class LoginTask extends AsyncTask<Void,Integer,Void> {

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
}
