///*
// * Copyright (C) 2011 The Android Open Source Project
// * Copyright (C) 2011 Jake Wharton
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.tjr.perval.widgets.indicator;
//
//import android.describes.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.HorizontalScrollView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.tjr.perval.R;
//
//import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
//import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//
///**
// * This widget implements the dynamic action bar tab behavior that can change
// * across different configurations or circumstances.
// */
//public class TabPageIndicatorText extends HorizontalScrollView {
//    /**
//     * Title text used when no title is provided by the adapter.
//     */
//    private static final CharSequence EMPTY_TITLE = "";
//
//    /**
//     * Interface for a callback when the selected tab has been reselected.
//     */
//    public interface OnTabReselectedListener {
//        /**
//         * Callback when the selected tab has been reselected.
//         *
//         * @param position Position of the current center item.
//         */
//        void onTabReselected(int position);
//    }
//
//    private Runnable mTabSelector;
//    public boolean isShowByname;//需要显示别名
//
//    private final OnClickListener mTabClickListener = new OnClickListener() {
//        public void onClick(View view) {
//            TabView tabView = (TabView) view;
//            final int newSelected = tabView.getIndex();
//            if (mSelectedTabIndex == newSelected) return;
//            setCurrentItem(newSelected);
//            if (mTabReselectedListener != null) {
//                mTabReselectedListener.onTabReselected(newSelected);
//            }
//        }
//    };
//
//    private final IcsLinearLayout mTabLayout;
//
//    private int mMaxTabWidth;
//    private int mSelectedTabIndex;
//
//    private OnTabReselectedListener mTabReselectedListener;
//
//
//    private Group<ProductName> productNames;
//
//    public void setProductNames(Group<ProductName> productNames) {
//        this.productNames = productNames;
//    }
//
//
//    public TabPageIndicatorText(Context context) {
//        this(context, null);
//    }
//
//    public TabPageIndicatorText(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setHorizontalScrollBarEnabled(false);
//
//        mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
//        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
//    }
//
//    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
//        mTabReselectedListener = listener;
//    }
//
//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
//        setFillViewport(lockedExpanded);
//
//        final int childCount = mTabLayout.getChildCount();
//        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
//            if (childCount > 2) {
//                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
//            } else {
//                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
//            }
//        } else {
//            mMaxTabWidth = -1;
//        }
////        CommonUtil.LogLa(2, "onMeasure is " + mMaxTabWidth);
//        final int oldWidth = getMeasuredWidth();
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        final int newWidth = getMeasuredWidth();
//
//        if (lockedExpanded && oldWidth != newWidth) {
//            // Recenter the tab display if we're at a new (scrollable) size.
//            setCurrentItem(mSelectedTabIndex);
//        }
//    }
//
//    private void animateToTab(final int position) {
//        final View tabView = mTabLayout.getChildAt(position);
//        if (mTabSelector != null) {
//            removeCallbacks(mTabSelector);
//        }
//        mTabSelector = new Runnable() {
//            public void run() {
//                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
//                smoothScrollTo(scrollPos, 0);
//                mTabSelector = null;
//            }
//        };
//        post(mTabSelector);
//    }
//
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (mTabSelector != null) {
//            // Re-post the selector we saved
//            post(mTabSelector);
//        }
//    }
//
//    @Override
//    public void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (mTabSelector != null) {
//            removeCallbacks(mTabSelector);
//        }
//    }
//
//    private void addTab(int index, CharSequence text, int iconResId) {
//        final TabView tabView = new TabView(getContext());
//        tabView.mIndex = index;
//        tabView.setFocusable(true);
//        tabView.setOnClickListener(mTabClickListener);
////        CommonUtil.LogLa(2, "tabView is " + text);
//        tabView.setText(text);
//
//        if (iconResId != 0) {
//            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
//        }
//
//        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
//
//    }
//
//    public void notifyDataSetChanged(int pos) {
//        mSelectedTabIndex = pos;
//        mTabLayout.removeAllViews();
//        for (int i = 0; i < productNames.size(); i++) {
//            ProductName productName = productNames.get(i);
//            addTab(i, isShowByname ? productName.byname : productName.jc, 0);
//        }
//        if (mSelectedTabIndex > productNames.size()) {
//            mSelectedTabIndex = productNames.size() - 1;
//        }
//        setCurrentItem(mSelectedTabIndex);
//        requestLayout();
//    }
//
//    public void notifyDataSetChanged() {
//        notifyDataSetChanged(0);
//    }
//
//    public void setCurrentItem(int item) {
//        mSelectedTabIndex = item;
//
//        final int tabCount = mTabLayout.getChildCount();
//        for (int i = 0; i < tabCount; i++) {
//            final View child = mTabLayout.getChildAt(i);
//            final boolean isSelected = (i == item);
//            child.setSelected(isSelected);
//            if (isSelected) {
//                animateToTab(item);
//            }
//        }
//    }
//
//
//    private class TabView extends TextView {
//        private int mIndex;
//
//        public TabView(Context context) {
//            super(context, null, R.attr.vpiTabPageIndicatorStyle);
//        }
//
//        @Override
//        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//            // Re-measure if we went beyond our maximum size.
////            CommonUtil.LogLa(2, "onMeasure is " + mMaxTabWidth + "getMeasuredWidth() is " + getMeasuredWidth());
//            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
//                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
//                        heightMeasureSpec);
//            }
//        }
//
//        public int getIndex() {
//            return mIndex;
//        }
//    }
//}
