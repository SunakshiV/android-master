package com.procoin.module.home.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.data.sharedpreferences.NormalShareData;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.http.model.User;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.module.home.HomeActivity;
import com.procoin.module.home.trade.RechargeCoinActivity;
import com.procoin.module.home.trade.TakeCoinActivity;
import com.procoin.module.home.trade.TransferCoinActivity;
import com.procoin.module.home.trade.history.CoinFollowHistoryActivity;
import com.procoin.module.home.trade.history.CoinTradeEntrustLeverActivity;
import com.procoin.module.legal.LegalMoneyActivity;
import com.procoin.module.myhome.MyHomeInfoActivity;
import com.procoin.module.myhome.MyMessageActivity;
import com.procoin.module.myhome.PaymentTermActivity;
import com.procoin.module.myhome.SettingActivity;
import com.procoin.module.myhome.UserHomeActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.BadgeView;
import com.procoin.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 19-1-29.
 */

public class HomeMineFragment extends UserBaseImmersionBarFragment implements View.OnClickListener {
    @BindView(R.id.ll_bar)
    LinearLayout ll_bar;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvDesc)
    TextView tvDesc;

    @BindView(R.id.llHead)
    LinearLayout llHead;

    @BindView(R.id.llNews)
    LinearLayout llNews;
    @BindView(R.id.ivSetting)
    LinearLayout ivSetting;


    @BindView(R.id.llRechargeCoin)
    LinearLayout llRechargeCoin;
    @BindView(R.id.llWithDrawCoin)
    LinearLayout llWithDrawCoin;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.llLegal)
    LinearLayout llLegal;

    @BindView(R.id.llFollowHis)
    LinearLayout llFollowHis;

    @BindView(R.id.ivGoHome)
    ImageView ivGoHome;


    @BindView(R.id.llFeedback)
    LinearLayout llFeedback;
    @BindView(R.id.tvFeedback)
    TextView tvFeedback;
    @BindView(R.id.llHelp)
    LinearLayout llHelp;
    //    @BindView(R.id.vPointOfEdit)
//    View vPointOfEdit;
    @BindView(R.id.tvLatestMsgTitle)
    TextView tvLatestMsgTitle;
    @BindView(R.id.flLatestMsgTitle)
    FrameLayout flLatestMsgTitle;

    @BindView(R.id.tvUsdtBalance)
    TextView tvUsdtBalance;
    @BindView(R.id.llFuturesHis)
    LinearLayout llFuturesHis;
    @BindView(R.id.llDigitalHis)
    LinearLayout llDigitalHis;

    @BindView(R.id.llShareApp)
    LinearLayout llShareApp;
    @BindView(R.id.ivLegal)
    AppCompatImageView ivLegal;


    private String shareUrl;
    private String latestMsgTitle = "";

    private String symbol = "";
    private String byyAmount = "";
    private String usdtBalance = "";
    private String helpCenterUrl = "";


    private MainApplication application;
    private TjrImageLoaderUtil tjrImageLoaderUtil;

    private BadgeView badgePrivateChat;
    private BadgeView badgeChat;

    private Call<ResponseBody> createChatTopicCall;
    private String chatTopic;
    private String headUrl;
    private String userName;
    private int type;

    private Call<ResponseBody> getMyHomeCall;

    private TjrBaseDialog abilityDialog;

    private String showCoin;


    public static HomeMineFragment newInstance() {
        HomeMineFragment fragment = new HomeMineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic);
        try {
            application = (MainApplication) getActivity().getApplicationContext();
//            user = application.getUser();
        } catch (Exception e) {

        }
    }

    public void setShowCoin(String showCoin) {
        this.showCoin = showCoin;
//        if(tvShowCoin!=null){
//            if (!TextUtils.isEmpty(showCoin)) {
//                tvShowCoin.setVisibility(View.VISIBLE);
//                tvShowCoin.setText(showCoin);
//            } else {
//                tvShowCoin.setVisibility(View.GONE);
//            }
//
//        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
//        flHead.setOnClickListener(this);
        llHead.setOnClickListener(this);
        llNews.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
//        llPrivateChat.setOnClickListener(this);
        llWithDrawCoin.setOnClickListener(this);
        llRechargeCoin.setOnClickListener(this);
        llTransfer.setOnClickListener(this);
        llLegal.setOnClickListener(this);
        llFollowHis.setOnClickListener(this);
        ivGoHome.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        llFuturesHis.setOnClickListener(this);
        llDigitalHis.setOnClickListener(this);
        llShareApp.setOnClickListener(this);

        badgePrivateChat = new BadgeView(getActivity(), tvFeedback);
        badgePrivateChat.setBadgeMargin(0, 0);
        badgePrivateChat.setBadgePosition(BadgeView.POSITION_VERTICAL_RIGHT);
        badgePrivateChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        badgeChat = new BadgeView(getActivity(), ivLegal);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        setUserInfo(getUser());//先初始化
        setPrivateChatNewsCount();
        showLegalPrivateChatNewsCount();
//        setShowCoin(showCoin);
//        setEditPointFlag();


        Log.d("V_AccountFragment", "onCreateView  getUserVisibleHint==" + getUserVisibleHint());
        return view;
    }


    private void startGetMyHomeCallCall() {
        if (getUser() == null || TextUtils.isEmpty(getUserId())) return;
        CommonUtil.cancelCall(getMyHomeCall);
        getMyHomeCall = VHttpServiceManager.getInstance().getVService().myHome();
        getMyHomeCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    latestMsgTitle = "";
                    shareUrl = resultData.getItem("shareUrl", String.class);
                    latestMsgTitle = resultData.getItem("latestMsgTitle", String.class);

                    symbol = resultData.getItem("symbol", String.class);
                    byyAmount = resultData.getItem("byyAmount", String.class);
                    usdtBalance = resultData.getItem("usdtBalance", String.class);

                    helpCenterUrl = resultData.getItem("helpCenterUrl", String.class);

                    if (!TextUtils.isEmpty(latestMsgTitle)) {
                        flLatestMsgTitle.setVisibility(View.VISIBLE);
                        tvLatestMsgTitle.setText(latestMsgTitle);
                    } else {
                        flLatestMsgTitle.setVisibility(View.GONE);
                    }
                    tvUsdtBalance.setText(usdtBalance);
//                    tvKBTBalance.setText(kbtAmount);

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    public void showLegalPrivateChatNewsCount() {
        if(badgeChat==null)return;
        int chatCount = 0;
        if (getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getAllPriChatRecordNum(getActivity(), getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.procoin.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }
    }

    public void setPrivateChatNewsCount() {
        if (getUser() == null) return;
        if (getActivity() == null) return;
        if (!(getActivity() instanceof HomeActivity)) return;
        if (badgePrivateChat == null) return;
        int serviceChatCount = 0;
        if (getUser() != null) {//现在客服是弹出二维码，青爷说先不加消息数
//            serviceChatCount = PrivateChatSharedPreferences.getAllPriChatRecordNum(getActivity(), getUser().getUserId());
        }
        if (serviceChatCount > 0) {//显示
            badgePrivateChat.show();
            badgePrivateChat.setBadgeText(CommonUtil.setNewsCount(serviceChatCount));
        } else {//不显示
            badgePrivateChat.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.flHead:
            case R.id.llHead:
                PageJumpUtil.pageJump(getActivity(), MyHomeInfoActivity.class);
                if (getUser() != null)
                    NormalShareData.saveUserEditFlag(getActivity(), getUser().userId, 1);
                break;
            case R.id.ivSetting:
                PageJumpUtil.pageJump(getActivity(), SettingActivity.class);
                break;
            case R.id.llNews:
                PageJumpUtil.pageJump(getActivity(), MyMessageActivity.class);
                break;

            case R.id.llRechargeCoin:
                PageJumpUtil.pageJump(getActivity(), RechargeCoinActivity.class);
//                RechargeCoinActivity.pageJump(getActivity(),"USDT");
                break;
            case R.id.llWithDrawCoin:
                PageJumpUtil.pageJump(getActivity(), TakeCoinActivity.class);
                break;
            case R.id.llTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;
            case R.id.llLegal:
                PageJumpUtil.pageJump(getActivity(), LegalMoneyActivity.class);
                break;

            case R.id.ivGoHome:
                if (getUser() != null)
                    UserHomeActivity.pageJump(getActivity(), getUser().userId);
                break;
            case R.id.llFeedback:
                if (!TextUtils.isEmpty(chatTopic) && !TextUtils.isEmpty(userName)) {
                    goChatOrWechatQr();
                } else {
                    startCreateChat();
                }
                break;
            case R.id.llPaymentTerm:
                PaymentTermActivity.pageJump(getActivity(), 0);
                break;
            case R.id.llHelp:
                if (!TextUtils.isEmpty(helpCenterUrl)) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), helpCenterUrl);
                }
                break;
            case R.id.llFuturesHis:
                CoinTradeEntrustLeverActivity.pageJump(getActivity(), "stock");
                break;
            case R.id.llDigitalHis:
                CoinTradeEntrustLeverActivity.pageJump(getActivity(), "digital");
                break;
            case R.id.llFollowHis:
//                PageJumpUtil.pageJump(getActivity(), CropyOrderHistoryActivity.class);
                PageJumpUtil.pageJump(getActivity(), CoinFollowHistoryActivity.class);
                break;
            case R.id.llShareApp:
                if (!TextUtils.isEmpty(shareUrl)) {
                    showShareDialogFragment();
                }
                break;


        }
    }

    String btnOktext = "";

    private void showAbilityDialog(String abilityValue) {
        abilityDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                if (btnOktext.equals("兑换")) {
                    startAbilityValueToAward();
                } else {
                    dismiss();
//                    PageJumpUtil.pageJump(getActivity(), LPHomeActivity.class);
                }

            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        String msg = Double.parseDouble(abilityValue) > 0 ? "是否把全部刻豆兑换成KBT？" : "目前暂无刻豆，请往小应用闪电预测获取";
        btnOktext = Double.parseDouble(abilityValue) > 0 ? "兑换" : "确定";
        abilityDialog.setTvTitle("兑换");
        abilityDialog.setMessage(msg);
        abilityDialog.setBtnOkText(btnOktext);
        abilityDialog.show();
    }


    private void startAbilityValueToAward() {
        CommonUtil.cancelCall(createChatTopicCall);
        ((HomeActivity) getActivity()).showProgressDialog();
        createChatTopicCall = VHttpServiceManager.getInstance().getVService().abilityValueToAward();
        createChatTopicCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                ((HomeActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    startGetMyHomeCallCall();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                ((HomeActivity) getActivity()).dismissProgressDialog();
            }
        });
    }

    private void goChatOrWechatQr() {
        if (type == 0) {
            if (!TextUtils.isEmpty(chatTopic) && !TextUtils.isEmpty(userName)) {
                ChatRoomActivity.pageJump(((HomeActivity) getActivity()), chatTopic, userName, headUrl);
            } else {
                CommonUtil.showmessage("未获取到客服信息", ((HomeActivity) getActivity()));
            }
        } else if (type == 1) {
            if (!TextUtils.isEmpty(headUrl)) {
                showWechatQRCodeFragment(chatTopic, userName, headUrl);
            } else {
                CommonUtil.showmessage("未获取到客服信息", ((HomeActivity) getActivity()));
            }

        }
    }

    private void startCreateChat() {
        CommonUtil.cancelCall(createChatTopicCall);
        ((HomeActivity) getActivity()).showProgressDialog();
        createChatTopicCall = VHttpServiceManager.getInstance().getVService().createChatTopic();
        createChatTopicCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                ((HomeActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    chatTopic = resultData.getItem("chatTopic", String.class);
                    headUrl = resultData.getItem("headUrl", String.class);
                    userName = resultData.getItem("userName", String.class);
                    type = resultData.getItem("type", Integer.class);//0跳转到聊天页面  1弹出微信二维码

                    goChatOrWechatQr();

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                ((HomeActivity) getActivity()).dismissProgressDialog();
            }
        });
    }


    WechatQRCodeFragment wechatQRCodeFragment;

    private void showWechatQRCodeFragment(String title, String subTitle, String qrUrl) {
        wechatQRCodeFragment = WechatQRCodeFragment.newInstance(title, subTitle, qrUrl);
        wechatQRCodeFragment.showDialog(getActivity().getSupportFragmentManager(), "");
    }

    ShareDialogFragment shareDialogFragment;

    private void showShareDialogFragment() {
        shareDialogFragment = ShareDialogFragment.newInstance(getUser(), shareUrl);
        shareDialogFragment.setOnShareDialogCallBack(new ShareDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                shareDialogFragment.dismiss();
            }
        });
        shareDialogFragment.showDialog(getActivity().getSupportFragmentManager(), "");
    }

    boolean refresh = false;


    @Override
    public void onResume() {
        super.onResume();
        Log.d("V_AccountFragment", "onResume   getUserVisibleHint()==" + getUserVisibleHint());
        if (getUserVisibleHint()) {
            startGetMyHomeCallCall();
//            startGetHomeAccount();
            setUserInfo(getUser());
            showLegalPrivateChatNewsCount();
//            setEditPointFlag();
        } else {
            refresh = true;
        }
    }

//    public void setEditPointFlag() {
//        if (getUser() != null) {
//            int editFlag = NormalShareData.getUserEditFlag(getActivity(), getUser().userId);
//            if (editFlag == 0) {
//                vPointOfEdit.setVisibility(View.VISIBLE);
//            } else {
//                vPointOfEdit.setVisibility(View.GONE);
//            }
//        }
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() == null) return;
        if (isVisibleToUser) {
            if (tvName != null && TextUtils.isEmpty(tvName.getText().toString())) {
                setUserInfo(getUser());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGetMyHomeCallCall();
                }
            }, 500);

            showLegalPrivateChatNewsCount();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).navigationBarColor(R.color.white).init();

    }

    public void immersionbar() {
        if (mImmersionBar != null && ll_bar != null) {
            mImmersionBar
                    .titleBar(ll_bar)
                    .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
                    .navigationBarColor(R.color.white)
                    .init();
        }
    }


    Call<ResponseBody> getHomeAccountCall;

//    private void startGetHomeAccount() {
//        refresh = false;
//        CommonUtil.cancelCall(getHomeAccountCall);
//        getHomeAccountCall = VHttpServiceManager.getInstance().getVService().homeAccount();
//        getHomeAccountCall.enqueue(new MyCallBack(getActivity()) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    String balance = resultData.getItem("balance", String.class);
//                    User u = resultData.getObject("user", User.class);
//                    tvTaojinzhiValue.setText(balance);
//                    if (u != null) {
//                        if (u.roomId == 0) {
//                            llMyLive.setVisibility(View.GONE);
//                        } else {
//                            llMyLive.setVisibility(View.VISIBLE);
//                        }
//                        roomId = u.roomId;
//                        tvName.setText(u.getUserName());
//                        if (!TextUtils.isEmpty(u.headUrl) && !u.headUrl.equals(ivHead.getTag())) {//防止闪烁
//                            tjrImageLoaderUtil.displayImage(u.headUrl, ivHead);
//                            ivHead.setTag(u.headUrl);
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//            }
//        });
//    }

    private void setUserInfo(User u) {
        if (u == null) return;
        tvName.setText(u.getUserName());
        tvId.setText("ID：" + String.valueOf(u.getUserId()));
        tvDesc.setText(u.getDescribes());
        tjrImageLoaderUtil.displayImageForHead(u.headUrl, ivHead);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
