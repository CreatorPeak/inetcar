package com.inetcar.me;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inetcar.startup.R;
import com.inetcar.tools.ImageFolder;
import com.inetcar.tools.ResultCodeUtils;
import com.inetcar.tools.WindowTranslucent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 相册列表类
 */
public class AlbumActivity extends Activity implements AdapterView.OnItemClickListener{

    private TextView tv_back;       //返回
    private ListView list_album;    //相册列表

    //存储所有图片的地址，格式为Map<目录名，该目录下图片地址>
    private HashMap<String,List<String>> mDirectoryMap = new HashMap<String,List<String>>();
    //存储每张图片的父目录及该目录的图片数量及第一张图片的地址
    private List<ImageFolder>mImgFolderList = new ArrayList<ImageFolder>();


    private final static int SCAN_FINISH = 1;   //扫描完成

    private ProgressDialog mProgressDialog;

    private AlbumListAdapter mAlbumAdapter;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mProgressDialog!=null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            switch (msg.what){
                case SCAN_FINISH:
                {
                    constructImgFolder();
                    mAlbumAdapter = new AlbumListAdapter(AlbumActivity.this,
                            mImgFolderList,list_album);
                    list_album.setAdapter(mAlbumAdapter);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowTranslucent.setWindowStatusColor(this, "#0f6ad5");
        setContentView(R.layout.activity_album);

        tv_back  = (TextView) this.findViewById(R.id.tv_album_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        list_album  = (ListView) this.findViewById(R.id.lv_album);
        getImages();
        list_album.setOnItemClickListener(this);

    }

    /**
     * 利用ContentProvider扫描手机中的图片
     */
    private void getImages(){

         if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
             Toast.makeText(this,"没有SD卡",Toast.LENGTH_SHORT).show();
             return;
         }

        mProgressDialog = ProgressDialog.show(this,null,"正在加载");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AlbumActivity.this.getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri,null,
                        MediaStore.Images.Media.MIME_TYPE+"=? or " +
                                MediaStore.Images.Media.MIME_TYPE+"=?",new String[]
                                {"image/jpeg","images/png"},
                        MediaStore.Images.Media.DATE_ADDED
                        );
                while(mCursor.moveToNext()){
                    //获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(
                            MediaStore.Images.Media.DATA));
                    //获取图片目录名
                    String directory = new File(path).getParentFile().getName();
                    //根据目录存放图片,如果是第一次则新建一个list，否则直接存入list中
                    if(!mDirectoryMap.containsKey(directory)){
                        List<String> childList = new ArrayList<String>();
                        childList.add(path);
                        mDirectoryMap.put(directory,childList);
                    }else{
                        mDirectoryMap.get(directory).add(path);
                    }
                }
                mCursor.close();

                //通知handler图片已经扫描完成
                mHandler.sendEmptyMessage(SCAN_FINISH);
            }
        }).start();
    }

    /**
     * 根据mDirectoryMap记录的图片构造ImageFolder列表
     */
    private void constructImgFolder(){

        if(mDirectoryMap==null || mDirectoryMap.size() == 0){
            return;
        }

        Iterator<Map.Entry<String,List<String>>>iterator = mDirectoryMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,List<String>> entry = iterator.next();
            ImageFolder tempFolder = new ImageFolder();
            tempFolder.setFolderName(entry.getKey());
            List<String> value = entry.getValue();
            tempFolder.setTopImagePath(value.get(0));
            tempFolder.setImageCounts(value.size());
            mImgFolderList.add(tempFolder);
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ArrayList<String> childList = (ArrayList<String>) mDirectoryMap.get(
                mImgFolderList.get(position).getFolderName());
        //跳转到图片显示界面，传入该目录下的图片地址到其中
        Intent intent = new Intent(AlbumActivity.this,ShowImageActivity.class);
        intent.putStringArrayListExtra("images",childList);
        intent.putExtra("folder",mImgFolderList.get(position).getFolderName());
        startActivityForResult(intent, ResultCodeUtils.SELECT_IMAGE);
    }


    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     * <p>This method is never invoked if your activity sets
     * {@link @android.R.styleable#AndroidManifestActivity_noHistory noHistory} to
     * <code>true</code>.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==ResultCodeUtils.SELECT_IMAGE){
            if(resultCode==Activity.RESULT_OK){
                setResult(Activity.RESULT_OK,data);
                finish();
            }else{
                setResult(Activity.RESULT_CANCELED);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
