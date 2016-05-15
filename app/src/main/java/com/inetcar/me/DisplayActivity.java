package com.inetcar.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inetcar.startup.R;
import com.inetcar.tools.NativeImageLoader;
import com.inetcar.tools.WindowTranslucent;

public class DisplayActivity extends Activity implements View.OnClickListener{

    private TextView tv_back;
    private TextView tv_queding;
    private ImageView img_content;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowTranslucent.setWindowStatusColor(this, "#0f6ad5");
        setContentView(R.layout.activity_display);

        tv_back = (TextView) findViewById(R.id.tv_display_back);
        tv_back.setOnClickListener(this);

        tv_queding = (TextView) findViewById(R.id.tv_display_queding);
        tv_queding.setOnClickListener(this);

        img_content = (ImageView) findViewById(R.id.img_display_content);

        path = this.getIntent().getStringExtra("path");

        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,
                new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                if(bitmap!=null){
                    img_content.setImageBitmap(bitmap);
                }
            }
        });

        if(bitmap!=null){
            img_content.setImageBitmap(bitmap);
        }else{
            img_content.setImageResource(R.mipmap.empty_picture);
        }

    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.tv_display_back:
           {
               setResult(Activity.RESULT_CANCELED);
               finish();
               break;
           }
           case R.id.tv_display_queding:
           {
               Intent data = new Intent();
               data.putExtra("path",path);
               setResult(Activity.RESULT_OK,data);
               finish();
               break;
           }
           default:
               break;
       }
    }


    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
