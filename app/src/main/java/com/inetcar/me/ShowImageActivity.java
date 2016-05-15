package com.inetcar.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.inetcar.startup.R;
import com.inetcar.tools.ResultCodeUtils;
import com.inetcar.tools.WindowTranslucent;

import java.util.List;

public class ShowImageActivity extends Activity {

    private TextView tv_back;
    private TextView tv_name;
    private GridView grid_image;
    private List<String> mImages;
    private String folder;
    private ShowImageAdapter mShowImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowTranslucent.setWindowStatusColor(this, "#0f6ad5");
        setContentView(R.layout.activity_show_image);

        mImages = this.getIntent().getStringArrayListExtra("images");
        folder = this.getIntent().getStringExtra("folder");

        initView();

    }
    private void initView(){

        tv_back = (TextView) findViewById(R.id.tv_back_showimage);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        tv_name = (TextView) findViewById(R.id.tv_name_showimage);
        if(folder!=null)
            tv_name.setText(folder);

        grid_image = (GridView) findViewById(R.id.grid_showimage);
        mShowImgAdapter = new ShowImageAdapter(this,mImages,grid_image);
        grid_image.setAdapter(mShowImgAdapter);

        grid_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowImageActivity.this,DisplayActivity.class);
                intent.putExtra("path",mImages.get(position));
                startActivityForResult(intent, ResultCodeUtils.SELECT_IMAGE);
            }
        });
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
     * {@link android.R.styleable#AndroidManifestActivity_noHistory noHistory} to
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
