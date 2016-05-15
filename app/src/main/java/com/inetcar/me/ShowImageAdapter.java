package com.inetcar.me;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.inetcar.startup.R;
import com.inetcar.tools.MyImageView;
import com.inetcar.tools.NativeImageLoader;

import java.util.List;

public class ShowImageAdapter extends BaseAdapter{

    private Activity activity;
    private List<String>mImages;
    private Point mPoint;
    private GridView gridView;

    public ShowImageAdapter(Activity activity, List<String> mImages,GridView gridView) {
        this.activity = activity;
        this.mImages = mImages;
        this.gridView = gridView;
        mPoint = new Point(0,0);
    }

    @Override
    public int getCount() {
        if(mImages!=null)
            return mImages.size();
        return 0;
    }


    @Override
    public Object getItem(int position) {
        if(mImages!=null)
            return mImages.get(position);
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        String path = mImages.get(position);

        if(convertView==null){

            convertView = LayoutInflater.from(activity).inflate(R.layout.gridview_showimage,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (MyImageView) convertView.findViewById(R.id.img_showimage);

            viewHolder.imageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {
                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width,height);
                }
            });

            convertView.setTag(viewHolder);
        }else{
           // Log.d("Tag", "getView: "+convertView.getTag().toString());
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.imageView.setImageResource(R.mipmap.empty_picture);
        }
        viewHolder.imageView.setTag(path);

        Bitmap mBitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint,
                new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                ImageView mImageView = (ImageView) gridView.findViewWithTag(path);
                if(bitmap!=null && mImageView!=null){
                    mImageView.setImageBitmap(bitmap);
                }
            }
        });
        if(mBitmap!=null){
            viewHolder.imageView.setImageBitmap(mBitmap);
        }else{
            viewHolder.imageView.setImageResource(R.mipmap.empty_picture);
        }
        return convertView;
    }
    class ViewHolder{
        MyImageView imageView;
    }
}
