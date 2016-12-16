package com.shutup.todo.controller.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shutup.todo.R;
import com.shutup.todo.model.normal_use.MenuItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shutup on 2016/12/14.
 */

public class DrawerMenuAdapter extends BaseAdapter {
    private Context mContext;
    private List<MenuItem> mMenuItems;
    private LayoutInflater mLayoutInflater;

    public DrawerMenuAdapter(Context context, List<MenuItem> menuItems) {
        mContext = context;
        mMenuItems = menuItems;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mMenuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.menu_item_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MenuItem menuItem = mMenuItems.get(i);
        viewHolder.mMenuTitle.setText(menuItem.getMenuName());
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.menu_title)
        TextView mMenuTitle;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
