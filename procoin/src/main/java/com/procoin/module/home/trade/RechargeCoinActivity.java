package com.procoin.module.home.trade;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.trade.adapter.KeyTypeAdapter;
import com.procoin.module.home.trade.dialog.TakeCoinSelectFragment;
import com.procoin.module.home.trade.entity.CoinResult;
import com.procoin.module.home.trade.entity.TakeCoin;
import com.procoin.module.home.trade.history.TakeCoinHistoryActivity;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.SimpleSpaceItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 充币
 */
public class RechargeCoinActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.llSelectCoin)
    LinearLayout llSelectCoin;
    @BindView(R.id.llKeyType)
    LinearLayout llKeyType;
    @BindView(R.id.tvSaveQr)
    TextView tvSaveQr;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvCopyAddress)
    TextView tvCopyAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.rvKeyType)
    RecyclerView rvKeyType;

    private Group<TakeCoin> coinGroup;

    private Call<ResponseBody> getCoinListCall;
    private Call<ResponseBody> getCoinInfo;


    private String currSymbol = "";
    private CoinResult currCoin;
    private String keyType = "";

    private Bitmap mBitmap;

    private KeyTypeAdapter keyTypeAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.recharge_coin;
    }


    @Override
    protected String getActivityTitle() {
        return "充币";
    }

    public static void pageJump(Context context, String defaultSymbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, defaultSymbol);
        PageJumpUtil.pageJump(context, RechargeCoinActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String defaultSymbol = "";
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                defaultSymbol = bundle.getString(CommonConst.SYMBOL, "");
            }
        }
        llSelectCoin.setOnClickListener(this);
        tvSaveQr.setOnClickListener(this);
        tvCopyAddress.setOnClickListener(this);
        keyTypeAdapter = new KeyTypeAdapter(this);
        rvKeyType.setLayoutManager(new GridLayoutManager(this, 4));
        rvKeyType.addItemDecoration(new SimpleSpaceItemDecoration(this, 0, 10, 5, 5));
        rvKeyType.setAdapter(keyTypeAdapter);
        keyTypeAdapter.setOnItemclickListen(new KeyTypeAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String keyType) {
                RechargeCoinActivity.this.keyType = keyType;
                startGetCoinInfo();//改变键类型要刷新
            }
        });
        tvMenu.setText("记录");
        tvMenu.setOnClickListener(this);
        setSymbol(defaultSymbol);
        startGetCoinList();
    }


    private void setSymbol(String symbol) {
        if (TextUtils.isEmpty(symbol)) return;
        currSymbol = symbol;
        currCoin = null;
        keyType = "";
        keyTypeAdapter.clearAllItem();
        mBitmap = null;
        ivQr.setImageBitmap(null);
        tvSymbol.setText(symbol);
        startGetCoinInfo();
    }

    private void startGetCoinInfo() {
        CommonUtil.cancelCall(getCoinInfo);
        getCoinInfo = VHttpServiceManager.getInstance().getVService().getWithdrawCoinInfo(currSymbol, keyType, 1);
        getCoinInfo.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    currCoin = resultData.getObject("coinResult", CoinResult.class);

                    setData();
                }
            }
        });
    }


    private void setData() {
        if (currCoin == null) return;
        Log.d("startGetCoinInfo", "currCoin.chainTypes==" + currCoin.chainTypes);
        if (currCoin.chainTypes != null && currCoin.chainTypes.length > 0) {
            llKeyType.setVisibility(View.VISIBLE);
//            currCoin.chainTypes=new String[]{"aaa","bbb","ccc","ddd","eee","fff"};
            keyTypeAdapter.setData(currCoin.chainTypes);
            if (TextUtils.isEmpty(keyType)) {
                keyType = currCoin.chainTypes[0];
                keyTypeAdapter.setSelected(keyType);
            }

        } else {
            llKeyType.setVisibility(View.GONE);
        }
//        tvTips.setText(CommonUtil.fromHtml(currCoin.inOutTip));
        tvTips.setText(currCoin.inOutTip);
        if (!TextUtils.isEmpty(currCoin.address)) {
            tvAddress.setText(currCoin.address);
            mBitmap = CodeUtils.createImage(currCoin.address, 400, 400, null);
            ivQr.setImageBitmap(mBitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSelectCoin:
                if (coinGroup == null) {
                    startGetCoinList();
                } else {
                    showTakeCoinSelectFragment();
                }
                break;
            case R.id.tvSaveQr:
                if (currCoin != null && !TextUtils.isEmpty(currCoin.address)) {
                    saveToSdcard(mBitmap);
                }
                break;
            case R.id.tvCopyAddress:
                if (currCoin != null && !TextUtils.isEmpty(currCoin.address)) {
                    com.procoin.util.CommonUtil.copyText(RechargeCoinActivity.this, currCoin.address);
                }
                break;

            case R.id.tvMenu:
                PageJumpUtil.pageJump(RechargeCoinActivity.this, TakeCoinHistoryActivity.class);
                break;
        }
    }


    private void startGetCoinList() {
        CommonUtil.cancelCall(getCoinListCall);
        getCoinListCall = VHttpServiceManager.getInstance().getVService().coinList(1);
        getCoinListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    coinGroup = resultData.getGroup("coinList", new TypeToken<Group<TakeCoin>>() {
                    }.getType());
                }
            }
        });
    }

    private void showTakeCoinSelectFragment() {
        TakeCoinSelectFragment manageCircleFragment = TakeCoinSelectFragment.newInstance(coinGroup);
        manageCircleFragment.setOnSelectCoinListen(new TakeCoinSelectFragment.OnSelectCoinListen() {
            @Override
            public void onclick(TakeCoin takeCoin) {
                setSymbol(takeCoin.symbol);
//                currCoin = takeCoin;
//                if (takeCoin.symbol.equals("USDT")) {
//                    llKeyType.setVisibility(View.VISIBLE);
//                } else {
//                    llKeyType.setVisibility(View.GONE);
//                }
//                tvSymbol.setText(takeCoin.symbol);
//
////                Bitmap mBitmap = CodeUtils.createImage(shareUrl, 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
//
//                Bitmap mBitmap = CodeUtils.createImage("asdasdfasdfasdf", 400, 400, null);
//                ivQr.setImageBitmap(mBitmap);

//                tvSymbol2.setText(takeCoin.symbol);
//                tvSymbol3.setText(takeCoin.symbol);
//                tvEnableAmount.setText("可提币数量：" + takeCoin.amount + takeCoin.symbol);
//                etFee.setText(takeCoin.withdrawFee);
//                tvMinWithdrawAmt.setText("最小提币数量为：" + takeCoin.minWithdrawAmt + takeCoin.symbol);
//                setLastAmountText(etAmount.getText().toString());
            }
        });
        manageCircleFragment.show(getSupportFragmentManager(), "");
    }


    public boolean saveToSdcard(Bitmap bitmap) {
        File file = null;
        try {
            if (bitmap == null) {
                com.procoin.social.util.CommonUtil.showToast(this, "没有获取到图片", Gravity.BOTTOM);
                return false;
            }

            String fileName = com.procoin.social.util.VeDate.getyyyyMMddHHmmss(com.procoin.social.util.VeDate.getNow())
                    + ".png";
            file = ((MainApplication) getApplicationContext())
                    .getmDCIMRemoteResourceManager().getFile(fileName);
            ((MainApplication) getApplicationContext())
                    .getmDCIMRemoteResourceManager().writeFile(file, bitmap,
                    false);
            if (((MainApplication) getApplicationContext()).isSDCard()) {
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                // 最后通知图库更新
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("saveToSdcard", "e==" + e);
            com.procoin.social.util.CommonUtil.showToast(this, "保存图片出错", Gravity.BOTTOM);
            return false;
        }
        CommonUtil.showmessage("二维码保存成功", this);
        return true;
    }
}
