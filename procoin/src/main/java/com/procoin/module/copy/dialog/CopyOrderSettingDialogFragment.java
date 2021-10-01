package com.procoin.module.copy.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.R;
import com.procoin.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CopyOrderSettingDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.etMaxMoney)
    EditText etMaxMoney;
    @BindView(R.id.tvStopWin)
    TextView tvStopWin;
    @BindView(R.id.sbStopWin)
    SeekBar sbStopWin;
    @BindView(R.id.tvStopLoss)
    TextView tvStopLoss;
    @BindView(R.id.sbStopLoss)
    SeekBar sbStopLoss;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private TJRBaseToolBarActivity mActivity;

    private static final int decimalCount = 4;//小数点数量,金额小数点2位，数量小数点是4位.


    private double atMaxCash;

    private double maxCopyBalance;

    private OnSettingDialogCallBack onSettingDialogCallBack;

    public void setOnSettingDialogCallBack(OnSettingDialogCallBack onSettingDialogCallBack) {
        this.onSettingDialogCallBack = onSettingDialogCallBack;
    }

    /**
     * @return
     */
    public static CopyOrderSettingDialogFragment newInstance(double stopWin, double stopLoss, double maxCopyBalance) {
        CopyOrderSettingDialogFragment dialog = new CopyOrderSettingDialogFragment();
//        dialog.user = user;
        Bundle bundle = new Bundle();
        bundle.putDouble("stopWin", stopWin);
        bundle.putDouble("stopLoss", stopLoss);
        bundle.putDouble("maxCopyBalance", maxCopyBalance);


        dialog.setArguments(bundle);
        return dialog;
    }

    public void showDialog(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }

    @Override
    public void onAttach(Activity mActivity) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onAttach ");
        super.onAttach(mActivity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreate ");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        mActivity = (TJRBaseToolBarActivity) getActivity();

    }

    @Override
    public void onStart() {
        CommonUtil.LogLa(2, "OLStarHomeDialogFragment                      ---> onStart ");
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreateView ");
        View v = inflater.inflate(R.layout.dialog_copy_setting, container, false);

        ButterKnife.bind(this, v);

        //入参处理
        Bundle b = getArguments();
        double stopWin = b.getDouble("stopWin", 0);
        double stopLoss = b.getDouble("stopLoss", 0);
        maxCopyBalance = b.getDouble("maxCopyBalance", 0);

        etMaxMoney.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > decimalCount) {//最多2位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
                atMaxCash = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    atMaxCash = Double.parseDouble(s.toString());
                } else {
                    atMaxCash = 0;
                }

            }
        });


        sbStopWin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvSetStopWin.setText("止盈: " + progress + "%");
                if (progress == 0) {
                    tvStopWin.setText("止盈: " + "无设置");
                } else {
                    tvStopWin.setText("止盈: " + progress + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbStopLoss.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvSetStopLoss.setText("止损: " + progress + "%");
                if (progress == 0) {
                    tvStopLoss.setText("止损: " + "无设置");
                } else {
                    tvStopLoss.setText("止损: " + progress + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        etMaxMoney.setText(String.valueOf(maxCopyBalance));
        etMaxMoney.setSelection(etMaxMoney.getText().length());
        sbStopWin.setProgress((int) (stopWin * 100));
        sbStopLoss.setProgress((int) (stopLoss * 100));

        tvSubmit.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                if (atMaxCash == 0) {
                    atMaxCash = maxCopyBalance;
//                    CommonUtil.showmessage("请输入最大金额", getActivity());
//                    return;
                }
                colseKeybord();
                if (onSettingDialogCallBack != null) {
                    onSettingDialogCallBack.onSubmit(atMaxCash, (double) sbStopWin.getProgress() / 100, (double) sbStopLoss.getProgress() / 100);
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->            onResume   isVisible = " + getUserVisibleHint());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->           setUserVisibleHint  isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public interface OnSettingDialogCallBack {
        void onSubmit(double atMaxCash, double stopWin, double stopLoss);
    }
}
