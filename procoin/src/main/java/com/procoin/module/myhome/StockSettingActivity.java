package com.procoin.module.myhome;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.data.sharedpreferences.StockSharedPreferences;
import com.procoin.module.myhome.adapter.StockSpeedAdapter;
import com.procoin.widgets.AppListView;

public class StockSettingActivity extends TJRBaseToolBarSwipeBackActivity {

    private AppListView lvList;//列表
    private StockSpeedAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.home_stock_setting;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.stockSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.home_stock_setting);
        Onclick onclick = new Onclick();
        adapter = new StockSpeedAdapter(this);

        //刷新频率
        lvList = (AppListView) findViewById(R.id.lvList);
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select = adapter.groupSecond.get(position);
                adapter.notifyDataSetChanged();
                StockSharedPreferences.saveSelectSpeed(StockSettingActivity.this, adapter.select);
            }
        });
    }

    private class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.llkongxin:
//                    if (cbshixin.isChecked()) {
//                        cbshixin.setChecked(false);
//                    }
//                    cbkongxin.setChecked(true);
//                    StockSharedPreferences.savePaintStyleFill(StockSettingActivity.this, false);
//                    break;
//                case R.id.llshixin:
//                    if (cbkongxin.isChecked()) {
//                        cbkongxin.setChecked(false);
//                    }
//                    cbshixin.setChecked(true);
//                    StockSharedPreferences.savePaintStyleFill(StockSettingActivity.this, true);
//                    break;
                default:
                    break;
            }
        }

    }

}
