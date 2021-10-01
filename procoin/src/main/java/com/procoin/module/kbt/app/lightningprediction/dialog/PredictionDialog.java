package com.procoin.module.kbt.app.lightningprediction.dialog;

import android.content.Context;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.module.kbt.app.lightningprediction.adapter.KbtTicketAdapter;
import com.procoin.module.kbt.app.lightningprediction.entity.KbtTicket;
import com.procoin.module.kbt.app.lightningprediction.entity.PreGame;
import com.procoin.util.DensityUtil;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.widget.dialog.base.AbstractBaseDialog;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class PredictionDialog extends AbstractBaseDialog {


//    @BindView(R.id.tvKBTBalance)
//    TextView tvKBTBalance;
    @BindView(R.id.tvTop)
    TextView tvTop;
    @BindView(R.id.btnPrediction)
    TextView btnPrediction;
    @BindView(R.id.llTopBg)
    LinearLayout llTopBg;
    @BindView(R.id.rvTickets)
    RecyclerView rvTickets;
    @BindView(R.id.tvMyAbility)
    TextView tvMyAbility;
    @BindView(R.id.tvResult)
    TextView tvResult;
    @BindView(R.id.ivMark)
    AppCompatImageView ivMark;


    private int type = 1;//-1不超 1超

    private String holdAmount = "0.00";//我的YYB数量
//    private String abilityValue = "0.00";//我的刻豆数量
    private String tips = "";//问号弹出框内容
    private Group<KbtTicket> kbtTickets;
    private PreGame preGame;

    private KbtTicketAdapter kbtTicketAdapter;

    private Context context;

    public PredictionDialog(Context context, int type, String holdAmount, String tips, Group<KbtTicket> kbtTickets, PreGame preGame) {
        super(context);
        this.context = context;
        this.type = type;
        this.holdAmount = holdAmount;
//        this.abilityValue = abilityValue;
        this.tips=tips;
        this.kbtTickets = kbtTickets;
        this.preGame = preGame;
        initTheme(context);
    }

    //    private void selected(int pos) {
//        if (pos == 0) {
//            llTicket1.setSelected(true);
//            llTicket2.setSelected(false);
//            llTicket3.setSelected(false);
//        } else if (pos == 1) {
//            llTicket1.setSelected(false);
//            llTicket2.setSelected(true);
//            llTicket3.setSelected(false);
//        } else {
//            llTicket1.setSelected(false);
//            llTicket2.setSelected(false);
//            llTicket3.setSelected(true);
//        }
//    }
    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            outRect.left = DensityUtil.dip2px(context, 5);
            outRect.right = DensityUtil.dip2px(context, 5);
//            outRect.top = DensityUtil.dip2px(context, 5);
//            outRect.bottom = DensityUtil.dip2px(context, 5);


        }
    }

    public void setResult() {
        if (preGame != null) {
            KbtTicket kbtTicket = kbtTicketAdapter.getSelectedTicket();
            if (kbtTicket != null) {
                double result = 0;
                if (type == 1) {
                    result = (Double) (preGame.tolAbilityLow / (preGame.tolAbilityHigh + Double.parseDouble(kbtTicket.price))) * Double.parseDouble(kbtTicket.price) + Double.parseDouble(kbtTicket.price);
                } else {
                    result = (Double) (preGame.tolAbilityHigh / (preGame.tolAbilityLow + Double.parseDouble(kbtTicket.price))) * Double.parseDouble(kbtTicket.price) + Double.parseDouble(kbtTicket.price);
                }
                tvResult.setText("猜对预计可得" + (long) result + "YYB，结果以竞猜结束时为准，仅供参考。");
            }

        }
    }

    public void updatePregame(PreGame preGame) {
        this.preGame = preGame;
        setResult();
    }

    private void initTheme(Context context) {
        OnClick onClick = new OnClick();
        this.setContentView(R.layout.prediction_dialog);
        ButterKnife.bind(this);

        rvTickets.addItemDecoration(new SpaceItemDecoration());
        kbtTicketAdapter = new KbtTicketAdapter(context, type);
        rvTickets.setLayoutManager(new GridLayoutManager(context, 3));
        rvTickets.setAdapter(kbtTicketAdapter);
        kbtTicketAdapter.setGroup(kbtTickets);
        kbtTicketAdapter.setCallBack(new KbtTicketAdapter.CallBack() {
            @Override
            public void onTicketSelected() {
                setResult();
            }
        });
//
//        llTicket1.setOnClickListener(onClick);
//        llTicket2.setOnClickListener(onClick);
//        llTicket3.setOnClickListener(onClick);
//        selected(0);
        if (type == 1) {
            tvTop.setText("超过");
            tvTop.setBackgroundResource(R.drawable.ic_not_surpass_top_bg);
            llTopBg.setBackgroundResource(R.drawable.shape_lp_not_surpass_bg);

            btnPrediction.setText("预测超过");
            btnPrediction.setBackgroundResource(R.drawable.shape_lp_not_surpass_btn_bg);
            ivMark.setImageResource(R.drawable.ic_svg_question_mark2);
        } else {
            tvTop.setText("不超");
            tvTop.setBackgroundResource(R.drawable.ic_surpass_top_bg);
            llTopBg.setBackgroundResource(R.drawable.shape_lp_surpass_bg);

            btnPrediction.setText("预测不超");
            btnPrediction.setBackgroundResource(R.drawable.shape_lp_surpass_btn_bg);
            ivMark.setImageResource(R.drawable.ic_svg_question_mark);


        }
        tvMyAbility.setText("我的YYB: " + holdAmount);
//        tvKBTBalance.setText("我的刻豆: " + abilityValue);
        btnPrediction.setOnClickListener(onClick);

        ivMark.setOnClickListener(onClick);


        setResult();

    }

    @Override
    public void onclickOk() {

    }

    @Override
    public void onclickClose() {

    }

    @Override
    public void setDownProgress(int progress) {

    }

    /**
     * 确定按钮
     */
    public abstract void onclickVote(String price, int vote);


    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnPrediction:
                    KbtTicket kbtTicket = kbtTicketAdapter.getSelectedTicket();
//                    if (Double.parseDouble(kbtTicket.price) > Double.parseDouble(holdAmount)) {
//                        CommonUtil.showmessage("可用KBT数量不足", context);
//                        return;
//                    }
                    if (kbtTicket != null) {
                        onclickVote(kbtTicket.price, type);
                        dismiss();
                    }

                    break;
                case R.id.ivMark:
                    showQuestionMarkDialog(tips);
                    break;

            }
//            if (v.getId() == R.id.btnOk) {
//                onclickOk();
//            } else if (v.getId() == R.id.btnClose) {
//                onclickClose();
//            }
        }

    }


    TjrBaseDialog questionMarkDialog;

    private void showQuestionMarkDialog(String tips) {
        questionMarkDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        questionMarkDialog.setTitleVisibility(View.GONE);
        questionMarkDialog.setBtnColseVisibility(View.GONE);
        questionMarkDialog.setMessage(tips);
        questionMarkDialog.setBtnOkText("知道了");
        questionMarkDialog.show();
    }

}
