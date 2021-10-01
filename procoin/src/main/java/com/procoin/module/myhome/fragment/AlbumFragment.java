package com.procoin.module.myhome.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.module.myhome.entity.PhotoEntity;
import com.procoin.util.InflaterUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.R;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by zhengmj on 18-11-21.
 */

public class AlbumFragment extends UserBaseFragment {
    private View view;
    private PhotoEntity entity;
    private OncePhotoTap tap;
    private TjrImageLoaderUtil mTjrImageLoaderUtil ;
    public static AlbumFragment newInstance(PhotoEntity entity,OncePhotoTap tap) {
        AlbumFragment fragment = new AlbumFragment();
        fragment.entity = entity;
        fragment.tap = tap;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = InflaterUtils.inflateView(getActivity(), R.layout.viewpager_photoview_item);
        mTjrImageLoaderUtil = new TjrImageLoaderUtil();
        PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_photo);
        photoView.setOnDoubleTapListener(new TJRDoubleTapListener(photoView));

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);

        mTjrImageLoaderUtil.displayImageWithListener("file://" + entity.path, photoView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//				progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
                Log.d("200","FailReason == "+failReason.getCause());
                if (failReason.getCause() instanceof OutOfMemoryError){

                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        });
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
//            if(pageType!=0){
//                try {
//                    ActionBar bar=((TJRBaseToolBarActivity)getActivity()).getSupportActionBar();
//                    if(bar.isShowing()){
//                        bar.hide();
//                    }else{
//                        bar.show();
//                    }
//                } catch (Exception e1) {
//                }
//            }else{
//                if(getActivity()!=null){
//                    getActivity().finish();
//                }
//            }
            if (tap!=null)tap.onceTap();
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
    public interface OncePhotoTap{
        void onceTap();
    }
}
