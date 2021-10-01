package com.procoin.module.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.http.model.User;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.util.CommonUtil;
import com.procoin.widgets.CircleImageView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShareDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.llPic)
    LinearLayout llPic;

    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    @BindView(R.id.tvInvite)
    TextView tvInvite;

    @BindView(R.id.ivPic)
    ImageView ivPic;

    @BindView(R.id.ll_wechat_share)
    LinearLayout llWechatShare;
    @BindView(R.id.ll_wechatC_share)
    LinearLayout llWechatCShare;
    @BindView(R.id.ll_qq_share)
    LinearLayout llQqShare;
    @BindView(R.id.ll_zone_share)
    LinearLayout llZoneShare;
    @BindView(R.id.ll_sina_share)
    LinearLayout llSinaShare;
    @BindView(R.id.tvIam)
    TextView tvIam;
    @BindView(R.id.tvContent1)
    TextView tvContent1;
    @BindView(R.id.tvContent2)
    TextView tvContent2;


    private TJRBaseToolBarActivity mActivity;

    private TjrImageLoaderUtil tjrImageLoaderUtil;

    private OnShareDialogCallBack onShareDialogCallBack;

    private User user;
    private String shareUrl;


    public void setOnShareDialogCallBack(OnShareDialogCallBack onShareDialogCallBack) {
        this.onShareDialogCallBack = onShareDialogCallBack;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static ShareDialogFragment newInstance(User user, String shareUrl) {
        ShareDialogFragment dialog = new ShareDialogFragment();
        dialog.user = user;
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.URLS, shareUrl);
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
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreateView ");
        View v = inflater.inflate(R.layout.share_dialog, container, false);

        tjrImageLoaderUtil = new TjrImageLoaderUtil();

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.URLS)) {
                shareUrl = bundle.getString(CommonConst.URLS);
            }
        }
        ButterKnife.bind(this, v);
        Bitmap mBitmap = CodeUtils.createImage(shareUrl, 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
        ivQr.setImageBitmap(mBitmap);

        if (user != null) {
            if (getActivity() != null) {
                MainApplication mainApplication = (MainApplication) getActivity().getApplicationContext();
                if (mainApplication != null) {
                    if (mainApplication.getUser() != null && mainApplication.getUser().userId == user.userId) {//未登录可以从高手排行榜跳转到个人主页，也是可以分享的
                        tvIam.setText("我是");
                        tvContent1.setText("邀请好友，开启环球数字资产交易");
                        tvContent2.setText("");
                    } else {
                        tvIam.setText("TA是");
                        tvContent1.setText("我已在W.W.C.T一键跟上了TA的实盘交易");
                        tvContent2.setText("TA赚了多少 我就赚多少");
                    }

                    tvId.setText("ID:" + String.valueOf(user.getUserId()));
                    tvName.setText(user.getUserName());
                    tjrImageLoaderUtil.displayImageForHead(user.getHeadUrl(), ivHead);

                }
            }

        }
//        LinearLayout.LayoutParams lp_parent = (LinearLayout.LayoutParams) llPic.getLayoutParams();//1005*1338


        ivPic.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (ivPic.getWidth() != 0) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ivPic.getLayoutParams();//1005*1338
                    lp.height = ivPic.getWidth() * 892 / 670;
                    Log.d("ShareDialogFragment", "lp.width==" + lp.width);
                    Log.d("ShareDialogFragment", "lp.height==" + lp.height);
                    ivPic.setLayoutParams(lp);

                    ivPic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });


        tvCancel.setOnClickListener(this);
        tvInvite.setOnClickListener(this);
//        llWechatShare.setOnClickListener(this);
//        llWechatCShare.setOnClickListener(this);
//        llQqShare.setOnClickListener(this);
//        llZoneShare.setOnClickListener(this);
//        llSinaShare.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wechat_share:
//                share("com.tencent.mm");
//                break;
//            case R.id.ll_wechatC_share:
//                share("com.tencent.mm");
//                break;
//            case R.id.ll_qq_share:
//                share("com.tencent.mobileqq");
//                break;
//            case R.id.ll_zone_share:
//                share("com.tencent.mobileqq");
//                break;
//            case R.id.ll_sina_share:
//                share("com.sina.weibo");
//                break;
            case R.id.tv_cancel:
                if (onShareDialogCallBack != null) {
                    onShareDialogCallBack.onDialogDismiss();
                }

                break;
            case R.id.tvInvite:
                share();
                break;

            default:
                break;
        }
    }


    private void share() {
        try {
            File f = com.procoin.http.util.CommonUtil.saveBitmapToFile(((MainApplication) getActivity().getApplicationContext()).getRemoteResourceChatManager(), com.procoin.http.util.CommonUtil.viewConversionBitmap(llPic));
            Log.d("ShareDialogFragment", "file==" + f.getAbsolutePath());
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(getActivity(), "com.procoin.fileprovider", f);
            } else {
                uri = Uri.fromFile(f);
            }
            Intent imageIntent = new Intent(Intent.ACTION_SEND);
//            imageIntent.setPackage(pkg);
            imageIntent.setType("image/*");
            imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
            getActivity().startActivity(Intent.createChooser(imageIntent, "邀请好友"));
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtil.showmessage("分享失败", getActivity());
            Log.d("ShareDialogFragment", "e==" + e);
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


    public interface OnShareDialogCallBack {
        void onDialogDismiss();
    }
}
