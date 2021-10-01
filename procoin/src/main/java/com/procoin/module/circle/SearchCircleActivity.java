package com.procoin.module.circle;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.module.circle.entity.CircleInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 搜索圈子
 * Created by zhengmj on 16-1-14.
 */
public class SearchCircleActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.btnSearch)
    TextView btnSearch;
    @BindView(R.id.ivCircleLogo)
    ImageView ivCircleLogo;
    @BindView(R.id.tvCircleName)
    TextView tvCircleName;
    @BindView(R.id.tvBrief)
    TextView tvBrief;
    @BindView(R.id.llResult)
    LinearLayout llResult;
    @BindView(R.id.llSearchNoResult)
    LinearLayout llSearchNoResult;
    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private CircleInfo circleInfo;

    @Override
    protected int setLayoutId() {
        return R.layout.circle_search;
    }

    @Override
    protected String getActivityTitle() {
        return "搜索圈子";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        btnSearch.setOnClickListener(this);
        llResult.setOnClickListener(this);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (etSearch != null) etSearch.requestFocus();
        }
    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && etSearch != null)
            imm.hideSoftInputFromWindow(etSearch.getApplicationWindowToken(), 0);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                String circleNum = etSearch.getText().toString().trim();
                Log.d("NearbyCircleActivity", "circleNum==" + circleNum);
                if (!TextUtils.isEmpty(circleNum)) {
                    hideInput();
                    startGetCircleInfo(circleNum);
                }
                break;
            case R.id.llResult:
                if (circleInfo != null && !TextUtils.isEmpty(circleInfo.circleId)) {
                    CircleInfoActivity.pageJumpThis(SearchCircleActivity.this, circleInfo.circleId);
                }
                break;

        }
    }

    private Call<ResponseBody> searchCircleCall;

    private void startGetCircleInfo(String circleId) {
        CommonUtil.cancelCall(searchCircleCall);
        showProgressDialog();
        searchCircleCall = VHttpServiceManager.getInstance().getVService().searchCircle(circleId);
        searchCircleCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    circleInfo = resultData.getObject("circle", CircleInfo.class);
                    if (circleInfo != null && !TextUtils.isEmpty(circleInfo.circleId)) {
                        setCircleData();
                        llResult.setVisibility(View.VISIBLE);
                        llSearchNoResult.setVisibility(View.GONE);
                    } else {
                        llResult.setVisibility(View.GONE);
                        llSearchNoResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void setCircleData() {
        if (circleInfo == null) return;
        tjrImageLoaderUtil.displayImage(circleInfo.circleLogo, ivCircleLogo);
        tvCircleName.setText(circleInfo.circleName);
        tvBrief.setText(circleInfo.brief);
    }
}
