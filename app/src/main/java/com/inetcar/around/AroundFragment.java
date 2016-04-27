package com.inetcar.around;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.inetcar.startup.R;
import com.inetcar.tools.WrapContentViewPager;

import java.util.ArrayList;

public class AroundFragment extends Fragment implements ViewPager.OnPageChangeListener,
        AdapterView.OnItemClickListener,View.OnClickListener{

    private View view_around;
    private Activity activity;  //宿主Activity

    private WrapContentViewPager vp_around;  //滑动页面之汽车与同城服务
    private ArrayList<View> aroundView_list;
    private AroundViewPagerAdapter vpAdapter;
    private int currentPagerIndex;
    private ImageView dot[];
    private LinearLayout linear_dot;

    private View view_car;        //汽车服务页面
//    private GridView gv_car;      //汽车服务列表
//    private ArrayList<GridViewContainer> carservice_list; //汽车服务项目
//    private AroundGridViewAdapter carAdapter; //汽车服务grid适配器


    private View view_city;        //同城服务页面
//    private GridView gv_city;     //同城服务列表
//    private ArrayList<GridViewContainer> cityservice_list; //同城服务项目
//    private AroundGridViewAdapter cityAdapter; //同城服务grid适配器

    private ListView lv_recommend;  //热门推荐 列表


    @Override
    public void onCreate(Bundle savedInstanceState) {

        activity = getActivity();

        /*carservice_list = new ArrayList<GridViewContainer>();
        carservice_list.add(new GridViewContainer("预约加油",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("交通违章",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("路况查询",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("天气查询",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("停车场",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("汽车美容",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("代驾",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("车险",R.mipmap.gv_tiku));
        carservice_list.add(new GridViewContainer("加油卡充值",R.mipmap.gv_tiku));
        carAdapter = new AroundGridViewAdapter(activity,carservice_list);

        cityservice_list = new ArrayList<GridViewContainer>();
        cityservice_list.add(new GridViewContainer("电影",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("酒店",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("美食",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("娱乐",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("车站",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("银行",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("医院",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("景点",R.mipmap.gv_tiku));
        cityservice_list.add(new GridViewContainer("更多",R.mipmap.gv_tiku));

        cityAdapter = new AroundGridViewAdapter(activity,cityservice_list);*/

        currentPagerIndex = 0;

        super.onCreate(savedInstanceState);
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

        view_around = inflater.inflate(R.layout.fragment_tab_around,container,false);

        vp_around = (WrapContentViewPager) view_around.findViewById(R.id.viewpager_tab_around);

        linear_dot = (LinearLayout) view_around.findViewById(R.id.linear_around_dot);

        dot = new ImageView[2];
        for(int i=0; i<dot.length; i++){
            dot[i] = (ImageView) linear_dot.getChildAt(i);
            dot[i].setEnabled(false);
        }
        dot[currentPagerIndex].setEnabled(true);

        aroundView_list = new ArrayList<View>(2);

        view_car = inflater.inflate(R.layout.tab_around_vp_car,null);
        view_city = inflater.inflate(R.layout.tab_around_vp_city,null);

        aroundView_list.add(view_car);
        aroundView_list.add(view_city);


       // gv_car = (GridView) view_car.findViewById(R.id.gridview_tab_around_car);
       // gv_city = (GridView) view_city.findViewById(R.id.gridview_tab_around_city);

       // gv_car.setAdapter(carAdapter);
        //gv_city.setAdapter(cityAdapter);

        //gv_car.setOnItemClickListener(this);
       // gv_city.setOnItemClickListener(this);


        vpAdapter = new AroundViewPagerAdapter(aroundView_list,activity);

        vp_around.setAdapter(vpAdapter);

        vp_around.setOnPageChangeListener(this);

        lv_recommend = (ListView) view_around.findViewById(R.id.listview_around_recommend);

        lv_recommend.setOnItemClickListener(this);

        return view_around;
    }


    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {

        if(position<0 || position >= aroundView_list.size() || currentPagerIndex==position){
            return;
        }

        dot[currentPagerIndex].setEnabled(false);
        currentPagerIndex = position;
        dot[currentPagerIndex].setEnabled(true);
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

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

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
