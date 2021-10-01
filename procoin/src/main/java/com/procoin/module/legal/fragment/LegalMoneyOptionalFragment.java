package com.procoin.module.legal.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.procoin.module.legal.AuthenticationActivity;
import com.procoin.module.legal.MyAdListActivity;
import com.procoin.module.legal.OtcOrderHistoryActivity;
import com.procoin.module.legal.adapter.OptionalFilterAdapter;
import com.procoin.module.legal.adapter.OptionalListAdapter;
import com.procoin.module.legal.dialog.OptionalBuySellDialogFragment;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.module.myhome.PaymentTermActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.DensityUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.BadgeView;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.widgets.SimpleSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 自选区
 * Created by zhengmj on 19-3-8.
 */

public class LegalMoneyOptionalFragment extends UserBaseFragment implements View.OnClickListener {


    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.ivArrow1)
    AppCompatImageView ivArrow1;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.llPop)
    LinearLayout llPop;
    @BindView(R.id.llPayWay)
    LinearLayout llPayWay;
    @BindView(R.id.tvFilterCny)
    TextView tvFilterCny;
    @BindView(R.id.ivArrow2)
    AppCompatImageView ivArrow2;
    @BindView(R.id.llPayAmount)
    LinearLayout llPayAmount;
    //    @BindView(R.id.ivArrow3)
//    AppCompatImageView ivArrow3;
//    @BindView(R.id.llPayNum)
//    LinearLayout llPayNum;
    @BindView(R.id.ivMore)
    AppCompatImageView ivMore;
    @BindView(R.id.ivHistory)
    AppCompatImageView ivHistory;


    @BindView(R.id.iv_hiden)
    ImageView iv_hiden;

    @BindView(R.id.rvOptionalList)
    LoadMoreRecycleView rvOptionalList;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private int type = 0;//0代表买，1卖
    private String filterPayWay; //筛选支付方式：0全部，1支付宝，2微信，3银行卡
    private String filterCny = "";//筛选金额
    private int pageSize = 15;
    private int pageNo = 1;

    private OptionalListAdapter optionalListAdapter;

    private OptionalBuySellDialogFragment optionalBuySellDialogFragment;

    private Call<ResponseBody> otcFindAdListCall;

    private BadgeView badgeChat;


    public static LegalMoneyOptionalFragment newInstance() {
        LegalMoneyOptionalFragment fragment = new LegalMoneyOptionalFragment();
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
        Log.d("onresumeTest", "onResume/////////=" + getClass() + "  getUserVisibleHint==" + getUserVisibleHint());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("setUserVisibleHint", "isVisibleToUser==" + isVisibleToUser + "      " + getClass());
        if (isVisibleToUser) {
            startOtcFindAdListCall();
            showPrivateChatNewsCount();
        }
    }

    private void startOtcFindAdListCall() {
        CommonUtil.cancelCall(otcFindAdListCall);
        otcFindAdListCall = VHttpServiceManager.getInstance().getVService().otcFindAdList(type == 0 ? "buy" : "sell", "全部".equals(filterPayWay) ? 0 : "支付宝".equals(filterPayWay) ? 1 : "微信".equals(filterPayWay) ? 2 : "银行卡".equals(filterPayWay) ? 3 : 0, filterCny, pageNo,"");
        otcFindAdListCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    Group<OptionalOrder> group = null;
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<OptionalOrder>>() {
                    }.getType());

                    if (pageNo == 1) {
                        optionalListAdapter.setGroup(group);
                        if (group != null && group.size() > 0) {
                            iv_hiden.setVisibility(View.GONE);
                            rvOptionalList.setVisibility(View.VISIBLE);
                        } else {
                            iv_hiden.setVisibility(View.VISIBLE);
                            rvOptionalList.setVisibility(View.GONE);
                        }
                    } else {
                        optionalListAdapter.addItem(group);
                        optionalListAdapter.notifyDataSetChanged();
                    }
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    optionalListAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                optionalListAdapter.onLoadComplete(false, false);
            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.legal_moeny_optional, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey("tab")) {
//            tab = bundle.getString("tab");
//            Log.d("MarkOptionalFragment", "tab==" + tab);
//        }

//        startGetOptional();
        llPayWay.setOnClickListener(this);
        llPayAmount.setOnClickListener(this);
//        llPayNum.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);

        ivMore.setOnClickListener(this);
        ivHistory.setOnClickListener(this);

        optionalListAdapter = new OptionalListAdapter(getActivity(), new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {

                showOptionalSellDialogFragment((OptionalOrder) t);
            }
        }, "", false);
        rvOptionalList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOptionalList.addItemDecoration(new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.dividerColor)));
        rvOptionalList.setAdapter(optionalListAdapter);
        rvOptionalList.setRecycleViewLoadMoreCallBack(callBack);
        optionalListAdapter.setRecycleViewLoadMoreCallBack(callBack);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startOtcFindAdListCall();
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

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (optionalListAdapter != null && optionalListAdapter.getRealItemCount() > 0) {
                OptionalOrder optionalOrder = optionalListAdapter.getItem(optionalListAdapter.getRealItemCount() - 1);
                if (optionalOrder == null) {
                    pageNo = 1;
                }
                startOtcFindAdListCall();
            }
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llPayWay:
                showPopPayWayMenu(v);

                break;
            case R.id.llPayAmount:
                showPopAmountMenu(v);
                break;
            case R.id.tvResetAmount:
                filterCny = "";
                amountAdapter.setSelected("");
                tvFilterCny.setText("交易金额");
                tvFilterCny.setTextColor(ContextCompat.getColor(getActivity(), R.color.c3d3a50));
                ivArrow2.setImageResource(R.drawable.ic_svg_legal_arrow_bottom);
                dissPopAmount();
                pageNo = 1;
                startOtcFindAdListCall();
                break;
            case R.id.tvFilterAmount:
                dissPopAmount();
                pageNo = 1;
                startOtcFindAdListCall();
                break;
//            case R.id.llPayNum:
//                showPopNumMenu(v);
//                break;
            case R.id.tvBuy:
                switchType(0);
                break;
            case R.id.tvSell:
                switchType(1);
                break;
//            case R.id.viewPopNumCancel:
//                dissPopNum();
//                break;
            case R.id.viewPopAmountCancel:
                dissPopAmount();
                break;

            case R.id.viewPopPayWayCancel:
                dissPopPayWay();
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


        }
    }


    private void switchType(int type) {
        this.type = type;
        pageNo = 1;
        if (type == 0) {
            tvBuy.setSelected(true);
            tvSell.setSelected(false);
            tvBuy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            tvSell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            optionalListAdapter.setBuySell("buy");
        } else {
            tvBuy.setSelected(false);
            tvSell.setSelected(true);
            tvBuy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvSell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            optionalListAdapter.setBuySell("sell");
        }

        startOtcFindAdListCall();
    }


    public void showOptionalSellDialogFragment(OptionalOrder optionalOrder) {
        optionalBuySellDialogFragment = OptionalBuySellDialogFragment.newInstance(optionalOrder);
        optionalBuySellDialogFragment.showDialog(getChildFragmentManager(), "");
    }


    //
    PopupWindow popPayWay;
    RecyclerView rvPayWay;
    OptionalFilterAdapter payWayAdapter;
    View viewPopPayWayCancel;


    private void showPopPayWayMenu(View parent) {
        if (popPayWay == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.pop_legal_filter_pay_way);
            popPayWay = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//
            rvPayWay = view.findViewById(R.id.rvPayWay);

            viewPopPayWayCancel = view.findViewById(R.id.viewPopPayWayCancel);
            payWayAdapter = new OptionalFilterAdapter(getActivity());
            rvPayWay.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rvPayWay.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 10, 0, 5, 5));
            rvPayWay.setAdapter(payWayAdapter);
            payWayAdapter.setData(new String[]{"全部", "银行卡", "支付宝", "微信"});
            payWayAdapter.setSelected("全部");
            filterPayWay = "全部";
            payWayAdapter.setOnItemclickListen(new OptionalFilterAdapter.onItemclickListen() {
                @Override
                public void onItemclick(String payWay) {
                    popPayWay.dismiss();
                    tvPayWay.setText(payWay);
                    filterPayWay = payWay;
                    pageNo = 1;
                    startOtcFindAdListCall();
                }
            });

            popPayWay.setOutsideTouchable(false);
            popPayWay.setFocusable(false);//
            popPayWay.setOutsideTouchable(true);
            popPayWay.setFocusable(true);
            popPayWay.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            popPayWay.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
//                    tvPayWay.setText("支付方式");
//                    tvPayWay.setTextColor(ContextCompat.getColor(getActivity(),R.color.c3d3a50));
//                    ivArrow1.setImageResource(R.drawable.ic_svg_legal_arrow);
                }
            });

        }
        viewPopPayWayCancel.setOnClickListener(this);
        if (popPayWay != null && !popPayWay.isShowing()) {
//            pop.showAsDropDown(parent);
            popPayWay.showAsDropDown(parent, 0, 0);
            payWayAdapter.setSelected(filterPayWay);
            tvPayWay.setText(payWayAdapter.getSelectedKeyType());
            tvPayWay.setTextColor(ContextCompat.getColor(getActivity(), R.color.c6175ae));
            ivArrow1.setImageResource(R.drawable.ic_svg_legal_arrow_up);
//            pop.showAtLocation(parent, Gravity.TOP,0,0);
        }
    }

    private void dissPopPayWay() {
        if (popPayWay != null & popPayWay.isShowing()) {
            popPayWay.dismiss();


        }
    }

//    //
//    PopupWindow popNum;
//    RecyclerView rvNum;
//    OptionalFilterAdapter numAdapter;
//    View viewPopNumCancel;
//    private void showPopNumMenu(View parent) {
//        if (popNum == null) {
//            View view = InflaterUtils.inflateView(getActivity(), R.layout.pop_legal_filter_num);
//            popNum = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//
//            rvNum=view.findViewById(R.id.rvNum);
//            viewPopNumCancel=view.findViewById(R.id.viewPopNumCancel);
//            numAdapter = new OptionalFilterAdapter(getActivity());
//            rvNum.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//            rvNum.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 10, 0, 5, 5));
//            rvNum.setAdapter(numAdapter);
//            numAdapter.setData(new String[]{"18", "20", "30", "50", "80", "100",});
//            numAdapter.setOnItemclickListen(new OptionalFilterAdapter.onItemclickListen() {
//                @Override
//                public void onItemclick(String leverType) {
//                }
//            });
//
//            popNum.setOutsideTouchable(false);
//            popNum.setFocusable(false);//
//            popNum.setOutsideTouchable(true);
//            popNum.setFocusable(true);
//            popNum.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
//            popNum.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                }
//            });
//
//        }
//        viewPopNumCancel.setOnClickListener(this);
//        if (popNum != null && !popNum.isShowing()) {
////            pop.showAsDropDown(parent);
//            popNum.showAsDropDown(parent, 0, 0);
////            pop.showAtLocation(parent, Gravity.TOP,0,0);
//        }
//    }
//
//    private void dissPopNum() {
//        if (popNum != null & popNum.isShowing()) {
//            popNum.dismiss();
//        }
//    }


    PopupWindow popAmount;
    RecyclerView rvAmount;
    OptionalFilterAdapter amountAdapter;
    View viewPopAmountCancel;
    EditText etAmount;

    TextView tvResetAmount;
    TextView tvFilterAmount;

    private void showPopAmountMenu(View parent) {
        if (popAmount == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.pop_legal_filter_amount);
            popAmount = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//
            rvAmount = view.findViewById(R.id.rvAmount);
            etAmount = view.findViewById(R.id.etAmount);

            tvResetAmount = view.findViewById(R.id.tvResetAmount);
            tvFilterAmount = view.findViewById(R.id.tvFilterAmount);

            viewPopAmountCancel = view.findViewById(R.id.viewPopAmountCancel);
            amountAdapter = new OptionalFilterAdapter(getActivity());
            rvAmount.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rvAmount.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 10, 0, 5, 5));
            rvAmount.setAdapter(amountAdapter);
            amountAdapter.setData(new String[]{"100", "1000", "10000", "50000", "200000", "500000",});
            amountAdapter.setOnItemclickListen(new OptionalFilterAdapter.onItemclickListen() {
                @Override
                public void onItemclick(String cny) {
                    filterCny = cny;
                    tvFilterCny.setText(cny);
                    etAmount.setText(cny);
                    etAmount.setSelection(cny.length());
                }
            });
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filterCny = s.toString();
                    amountAdapter.setSelected(filterCny);
                    if (TextUtils.isEmpty(filterCny)) {
                        tvFilterCny.setText("交易金额");
                    } else {
                        tvFilterCny.setText(filterCny);
                    }
                }
            });

            tvResetAmount.setOnClickListener(this);
            tvFilterAmount.setOnClickListener(this);

            popAmount.setOutsideTouchable(false);
            popAmount.setFocusable(false);//
            popAmount.setOutsideTouchable(true);
            popAmount.setFocusable(true);
            popAmount.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            popAmount.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (TextUtils.isEmpty(filterCny)) {
                        tvFilterCny.setText("交易金额");
                        tvFilterCny.setTextColor(ContextCompat.getColor(getActivity(), R.color.c3d3a50));
                        ivArrow2.setImageResource(R.drawable.ic_svg_legal_arrow_bottom);
                    }
                }
            });

        }
        viewPopAmountCancel.setOnClickListener(this);
        if (popAmount != null && !popAmount.isShowing()) {
//            pop.showAsDropDown(parent);
            popAmount.showAsDropDown(parent, 0, 0);

            if (TextUtils.isEmpty(filterCny)) {
                tvFilterCny.setText("交易金额");
            } else {
                tvFilterCny.setText(filterCny);
            }
            amountAdapter.setSelected(filterCny);
            tvFilterCny.setTextColor(ContextCompat.getColor(getActivity(), R.color.c6175ae));
            ivArrow2.setImageResource(R.drawable.ic_svg_legal_arrow_up);
        }
    }

    private void dissPopAmount() {
        if (popAmount != null & popAmount.isShowing()) {
            popAmount.dismiss();
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


}
