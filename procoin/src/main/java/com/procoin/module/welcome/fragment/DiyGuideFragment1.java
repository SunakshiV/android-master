package com.procoin.module.welcome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.procoin.http.util.CommonUtil;
import com.procoin.R;
import com.procoin.util.InflaterUtils;

/**
 * 6.0的欢迎页3
 */
public class DiyGuideFragment1 extends BaseWelComeFragment {
    private View view;
//    private RelativeLayout rlImage;
//    private LinearLayout lltext;

    public static DiyGuideFragment1 newInstance() {
        DiyGuideFragment1 fragment = new DiyGuideFragment1();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "DiyGuideFragment2 onCreateView");
        view = InflaterUtils.inflateView(getActivity(), R.layout.welcome_goldgame_nv_guide_1);
//        rlImage = (RelativeLayout) view.findViewById(R.id.rlImage);
//        lltext = (LinearLayout) view.findViewById(R.id.lltext);
//
//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onGlobalLayout() {
//                startleftAnim(false);
//                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
//        CommonUtil.LogLa(2, "DiyGuideFragment2 setUserVisibleHint");
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
//                lltext.setVisibility(View.VISIBLE);
//                rlImage.setVisibility(View.VISIBLE);
//                AnimatorSet showAnimaimgSet = new AnimatorSet();
//                Animator alphaAnimator = ObjectAnimator.ofFloat(rlImage, AnimatorCommonConst.ALPHA, 0, 1).setDuration(500);
//                Animator translationyAnimator = ObjectAnimator.ofFloat(rlImage, AnimatorCommonConst.TRANSLATION_Y, 45f, 0f).setDuration(500);
//
//
//                Animator alphaimgAnimator = ObjectAnimator.ofFloat(lltext, AnimatorCommonConst.ALPHA, 0, 1).setDuration(500);
//                Animator translationyimgAnimator = ObjectAnimator.ofFloat(lltext, AnimatorCommonConst.TRANSLATION_Y, -45f, 0f).setDuration(500);
//
//                showAnimaimgSet.playTogether(alphaAnimator, translationyAnimator, alphaimgAnimator, translationyimgAnimator);
//                showAnimaimgSet.start();
//
//            }
//        });
//    }
}
