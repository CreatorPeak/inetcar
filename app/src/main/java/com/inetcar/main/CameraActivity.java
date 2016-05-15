package com.inetcar.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.inetcar.camera.CameraManager;
import com.inetcar.decoding.CaptureActivityHandler;
import com.inetcar.decoding.InactivityTimer;
import com.inetcar.startup.R;
import com.inetcar.tools.WindowTranslucent;
import com.inetcar.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

import static com.inetcar.startup.R.id.preview_camera;

public class CameraActivity extends Activity implements SurfaceHolder.Callback{

    private TextView tv_back;
    private ViewfinderView viewfinderView;

    private boolean hasSurface = false;
    private InactivityTimer inactivityTimer;
    private CaptureActivityHandler mHandler;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowTranslucent.setWindowStatusColor(this, "#0f6ad5");
        setContentView(R.layout.activity_camera);

        tv_back = (TextView) findViewById(R.id.tv_camera_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_camera);

        CameraManager.init(getApplicationContext());
        inactivityTimer = new InactivityTimer(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(preview_camera);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if(hasSurface){
            initCamera(surfaceHolder);
        }else{
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }catch (RuntimeException e){
            e.printStackTrace();
            return;
        }
        if(mHandler==null){
            mHandler = new CaptureActivityHandler(this,decodeFormats,characterSet);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(CameraActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
        }else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result",resultString);
//            Bundle bundle = new Bundle();
//            bundle.putString("result", resultString);
            Log.d("path", "handleDecode result: "+resultString);
            //bundle.putParcelable("bitmap", barcode);
            //resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link @Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(!hasSurface){
            hasSurface = true;
            initCamera(holder);
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView(){
        return viewfinderView;
    }

    public Handler getHandler(){
        return mHandler;
    }

    public void drawViewfinder(){
        viewfinderView.drawViewfinder();
    }
}
