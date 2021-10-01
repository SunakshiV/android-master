package com.procoin.module.myhome;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.procoin.util.PageJumpUtil;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 废弃
 */
public class MyPayPasswordActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.llModifyPayPass)
    LinearLayout llModifyPayPass;
    @BindView(R.id.llForgetPayPass)
    LinearLayout llForgetPayPass;

    @Override
    protected int setLayoutId() {
        return R.layout.pay_password;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.payPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        llModifyPayPass.setOnClickListener(this);
        llForgetPayPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llModifyPayPass:
                break;
            case R.id.llForgetPayPass:
                PageJumpUtil.pageJump(this,SettingPayPasswordActivity.class);

                break;
        }
    }
}
