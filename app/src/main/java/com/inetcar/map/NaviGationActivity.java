package com.inetcar.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.inetcar.startup.R;
import com.inetcar.tools.TTSController;

import java.util.ArrayList;

public class NaviGationActivity extends Activity implements AMapNaviListener,AMapNaviViewListener{

   // private TextView tv_back;

    private AlertDialog.Builder builder;

    private AMapNaviView aMapNaviView;
    private AMapNavi aMapNavi;

    private ArrayList<NaviLatLng> mStartList = new ArrayList<NaviLatLng>(); //导航起点列表
    private ArrayList<NaviLatLng> mEndList = new ArrayList<NaviLatLng>();   //导航终点列表

    private NaviLatLng mStartLatLng;        //导航起点
    private NaviLatLng mEndLatLng;          //导航终点

    private TTSController mTTSController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowTranslucent.setWindowStatusColor(this,"#0f6ad5");
        setContentView(R.layout.activity_navigation);

        //获取导航起始点和终点
        Intent intent = this.getIntent();
        mStartLatLng = new NaviLatLng(intent.getDoubleExtra("start_lat",0),
                intent.getDoubleExtra("start_lon",0));
        mEndLatLng = new NaviLatLng(intent.getDoubleExtra("end_lat",0),
                intent.getDoubleExtra("end_lon",0));
        mStartList.add(mStartLatLng);
        mEndList.add(mEndLatLng);

        initView();

        aMapNaviView = (AMapNaviView) findViewById(R.id.naviview_navigation);
        aMapNaviView.onCreate(savedInstanceState);
        aMapNaviView.setAMapNaviViewListener(this);

        aMapNavi = AMapNavi.getInstance(this);
        //设置模拟导航的速度为100km/h
        aMapNavi.setEmulatorNaviSpeed(100);
        aMapNavi.addAMapNaviListener(this);
        setAmapNaviViewOptions();

        //初始化语音合成对象
        mTTSController = TTSController.getInstance(this.getApplicationContext());
        mTTSController.init();
        mTTSController.startSpeaking();
    }

    /**
     * 设置导航界面的主题、图标及菜单项
     */
    private void setAmapNaviViewOptions() {
        if(aMapNaviView==null)
            return;

        AMapNaviViewOptions options = new AMapNaviViewOptions();
        //设置菜单按钮是否在导航界面显示
        options.setSettingMenuEnabled(false);
        //设置手显示黑夜模式
        options.setNaviNight(false);
        //设置偏航是是否重新计算路径
        options.setReCalculateRouteForYaw(true);
        //设置前方拥堵时是否重新计算路径
        options.setReCalculateRouteForTrafficJam(true);
        //设置交通播报是否打开
        options.setTrafficInfoUpdateEnabled(true);
        //设置摄像头播报是否打开
        options.setCameraInfoUpdateEnabled(true);
        //设置导航时屏幕是否一致开启
        options.setScreenAlwaysBright(true);
        //设置导航界面的颜色主题
        options.setNaviViewTopic(AMapNaviViewOptions.BLUE_COLOR_TOPIC);
        //设置是否开启路口放大图功能
        options.setCrossDisplayEnabled(false);
        aMapNaviView.setViewOptions(options);
    }

    private void initView() {

        builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否退出导航？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("否",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        /*
        tv_back = (TextView) findViewById(R.id.tv_back_navigation);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(builder!=null){
                    builder.show();
                }
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(builder!=null){
            builder.show();
        }
    }

    /**
     * 导航界面右下角功能设置按钮的回调函数
     */
    @Override
    public void onNaviSetting() {  }

    /**
     * 导航页面左下角返回按钮点击后弹出的『退出导航对话框』中选择『确定』后的回调接口。
     */
    @Override
    public void onNaviCancel() {  }

    /**
     * 导航页面左下角返回按钮的回调接口 false-由SDK主动弹出『退出导航』对话框，true-SDK不主动弹出『退出导航对话框』
     * 由用户自定义
     * @return
     */
    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    /**
     * 导航界面地图状态的回调函数
     * @param isLock 0:车头朝上状态；1:非锁车状态,即车标可以任意显示在地图区域内
     */
    @Override
    public void onNaviMapMode(int isLock) {  }

    /**
     * 界面左上角转向操作的点击回调函数
     */
    @Override
    public void onNaviTurnClick() {    }

    /**
     * 界面下一道路名称的点击回调函数
     */
    @Override
    public void onNextRoadClick() {  }

    /**
     * 界面全览按钮的点击回调
     */
    @Override
    public void onScanViewButtonClick() {  }

    /**
     * 是否锁定地图的回调函数
     * @param isLock
     */
    @Override
    public void onLockMap(boolean isLock) {  }

    /**
     * 导航view加载完成回调函数
     */
    @Override
    public void onNaviViewLoaded() { }

    /**
     * 导航创建失败时的回调函数
     */
    @Override
    public void onInitNaviFailure() {
        Toast.makeText(this,"初始化导航失败",Toast.LENGTH_SHORT).show();
    }

    /**
     * 导航创建成功时回调函数
     */
    @Override
    public void onInitNaviSuccess() {
        //计算导航路线,calculateDriveRoute(List<NaviLatLng>from,List<NaviLatLng> to,
        // List<NaviLatLng> wayPoints, int strategy)
        //from     : 指定的导航起点。支持多个起点，起点列表的尾点为实际导航起点，其他坐标点为辅助信息
        //to       : 指定的导航终点。支持多个终点，终点列表的尾点为实际导航终点，其他坐标点为辅助信息
        //wayPoints: 途经点，同时支持最多4个途经点的路径规划
        //strategy : 驾车路径规划的计算策略。参见PathPlanningStrategy
        aMapNavi.calculateDriveRoute(mStartList,mEndList,null,
                PathPlanningStrategy.DRIVING_FASTEST_TIME);
       // mTTSController.playText("正在计算最优路径");
    }

    /**
     * 启动导航后回调函数
     * @param type 1表示实时导航 2表示模拟导航
     */
    @Override
    public void onStartNavi(int type) {
        mTTSController.playText("开始导航");
    }

    /**
     * 当前方路况光柱信息有更新时回调函数
     */
    @Override
    public void onTrafficStatusUpdate() {
    }

    /**
     * 当GPS位置有更新时的回调函数
     * @param aMapNaviLocation GPS位置信息
     */
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
    }

    /**
     * 导航播报信息回调函数
     * @param type 播报类型，1-导航信息播报 2-前方路况播报 3-整体路况播报
     * @param text 播报文字
     */
    @Override
    public void onGetNavigationText(int type, String text) {
        mTTSController.playText(text);
    }

    /**
     * 模拟导航停止后回调函数
     */
    @Override
    public void onEndEmulatorNavi() {
        mTTSController.playText("导航结束");
    }

    /**
     * 到达目的地后回调函数
     */
    @Override
    public void onArriveDestination() {
        mTTSController.playText("到达目的地");
    }

    /**
     * 多路线算路成功回调函数
     */
    @Override
    public void onCalculateRouteSuccess() {
        aMapNavi.startNavi(NaviType.EMULATOR);  //路径计算成功后开始导航
    }

    /**
     * 步行或驾车路径规划失败后的回调函数
     * @param errorInfo 错误信息，参见PathPlanningErrCode
     */
    @Override
    public void onCalculateRouteFailure(int errorInfo) {
         Toast.makeText(this,"路径规划失败:"+errorInfo,Toast.LENGTH_SHORT).show();
        mTTSController.playText("路径规划失败,请检查网络");
    }

    /**
     * 步行或驾车导航时,出现偏航后需要重新计算路径的回调函数
     */
    @Override
    public void onReCalculateRouteForYaw() {
        mTTSController.playText("您已偏航");
    }

    /**
     * 驾车导航时，如果前方遇到拥堵时需要重新计算路径的回调
     */
    @Override
    public void onReCalculateRouteForTrafficJam() {
        mTTSController.playText("前方拥堵");
    }

    /**
     * 驾车路径导航达到某个途经点的回调函数
     * @param wayID 到达途径点的编号,标号从1开始,依次累加. 模拟导航下不工作.
     */
    @Override
    public void onArrivedWayPoint(int wayID) {
    }

    /**
     * 用户手机GPS设置是否开启的回调函数
     * @param enabled true代表开启 false表示未开启
     */
    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    /**
     * @param aMapNaviInfo
     * @deprecated 使用onNaviInfoUpdate(NaviInfo naviInfo)
     */
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) { }

    /**
     * 导航引导信息回调函数
     * @param naviInfo 导航信息类
     */
    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
    }

    /**
     * 道路设施信息更新回调
     * @param aMapNaviTrafficFacilityInfo
     * @deprecated
     */
    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) { }

    /**
     * @param trafficFacilityInfo
     * @deprecated
     */
    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) { }

    /**
     * 显示路口放大图回调
     * @param aMapNaviCross 路口放大图类，可以获得此路口放大图bitmap
     */
    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    /**
     * 关闭路口放大图回调函数
     */
    @Override
    public void hideCross() { }

    /**
     * 显示道路信息回调
     * @param aMapLaneInfos 道路信息数组，课获得各条道路分别是什么类型，可用于用户使用自己的素材完全自定义显示
     * @param laneBackgroundInfo  道路背景数据数组，可用于装载官方的DriveWayView并显示
     * @param laneRecommendedInfo 道路推荐数据数组，可用于装载官方的DriveWayView并显示
     */
    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] laneBackgroundInfo,
                             byte[] laneRecommendedInfo) {
    }

    /**
     * 关闭道路信息回调
     */
    @Override
    public void hideLaneInfo() {  }

    /**
     * 多线路算路成功回调函数
     * @param routeIds 路线id数组
     */
    @Override
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {
    }

    /**
     * 通知当前是否显示平行路切换
     * @param parallelRoadType 0表示隐藏 1表示显示主路 2表示显示辅路
     */
    @Override
    public void notifyParallelRoad(int parallelRoadType) {   }

    /**
     * 道路设施信息更新回调
     * @param aMapNaviTrafficFacilityInfos
     */
    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
    }

    /**
     * 巡航模式（无路线规划）下，统计信息更新回调函数
     * @param aimLessModeStat
     */
    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {  }

    /**
     * 巡航模式（无路线规划）下，统计信息更新回调函数
     * @param aimLessModeCongestionInfo
     */
    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {  }

    @Override
    protected void onResume() {
        aMapNaviView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        aMapNaviView.onPause();
        mTTSController.stopSpeaking();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        aMapNaviView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        aMapNaviView.onDestroy();
        aMapNavi.stopNavi();
        aMapNavi.destroy();
        mTTSController.destroy();
        super.onDestroy();
    }
}
