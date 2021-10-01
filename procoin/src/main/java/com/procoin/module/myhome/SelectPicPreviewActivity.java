package com.procoin.module.myhome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.module.myhome.adapter.AlbumSelectAdapter2;
import com.procoin.module.myhome.entity.ImageSelectGroup;
import com.procoin.module.myhome.fragment.AlbumFragment2;
import com.procoin.util.DensityUtil;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.PhotoViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.procoin.http.base.Group;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.social.util.CommonUtil;
import com.procoin.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择图片的时候 预览图片
 */
public class SelectPicPreviewActivity extends TJRBaseToolBarSwipeBackActivity {
    @BindView(R.id.vp_photo)
    PhotoViewPager vpPhoto;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.ll_bot)
    LinearLayout llBot;
    @BindView(R.id.ckbSeclected)
    CheckBox ckbSeclected;

    private int defaultPos;
    private int maxPicCount;
    private boolean needCut;
    private boolean artwork=false;//是否原图
    private String className;
    private int type = 0; // 当一个页面有多个地方需要调用这个页面的话，返回的时候就用这个来区分
    private TjrImageLoaderUtil mTjrImageLoaderUtil;
    protected DisplayImageOptions imageOptions;
    private PicPagerAdapter mAdapter;
    private AlbumSelectAdapter2 selectAdapter;

    private Group<ImageSelectGroup> imgUrls;
    private Group<ImageSelectGroup> selectedGroups;

    @Override
    protected int setLayoutId() {
        return R.layout.select_pic_preview;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    public static void pageJumpThis(Context context, int defaultPos, int maxPicCount, String className, boolean needCut, int type,boolean artwork) {
        Bundle bunlde = new Bundle();
        bunlde.putInt(CommonConst.DEFAULTPOS, defaultPos);
        bunlde.putInt(CommonConst.MAXPICCOUNT, maxPicCount);
        bunlde.putString(CommonConst.CLASSNAME, className);
        bunlde.putBoolean(CommonConst.NEEDCUT, needCut);
        bunlde.putBoolean(CommonConst.ARTWORK, artwork);
        bunlde.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
        PageJumpUtil.pageJump(context, SelectPicPreviewActivity.class, bunlde);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        defaultPos = bundle.getInt(CommonConst.DEFAULTPOS);
        maxPicCount = bundle.getInt(CommonConst.MAXPICCOUNT);
        needCut = bundle.getBoolean(CommonConst.NEEDCUT);
        artwork = bundle.getBoolean(CommonConst.ARTWORK);
        className = bundle.getString(CommonConst.CLASSNAME, "");
        type = bundle.getInt(CommonConst.KEY_EXTRAS_TYPE);
        imgUrls = getApplicationContext().allGroups;
        selectedGroups = getApplicationContext().selectedGroup;
        if (imgUrls.size() == 0) {
            com.procoin.http.util.CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_common_mic)
                .showImageOnFail(R.drawable.ic_common_mic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        mActionBar.setTitle((defaultPos + 1) + "/" + imgUrls.size());
        mTjrImageLoaderUtil = new TjrImageLoaderUtil();

        mAdapter = new PicPagerAdapter(
                getSupportFragmentManager());
        vpPhoto.setAdapter(mAdapter);
        if (this.getIntent() == null) {
            finish();
            CommonUtil.showToast(this, "参数错误", Gravity.BOTTOM);
            return;
        }
        vpPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mActionBar.setTitle((arg0 + 1) + "/" + imgUrls.size());
                setItemselected(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        ckbSeclected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = vpPhoto.getCurrentItem();
                ImageSelectGroup imageSelectGroup = imgUrls.get(pos);
                if (isChecked) {
                    if (selectedGroups != null && !selectedGroups.contains(imageSelectGroup)) {
                        if (selectAdapter.getItemCount() >= maxPicCount) {
                            buttonView.setChecked(false);
                            com.procoin.util.CommonUtil.showmessage("当前最多添加" + maxPicCount + "张图片", SelectPicPreviewActivity.this);
                            return;
                        }
                        imageSelectGroup.setSelected(true);
                        selectAdapter.addItem(imageSelectGroup);
//                        selectAdapter.notifyDataSetChanged();
                        selectAdapter.notifyItemInserted(selectAdapter.getItemCount() - 1);
                        rvList.scrollToPosition(selectAdapter.getItemCount() - 1);

                    }
                } else {
                    if (selectedGroups != null && selectedGroups.contains(imageSelectGroup)) {
                        int index = selectAdapter.getIndex(imageSelectGroup.getPathStr());
                        if (selectAdapter.removeItem(imageSelectGroup)) {
                            if (index >= 0) {
                                selectAdapter.notifyItemRemoved(index);
                            }
                        }
//                        selectAdapter.notifyDataSetChanged();
                    }
                }
                setTvFinishText();

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvList.setLayoutManager(layoutManager);
        selectAdapter = new AlbumSelectAdapter2(this);
//        selectAdapter.setHasStableIds(true);
        rvList.setAdapter(selectAdapter);
        selectAdapter.setOnItemSelectListener(new AlbumSelectAdapter2.OnItemSelectListener() {
            @Override
            public void onItemSelect(int pos, ImageSelectGroup entity) {
                int pageIndex = getIndexOfViewPager(entity);
                if (pageIndex >= 0) {
                    vpPhoto.setCurrentItem(pageIndex, false);
                }
            }
        });
        selectAdapter.setGroup(selectedGroups);
        rvList.addItemDecoration(new SpaceItemDecoration());

        vpPhoto.setCurrentItem(defaultPos);
        setItemselected(defaultPos);
        setTvFinishText();
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGroups == null || selectedGroups.size() == 0) {
                    com.procoin.util.CommonUtil.showmessage("请选择图片", SelectPicPreviewActivity.this);
                    return;
                }
                if (maxPicCount == 1 && needCut) {
                    MyHomeCropActivity.jumpThis(SelectPicPreviewActivity.this, selectedGroups.get(0).getPathStr(), className, type);
                } else {
                    if (selectedGroups.size() >= 4) showProgressDialog();
                    ArrayList<String> list = new ArrayList<>();
                    for (ImageSelectGroup imageSelectGroup : selectedGroups) {
                        try {
                            if(artwork){
                                list.add(imageSelectGroup.getPathStr());
                                Log.d("selectPicPreview","原图 size =="+new File(imageSelectGroup.getPathStr()).length());
                            }else{
                                Bitmap bitmap = com.procoin.http.util.CommonUtil.getSmallBitmap(imageSelectGroup.getPathStr(), true);
                                File f = com.procoin.http.util.CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmap);
                                Log.d("selectPicPreview","非原图 size =="+f.length());
                                list.add(f.getAbsolutePath());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (selectedGroups.size() >= 4) dismissProgressDialog();
                    Intent intent = new Intent();
                    intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, type);
                    intent.setClassName(getPackageName(), className);
                    intent.putStringArrayListExtra(CommonConst.PICLIST, list);
                    PageJumpUtil.pageJump(SelectPicPreviewActivity.this, intent);
                }
//                if (getApplicationContext().selectedGroup != null) {
//                    getApplicationContext().selectedGroup.clear();
//                    getApplicationContext().selectedGroup = null;
//                }
            }
        });


    }

    private void setTvFinishText() {
        if (selectedGroups != null && selectedGroups.size() > 0) {
            tvFinish.setText("完成(" + selectedGroups.size() + ")");
        } else {
            tvFinish.setText("完成");
        }
    }


    private void setItemselected(int pos) {
        String path = imgUrls.get(pos).getPathStr();
        int index = selectAdapter.setSelected(path);
        if (index >= 0) {
            rvList.scrollToPosition(index);
            ckbSeclected.setChecked(true);
        } else {
            ckbSeclected.setChecked(false);
        }
        Log.d("vpPhoto", "path==" + path + "  pos==" + pos + "  index==" + index);
    }


    private int getIndexOfViewPager(ImageSelectGroup entity) {
        int index = -1;
        if (imgUrls != null && imgUrls.size() > 0) {
            for (int i = 0, m = imgUrls.size(); i < m; i++) {
                if (imgUrls.get(i).getPathStr().equals(entity.getPathStr())) {
                    index = i;
                    break;
                }
            }
        }
        return index;

    }

    @Override
    protected void onPause() {
        if (isFinishing()) {
            for (ImageSelectGroup imageSelectGroup : selectedGroups) {
                imageSelectGroup.setSelected(false);
            }
        }
        super.onPause();
    }

    class PicPagerAdapter extends FragmentPagerAdapter {

        public PicPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Log.d("PicPagerAdapter", "...........");
            return AlbumFragment2
                    .newInstance(imgUrls.get(arg0),
                            mTjrImageLoaderUtil, imageOptions);
        }

        @Override
        public int getCount() {
            return imgUrls.size();
        }

    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = DensityUtil.dip2px(SelectPicPreviewActivity.this, 7);
            outRect.right = DensityUtil.dip2px(SelectPicPreviewActivity.this, 7);
            outRect.top = DensityUtil.dip2px(SelectPicPreviewActivity.this, 7);
            outRect.bottom = DensityUtil.dip2px(SelectPicPreviewActivity.this, 7);

        }
    }

}
