package com.procoin.module.legal.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.module.home.trade.TransferCoinActivity;
import com.procoin.module.legal.AuthenticationActivity;
import com.procoin.module.legal.MyAdListActivity;
import com.procoin.module.legal.OtcOrderHistoryActivity;
import com.procoin.module.legal.dialog.ConfirmSellDialogFragment;
import com.procoin.module.legal.dialog.LegalQuickBuyDialogFragment;
import com.procoin.module.legal.dialog.SelectReceiptTermDialogFragment;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.module.myhome.PaymentTermActivity;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.CommonUtil;
import com.procoin.util.DensityUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.BadgeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 快捷区
 * Created by zhengmj on 19-3-8.
 */

public class LegalMoneyQuickFragment extends UserBaseFragment implements View.OnClickListener {


    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvOrientationText)
    TextView tvOrientationText;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @BindView(R.id.ivHistory)
    AppCompatImageView ivHistory;
    @BindView(R.id.ivMore)
    AppCompatImageView ivMore;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvAll)
    TextView tvAll;

    private int type = 0;//0代表买，1卖
    private String holdAmount = "0.00";
    private int amount_decimalcount = 2;//数量的小数点数量

    private Call<ResponseBody> otcFindAdListCall;
    private Call<ResponseBody> outHoldAmountCall;


    private LegalQuickBuyDialogFragment legalQuickBuyDialogFragment;
    private SelectReceiptTermDialogFragment selectReceiptTermDialogFragment;


    private ConfirmSellDialogFragment confirmSellDialogFragment;

    private OptionalOrder optionalOrder;

    private BadgeView badgeChat;

    public static LegalMoneyQuickFragment newInstance() {
        LegalMoneyQuickFragment fragment = new LegalMoneyQuickFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("tab", tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            startOtcFindAdListCall();
            showPrivateChatNewsCount();

        }
        Log.d("onresumeTest", "onResume/////////=" + getClass() + "  getUserVisibleHint==" + getUserVisibleHint()+"  badgeChat=="+badgeChat);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("setUserVisibleHint", "isVisibleToUser==" + isVisibleToUser + "      " + getClass()+"  badgeChat=="+badgeChat);
        if (isVisibleToUser) {
            startOtcFindAdListCall();
            showPrivateChatNewsCount();
        }
    }

    private void startOtcFindAdListCall() {
        CommonUtil.cancelCall(otcFindAdListCall);
        otcFindAdListCall = VHttpServiceManager.getInstance().getVService().otcFindAdList(type == 0 ? "buy" : "sell", 0, "", 1, "fast");
        otcFindAdListCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<OptionalOrder> group = resultData.getGroup("data", new TypeToken<Group<OptionalOrder>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        optionalOrder = group.get(0);
                        if (optionalOrder != null) {
                            tvPrice.setText(optionalOrder.price);
                            tvLimit.setText("限额 ¥" + optionalOrder.minCny + "-¥" + optionalOrder.maxCny);
                        }
                    }

                    if (type == 1) {
                        startOutHoldAmountCall();
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }

        });
    }

    public void startOutHoldAmountCall() {
        CommonUtil.cancelCall(outHoldAmountCall);
        outHoldAmountCall = VHttpServiceManager.getInstance().getVService().outHoldAmount("balance");
        outHoldAmountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdAmount = resultData.getItem("holdAmount", String.class);
                    tvBalance.setText("余额 " + holdAmount + " USDT");
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal_money_quick, container, false);
        ButterKnife.bind(this, view);
//        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey("tab")) {
//            tab = bundle.getString("tab");
//            Log.d("MarkOptionalFragment", "tab==" + tab);
//        }
        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivHistory.setOnClickListener(this);
        llTransfer.setOnClickListener(this);
        tvAll.setOnClickListener(this);
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
                    if (s.length() - 1 - posDot > amount_decimalcount) {
                        s.delete(posDot + (amount_decimalcount + 1), posDot + (amount_decimalcount + 2));
                    }
                }
//                if (currCoin == null) return;
//                setLastAmountText(s.toString());
            }
        });
        if (savedInstanceState != null) {
            type = savedInstanceState.getInt(CommonConst.KEY_EXTRAS_TYPE);
        }
        switchType(type);

        badgeChat = new BadgeView(getActivity(), ivHistory);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        showPrivateChatNewsCount();
        return view;
    }


    public void showPrivateChatNewsCount() {
        if(badgeChat==null)return;
        int chatCount = 0;
        if (getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getAllPriChatRecordNum(getContext(),  getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.procoin.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void refresh() {
        if (selectReceiptTermDialogFragment != null && selectReceiptTermDialogFragment.isAdded()) {
            selectReceiptTermDialogFragment.startOtcFindMyPaymentList();
        }
    }

    public void showLegalQuickSellDialogFragment(final String amount) {
        selectReceiptTermDialogFragment = SelectReceiptTermDialogFragment.newInstance(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                if (selectReceiptTermDialogFragment != null) {
                    selectReceiptTermDialogFragment.dismiss();
                }

                Receipt receipt = (Receipt) t;
                showConfirmSellDialogFragment(receipt, amount);
            }
        });

        selectReceiptTermDialogFragment.showDialog(getChildFragmentManager(), "");
    }


    public void showLegalQuickBuyDialogFragment(String amount) {
        legalQuickBuyDialogFragment = LegalQuickBuyDialogFragment.newInstance(optionalOrder, amount);
        legalQuickBuyDialogFragment.showDialog(getChildFragmentManager(), "");
    }


    public void showConfirmSellDialogFragment(Receipt receipt, String amount) {
        confirmSellDialogFragment = ConfirmSellDialogFragment.newInstance(optionalOrder, receipt, amount);
        confirmSellDialogFragment.showDialog(getChildFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvBuy:

                switchType(0);
                break;
            case R.id.tvSell:

                switchType(1);
                break;
            case R.id.tvSubmit:
                if (optionalOrder == null) {
                    return;
                }
                String amount = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) == 0) {
                    if (type == 0) {
                        CommonUtil.showmessage("请输入购买数量", getActivity());
                    } else {
                        CommonUtil.showmessage("请输入卖出数量", getActivity());

                    }
                    return;
                }
                if (type == 0) {
                    showLegalQuickBuyDialogFragment(amount);
                } else {
                    showLegalQuickSellDialogFragment(amount);
                }
                break;
            case R.id.ivMore:
                showPopMoreMenu(v);
                break;
            case R.id.ivHistory:
                PageJumpUtil.pageJump(getActivity(), OtcOrderHistoryActivity.class);
                break;

            case R.id.llPublish:
                dissPopMore();
                PageJumpUtil.pageJump(getActivity(), MyAdListActivity.class);
                break;
            case R.id.llAuthen:
                dissPopMore();
                PageJumpUtil.pageJump(getActivity(), AuthenticationActivity.class);
                break;
            case R.id.llReceiptManager:
                dissPopMore();
                PageJumpUtil.pageJump(getActivity(), PaymentTermActivity.class);
                break;
            case R.id.llTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;
            case R.id.tvAll:
                etAmount.setText(holdAmount);
                etAmount.setSelection(etAmount.getText().toString().length());
                break;


        }
    }

    private PopupWindow popMore;
    private LinearLayout llPublish, llAuthen, llReceiptManager;

    private void showPopMoreMenu(View parent) {
        if (popMore == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.pop_legal_more);
            popMore = new PopupWindow(view, DensityUtil.dip2px(getActivity(), 150), ViewGroup.LayoutParams.WRAP_CONTENT);//
            llPublish = view.findViewById(R.id.llPublish);
            llAuthen = view.findViewById(R.id.llAuthen);
            llReceiptManager = view.findViewById(R.id.llReceiptManager);

            llPublish.setOnClickListener(this);
            llAuthen.setOnClickListener(this);
            llReceiptManager.setOnClickListener(this);

            popMore.setOutsideTouchable(false);
            popMore.setFocusable(false);//
            popMore.setOutsideTouchable(true);
            popMore.setFocusable(true);
            popMore.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            popMore.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (popMore != null && !popMore.isShowing()) {
//            pop.showAsDropDown(parent);
            popMore.showAsDropDown(parent, -20, -20);
//            pop.showAtLocation(parent, Gravity.TOP,0,0);
        }
    }

    private void dissPopMore() {
        if (popMore != null & popMore.isShowing()) {
            popMore.dismiss();
        }
    }

    private void switchType(int type) {
        this.type = type;
        if (type == 0) {
            tvBuy.setSelected(true);
            tvSell.setSelected(false);
            tvBuy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            tvSell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvOrientationText.setText("购买数量");
            tvSubmit.setText("0手续费购买");
            llTransfer.setVisibility(View.GONE);
            tvBalance.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
            tvAll.setVisibility(View.GONE);
        } else {
            tvBuy.setSelected(false);
            tvSell.setSelected(true);
            tvBuy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvSell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            tvOrientationText.setText("出售数量");
            tvSubmit.setText("0手续费出售");
            llTransfer.setVisibility(View.VISIBLE);
            tvBalance.setVisibility(View.VISIBLE);
            vLine.setVisibility(View.VISIBLE);
            tvAll.setVisibility(View.VISIBLE);
        }

        startOtcFindAdListCall();
    }


}
