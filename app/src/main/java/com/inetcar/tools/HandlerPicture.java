package com.inetcar.tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HandlerPicture {

    //裁剪图片
    public static Bitmap cutPicture(String path,int width,int height)
    {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;  //可以不用引进图片到内存而知道它的尺寸
        BitmapFactory.decodeFile(path, option);
        option.inSampleSize = calculateSize(option,width,height);
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, option);
    }
    public static Bitmap cutPicture(String path,int scalesize)
    {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = scalesize;
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, option);
    }


    public static Bitmap cutPicture(Context context,int resource,int width,int height)
    {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;  //可以不用引进图片到内存而知道它的尺寸
        Resources res = context.getResources();
        BitmapFactory.decodeResource(res, resource, option);
        option.inSampleSize = calculateSize(option,width,height);
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resource, option);
    }

    //计算裁剪比例
    public static int calculateSize(BitmapFactory.Options options,
                                    int reqWidth, int reqHeight)
    {
        //源图片的高度和宽度
        int realheight = options.outHeight;
        int realwidth = options.outWidth;
        //Log.i("height", realheight+"");
        //Log.i("weight", realheight+"");
        int mysize = 1;
        if(realheight > reqHeight || realwidth > reqWidth)
        {

            //四舍五入
            int heightratio = (int)Math.round((float)realheight/(float)reqHeight);
            int widthratio = (int)Math.round((float)realwidth/(float)reqWidth);

            mysize = heightratio<widthratio? heightratio:widthratio;

            //Log.i("size", mysize+"");
        }
        return mysize;
    }
}