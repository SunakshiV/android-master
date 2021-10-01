package com.procoin.module.myhome.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.entity.ResultData;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.OnItemClick;
import com.procoin.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 18-10-26.
 */

public class PaymentTernAdapter extends BaseImageLoaderRecycleAdapter<Receipt> {


    private Context context;

    private Call<ResponseBody> getReceiptDeleteCall;
    private Call<ResponseBody> getReceiptSetDefaultCall;

    private OnItemClick onItemClick;//type==1的时候可以点击
    private int jumpType = 0;//0 我的收款方式 1选择我的收款方式

    public PaymentTernAdapter(Context context, int jumpType) {
        super(R.drawable.ic_common_mic);
        this.context = context;
        this.jumpType = jumpType;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.payment_tern_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivReceiptLogo)
        ImageView ivReceiptLogo;
        @BindView(R.id.tvReceiptTypeValue)
        TextView tvReceiptTypeValue;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.llPaymentTerm)
        LinearLayout llPaymentTerm;

//        @BindView(R.id.tvDefault)
//        TextView tvDefault;
//        @BindView(R.id.tvSetDefault)
//        TextView tvSetDefault;

        @BindView(R.id.tvDelete)
        TextView tvDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Receipt data, final int position) {
            displayImageRound(data.receiptLogo, ivReceiptLogo);
            tvAccount.setText(data.receiptNo);
            tvName.setText(data.receiptName);
            if (data.receiptType == 3) {
                tvReceiptTypeValue.setText(data.bankName);
            } else {
                tvReceiptTypeValue.setText(data.receiptTypeValue);
            }

//            if (data.isDefault == 1) {
//                tvDefault.setVisibility(View.VISIBLE);
//                tvSetDefault.setVisibility(View.INVISIBLE);
//            } else {
//                tvDefault.setVisibility(View.GONE);
//                tvSetDefault.setVisibility(View.VISIBLE);
//                tvSetDefault.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startReceiptSetDefault(data.receiptId);
//                    }
//                });
//            }

            if (jumpType == 1) {
                tvDelete.setVisibility(View.INVISIBLE);
                llPaymentTerm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClick != null) {
                            onItemClick.onItemClickListen(position, data);
                        }
                    }
                });
            } else {
                tvDelete.setVisibility(View.VISIBLE);
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDelDialog(data, position);
                    }
                });
            }


        }
    }


    TjrBaseDialog delDialog;

    private void showDelDialog(final Receipt entity, final int pos) {
        delDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
                startReceiptDelete(entity, pos, "");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        delDialog.setMessage("确定删除");
        delDialog.setBtnOkText("删除");
        delDialog.setBtnColseText("取消");
        delDialog.setTitleVisibility(View.GONE);
        delDialog.show();
    }


    private void startReceiptDelete(final Receipt entity, final int pos, String payPass) {
        CommonUtil.cancelCall(getReceiptDeleteCall);
        getReceiptDeleteCall = VHttpServiceManager.getInstance().getVService().otcPaymentDelete(entity.paymentId);
        getReceiptDeleteCall.enqueue(new MyCallBack(context) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, context);
                    removeItem(pos);
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startReceiptDelete(entity, pos, pwString);
            }

        });
    }

    private void startReceiptSetDefault(final long receiptId) {
        CommonUtil.cancelCall(getReceiptSetDefaultCall);
        getReceiptSetDefaultCall = VHttpServiceManager.getInstance().getVService().receiptSetDefault(receiptId);
        getReceiptSetDefaultCall.enqueue(new MyCallBack(context) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, context);
                    setDefault(receiptId);
                }
            }
        });
    }

    private void setDefault(long receiptId) {
        for (Receipt receipt : group) {
            if (receipt.paymentId == receiptId) {
                receipt.isDefault = 1;
            } else {
                receipt.isDefault = 0;
            }
        }
        notifyDataSetChanged();
    }
}
