package com.procoin.module.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.module.home.trade.dialog.BuyDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 申请Croyp
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class ApplyCropymeActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.tvBuy)
    TextView tvBuy;
    BuyDialogFragment buyDialogFragment;
    @Override
    protected int setLayoutId() {
        return R.layout.market;
    }

    @Override
    protected String getActivityTitle() {
        return "BTC";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.market);
        ButterKnife.bind(this);
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOLStarHomeDialogFragment();
            }
        });
    }


    private void showOLStarHomeDialogFragment() {
        buyDialogFragment = BuyDialogFragment.newInstance();
//        buyDialogFragment.setOnDialogCallbackListener(new OnDialogCallbackListener() {
//            @Override
//            public void onSwitchCallback(short trade_opp_type, short item_type) {
//                tradeDialog.dismiss();
//                showOlStarHomePickDialogFragment(trade_opp_type, item_type);
//                SysShareData.setSharedDate(OLStarHomeActivity.this, CommonConst.ISOPENSYSPICKUP, "0");
//            }
//
//            @Override
//            public void onDismiss() {
//                tradeDialog.dismiss();
//            }
//        });
        buyDialogFragment.showDialog(getSupportFragmentManager(), "");
    }


}
