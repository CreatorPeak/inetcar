package com.inetcar.tools;

/**
 * 所有网络请求的返回结果
 */
public class ResultCodeUtils {

    public final static int MSG_SUCCESS = 100;
    public final static int MSG_EXCEPTION = 101;
    public final static int MSG_EMPTY = 102;
    public final static String USERINFO = "USERINFO";

    public final static int SELECT_IMAGE = 11; //从相册选取图片requestCode
    public final static int USE_CAMERA = 12;   //拍照requestCode

    public final static int SAVE_SUCCESS = 201; //文件保存成功
    public final static int UPLOAD_SUCCESS = 202;  //文件上传成功
    public final static int UPLOAD_FAILED = 203;    //文件上传失败

}
