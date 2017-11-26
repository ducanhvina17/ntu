package com.mdp17.group12.labmoverscontroller.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdp17.group12.labmoverscontroller.R;

import java.util.ArrayList;

/**
 * Created by mrawesome on 4/2/17.
 */

public class CustomListAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView iconView;
        TextView titleView;
    }

    private ArrayList<CustomMenuItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<CustomMenuItem> listData) {
        this.listData = listData;
        this.layoutInflater = layoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.custom_navigation_drawer, null);
            viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) view.findViewById(R.id.menu_item_icon);
            viewHolder.titleView = (TextView) view.findViewById(R.id.menu_item_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.iconView.setImageResource(listData.get(i).getIcon());
        viewHolder.titleView.setText(listData.get(i).getTitle());
        return view;
    }
}
