package com.procoin.module.myhome;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.legal.AddAdActivity;
import com.procoin.module.legal.LegalMoneyActivity;
import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 添加银行卡收款页面
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class AddBankPayActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvBankNo)
    EditText tvBankNo;
    @BindView(R.id.tvBankName)
    EditText tvBankName;
//    @BindView(R.id.tvBankAddress)
//    EditText tvBankAddress;

    private int receiptType;
    private int from;//0 收款管理过来 1 添加广告过来

    private Call<ResponseBody> getSaveReceiptsCall;


    @Override
    protected int setLayoutId() {
        return R.layout.add_bank_pay;
    }

    @Override
    protected String getActivityTitle() {
        return "添加银行卡";
    }


    public static void pageJump(Context context, int receiptType, int from) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.RECEIPTTYPE, receiptType);
        bundle.putInt(CommonConst.JUMPTYPE, from);
        PageJumpUtil.pageJump(context, AddBankPayActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_bank_pay);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.RECEIPTTYPE)) {
                receiptType = bundle.getInt(CommonConst.RECEIPTTYPE, 3);
            }
            if (bundle.containsKey(CommonConst.JUMPTYPE)) {
                from = bundle.getInt(CommonConst.JUMPTYPE, 0);
            }
        }
        ButterKnife.bind(this);
        immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivQr:
                MyhomeMultiSelectImageActivity.pageJumpThis(AddBankPayActivity.this, 1, true, false, AddBankPayActivity.class.getName());
                break;
            case R.id.tvSave:
                String name = tvName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    CommonUtil.showmessage("请输入姓名", AddBankPayActivity.this);
                    return;
                }
                String bankNo = tvBankNo.getText().toString().trim();
                if (TextUtils.isEmpty(bankNo)) {
                    CommonUtil.showmessage("请输入银行卡号", AddBankPayActivity.this);
                    return;
                }

                String bankName = tvBankName.getText().toString().trim();
                if (TextUtils.isEmpty(bankName)) {
                    CommonUtil.showmessage("请输入开户银行名称", AddBankPayActivity.this);
                    return;
                }
                startSaveReceipts(name, bankNo, bankName, "");
                break;
        }
    }

    private void startSaveReceipts(final String name, final String bankNo, final String bankName, String payPass) {
        CommonUtil.cancelCall(getSaveReceiptsCall);
        getSaveReceiptsCall = VHttpServiceManager.getInstance().getVService().otcSavePayment(receiptType, 0, name, bankNo, bankName, "");
        getSaveReceiptsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AddBankPayActivity.this);
                    if(from==0){
                        PageJumpUtil.pageJump(AddBankPayActivity.this, PaymentTermActivity.class);
                    }else if(from==1){
                        PageJumpUtil.pageJump(AddBankPayActivity.this, AddAdActivity.class);
                    }else if(from==2){
                        PageJumpUtil.pageJump(AddBankPayActivity.this, LegalMoneyActivity.class);
                    }
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startSaveReceipts(name, bankNo, bankName, pwString);
            }
        });
    }

}
