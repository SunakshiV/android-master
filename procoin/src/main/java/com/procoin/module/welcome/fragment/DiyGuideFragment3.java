package com.procoin.module.welcome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.util.InflaterUtils;

public class DiyGuideFragment3 extends BaseWelComeFragment {
    private View view;
//    private LinearLayout lltext;
//    private RelativeLayout rlImage;
    private TextView btnOk;

    public static DiyGuideFragment3 newInstance() {
        DiyGuideFragment3 fragment = new DiyGuideFragment3();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = InflaterUtils.inflateView(getActivity(), R.layout.welcome_goldgame_nv_guide_3);
//        lltext = (LinearLayout) view.findViewById(R.id.lltext);
//        rlImage = (RelativeLayout) view.findViewById(R.id.rlImage);
//        btnOk = (TextView) view.findViewById(R.id.btnOk);
//        btnOk.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (getActivity() != null) {
//                    ((GuideActivity) getActivity()).jumptoPage();
//                }
//            }
//        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (lltext != null) {
//                if (left) {
//                    startleftAnim(false);
//                }
//                if (right) {
//                    startleftAnim(true);
//                }
//            }
//        } else {
//            if (rlImage != null) {
//                rlImage.setVisibility(View.INVISIBLE);
//            }
//            if (btnOk != null) {
//                btnOk.setVisibility(View.INVISIBLE);
//            }
//            if (lltext != null) {
//                lltext.setVisibility(View.INVISIBLE);
//            }
//        }
//    }
//
//    private void startleftAnim(final boolean right) {
//        if (lltext == null) return;
//        lltext.post(new Runnable() {
//
//            @Override
//            public void run() {
//                if (lltext == null) return;
//                rlImage.setVisibility(View.VISIBLE);
//                lltext.setVisibility(View.VISIBLE);
//
//                AnimatorSet showAnimaimgSetrl = new AnimatorSet();
//                Animator rlalphaAnimator = ObjectAnimator.ofFloat(rlImage, AnimatorCommonConst.ALPHA, 0, 1).setDuration(500);
//                Animator rltranslationyAnimator = ObjectAnimator.ofFloat(rlImage, AnimatorCommonConst.TRANSLATION_Y, 45f, 0f).setDuration(500);
//                Animator llalphaimgAnimator = ObjectAnimator.ofFloat(lltext, AnimatorCommonConst.ALPHA, 0, 1).setDuration(500);
//                Animator lltranslationyimgAnimator = ObjectAnimator.ofFloat(lltext, AnimatorCommonConst.TRANSLATION_Y, -45f, 0f).setDuration(500);
//                showAnimaimgSetrl.playTogether(rlalphaAnimator, rltranslationyAnimator, llalphaimgAnimator, lltranslationyimgAnimator);
//                showAnimaimgSetrl.start();
//
//
//                AnimatorSet showAnimaimgSet = new AnimatorSet();
//                Animator alphaimgAnimator = ObjectAnimator.ofFloat(btnOk, AnimatorCommonConst.ALPHA, 0, 1).setDuration(500);
//                alphaimgAnimator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animator) {
//                        btnOk.setVisibility(View.VISIBLE);
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animator) {
//
//                    }
//                });
//                showAnimaimgSet.playTogether(alphaimgAnimator);
//                showAnimaimgSet.setStartDelay(500);
//                showAnimaimgSet.start();
//
//            }
//        });
//    }

}
