package com.procoin.module.kbt.app.lightningprediction;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.module.kbt.app.lightningprediction.entity.KbtTicket;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.http.TjrBaseApi;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.SubPushHttp;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.MarketActivity;
import com.procoin.module.home.trade.TradeActivity;
import com.procoin.module.kbt.app.lightningprediction.adapter.CommentAdapter;
import com.procoin.module.kbt.app.lightningprediction.adapter.HorizontalAbilityNumAdapter;
import com.procoin.module.kbt.app.lightningprediction.adapter.HorizontalHistoryAdapter;
import com.procoin.module.kbt.app.lightningprediction.chart.MinuteTimeLineChart;
import com.procoin.module.kbt.app.lightningprediction.dialog.PredictionDialog;
import com.procoin.module.kbt.app.lightningprediction.dialog.SendCommentDialogFragment;
import com.procoin.module.kbt.app.lightningprediction.entity.Comment;
import com.procoin.module.kbt.app.lightningprediction.entity.PreGame;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.DensityUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.SimpleRecycleDivider;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class LPHomeActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;

    @BindView(R.id.tvMenu)
    AppCompatImageView tvMenu;
    @BindView(R.id.horizontalNumList)
    RecyclerView horizontalNumList;
    @BindView(R.id.tvPredictionSurpass)
    TextView tvPredictionSurpass;
    @BindView(R.id.tvPredictionNotSurpass)
    TextView tvPredictionNotSurpass;
    @BindView(R.id.llSendComment)
    LinearLayout llSendComment;
    @BindView(R.id.tvMyPower)
    TextView tvMyPower;
    @BindView(R.id.rvCommentList)
    RecyclerView rvCommentList;

    @BindView(R.id.horizontalHistoryList)
    RecyclerView horizontalHistoryList;


    @BindView(R.id.tvVotePrice)
    TextView tvVotePrice;


    @BindView(R.id.tvStopTime)
    TextView tvStopTime;
    @BindView(R.id.llBtn)
    LinearLayout llBtn;
    @BindView(R.id.tvMyVote)
    TextView tvMyVote;
    @BindView(R.id.tvResultTime)
    TextView tvResultTime;
    @BindView(R.id.llResult)
    LinearLayout llResult;
    @BindView(R.id.chart)
    MinuteTimeLineChart chart;
    @BindView(R.id.tvPbTextSurpass)
    TextView tvPbTextSurpass;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.tvPbTextNotSurpass)
    TextView tvPbTextNotSurpass;


    private HorizontalAbilityNumAdapter horizontalAbilityNumAdapter;
    private GridLayoutManager gridLayoutManagerAbility;

    private HorizontalHistoryAdapter horizontalHistoryAdapter;
    private GridLayoutManager gridLayoutManagerHistory;

    private CommentAdapter commentAdapter;


    @Override
    protected int setLayoutId() {
        return R.layout.lp_home;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    private GameSeverConnect gameSeverConnect;

    private PredictionDialog predictionDialog;
    private SendCommentDialogFragment sendCommentDialogFragment;

//    private String abilityValue = "0";//我的刻豆
    private String holdAmount = "0.00";//我的YYB数量
    private String tips = "";//点问号弹出内容
    private Group<KbtTicket> kbtTickets;

    private PreGame preGame;

    private String serverHost = "";
    private String serverHttp = "";

    private String rulesUrl = "";
    private String statementUrl = "";

    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Object obj = msg.obj;
            if (obj instanceof ReceiveModel) {
                ReceiveModel d = (ReceiveModel) obj;
                Log.d("messageHandler", "ReceiveModel.type=" + d.type);
                switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(d.type)) {
                    case game_predict_main://
                        if (d.obj != null && d.obj instanceof PreGame) {
                            preGame = (PreGame) d.obj;
                            Log.d("messageHandler", "preGame=" + preGame.toString());
                            setPreGame20000(preGame);
                            //图
                            chart.refreshData(preGame);
                        }
                        break;
                    case game_predict_user://{\"abilityValue\":0,\"holdAmount\":0}"
                        if (d.obj != null && d.obj instanceof String) {
                            String str = (String) d.obj;
                            try {
                                JSONObject jsonObject = new JSONObject(str);
                                if (JsonParserUtils.hasAndNotNull(jsonObject, "abilityValue")) {
//                                    abilityValue = jsonObject.getString("abilityValue");
                                }
                                if (JsonParserUtils.hasAndNotNull(jsonObject, "holdAmount")) {
                                    holdAmount = jsonObject.getString("holdAmount");
                                }

//                                if (JsonParserUtils.hasAndNotNull(jsonObject, "descr")) {
//                                    descr = jsonObject.getString("descr");
//                                }


                                setValue20001();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case game_predict_tickets:
                        if (d.obj != null && d.obj instanceof Group) {
                            kbtTickets = (Group<KbtTicket>) d.obj;
                        }
                        break;
                    case game_predict_chat:

                        if (d.obj != null && d.obj instanceof Comment) {
                            Comment comment = (Comment) d.obj;
                            Log.d("messageHandler", "comment=" + comment.toString());
                            if (commentAdapter.getItemCount() > 0) {
                                commentAdapter.addItem(comment);
                                commentAdapter.notifyDataSetChanged();
                            } else {
                                Group<Comment> comments = new Group<>();
                                comments.add(comment);
                                commentAdapter.setGroup(comments);
                            }
                            rvCommentList.scrollToPosition(commentAdapter.getItemCount() - 1);

                        }
                        break;
                    case game_kbt_not_enough://kbt不足去购买

                        if (d.obj != null && d.obj instanceof String) {
                            String str = (String) d.obj;
                            try {
                                String symbol = "";
                                JSONObject jsonObject = new JSONObject(str);
                                if (JsonParserUtils.hasAndNotNull(jsonObject, "data")) {
                                    JSONObject jsonData = jsonObject.getJSONObject("data");
                                    if (JsonParserUtils.hasAndNotNull(jsonData, "symbol")) {
                                        symbol = jsonData.getString("symbol");
                                    }
//
                                }
                                if (JsonParserUtils.hasAndNotNull(jsonObject, "msg")) {
                                    String notEnoughMsg = jsonObject.getString("msg");
                                    showGoBuyKbtDialog(notEnoughMsg, symbol);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case connection_error:
                        if (d.obj != null && d.obj instanceof String) {
                            String str = (String) d.obj;
                            try {
                                JSONObject jsonObject = new JSONObject(str);
                                if (JsonParserUtils.hasAndNotNull(jsonObject, "msg")) {
                                    CommonUtil.showmessage(jsonObject.getString("msg"), LPHomeActivity.this);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public static void pageJumpLPHomeActivity(Context context, String host) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SERVERHOST, host);
        PageJumpUtil.pageJump(context, LPHomeActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);
        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
//            String params = bundle.getString(CommonConst.PARAMS,"");
//            CommonUtil.showmessage(params,this);
            parserParamsBack(bundle, new ParamsBack() {
                @Override
                public void paramsBack(Bundle bundle, JSONObject jsonObject) throws Exception {
                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.SERVERHOST)) {
                        bundle.putString(CommonConst.SERVERHOST, jsonObject.getString(CommonConst.SERVERHOST));
                    }

                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.SERVERHTTP)) {
                        bundle.putString(CommonConst.SERVERHTTP, jsonObject.getString(CommonConst.SERVERHTTP));
                    }

                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.STATEMENTURL)) {
                        bundle.putString(CommonConst.STATEMENTURL, jsonObject.getString(CommonConst.STATEMENTURL));
                    }

                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.RULESURL)) {
                        bundle.putString(CommonConst.RULESURL, jsonObject.getString(CommonConst.RULESURL));
                    }

                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.TIPS)) {
                        bundle.putString(CommonConst.TIPS, jsonObject.getString(CommonConst.TIPS));
                    }


                }
            });
            serverHost = bundle.getString(CommonConst.SERVERHOST, TjrBaseApi.gamePredictSocket.uri());
            serverHttp = bundle.getString(CommonConst.SERVERHTTP);
            Log.d("LPHomeActivity", "serverHost==" + serverHost + "  serverHttp==" + serverHttp);
            if (!TextUtils.isEmpty(serverHttp)) {
                TjrBaseApi.gamePredictHttp.setUri(serverHttp);
                VHttpServiceManager.getInstance().resetPredictGameService();
            }
            tips = bundle.getString(CommonConst.TIPS, "");
            rulesUrl = bundle.getString(CommonConst.RULESURL, "");
            statementUrl = bundle.getString(CommonConst.STATEMENTURL, "");
        }
        if (TextUtils.isEmpty(serverHost)) {
            CommonUtil.showmessage("参数错误", LPHomeActivity.this);
            finish();
            return;
        }
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        gridLayoutManagerAbility = new GridLayoutManager(this, 1);
        horizontalNumList.setLayoutManager(gridLayoutManagerAbility);
        horizontalAbilityNumAdapter = new HorizontalAbilityNumAdapter(this);
        horizontalNumList.setAdapter(horizontalAbilityNumAdapter);
        horizontalAbilityNumAdapter.setNumData("0");


        gridLayoutManagerHistory = new GridLayoutManager(this, 3);
        horizontalHistoryList.setLayoutManager(gridLayoutManagerHistory);
        horizontalHistoryAdapter = new HorizontalHistoryAdapter(this);
        horizontalHistoryList.setAdapter(horizontalHistoryAdapter);

        rvCommentList.setLayoutManager(new LinearLayoutManager(this));
        rvCommentList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.transparent), 5));
        commentAdapter = new CommentAdapter(this);
        rvCommentList.setAdapter(commentAdapter);

        tvPredictionSurpass.setOnClickListener(this);
        tvPredictionNotSurpass.setOnClickListener(this);
        tvMenu.setOnClickListener(this);
        llSendComment.setOnClickListener(this);
        tvSymbol.setOnClickListener(this);
        messageHandler.post(new Runnable() {
            @Override
            public void run() {
                gameSeverConnect = new GameSeverConnect(getUserIdLong(), serverHost, messageHandler);
//                gameSeverConnect = new GameSeverConnect(getUserIdLong(), "192.168.1.68", messageHandler);
                gameSeverConnect.start();
            }

        });
    }

    @Override
    protected void onDestroy() {
        if (gameSeverConnect != null) gameSeverConnect.shutBootstrap();
//        if (stopTimer != null) {
//            stopTimer.cancel();
//            stopTimer = null;
//        }
//        if (resultTimer != null) {
//            resultTimer.cancel();
//            resultTimer = null;
//        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPredictionSurpass:
                showPredictionDialog(1, holdAmount, tips, kbtTickets, preGame);
                break;
            case R.id.tvPredictionNotSurpass:
                showPredictionDialog(-1, holdAmount, tips, kbtTickets, preGame);
                break;
            case R.id.llSendComment:
                showSendCommentDialogFragment();
                break;
            case R.id.tvMenu:
                showPopupMenu(tvMenu);
                break;
            case R.id.tvKbtRules:
                dimissPop();
                if (!TextUtils.isEmpty(rulesUrl))
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(LPHomeActivity.this, rulesUrl);
                break;
//            case R.id.tvKbtDisclaimer:
//                dimissPop();
//                if (!TextUtils.isEmpty(statementUrl))
//                    CommonWebViewActivity.pageJumpCommonWebViewActivity(LPHomeActivity.this, statementUrl);
//                break;
            case R.id.tvHistory:
                dimissPop();
                PageJumpUtil.pageJump(LPHomeActivity.this, LpHistoryActivity.class);
                break;
            case R.id.tvSymbol:
                if (preGame != null) {
                    MarketActivity.pageJump(LPHomeActivity.this, preGame.symbol);
                }
                break;
        }
    }


    PopupWindow pop;

    private void showPopupMenu(View parent) {
        if (pop == null) {
            View view = InflaterUtils.inflateView(this, R.layout.lp_menu);
            TextView tvKbtRules = view.findViewById(R.id.tvKbtRules);
//            TextView tvKbtDisclaimer = view.findViewById(R.id.tvKbtDisclaimer);
            TextView tvHistory = view.findViewById(R.id.tvHistory);
            tvKbtRules.setOnClickListener(this);
//            tvKbtDisclaimer.setOnClickListener(this);
            tvHistory.setOnClickListener(this);
            pop = new PopupWindow(view, DensityUtil.dip2px(this, 140), ViewGroup.LayoutParams.WRAP_CONTENT);//
            pop.setOutsideTouchable(false);
            pop.setFocusable(false);//
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));// 特别留意这个东东
//            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                }
//            });
        }
        if (pop != null && !pop.isShowing()) {
//            pop.showAsDropDown(parent);
            pop.showAsDropDown(parent, 20, -20);
//            pop.showAtLocation(parent,Gravity.CENTER,50,50);
        }
    }

    private void dimissPop() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }


    private void showPredictionDialog(int type, String holdAmount,  String tips, Group<KbtTicket> kbtTickets, PreGame preGame) {
        predictionDialog = new PredictionDialog(this, type, holdAmount,  tips, kbtTickets, preGame) {
            @Override
            public void onclickVote(String price, int vote) {
                int result = gameSeverConnect.sendText(SubPushHttp.getInstance().predictVote(getUserIdLong(), price, vote));
                Log.d("LPHomeActivity", "predictVote result==" + result + "  getUserIdLong==" + getUserIdLong());

            }
        };
        predictionDialog.setCanceledOnTouchOutside(true);
        predictionDialog.show();
    }


    private void showSendCommentDialogFragment() {
        sendCommentDialogFragment = new SendCommentDialogFragment();
        sendCommentDialogFragment.setOnSendCommentCallBack(new SendCommentDialogFragment.OnSendCommentCallBack() {
            @Override
            public void onDialogDismiss() {
                sendCommentDialogFragment.dismiss();
            }

            @Override
            public void onSendComment(String comment) {
                int result = gameSeverConnect.sendText(SubPushHttp.getInstance().predictChat(getUserIdLong(), comment));
                Log.d("LPHomeActivity", "predictChat result==" + result + "  getUserIdLong==" + getUserIdLong());
                if (result == 1) {
                    CommonUtil.showmessage("评论成功", LPHomeActivity.this);
                    messageHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (sendCommentDialogFragment != null)
                                sendCommentDialogFragment.dismiss();
                        }
                    }, 300);

                } else {

                }
            }
        });
        sendCommentDialogFragment.show(getSupportFragmentManager(), "");
    }


    private void setPreGame20000(PreGame preGame) {
        if (preGame != null) {
            tvSymbol.setText(preGame.symbol + "走势>>");
            String tolAbilityValue = String.valueOf(preGame.tolAbilityValue);
//            String tolAbilityValue=String.valueOf(new Random().nextInt(100));
//            Log.d("setPreGame20000","tolAbilityValue=="+tolAbilityValue);
            gridLayoutManagerAbility.setSpanCount(Math.max(tolAbilityValue.length(), 1));
            horizontalAbilityNumAdapter.setNumData(tolAbilityValue);

            if (preGame.tolAbilityValue > 0) {

                int progress = (int) (preGame.tolAbilityHigh * 1.0 / preGame.tolAbilityValue * 100);
                pb.setMax(100);
                pb.setProgress(progress);
                Log.d("setPreGame20000", "progress==" + progress + "  preGame.tolAbilityHigh==" + preGame.tolAbilityHigh + "  preGame.tolAbilityValue==" + preGame.tolAbilityValue);

                String rateHigh = StockChartUtil.formatNumber(1, preGame.tolAbilityHigh * 1.0 / preGame.tolAbilityValue * 100);
                String rateLow = StockChartUtil.formatNumber(1, 100.0 - Double.parseDouble(rateHigh));
                Log.d("setPreGame20000", "rateHigh==" + rateHigh + "  rateLow==" + rateLow);


                tvPbTextSurpass.setText("超过" + rateHigh + "%");
                tvPbTextNotSurpass.setText("不超过" + rateLow + "%");
            } else {
                pb.setMax(100);
                pb.setProgress(0);
                tvPbTextSurpass.setText("超过0.0%");
                tvPbTextNotSurpass.setText("不超过0.0%");
            }


            if (preGame.hisVotes != null) {
                gridLayoutManagerHistory.setSpanCount(Math.max(preGame.hisVotes.size(), 1));
                horizontalHistoryAdapter.setNumData(preGame.hisVotes);
            }
            tvVotePrice.setText("预测「" + preGame.symbol + "」在" + DateUtils.getStringDateOfString2(preGame.endTime, DateUtils.TEMPLATE_HHmmss) + " 是否超过" + preGame.votePrice);

            if (preGame.isVote && preGame.myVote == 0) {//可以预测
                llBtn.setVisibility(View.VISIBLE);
                llResult.setVisibility(View.GONE);
                setStopTimer(DateUtils.getDifference(preGame.midTime));

            } else {
                llBtn.setVisibility(View.GONE);
                llResult.setVisibility(View.VISIBLE);
                setResultTimer(DateUtils.getDifference(preGame.endTime));
//                tvResultTime.setText(getTime(preGame.endTime));
                if (preGame.myVote == 0) {
                    tvMyVote.setText("暂未预测");
                } else if (preGame.myVote == 1) {
                    tvMyVote.setText("超过");
                } else {
                    tvMyVote.setText("不超");
                }
                tvMyVote.setTextColor(StockChartUtil.getRateTextColor(this, preGame.myVote));

                if (predictionDialog != null && predictionDialog.isShowing()) {
                    predictionDialog.dismiss();
                    CommonUtil.showmessage("本场预测结束,请等待下一场", LPHomeActivity.this);
                }
            }

            if (predictionDialog != null && predictionDialog.isShowing()) {
                predictionDialog.updatePregame(preGame);
            }
        }
    }

//    CountDownTimer stopTimer;
//    boolean stopTimerFlag;

    private void setStopTimer(long millisInFuture) {
//        if (stopTimerFlag) return;
//        stopTimer = new CountDownTimer(millisInFuture, 1000) {
//            @Override
//            public void onTick(long l) {
//                stopTimerFlag = true;
//                String[] time = VeDate.formatSecToTime(l / 1000);
//                tvStopTime.setText(time[1] + ":" + time[2] + ":" + time[3]);
//            }
//
//            @Override
//            public void onFinish() {
//                stopTimerFlag = false;
//        if(stopTimer!=null){
//                stopTimer.cancel();
//                stopTimer = null;
//        }

//            }
//        }.start();
    }

//    CountDownTimer resultTimer;
//    boolean resultTimerFlag;

    private void setResultTimer(long millisInFuture) {
//        if (resultTimerFlag) return;
//        resultTimer = new CountDownTimer(millisInFuture, 1000) {
//            @Override
//            public void onTick(long l) {
//                resultTimerFlag = true;
//                String[] time = VeDate.formatSecToTime(l / 1000);
//                tvResultTime.setText(time[1] + ":" + time[2] + ":" + time[3]);
//            }
//
//            @Override
//            public void onFinish() {
//                resultTimerFlag = false;
//        if(resultTimer!=null){
//        resultTimer.cancel();
//        resultTimer = null;
//        }

//            }
//        }.start();
    }


//    private String getTime(String dateString) {
//        String[] time = VeDate.formatSecToTime();
//        return time[1] + ":" + time[2] + ":" + time[3];
//    }

//    private String getTime(int maxIndex, int currIndex) {
//
//        int seconds = maxIndex - currIndex;
//        String m = "00", s = "00";
//        int minute = seconds / 60;
//        if (minute < 10) {
//            m = "0" + minute;
//        } else {
//            m = String.valueOf(minute);
//        }
//        int second = seconds % 60;
//        if (second < 10) {
//            s = "0" + second;
//        } else {
//            s = String.valueOf(second);
//        }
//        return m + ":" + s;
//    }

    private void setValue20001() {
        tvMyPower.setText("我的YYB： " + holdAmount);
    }


    TjrBaseDialog goBuyKbtDialog;

    private void showGoBuyKbtDialog(String msg, final String symbol) {
        goBuyKbtDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                TradeActivity.pageJump(LPHomeActivity.this, symbol, 1);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        goBuyKbtDialog.setMessage(msg);
        goBuyKbtDialog.setBtnOkText("去购买");
        goBuyKbtDialog.setBtnColseText("取消");
        goBuyKbtDialog.setTitleVisibility(View.GONE);
        goBuyKbtDialog.show();
    }

}
