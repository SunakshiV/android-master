package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.data.sharedpreferences.NormalShareData;
import com.procoin.http.base.Group;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.adapter.HomePositionAdapter;
import com.procoin.module.home.entity.Position;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 持仓
 * Created by zhengmj on 19-3-8.
 */

public class HomeAccountPositionFragment extends UserBaseFragment {

    @BindView(R.id.llHide)
    LinearLayout llHide;
    @BindView(R.id.cbSign)
    CheckBox cbSign;
    @BindView(R.id.ivQuestionMark)
    ImageView ivQuestionMark;

    @BindView(R.id.rvPositionList)
    RecyclerView rvPositionList;
    @BindView(R.id.tvNoDataPosition)
    TextView tvNoDataPosition;

    private String minMarketBalance;
    private Group<Position> groupPostion;

    private HomePositionAdapter homePositionAdapter;


    public static HomeAccountPositionFragment newInstance() {
        HomeAccountPositionFragment fragment = new HomeAccountPositionFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
        if (getUser() != null && cbSign != null)
            cbSign.setChecked(NormalShareData.getIsHideSmallFlag(getActivity(), getUser().getUserId()));
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
        }
    }

    public void setData(Group<Position> groupPostion,String minMarketBalance){
        if(rvPositionList==null)return;
        this.minMarketBalance=minMarketBalance;
        this.groupPostion=groupPostion;
        if (groupPostion != null && groupPostion.size() > 0) {
            rvPositionList.setVisibility(View.VISIBLE);
            tvNoDataPosition.setVisibility(View.GONE);
            homePositionAdapter.setGroupIsHide(groupPostion, cbSign.isChecked());
        } else {
            rvPositionList.setVisibility(View.GONE);
            tvNoDataPosition.setVisibility(View.VISIBLE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_account_position, container, false);
        ButterKnife.bind(this, view);

        homePositionAdapter = new HomePositionAdapter(getActivity());
        rvPositionList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);
        rvPositionList.addItemDecoration(simpleRecycleDivider);
        rvPositionList.setAdapter(homePositionAdapter);

        cbSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getUser() != null) {
                    NormalShareData.saveIsHideSmall(getActivity(), getUser().getUserId(), isChecked);
                }
                setData(groupPostion,minMarketBalance);
//                if (homePositionAdapter != null)
//                    homePositionAdapter.setGroupIsHide(groupPostion, isChecked);
            }
        });
        ivQuestionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!minMarketBalance.equals("0")) showSubmitTipsDialog(minMarketBalance);
            }
        });
        if (getUser() != null)
            cbSign.setChecked(NormalShareData.getIsHideSmallFlag(getActivity(), getUser().getUserId()));
        return view;
    }

    TjrBaseDialog questionMarkDialog;

    private void showSubmitTipsDialog(String tips) {
        questionMarkDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        questionMarkDialog.setTitleVisibility(View.GONE);
        questionMarkDialog.setBtnColseVisibility(View.GONE);
        questionMarkDialog.setMessage("市值小于" + tips + "USDT的币种");
        questionMarkDialog.setBtnOkText("知道了");
        questionMarkDialog.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
