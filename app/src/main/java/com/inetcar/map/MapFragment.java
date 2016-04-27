package com.inetcar.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.inetcar.startup.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 加油Tab页面，用于处理地图、导航
 */
public class MapFragment extends Fragment implements LocationSource,
        AMapLocationListener, PoiSearch.OnPoiSearchListener,
        AMap.OnMarkerClickListener,View.OnClickListener,AMap.OnMapClickListener,
        GeocodeSearch.OnGeocodeSearchListener{

    private View view_map;  //加油Tab页面

    private ImageView im_VoiceSearch;  //语音搜索按钮
    private ImageView im_Search;       //地点搜索按钮
    private TextView tv_search;        //地点搜索文本框
    private ImageView im_Station;  //加油站按钮

    private LinearLayout linear_station;
    private TextView tv_StationTitle;   //加油站名称
    private TextView tv_StationType;    //加油站类型及距离
    private TextView tv_StationAddress; //加油站地址
    private LinearLayout linear_StationNavi;  //加油站导航
    private LinearLayout linear_StationDetail;//加油站详情
    private LinearLayout linear_StationOther; //其它加油站

    private LinearLayout linear_clicklocation;
    private TextView tv_ClickLocationTitle;         //地点名称及距离
    private LinearLayout linear_ClickLocationNavi;  //地点导航
    private LinearLayout linear_ClickLocationSearch;//搜索周边

    private MapView mapView; //地图控件
    private AMap aMap;      //地图对象
    private UiSettings mUiSettings; //地图控件UI设置类
    private AMapLocationClient mLocationClient;  //定位信息类
    private AMapLocationClientOption mLocationOption; //定位参数设置
    private OnLocationChangedListener mLocationListener;
    private AMapLocation lastLocation; //上一次定位地址信息

    private PoiSearch poiSearch; //poi搜索
    private PoiResult poiResult; //poi搜索结果
    private PoiSearch.Query poiQuery; //poi搜索条件
    private int currentPage = 0; //搜索结果当前页面，从0开始
    private ArrayList<PoiItem> poiItems; //poi数据

    private LatLonPoint latLonPoint;
    private myPoiOverlay mPoiOverlay;
    private Marker lastMarker;

    private LatLng lastLatLng;
    private Marker mClickedMarker;

    private MyLocationCallback mlocationCallback;
    private Context mContext;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private GeocodeSearch mGeocodeSearch;
    private RegeocodeResult mRegeocodeResult;
    /**
     * 定位信息回调接口，用于和主FragmentActivity交互
     */
    public interface MyLocationCallback{
        /**
         * @param city      城市
         * @param district  城区
         */
        void locationCallback(String city, String district);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mlocationCallback = (MyLocationCallback)context;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view_map = inflater.inflate(R.layout.fragment_tab_map,container,false);
        mapView = (MapView) view_map.findViewById(R.id.mapview_tab_map);
        mapView.onCreate(savedInstanceState); //此方法必须重写
        init();
        return view_map;
    }

     /**
     * 加载控件及初始化AMap对象
     */
    private void init(){

        im_VoiceSearch = (ImageView) view_map.findViewById(R.id.im_tab_map_voice);
        im_VoiceSearch.setOnClickListener(this);

        im_Search = (ImageView) view_map.findViewById(R.id.im_tab_map_search);
        im_Search.setOnClickListener(this);
        tv_search = (TextView) view_map.findViewById(R.id.tv_tab_map_search);
        tv_search.setOnClickListener(this);

        im_Station = (ImageView) view_map.findViewById(R.id.im_tab_map_station);
        im_Station.setOnClickListener(this);

        linear_station = (LinearLayout) view_map.findViewById(R.id.linear_station_tab_map);
        linear_station.setVisibility(View.GONE);
        tv_StationTitle = (TextView) view_map.findViewById(R.id.tv_station_title);
        tv_StationType = (TextView) view_map.findViewById(R.id.tv_station_type_distance);
        tv_StationAddress = (TextView) view_map.findViewById(R.id.tv_station_address);

        linear_StationNavi = (LinearLayout) view_map.findViewById(R.id.linear_station_navigation);
        linear_StationNavi.setOnClickListener(this);
        linear_StationDetail = (LinearLayout) view_map.findViewById(R.id.linear_station_detail);
        linear_StationDetail.setOnClickListener(this);
        linear_StationOther = (LinearLayout) view_map.findViewById(R.id.linear_station_other);
        linear_StationOther.setOnClickListener(this);

        linear_clicklocation = (LinearLayout) view_map.findViewById(R.id.linear_clicklocation_tab_map);
        tv_ClickLocationTitle = (TextView) view_map.findViewById(R.id.tv_clicklocation_title);
        linear_ClickLocationNavi = (LinearLayout) view_map.findViewById(
                R.id.linear_clicklocation_navigation);
        linear_ClickLocationNavi.setOnClickListener(this);
        linear_ClickLocationSearch = (LinearLayout) view_map.findViewById(
                R.id.linear_clicklocation_search);
        linear_ClickLocationSearch.setOnClickListener(this);

        if(aMap==null){
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
        //设置是否显示缩放按钮
        mUiSettings.setZoomControlsEnabled(true);
        //设置比例尺是否显示
        mUiSettings.setScaleControlsEnabled(false);
        //设置定位按钮是否显示
        mUiSettings.setMyLocationButtonEnabled(false);
        mGeocodeSearch = new GeocodeSearch(mContext);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 设置amap属性
     */
    public void setUpMap(){

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置定位的图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.location_marker));
        //设置圆形区域的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(50,0,0,0));
        aMap.setMyLocationStyle(myLocationStyle);

        //设置定位监听
        aMap.setLocationSource(this);
        //设置true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位
        aMap.setMyLocationEnabled(true);
        //设置定位模式为跟随模式，另外还有自由定位、旋转模式
       // aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
        //设置是否显示实时交通
        aMap.setTrafficEnabled(false);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 定位成功后回调函数
     * @param aMapLocation 定位信息
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(mLocationListener!=null && aMapLocation!=null){
            if(aMapLocation!=null &&
                    aMapLocation.getErrorCode()==AMapLocation.LOCATION_SUCCESS){
                mLocationListener.onLocationChanged(aMapLocation); //显示定位图标
                //只有不是GPS返回地址信息的情况下，才能获取城市及城区信息
                if(!aMapLocation.getProvider().equalsIgnoreCase( LocationManager.GPS_PROVIDER)){
                    mlocationCallback.locationCallback(aMapLocation.getCity(),
                            aMapLocation.getDistrict());
                    aroundSearch(aMapLocation);
                    lastLocation = aMapLocation;
                }
            }else{
                Toast.makeText(getActivity(),"定位失败："+
                        aMapLocation.getErrorInfo(),Toast.LENGTH_LONG).show();
                mlocationCallback.locationCallback(null,null);
            }
        }
    }

    /**
     * 激活定位
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationListener = onLocationChangedListener;
        if(mLocationClient==null){
            mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());

            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度定位，Batter_saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//
            //设置定位间隔10s，默认2秒
            mLocationOption.setInterval(6000);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.setLocationListener(this);
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mLocationListener = null;
        if(mLocationClient!=null){
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }


    /**
     * 搜索附近的加油站及充电站
     * @param newLocation 当前地址信息，不为null
     */
    public void aroundSearch(AMapLocation newLocation){
        if(lastLocation==null || AMapUtils.calculateLineDistance(
                new LatLng(newLocation.getLatitude(),newLocation.getLongitude()),
                new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()))>=100){
            poiQuery = new PoiSearch.Query("加油站","汽车服务",newLocation.getCity());

           // Log.d("mytime",poiQuery.getCity()+" "+poiQuery.getCategory());
            //设置查第一页
            poiQuery.setPageNum(currentPage);
            //每页最多10条
            poiQuery.setPageSize(8);
            poiQuery.setCityLimit(true);

            poiSearch = new PoiSearch(mContext,poiQuery);
            poiSearch.setOnPoiSearchListener(this);
           // Log.d("mytime", "city:"+newLocation.getCity()+" "+newLocation.getDistrict()+" "+
            //        newLocation.getStreet());
            latLonPoint = new LatLonPoint(newLocation.getLatitude(),newLocation.getLongitude());
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint,6000,true));
            poiSearch.searchPOIAsyn();
        }
    }

    /**
     * 返回POI搜索异步处理的结果
     * @param result POI搜索结果
     * @param errorCode 结果成功或失败的响应吗，1000为成功，其他为失败
     */
    @Override
    public void onPoiSearched(PoiResult result, int errorCode) {
        if(errorCode==1000){
            if(result!=null && result.getQuery()!=null){
                //判断是不是同一条搜索结果
                if(result.getQuery().equals(poiQuery)){
                    poiResult = result;
                    poiItems = poiResult.getPois();
                    if(poiItems!=null && poiItems.size()>0){

                        //清除之前的搜索结果的Marker
                        if(mPoiOverlay!=null){
                            mPoiOverlay.removeFromMap();
                        }
                        aMap.clear();
                        mPoiOverlay = new myPoiOverlay(aMap,poiItems);
                        mPoiOverlay.addToMap();
                        mPoiOverlay.zoomToSpan();
                        addCenterMarker();


                    }else{
                        Toast.makeText(mContext,"poiItems为空",Toast.LENGTH_LONG).show();
                        Log.d("mytime", "poiItems为空");
                    }
                }else{
                    Log.d("mytime", "query不同");
                }
            }else{
                Toast.makeText(mContext,"result为空",Toast.LENGTH_LONG).show();
                Log.d("mytime", "errorCode:"+errorCode);
            }
        }else{
            Toast.makeText(mContext,"errorCode:"+errorCode,Toast.LENGTH_LONG).show();
            Log.d("mytime", "errorCode:"+errorCode);
        }
    }

    /**
     * 在地图上添加用户当前位置Marker
     */
    public void addCenterMarker(){
        if(lastLocation!=null){
            aMap.addMarker(new MarkerOptions().
                    position(new LatLng(lastLocation.getLatitude(),
                                        lastLocation.getLongitude())).
                    icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker)));
            /*
            aMap.addCircle(new CircleOptions()
                    .center(new LatLng(lastLocation.getLatitude(),
                                       lastLocation.getLongitude())).radius(100)
                    .fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(0));
             */
        }
    }

    /**
     * POI ID搜索的结果回调
     * @param poiItem POI信息
     * @param errorCode 结果成功或失败的响应吗，1000为成功，其他为失败
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int errorCode) {
    }

    /**
     * marker点击事件
     * @param marker
     * @return true表示接口已相应事件，否则返回false
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        linear_clicklocation.setVisibility(View.GONE);
        if(mClickedMarker!=null && mClickedMarker.isVisible()){
            mClickedMarker.remove();
        }
        if(marker.getObject()!=null){

            if(lastMarker!=null){
                lastMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.station_marker_normal));
            }
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.station_marker_selected));
            PoiItem currentPoiItem = (PoiItem) marker.getObject();
            float distance = AMapUtils.calculateLineDistance(
                    new LatLng(currentPoiItem.getLatLonPoint().getLatitude(),
                               currentPoiItem.getLatLonPoint().getLongitude()),
                    new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()))/1000;


            linear_station.setVisibility(View.VISIBLE);

            tv_StationTitle.setText(currentPoiItem.getTitle());
            String type[] = currentPoiItem.getTypeDes().split(";");
            tv_StationType.setText(type[type.length-1]+", 距您:"+
                    decimalFormat.format(distance)+"公里");
            tv_StationAddress.setText(currentPoiItem.getSnippet());
        }
        if(lastMarker==null || !lastMarker.equals(marker))
             lastMarker = marker;
        return true;
    }

    /**
     * 地图点击回调函数
     * @param latLng 经纬度坐标值类
     */
    @Override
    public void onMapClick(LatLng latLng) {
        //隐藏点击Marker弹出的加油站信息框

        linear_station.setVisibility(View.GONE);
        linear_clicklocation.setVisibility(View.GONE);

        if(lastMarker!=null){
            lastMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.station_marker_normal));
        }
        if(mClickedMarker!=null){
            mClickedMarker.remove();
        }
        lastLatLng = latLng;
        mClickedMarker = aMap.addMarker(new MarkerOptions().position(latLng).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_click)));
        //第一个参数为Latlng,第二个参数表示范围多少米,第三个参数表示坐标系是AMAP还是GPS
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude,
                latLng.longitude),200,GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(query);

    }

    /**
     * 逆地理编码回调,根据给定经纬度和范围返回逆地理编码的结果
     * @param regeocodeResult 逆地理编码返回结果
     * @param resultCode 返回结果成功或失败的响应码，1000为成功，其它为失败
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resultCode) {
        if(resultCode==1000){
            if(regeocodeResult!=null && regeocodeResult.getRegeocodeAddress()!=null &&
                    regeocodeResult.getRegeocodeAddress().getFormatAddress()!=null){

                mRegeocodeResult = regeocodeResult;

                linear_station.setVisibility(View.GONE);
                linear_clicklocation.setVisibility(View.VISIBLE);

                float distance = AMapUtils.calculateLineDistance(lastLatLng,
                        new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()))/1000;
                tv_ClickLocationTitle.setText(regeocodeResult.getRegeocodeAddress().
                        getFormatAddress()+", 距您"+decimalFormat.format(distance)+"公里");
            }else{
                Toast.makeText(mContext,"无该地点信息",Toast.LENGTH_LONG).show();
                linear_station.setVisibility(View.GONE);
                linear_clicklocation.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 地理编码回调，根据给定的地理名称和查询城市返回地理编码的结果
     * @param geocodeResult 地理编码返回结果
     * @param resultCode 返回结果成功或失败的响应码，1000为成功，其它为失败
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int resultCode) {
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.im_tab_map_search:
            case R.id.tv_tab_map_search://地点搜索按钮
            {
                break;
            }
            case R.id.im_tab_map_voice: //语音搜索按钮
            {
                break;
            }
            case R.id.im_tab_map_station: //加油站按钮
            {
                break;
            }
            case R.id.linear_station_navigation: //点击导航按钮
            {
                break;
            }
            case R.id.linear_station_detail: //点击详情按钮
            {
                break;
            }
            case R.id.linear_station_other: //点击其它加油站按钮
            {
                break;
            }
            case R.id.linear_clicklocation_navigation: //导航到点击地点
            {
                break;
            }case R.id.linear_clicklocation_search: //搜索点击地点周边
            {
                break;
            }
            default:
                break;
        }
    }

    public void searchStation(){

        if(lastLocation!=null){
            poiQuery = new PoiSearch.Query("加油站","汽车服务",lastLocation.getCity());
            poiQuery.setPageNum(currentPage);
            //每页最多10条
            poiQuery.setPageSize(8);
            poiQuery.setCityLimit(true);

            poiSearch = new PoiSearch(mContext,poiQuery);
            poiSearch.setOnPoiSearchListener(this);

            latLonPoint = new LatLonPoint(lastLocation.getLatitude(),lastLocation.getLongitude());
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint,6000,true));
            poiSearch.searchPOIAsyn();
        }
    }

    /**
     * 用Marker显示poi结果
     */
    class myPoiOverlay{
        private AMap mAMap;
        private ArrayList<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarkers = new ArrayList<Marker>();

        public myPoiOverlay(AMap mAMap, ArrayList<PoiItem> mPois) {
            this.mAMap = mAMap;
            this.mPois = mPois;
        }

        /**
         * 将Marker添加到地图上
         */
        public void addToMap(){
            for(int i=0; i<mPois.size(); i++){
                Marker marker = mAMap.addMarker(getMarkerOptions(i));
                marker.setObject(mPois.get(i));
                mPoiMarkers.add(marker);
            }
        }

        /**
         * 去掉poioverlay上所有Maker去掉
         */
        public void removeFromMap(){
            for(Marker marker:mPoiMarkers){
                marker.remove();
            }
            mPoiMarkers.clear();
            mPois.clear();
        }

        /**
         * 移动镜头到当前视角
         */
        public void zoomToSpan(){
            if(mPois!=null && mPois.size()>0){
                if(mAMap==null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,50));
            }
        }

        private LatLngBounds getLatLngBounds(){
            LatLngBounds.Builder b = LatLngBounds.builder();
            for(int i=0; i<mPois.size(); i++){
                LatLonPoint latLonPoint = mPois.get(i).getLatLonPoint();
                b.include(new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index){
            LatLonPoint latLonPoint = mPois.get(index).getLatLonPoint();
            return new MarkerOptions().
                    position(new LatLng(latLonPoint.getLatitude(),
                                       latLonPoint.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.station_marker_normal));
        }
    }
}
