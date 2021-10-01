package com.procoin.common.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.util.DynamicPermission;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.PermissionUtils;
import com.procoin.widgets.PhotoViewPager;
import com.procoin.util.VeDate;
import com.procoin.widgets.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.procoin.http.model.User;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.social.util.CommonUtil;
import com.procoin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerPhotoViewActivity extends TJRBaseToolBarSwipeBackActivity {
    // private RelativeLayout rlAll;
    private PhotoViewPager veiePhotoView;
    // private RelativeLayout rlTitleBar;
    // private TextView tvTitle;
    // private SAutoBgButton btnBack;
    // private SAutoBgButton btnShare;
    private int defaultPos;
    /*
     * 0 查看头像大图 1 热点新闻 2 股友吧 3 私聊 4 微访谈 5.圈子
     */
    private int pageType;
    private Bundle mBundle;
    public final static String IMAGEURLS = "imageUrls";
    public final static String IMGSRC = "imgSrc";
    private final List<String> imgUrls = new ArrayList<String>();//大图
    private final List<String> imgUrlsMin = new ArrayList<String>();//小图
    //	private String picString;
//	private String imgSrc;
    private TjrImageLoaderUtil mTjrImageLoaderUtil;
    private PicPagerAdapter mAdapter;
    private User user;

    private AppCompatImageView ivSavePic;
    private DynamicPermission dynamicPermission;


    @Override
    protected int setLayoutId() {
        return R.layout.hot_news_image;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (this.getIntent().getExtras() != null) {
            mBundle = this.getIntent().getExtras();
            if (mBundle.containsKey(IMAGEURLS)) {
                imgUrls.addAll(mBundle.getStringArrayList(IMAGEURLS));
            }
            if (mBundle.containsKey(CommonConst.PICSTRING)) {
                parserPicString(mBundle.getString(CommonConst.PICSTRING));
            }
            if (mBundle.containsKey(CommonConst.DEFAULTPOS)) {
                defaultPos = mBundle.getInt(CommonConst.DEFAULTPOS);
            }
//			if (mBundle.containsKey(IMGSRC)) {
//				imgSrc = mBundle.getString(IMGSRC);
//			}
            if (mBundle.containsKey(CommonConst.PAGETYPE)) {
                pageType = mBundle.getInt(CommonConst.PAGETYPE);
            }
//            if (mBundle.containsKey(CommonConst.PARAMS)) parserJsonParams(mBundle);
            if (mBundle.containsKey(CommonConst.KEY_EXTRAS_USER_INFO)) {
                user = mBundle.getParcelable(CommonConst.KEY_EXTRAS_USER_INFO);
                imgUrls.add(user.getHeadUrl());
            }
            if (mBundle.containsKey(CommonConst.SINGLEPICSTRING)) {//单张图片
                String singlePicString = mBundle.getString(CommonConst.SINGLEPICSTRING);
                LogUtils.d("ViewPagerPhotoView", "singlePicString==+singlePicString");
                if (!TextUtils.isEmpty(singlePicString)) {
                    imgUrls.add(singlePicString);
                }
            }
        }
        if (imgUrls.size() == 0) {
            finish();
            com.procoin.http.util.CommonUtil.showmessage("参数错误", this);
            return;
        }
        super.onCreate(savedInstanceState);
        if (pageType == 0 ) {
            mActionBar.hide();
        } else {
            mActionBar.setTitle((defaultPos + 1) + "/" + imgUrls.size());
        }
//        setContentView(R.layout.hot_news_image);
        veiePhotoView = (PhotoViewPager) findViewById(R.id.photoViewpager);
        veiePhotoView.setOffscreenPageLimit(imgUrls.size() - 1);
        mTjrImageLoaderUtil = new TjrImageLoaderUtil();
        mAdapter = new PicPagerAdapter(
                getSupportFragmentManager());
        veiePhotoView.setAdapter(mAdapter);
        veiePhotoView.setCurrentItem(defaultPos);

        ivSavePic = (AppCompatImageView) findViewById(R.id.ivSavePic);
        ivSavePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDynamicPermission();
            }
        });


        // rlAll = (RelativeLayout) findViewById(R.id.rlAll);
        // rlTitleBar = (RelativeLayout) findViewById(R.id.rlTitleBar);
        // btnBack = (SAutoBgButton) findViewById(R.id.btnBack);
        // btnShare = (SAutoBgButton) findViewById(R.id.btnShare);
        // btnBack.setOnClickListener(onclick);
        // btnShare.setOnClickListener(onclick);
        // tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (this.getIntent() == null) {
            finish();
            CommonUtil.showToast(this, "参数错误", Gravity.BOTTOM);
            return;
        }
        // veiePhotoView = new ViewPagerPhotoViewUI(this,
        // this.getIntent().getExtras());
        // rlAll.addView(veiePhotoView.showView());
        // veiePhotoView.setheadBar(rlTitleBar);
        veiePhotoView.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mActionBar.setTitle((arg0 + 1) + "/" + imgUrls.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void checkDynamicPermission() {
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission(this, new DynamicPermission.RequestPermissionsCallBack() {
                @Override
                public void onRequestSuccess(String[] permissions, int requestCode) {
                    saveToSdcard();
                }

                @Override
                public void onRequestFail(String[] permissions, int requestCode) {
                }
            });
        }
        dynamicPermission.checkSelfPermission(PermissionUtils.EXTERNAL_STORAGE, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (dynamicPermission != null)
            dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*
         * 0 查看头像大图 1 热点新闻 2 股友吧 3 私聊 4 微访谈 5.圈子->聚会
         */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem menuItem_edit = menu.findItem(R.id.action_edit);
//        menuItem_edit.setVisible(pageType == 3);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.pic_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
//                checkDynamicPermission();
                break;
//            case R.id.action_edit:
//                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imgUrls.get(veiePhotoView.getCurrentItem()));
//                if (bitmap != null) {
//                    Bundle bundle = new Bundle();
//                    bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, CommonUtil.bitmapToBytes(bitmap));
//                    PageJumpUtil.pageJumpToData(ViewPagerPhotoViewActivity.this, ChatCanvasActivity.class, bundle);
//                    finish();
//                }
//                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean saveToSdcard() {
        // TODO remove 东西
        File file = null;
        try {
            String uri = imgUrls.get(veiePhotoView.getCurrentItem());
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri);
            if (bitmap == null) {
                CommonUtil.showToast(this, "没有获取到图片", Gravity.BOTTOM);
                return false;
            }

            String fileName = VeDate.getyyyyMMddHHmmss(VeDate.getNow()) + ".png";
            file = getApplicationContext().getmDCIMRemoteResourceManager().getFile(fileName);
            getApplicationContext().getmDCIMRemoteResourceManager().writeFile(file, bitmap, false);
            if ((getApplicationContext()).isSDCard()) {
                // 最后通知图库更新
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
            }
        } catch (Exception e) {
            CommonUtil.showToast(this, "保存图片出错", Gravity.BOTTOM);
            return false;
        }
        CommonUtil.showToast(this, "保存图片到" + file.getParent(), Gravity.BOTTOM);
        return true;
    }

    /**
     * 股友吧有用到大小图
     *
     * @param urlString
     */
    private void parserPicString(String urlString) {
        if (TextUtils.isEmpty(urlString)) {
            return;
        }
        try {
            JSONObject json = new JSONObject(urlString);
            if (JsonParserUtils.hasAndNotNull(json, "min") && JsonParserUtils.hasAndNotNull(json, "max")) {
                JSONArray jaMin = json.getJSONArray("min");
                JSONArray jaMax = json.getJSONArray("max");
                if (jaMin.length() == jaMax.length()) {
//					ImageEntity entity = null;
                    for (int i = 0; i < jaMax.length(); i++) {
//						entity = new ImageEntity(jaMax.getString(i), jaMax.getString(i));
//						entitys.add(entity);
                        imgUrls.add(jaMax.getString(i));
                        imgUrlsMin.add(jaMin.getString(i));
                    }
                }

            }
        } catch (JSONException e) {
        }
    }

    class PicPagerAdapter extends FragmentPagerAdapter {

        public PicPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return PicFragment
                    .newInstance(new ImageEntity(getMinPicByPos(arg0), imgUrls.get(arg0)),
                            mTjrImageLoaderUtil, pageType);
        }

        @Override
        public int getCount() {
            return imgUrls.size();
        }

    }

    public String getMinPicByPos(int pos) {
        if (pos > imgUrlsMin.size() - 1) {
            return "";
        }
        return imgUrlsMin.get(pos);
    }


}
