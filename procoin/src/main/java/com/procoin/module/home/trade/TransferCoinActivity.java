package com.procoin.module.home.trade;


import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.entity.Position;
import com.procoin.module.home.trade.entity.AccountType;
import com.procoin.module.home.trade.entity.OtcEntity;
import com.procoin.module.home.trade.history.TransferCoinHistoryActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;

/**
 * 划转
 */
public class TransferCoinActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvEnableAmount)
    TextView tvEnableAmount;
    @BindView(R.id.tvTransferCoin)
    TextView tvTransferCoin;
    @BindView(R.id.llFrom)
    LinearLayout llFrom;
    @BindView(R.id.llTo)
    LinearLayout llTo;
    @BindView(R.id.tvFrom)
    TextView tvFrom;
    @BindView(R.id.tvTo)
    TextView tvTo;

    private Call<ResponseBody> listAccountTypeCall;
    private Call<ResponseBody> outHoldAmountCall;
    private Call<ResponseBody> transferCall;
    private Group<AccountType> group;
    private AccountType accountTypeFrom;
    private AccountType accountTypeTo;


    private String holdAmount = "";
    private int amountDecimals = 6;

//    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.transfer_coin;
    }


    @Override
    protected String getActivityTitle() {
        return "划转";
    }

    public static void pageJump(Context context, String defaultSymbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, defaultSymbol);
        PageJumpUtil.pageJump(context, TransferCoinActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        llFrom.setOnClickListener(this);
        llTo.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        tvMenu.setOnClickListener(this);
        tvTransferCoin.setOnClickListener(this);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > amountDecimals) {//
                        s.delete(posDot + (amountDecimals + 1), posDot + (amountDecimals + 2));
                    }
                }
            }
        });

        startListAccountTypeCall();
    }


    public void startListAccountTypeCall() {
        CommonUtil.cancelCall(listAccountTypeCall);
        listAccountTypeCall = VHttpServiceManager.getInstance().getVService().listAccountType();
        listAccountTypeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    group = resultData.getGroup("accountTypeList", new TypeToken<Group<AccountType>>() {
                    }.getType());
                    if (group != null) {
                        if (group.size() > 0) {
                            if (accountTypeFrom == null) {
                                accountTypeFrom = group.get(0);
                                tvFrom.setText(accountTypeFrom.accountName);
                            }
                            startOutHoldAmountCall(accountTypeFrom.accountType);
                        }
                        if (group.size() > 1) {
                            if(accountTypeTo==null){
                                accountTypeTo = group.get(1);
                                tvTo.setText(accountTypeTo.accountName);
                            }
                        }
                        getApplicationContext().accountTypeGroup = group;


                    }
                }
            }
        });
    }

    public void startOutHoldAmountCall(String accountType) {
        CommonUtil.cancelCall(outHoldAmountCall);
        outHoldAmountCall = VHttpServiceManager.getInstance().getVService().outHoldAmount(accountType);
        outHoldAmountCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    amountDecimals = resultData.getItem("amountDecimals", Integer.class);
                    holdAmount = resultData.getItem("holdAmount", String.class);
                    tvEnableAmount.setText("可用数量: " + holdAmount + " USDT");
                }
            }
        });
    }


    public void startTransferCall(final String amount, final String accountTypeFrom, final String accountTypeTo, String payPass) {
        CommonUtil.cancelCall(transferCall);
        transferCall = VHttpServiceManager.getInstance().getVService().transfer(amount, accountTypeFrom, accountTypeTo,payPass);
        transferCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, TransferCoinActivity.this);
                    etAmount.setText("");
                    startListAccountTypeCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startTransferCall(amount, accountTypeFrom,accountTypeTo,pwString);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String accountName = data.getStringExtra("accountName");
                String accountType = data.getStringExtra("accountType");
                AccountType account = new AccountType(accountName, accountType);
                if (requestCode == 0x123) {
                    accountTypeFrom = account;
                    tvFrom.setText(accountName);
                    startOutHoldAmountCall(accountTypeFrom.accountType);
                } else if (requestCode == 0x456) {
                    accountTypeTo = account;
                    tvTo.setText(accountName);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llFrom:
                if (group == null || group.size() == 0) return;
                PageJumpUtil.pageJumpResult(TransferCoinActivity.this, TransferSelectAccountActivity.class, new Intent(), 0x123);
                break;
            case R.id.llTo:
                if (group == null || group.size() == 0) return;
                PageJumpUtil.pageJumpResult(TransferCoinActivity.this, TransferSelectAccountActivity.class, new Intent(), 0x456);
                break;
            case R.id.tvAll:
                etAmount.setText(holdAmount);
                etAmount.setSelection(holdAmount.length());
                break;
            case R.id.tvTransferCoin:
                String amount = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)) {
                    CommonUtil.showmessage("请输入划转数量", TransferCoinActivity.this);
                    return;
                }

                if (accountTypeFrom != null && accountTypeTo != null) {
                    if (accountTypeFrom.accountType.equals(accountTypeTo.accountType)) {
                        CommonUtil.showmessage("相同账户之间不能划转", TransferCoinActivity.this);
                        return;
                    }
                    startTransferCall(amount, accountTypeFrom.accountType, accountTypeTo.accountType,"");
                }
                break;
            case R.id.tvMenu:
                PageJumpUtil.pageJump(TransferCoinActivity.this, TransferCoinHistoryActivity.class);
                break;
        }
    }

    @Override
    public void finish() {
        getApplicationContext().accountTypeGroup = null;
        super.finish();
    }
}
