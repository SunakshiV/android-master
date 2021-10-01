package com.procoin.module.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.module.home.HomeActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.VeDate;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WechatQRCodeFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvSaveQr)
    TextView tvSaveQr;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    private TJRBaseToolBarActivity mActivity;

    private TjrImageLoaderUtil tjrImageLoaderUtil;


    private String qrUrl = "";
    private String title = "";
    private String subTitle = "";


    /**
     * 非摘单 入参
     *
     * @return
     */
    public static WechatQRCodeFragment newInstance(String title, String subTitle, String qrUrl) {
        WechatQRCodeFragment dialog = new WechatQRCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("subTitle", subTitle);
        bundle.putString(CommonConst.URLS, qrUrl);
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
        //入参处理
        Bundle b = getArguments();
        if (null == b) return;

    }

    @Override
    public void onStart() {
        CommonUtil.LogLa(2, "OLStarHomeDialogFragment                      ---> onStart ");
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreateView ");
        View v = inflater.inflate(R.layout.wechat_qr_code_dialog, container, false);

        tjrImageLoaderUtil = new TjrImageLoaderUtil();

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.URLS)) {
                title = bundle.getString("title", "");
                subTitle = bundle.getString("subTitle", "");
                qrUrl = bundle.getString(CommonConst.URLS);
            }
        }
        ButterKnife.bind(this, v);
        tvTitle.setText(title);
        tvSubTitle.setText(subTitle);
        tjrImageLoaderUtil.displayImage(qrUrl, ivQr);
        tvSaveQr.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSaveQr:
                saveToSdcard();
                break;

            default:
                break;
        }
    }


    public boolean saveToSdcard() {
        // TODO remove 东西
        if (TextUtils.isEmpty(qrUrl)) return false;
        if (getActivity() == null) return false;
        File file = null;
        try {
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(qrUrl);
            if (bitmap == null) {
                CommonUtil.showmessage("没有获取到图片", getActivity());
                return false;
            }

            String fileName = VeDate.getyyyyMMddHHmmss(VeDate.getNow()) + ".png";
            MainApplication mainApplication = ((HomeActivity) getActivity()).getApplicationContext();
            file = mainApplication.getmDCIMRemoteResourceManager().getFile(fileName);
            mainApplication.getmDCIMRemoteResourceManager().writeFile(file, bitmap, false);
            if (mainApplication.isSDCard()) {
                // 最后通知图库更新
                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
            }
        } catch (Exception e) {
            CommonUtil.showmessage("保存图片出错", getActivity());
            return false;
        }
        CommonUtil.showmessage("保存图片到" + file.getParent(), getActivity());
        return true;
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


    public interface OnShareDialogCallBack {
        void onDialogDismiss();
    }
}
