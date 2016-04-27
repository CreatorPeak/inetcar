package com.inetcar.around;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 周边Tab的viewpager
 */
public class AroundViewPagerAdapter extends PagerAdapter{

    private ArrayList<View> view_list;
    private Activity activity;

    public AroundViewPagerAdapter(ArrayList<View> view_list, Activity activity) {
        this.view_list = view_list;
        this.activity = activity;
    }


    @Override
    public int getCount() {

        if(view_list==null)
             return 0;
        return view_list.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position  The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(view_list.get(position),0);
        return view_list.get(position);
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position  The page position to be removed.
     * @param object    The same object that was returned by
     *                  {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(view_list.get(position));
    }
}
