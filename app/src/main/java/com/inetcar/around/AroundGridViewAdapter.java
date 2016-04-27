package com.inetcar.around;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inetcar.startup.R;

import java.util.ArrayList;

/**
 * 周边Tab的GirdView适配器
 */
public class AroundGridViewAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<GridViewContainer> content_list;

    public AroundGridViewAdapter(Activity activity, ArrayList<GridViewContainer> content_list) {
        this.activity = activity;
        this.content_list = content_list;
    }

    @Override
    public int getCount() {
        if(content_list==null)
            return 0;
        return content_list.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
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

        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.gv_tab_around_style,null);
            viewHolder.im_icon = (ImageView) convertView.findViewById(R.id.im_tab_around_gv);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_tab_around_gv);
            viewHolder.im_icon.setImageResource(content_list.get(position).getIcon());
            viewHolder.tv_name.setText(content_list.get(position).getName());
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        ImageView im_icon;
        TextView  tv_name;
    }
}
