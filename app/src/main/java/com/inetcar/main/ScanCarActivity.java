package com.inetcar.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inetcar.model.Car;
import com.inetcar.startup.R;
import com.inetcar.tools.CarIconUtils;
import com.inetcar.tools.LoadingDialog;
import com.inetcar.tools.MyResult;
import com.inetcar.tools.NetWorkUtils;
import com.inetcar.tools.ResultCodeUtils;
import com.inetcar.tools.WindowTranslucent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;

public class ScanCarActivity extends Activity implements View.OnClickListener,
        DialogInterface.OnClickListener {

    private TextView tv_back;
    private Button btn_add;

    private ImageView img_icon;                    //汽车标志
    private TextView tv_brand;                     //汽车品牌
    private TextView tv_type;                      //汽车型号
    private TextView tv_platenumber;               //汽车品牌
    private TextView tv_enginenumber;              //汽车品牌
    private TextView tv_bodylevel;                 //汽车品牌
    private TextView tv_mileage;                   //汽车品牌
    private TextView tv_gasline;                   //汽车品牌
    private TextView tv_engineperformance;         //汽车品牌
    private TextView tv_transmissionperformance;   //汽车品牌
    private TextView tv_light;                     //汽车品牌

    private Car car;
    private int uid;

    private AlertDialog.Builder builder;
    private LoadingDialog loadingDialog;

    private Gson mGson;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(loadingDialog!=null && loadingDialog.isShowing())
                loadingDialog.hide();

            if(msg.what==ResultCodeUtils.MSG_EMPTY){
                Toast.makeText(ScanCarActivity.this,"请连接网络",Toast.LENGTH_SHORT).show();
            }else{
                MyResult myResult = mGson.fromJson((String)msg.obj,MyResult.class);
                switch (myResult.getStatus()){
                    case 305:
                    {
                        Toast.makeText(ScanCarActivity.this,myResult.getMsg(),Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 307:
                    {
                        Toast.makeText(ScanCarActivity.this,"添加失败，请重试",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 308:
                    {
                        Toast.makeText(ScanCarActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        int cid = Integer.parseInt(myResult.getMsg());
                        Intent intent = new Intent();
                        intent.putExtra("cid",cid);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                        break;
                    }

                    default:
                        break;
                }
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowTranslucent.setWindowStatusColor(this, "#0f6ad5");
        setContentView(R.layout.activity_scan_car);
        car = (Car) this.getIntent().getSerializableExtra("car");
        uid = this.getIntent().getIntExtra("uid",0);
        mGson = new Gson();
        initView();
    }

    private void initView() {

        loadingDialog = new LoadingDialog(this);

        tv_back = (TextView) findViewById(R.id.tv_back_scancar);
        tv_back.setOnClickListener(this);

        btn_add = (Button) findViewById(R.id.btn_add_scancar);
        btn_add.setOnClickListener(this);

        img_icon = (ImageView) findViewById(R.id.img_caricon_scancar);

        tv_brand =(TextView) findViewById(R.id.tv_brand_right_scancar);
        tv_type =(TextView) findViewById(R.id.tv_type_right_scancar);
        tv_platenumber =(TextView) findViewById(R.id.tv_platenumber_right_scancar);
        tv_enginenumber =(TextView) findViewById(R.id.tv_enginenumber_right_scancar);
        tv_bodylevel =(TextView) findViewById(R.id.tv_bodylevel_right_scancar);
        tv_mileage =(TextView) findViewById(R.id.tv_mileage_right_scancar);
        tv_gasline =(TextView) findViewById(R.id.tv_gasline_right_scancar);
        tv_engineperformance =(TextView) findViewById(R.id.tv_engineperformance_right_scancar);
        tv_transmissionperformance =(TextView) findViewById(R.id.tv_transmissionperformance_right_scancar);
        tv_light =(TextView) findViewById(R.id.tv_light_right_scancar);

        if(car!=null){
            if(CarIconUtils.ImageIcons.containsKey(car.getBrand()))
                car.setLogo(CarIconUtils.ImageIcons.get(car.getBrand()));

            img_icon.setImageResource(car.getLogo());
            tv_brand.setText(car.getBrand());
            tv_type.setText(car.getType());
            tv_platenumber.setText(car.getPlateNumber());
            tv_enginenumber.setText(car.getEngineNumber());
            tv_bodylevel.setText(car.getBodyLevel());
            tv_mileage.setText(car.getMileage()+"公里");
            tv_gasline.setText(car.getGasoline()+"%");
            if(car.getEnginePerformance()!='1')
                tv_engineperformance.setText("异常");
            if(car.getTransmissionPerformance()!='1')
                tv_transmissionperformance.setText("异常");
            if(car.getLightPerformance()!='1')
                tv_light.setText("异常");

        }

        builder = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否放弃添加汽车？")
                    .setPositiveButton("是",this)
                    .setNegativeButton("否",this);

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        builder.show();
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.tv_back_scancar){    //返回
            builder.show();
        }else if(v.getId()==R.id.btn_add_scancar){  //添加到我的车库

            new Thread(new Runnable(){
                @Override
                public void run() {
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("uid",uid+""));
                    params.add(new BasicNameValuePair("brand",car.getBrand()));
                    params.add(new BasicNameValuePair("type",car.getType()));
                    params.add(new BasicNameValuePair("platenumber",car.getPlateNumber()));
                    params.add(new BasicNameValuePair("enginenumber",car.getEngineNumber()));
                    params.add(new BasicNameValuePair("bodylevel",car.getBodyLevel()));
                    params.add(new BasicNameValuePair("mileage",car.getMileage()+""));
                    params.add(new BasicNameValuePair("gasoline",car.getGasoline()+""));
                    params.add(new BasicNameValuePair("engine",car.getEnginePerformance()+""));
                    params.add(new BasicNameValuePair("transmission",car.getTransmissionPerformance()+""));
                    params.add(new BasicNameValuePair("light",car.getLightPerformance()+""));
                    try {
                        String result = NetWorkUtils.doTask("/SaveCarServlet",params);
                        if(result==null){
                            mHandler.sendEmptyMessage(ResultCodeUtils.MSG_EMPTY);
                        }else{
                            Message msg = mHandler.obtainMessage();
                            msg.obj = result;
                            msg.what = ResultCodeUtils.MSG_SUCCESS;
                            mHandler.sendMessage(msg);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param which  The button that was clicked (e.g.
     *               {@link DialogInterface#BUTTON1}) or the position
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        if(which==DialogInterface.BUTTON_POSITIVE){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }


    @Override
    protected void onStop() {
        if(loadingDialog!=null)
            loadingDialog.dismiss();
        super.onStop();
    }
}
