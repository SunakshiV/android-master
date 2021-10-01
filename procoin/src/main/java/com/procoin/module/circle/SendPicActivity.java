package com.procoin.module.circle;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.R;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhengmj on 15-12-21.
 */
public class SendPicActivity extends TJRBaseToolBarSwipeBackActivity {
    private String uri;
    private PhotoView iv_photo;
    private TjrImageLoaderUtil mTjrImageLoaderUtil;

    private String buttonText;

    @Override
    protected int setLayoutId() {
        return R.layout.send_pic;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.picture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            uri = b.getString("uri");
            buttonText=b.getString(CommonConst.BUTTONTEXT);
            if (TextUtils.isEmpty(uri)) {
                CommonUtil.showmessage("参数错误", this);
                finish();
                return;
            }
        }
//        setContentView(R.layout.send_pic);
//        addCustomView();
        mTjrImageLoaderUtil = new TjrImageLoaderUtil();
        iv_photo = (PhotoView) findViewById(R.id.iv_photo);
        iv_photo.setOnDoubleTapListener(new TJRDoubleTapListener(iv_photo));
//        mTjrImageLoaderUtil.displayImage("file://" + Uri.parse(uri).getPath(), iv_photo);
        mTjrImageLoaderUtil.displayImage("file://" +uri, iv_photo);
        TextView tvSend = (TextView) findViewById(R.id.tvSend);
        if(!TextUtils.isEmpty(buttonText))tvSend.setText(buttonText);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0x258, getIntent());
                finish();

            }
        });

    }


//    private void addCustomView() {
//        View custom = InflaterUtils.inflateView(this, R.layout.send_pic_btn);
//        TextView tvSend = (TextView) custom.findViewById(R.id.tvSend);
//        if(!TextUtils.isEmpty(buttonText)){
//            tvSend.setText(buttonText);
//        }
//        tvSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setResult(0x258, getIntent());
//                finish();
//
//            }
//        });
//        ActionBar.LayoutParams mCustomViewLayoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
//        mCustomViewLayoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//        mCustomViewLayoutParams.setMargins(DensityUtil.dip2px(getApplicationContext(), 8), DensityUtil.dip2px(getApplicationContext(), 8), DensityUtil.dip2px(getApplicationContext(), 8), DensityUtil.dip2px(getApplicationContext(), 8));
//        mActionBar.setCustomView(custom, mCustomViewLayoutParams);
//    }


    class TJRDoubleTapListener implements GestureDetector.OnDoubleTapListener {

        private PhotoView photoView;

        public TJRDoubleTapListener(PhotoView photoView) {
            setPhotoViewAttacher(photoView);
        }

        public void setPhotoViewAttacher(PhotoView photoView) {
            this.photoView = photoView;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            try {
                if (mActionBar.isShowing()) {
                    mActionBar.hide();
                } else {
                    mActionBar.show();

                }
            } catch (Exception e1) {
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            try {
                float scale = photoView.getScale();
                float x = ev.getX();
                float y = ev.getY();

                if (scale < photoView.getMediumScale()) {
                    photoView.setScale(photoView.getMaximumScale(), x, y, true);
                } else {
                    photoView.setScale(photoView.getMinimumScale(), x, y, true);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
            } catch (IllegalArgumentException ex) {

            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

}
