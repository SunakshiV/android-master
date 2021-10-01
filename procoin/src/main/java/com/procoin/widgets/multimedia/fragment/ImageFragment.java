//package com.tjr.bee.widgets.multimedia.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import TjrImageLoaderUtil;
//import com.tjr.bee.R;
//import CommonConst;
//import ViewPagerPhotoViewActivity;
//import PageJumpUtil;
//
//import java.util.ArrayList;
//
///**
// * Created by kechenng on 17-6-7.
// */
//
//public class ImageFragment extends Fragment {
//
//    private ImageView imageView;
//    private TjrImageLoaderUtil imageLoader;
//    private Context context;
//    public HomeAd mediaEntity;
//    private ArrayList<HomeAd> mediaList;
//    private ArrayList<String> imageList;
//
//    private boolean jumpByParams;//false 代表直接跳图片浏览器,true代表根据pkg。cls。params跳转
//
//    public static ImageFragment newInstance() {
//        return new ImageFragment();
//    }
//
//    public ImageFragment setData(HomeAd mediaEntity, ArrayList<HomeAd> mediaList, boolean jumpByParams) {
//        this.mediaList = mediaList;
//        this.mediaEntity = mediaEntity;
//        this.jumpByParams = jumpByParams;
//        filtrateImage();
//        return this;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getContext();
//        imageLoader = new TjrImageLoaderUtil(R.drawable.ic_common_mic);
//    }
//
//    private void filtrateImage() {  //筛选图片
//        imageList = new ArrayList<>();
//        for (int i = 0; i < mediaList.size(); i++) {
//            HomeAd entity = mediaList.get(i);
//            if (entity.file_type == 0) {
//                imageList.add(entity.extend1);
//            }
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d("onCreateView","onCreateView/////");
//        imageView = new ImageView(context);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("card", "onClick");
//                if (jumpByParams) {
//                    if (mediaEntity != null)
//                        PageJumpUtil.jumpByPkg(getActivity(), mediaEntity.pkg, mediaEntity.cls, mediaEntity.params);
//                } else {
//                    if (mediaEntity == null) return;
//                    int currPosition = -1;
//                    for (int i = 0; i < imageList.size(); i++) {
//                        if (mediaEntity.extend1.equals(imageList.get(i))) {
//                            currPosition = i;
//                            break;
//                        }
//                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putStringArrayList("imageUrls", imageList);
//                    bundle.putInt(CommonConst.DEFAULTPOS, currPosition);
//                    bundle.putInt(CommonConst.PAGETYPE, 5);
//                    PageJumpUtil.pageJump(context, ViewPagerPhotoViewActivity.class, bundle);
//                }
//
//            }
//        });
//        return imageView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
////        Log.d("onResume","mediaEntity.extend1=="+mediaEntity.extend1);
//        if (mediaEntity != null) imageLoader.displayImage(mediaEntity.extend1, imageView);
//    }
//}
