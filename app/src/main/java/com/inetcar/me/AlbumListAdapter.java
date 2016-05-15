package com.inetcar.me;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.inetcar.startup.R;
import com.inetcar.tools.ImageFolder;
import com.inetcar.tools.MyImageView;
import com.inetcar.tools.NativeImageLoader;

import java.util.List;

/**
 *x相册列表的adapter
 */
public class AlbumListAdapter extends BaseAdapter{

    private Activity mActivity;
    private List<ImageFolder>mImgFolderList;
    private ListView mListView;
    private Point mPoint;   //封装ImageView的宽和高

    public AlbumListAdapter(Activity mActivity, List<ImageFolder> mImgFolderList, ListView mListView) {
        this.mActivity = mActivity;
        this.mImgFolderList = mImgFolderList;
        this.mListView = mListView;
        mPoint = new Point(0,0);
    }

    @Override
    public int getCount() {
        if(mImgFolderList!=null)
            return mImgFolderList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mImgFolderList!=null)
            return mImgFolderList.get(position);
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewholder;
        ImageFolder tempImageFolder = mImgFolderList.get(position);
        String path = tempImageFolder.getTopImagePath();

        if(convertView==null){
            viewholder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.album_listview,parent,false);
            viewholder.im_content = (MyImageView) convertView.findViewById(R.id.im_album_content);
            viewholder.tv_content = (TextView) convertView.findViewById(R.id.tv_album_directory);
            viewholder.tv_count = (TextView) convertView.findViewById(R.id.tv_album_count);
            viewholder.im_content.setOnMeasureListener(new MyImageView.OnMeasureListener() {
                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width,height);
                }
            });

            convertView.setTag(viewholder);
        }else{
            viewholder = (ViewHolder) convertView.getTag();
            viewholder.im_content.setImageResource(R.mipmap.empty_picture);
        }
        viewholder.tv_content.setText(tempImageFolder.getFolderName());
        viewholder.tv_count.setText("共"+tempImageFolder.getImageCounts()+"张");
        //给imageview设置路径tag，异步加载图片的小技巧
        viewholder.im_content.setTag(path);

        //利用NativeImageLoader加载本地图片
        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint,
                new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                ImageView imageView = (ImageView) mListView.findViewWithTag(path);
                if(bitmap!=null && imageView!=null){ //图片加载完毕会调用该函数
                    imageView.setImageBitmap(bitmap); //设置图片
                }
            }
        });

        if(bitmap!=null){
            viewholder.im_content.setImageBitmap(bitmap);
        }else{
            viewholder.im_content.setImageResource(R.mipmap.empty_picture);
        }
        return convertView;
    }

    class ViewHolder{
        MyImageView im_content;
        TextView tv_content;
        TextView tv_count;
    }
}
