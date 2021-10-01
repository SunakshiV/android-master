package com.procoin.module.copy.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

import com.procoin.common.constant.CommonConst;
import com.procoin.R;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * 废弃(ImmersionBar无效)
 */

public class ModifyCopySetDialogFragment extends BaseDialogFragment implements View.OnClickListener {


    @BindView(R.id.llTop)
    LinearLayout llTop;

    /**
     *
     *
     * @return
     */
    public static ModifyCopySetDialogFragment newInstance() {
        ModifyCopySetDialogFragment dialog = new ModifyCopySetDialogFragment();

        return dialog;
    }

    public void showDialog(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_copy_setting;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .titleBar(llTop,true)
                .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
                .flymeOSStatusBarFontColor(R.color.black)
                .navigationBarColor(R.color.white).init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
