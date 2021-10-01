package com.procoin.module.myhome.fragment;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.util.InflaterUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.R;
import com.procoin.module.myhome.entity.ImageSelectGroup;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhengmj on 18-11-21.
 */

public class AlbumFragment2 extends UserBaseFragment {
    private View view;
    private ImageSelectGroup entity;
    private TjrImageLoaderUtil mTjrImageLoaderUtil;
    private DisplayImageOptions imageOptions;

    public static AlbumFragment2 newInstance(ImageSelectGroup entity, TjrImageLoaderUtil mTjrImageLoaderUtil, DisplayImageOptions imageOptions) {
        AlbumFragment2 fragment = new AlbumFragment2();
        fragment.entity = entity;
        fragment.mTjrImageLoaderUtil = mTjrImageLoaderUtil;
        fragment.imageOptions = imageOptions;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = InflaterUtils.inflateView(getActivity(), R.layout.viewpager_photoview_item);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_photo);
        photoView.setOnDoubleTapListener(new TJRDoubleTapListener(photoView));

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        mTjrImageLoaderUtil.displayImage("file://" + entity.getPathStr(), photoView, imageOptions);

        return view;
    }

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
                ActionBar bar = ((TJRBaseToolBarActivity) getActivity()).getSupportActionBar();
                if (bar.isShowing()) {
                    bar.hide();
                } else {
                    bar.show();
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
