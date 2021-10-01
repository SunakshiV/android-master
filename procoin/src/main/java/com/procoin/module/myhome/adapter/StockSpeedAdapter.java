package com.procoin.module.myhome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.data.sharedpreferences.StockSharedPreferences;

import java.util.ArrayList;

public class StockSpeedAdapter extends BaseAdapter {

    private Context activity;
    private ArrayList<String> group = new ArrayList<String>();
    public ArrayList<Integer> groupSecond = new ArrayList<Integer>();
    public int select;//默认是3秒《现在选择的item

    public StockSpeedAdapter(Context activity) {
        this.activity = activity;
        group.add("1秒");
        group.add("2秒");
        group.add("3秒");
        group.add("4秒");
        group.add("5秒");

        groupSecond.add(1);
        groupSecond.add(2);
        groupSecond.add(3);
        groupSecond.add(4);
        groupSecond.add(5);
        select = StockSharedPreferences.getSelectSpeed(activity);
    }


    @Override
    public int getCount() {
        if (group != null) return group.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        if (group == null) return null;
        try {
            return position < getCount() ? group.get(position) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.home_stock_setting_speed_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String item = (String) getItem(position);
        if (item != null)
            holder.setValue(item, position);
        return convertView;
    }

    class ViewHolder {

        private TextView tvTag;
        private CheckBox cbshuaxin;


        public ViewHolder(View convertView) {
            tvTag = (TextView) convertView.findViewById(R.id.tvTag);
            cbshuaxin = (CheckBox) convertView.findViewById(R.id.cbshuaxin);
        }

        public void setValue(String item, int position) {
            tvTag.setText(item);
            if (select == groupSecond.get(position).intValue()) {
                cbshuaxin.setChecked(true);
            } else {
                cbshuaxin.setChecked(false);
            }
        }
    }

}
