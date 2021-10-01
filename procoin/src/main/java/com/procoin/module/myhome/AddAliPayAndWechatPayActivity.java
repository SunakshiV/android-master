package com.procoin.module.myhome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.legal.AddAdActivity;
import com.procoin.module.legal.LegalMoneyActivity;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.R;
import com.procoin.http.retrofitservice.UploadFileUtils;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 添加支付宝或者微信收款页面
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class AddAliPayAndWechatPayActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvAccount)
    EditText tvAccount;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvNameType)
    TextView tvNameType;
    @BindView(R.id.tvAccountType)
    TextView tvAccountType;
    private Call<ResponseBody> getSaveReceiptsCall;
    private Call<ResponseBody> callUploadFile;

    private TjrImageLoaderUtil tjrImageLoaderUtil;

    private int receiptType;//1 支付宝 2微信
    private int from;//0 收款管理过来 1 添加广告过来
    private String qrCodeUrl;

    @Override
    protected int setLayoutId() {
        return R.layout.add_alipay_or_wechatpay;
    }

    @Override
    protected String getActivityTitle() {
        return "添加";
    }


    public static void pageJump(Context context, int receiptType,int from) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.RECEIPTTYPE, receiptType);
        bundle.putInt(CommonConst.JUMPTYPE, from);
        PageJumpUtil.pageJump(context, AddAliPayAndWechatPayActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_alipay_or_wechatpay);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.RECEIPTTYPE)) {
                receiptType = bundle.getInt(CommonConst.RECEIPTTYPE, 1);
            }
            if (bundle.containsKey(CommonConst.JUMPTYPE)) {
                from = bundle.getInt(CommonConst.JUMPTYPE, 0);
            }
        }
        ButterKnife.bind(this);
        if (receiptType == 1) {
            mActionBar.setTitle("添加支付宝");
            tvNameType.setText("支付宝姓名");
            tvAccountType.setText("支付宝账号");
        } else {
            mActionBar.setTitle("添加微信");
            tvNameType.setText("微信名称");
            tvAccountType.setText("微信账号");
        }
        immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        ivQr.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivQr:
                MyhomeMultiSelectImageActivity.pageJumpThis(AddAliPayAndWechatPayActivity.this, 1, true, false, AddAliPayAndWechatPayActivity.class.getName());
                break;
            case R.id.tvSave:
                String name = tvName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    CommonUtil.showmessage("请输入姓名", AddAliPayAndWechatPayActivity.this);
                    return;
                }
                String account = tvAccount.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    CommonUtil.showmessage("请输入账号", AddAliPayAndWechatPayActivity.this);
                    return;
                }
                if (TextUtils.isEmpty(qrCodeUrl)) {
                    CommonUtil.showmessage("请添加收款二维码", AddAliPayAndWechatPayActivity.this);
                    return;
                }
                startSaveReceipts(name, account, qrCodeUrl, "");
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, -1) == 0) {
                ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
                if (flist != null && flist.size() > 0) {
                    String filePath = flist.get(0);
                    tjrImageLoaderUtil.displayImageForHead("file://" + filePath, ivQr);
                    qrCodeUrl = "";
                    startUploadQr(filePath);
                }
            }

        }
    }


    private void startUploadQr(String filePath) {
        com.procoin.social.util.CommonUtil.cancelCall(callUploadFile);
        showProgressDialog();
        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetOriginal", "receipt", UploadFileUtils.getImageFilesMap(filePath));
        callUploadFile.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    String[] imageUrlList = resultData.getStringArray("imageUrlList");
                    if (imageUrlList != null && imageUrlList.length > 0) {
                        qrCodeUrl = imageUrlList[0];
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

    private void startSaveReceipts(final String name, final String account, final String qrCodeUrl, String payPass) {
        CommonUtil.cancelCall(getSaveReceiptsCall);
        showProgressDialog();
        getSaveReceiptsCall = VHttpServiceManager.getInstance().getVService().otcSavePayment(receiptType, 0, name, account, "", qrCodeUrl);
        getSaveReceiptsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AddAliPayAndWechatPayActivity.this);
                    if(from==0){
                        PageJumpUtil.pageJump(AddAliPayAndWechatPayActivity.this, PaymentTermActivity.class);
                    }else if(from==1){
                        PageJumpUtil.pageJump(AddAliPayAndWechatPayActivity.this, AddAdActivity.class);
                    }else if(from==2){
                        PageJumpUtil.pageJump(AddAliPayAndWechatPayActivity.this, LegalMoneyActivity.class);
                    }
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startSaveReceipts(name, account, qrCodeUrl, pwString);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }


        });
    }

}
