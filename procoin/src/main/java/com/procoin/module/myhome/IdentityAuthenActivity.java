package com.procoin.module.myhome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.ImageUtil;
import com.procoin.module.myhome.entity.IdentityAuthen;
import com.procoin.util.DynamicPermission;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.PermissionUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.procoin.http.retrofitservice.UploadFileUtils;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.R;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 实名认证页面
 */
public class IdentityAuthenActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etNo)
    EditText etNo;
    @BindView(R.id.ivFrontImg)
    ImageView ivFrontImg;
    @BindView(R.id.ivFrontTake)
    ImageView ivFrontTake;
    @BindView(R.id.ivBackImg)
    ImageView ivBackImg;
    @BindView(R.id.ivBackTake)
    ImageView ivBackTake;
    @BindView(R.id.ivHoldImg)
    ImageView ivHoldImg;
    @BindView(R.id.ivHoldTake)
    ImageView ivHoldTake;
    @BindView(R.id.tvComplete)
    TextView tvComplete;
    @BindView(R.id.tvState)
    TextView tvState;

    @BindView(R.id.tvViewDemoFrontImg)
    TextView tvViewDemoFrontImg;
    @BindView(R.id.tvViewDemoBackImg)
    TextView tvViewDemoBackImg;
    @BindView(R.id.tvViewHoldImgImg)
    TextView tvViewHoldImgImg;


    //这个3个才是上传的路径
    private String frontImgFile;
    private String backImgFile;
//    private String holdImgFile;

    //这个3个是拍照路径
    private String frontTake;
    private String backTake;
//    private String holdTake;


    private Call<ResponseBody> getIdentityAuthenCall;
    private Call<ResponseBody> uploadFrontCall;
    private Call<ResponseBody> uploadBackCall;
    private Call<ResponseBody> submitIdentityAuthenCall;
    private IdentityAuthen identityAuthen;

    private DynamicPermission dynamicPermission;
    private TjrImageLoaderUtil tjrImageLoaderUtil;

//    private ArrayList<String> viewDemoUrls;

    protected DisplayImageOptions imageOptions;//显示原图用到

    @Override
    protected int setLayoutId() {
        return R.layout.activity_identity_authen;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.identityAuthen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_common_mic)
                .showImageOnFail(R.drawable.ic_common_mic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ivFrontTake.setOnClickListener(this);
        ivBackTake.setOnClickListener(this);
        ivHoldTake.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
        tvViewDemoFrontImg.setOnClickListener(this);
        tvViewDemoBackImg.setOnClickListener(this);
        tvViewHoldImgImg.setOnClickListener(this);


        startGetIdentityAuthen();
    }

    private void startGetIdentityAuthen() {
        getIdentityAuthenCall = VHttpServiceManager.getInstance().getVService().getIdentityAuthen();
        getIdentityAuthenCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
//                    String frontImgDemoUrl = resultData.getItem("frontImgDemoUrl", String.class);
//                    String backImgDemoUrl = resultData.getItem("backImgDemoUrl", String.class);
//                    String holdImgDemoUrl = resultData.getItem("holdImgDemoUrl", String.class);
//                    if (!TextUtils.isEmpty(frontImgDemoUrl) && !TextUtils.isEmpty(backImgDemoUrl) && !TextUtils.isEmpty(holdImgDemoUrl)) {
//                        viewDemoUrls = new ArrayList<>();
//                        viewDemoUrls.add(frontImgDemoUrl);
//                        viewDemoUrls.add(backImgDemoUrl);
//                        viewDemoUrls.add(holdImgDemoUrl);
//                    }

                    identityAuthen = resultData.getObject("identityAuth", IdentityAuthen.class);

                    if (identityAuthen == null) {//还没有提交过
                        tvState.setVisibility(View.GONE);
                    } else {
                        setData();
                    }
                }

            }
        });
    }

    private void setData() {
        if (identityAuthen != null) {
            etName.setText(identityAuthen.name);
            etNo.setText(identityAuthen.certNo);
            tvState.setVisibility(View.VISIBLE);

            tjrImageLoaderUtil.displayImage(identityAuthen.frontImgUrl, ivFrontImg);
            tjrImageLoaderUtil.displayImage(identityAuthen.backImgUrl, ivBackImg);
//            tjrImageLoaderUtil.displayImage(identityAuthen.images.holdImgUrl, ivHoldImg);

            if (identityAuthen.state == 0 || identityAuthen.state == 1) {
                etName.setEnabled(false);
                etNo.setEnabled(false);
                ivFrontTake.setVisibility(View.GONE);
                ivBackTake.setVisibility(View.GONE);
                ivHoldTake.setVisibility(View.GONE);
                tvComplete.setEnabled(false);
                tvState.setText("状态: " + identityAuthen.stateDesc);

            } else if (identityAuthen.state == 2) {
                etName.setEnabled(true);
                etNo.setEnabled(true);
                ivFrontTake.setVisibility(View.VISIBLE);
                ivBackTake.setVisibility(View.VISIBLE);
                ivHoldTake.setVisibility(View.VISIBLE);
                tvComplete.setEnabled(true);
                tvState.setText("状态: " + identityAuthen.stateDesc + "(" + "原因: " + identityAuthen.checkMsg + ")");
            }


        }

    }

    private void initDynamicPermission() {
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission(this, new DynamicPermission.RequestPermissionsCallBack() {
                @Override
                public void onRequestSuccess(String[] permissions, int requestCode) {
                    if (requestCode == 100) {
                        frontTake = ImageUtil.getNewPhotoPath();
                        Log.d("initDynamicPermission","frontTake=="+frontTake);
                        Intent intent = ImageUtil.takeBigPicture(IdentityAuthenActivity.this, frontTake);
                        startActivityForResult(intent, 0x123);
                    } else if (requestCode == 200) {
                        backTake = ImageUtil.getNewPhotoPath();
                        Intent intent = ImageUtil.takeBigPicture(IdentityAuthenActivity.this, backTake);
                        startActivityForResult(intent, 0x456);
                    } else if (requestCode == 300) {
//                        holdTake = ImageUtil.getNewPhotoPath();
//                        Intent intent = ImageUtil.takeBigPicture(IdentityAuthenActivity.this, holdTake);
//                        startActivityForResult(intent, 0x789);
                    }

                }

                @Override
                public void onRequestFail(String[] permissions, int requestCode) {
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (dynamicPermission != null)
            dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFrontTake:
                initDynamicPermission();
                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 100);
//                MyhomeMultiSelectImageActivity.pageJumpThis(IdentityAuthenActivity.this, 1, true, false, IdentityAuthenActivity.class.getName(), 1, true);//需要设置android:launchMode="singleTask"
                break;
            case R.id.ivBackTake:
                initDynamicPermission();
                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 200);
//                MyhomeMultiSelectImageActivity.pageJumpThis(IdentityAuthenActivity.this, 1, true, false, IdentityAuthenActivity.class.getName(), 2, true);
                break;
//            case R.id.ivHoldTake:
//                initDynamicPermission();
//                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 300);
//                MyhomeMultiSelectImageActivity.pageJumpThis(IdentityAuthenActivity.this, 1, true, false, IdentityAuthenActivity.class.getName(), 3,true);
//                break;
            case R.id.tvComplete:
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    CommonUtil.showmessage("请输入身份证姓名", IdentityAuthenActivity.this);
                    return;
                }
                String no = etNo.getText().toString().trim();
                if (TextUtils.isEmpty(no)) {
                    CommonUtil.showmessage("请输入身份证号码", IdentityAuthenActivity.this);
                    return;
                }
//                String invidateNo = IDCardValidate.validate_effective(no, false);
//                if (!invidateNo.equals(no)) {
//                    CommonUtil.showmessage(invidateNo, IdentityAuthenActivity.this);
//                    return;
//                }
                if (TextUtils.isEmpty(frontImgFile)) {
                    CommonUtil.showmessage("请上传人像照", IdentityAuthenActivity.this);
                    return;
                }

                if (TextUtils.isEmpty(backImgFile)) {
                    CommonUtil.showmessage("请上传国徽照", IdentityAuthenActivity.this);
                    return;
                }
//                if (TextUtils.isEmpty(holdImgFile) || !new File(holdImgFile).exists()) {
//                    CommonUtil.showmessage("请上传手持身份证照", IdentityAuthenActivity.this);
//                    return;
//                }
//                startUpload(name, no);
                startSubmitIdentityAuthen(name, no, frontImgFile, backImgFile);
                break;
//            case R.id.tvViewDemoFrontImg:
//                goViewPagerPhotoViewActivity(0);
//                break;
//            case R.id.tvViewDemoBackImg:
//                goViewPagerPhotoViewActivity(1);
//                break;
//            case R.id.tvViewHoldImgImg:
//                goViewPagerPhotoViewActivity(2);
//                break;
        }
    }


//    private void goViewPagerPhotoViewActivity(int pos) {
//        if (viewDemoUrls != null && viewDemoUrls.size() == 3) {
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("imageUrls", viewDemoUrls);
//            bundle.putInt(CommonConst.DEFAULTPOS, pos);
//            bundle.putInt(CommonConst.PAGETYPE, 5);
//            PageJumpUtil.pageJump(this, ViewPagerPhotoViewActivity.class, bundle);
//        }
//
//    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            int type = intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, 0);
            ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
            if (flist == null || flist.size() == 0) return;
            String file = flist.get(0);
            if (type == 1) {
//                String frontImgFile = file;
//                ivFrontImg.setImageURI(Uri.fromFile(new File(frontImgFile)));
//                tjrImageLoaderUtil.displayImage("file://" + file, ivFrontImg, imageOptions);//原图要这样显示
                startUploadFront(file);
            } else if (type == 2) {
//                String backImgFile = file;
//                ivBackImg.setImageURI(Uri.fromFile(new File(backImgFile)));
//                tjrImageLoaderUtil.displayImage("file://" + file, ivBackImg, imageOptions);
                startUploadBack(file);
            } else if (type == 3) {
//                holdImgFile = file;
//                ivHoldImg.setImageURI(Uri.fromFile(new File(holdImgFile)));
//                tjrImageLoaderUtil.displayImage("file://" + file, ivHoldImg, imageOptions);
            }
        }
    }

    private void startUploadFront(String file) {
        CommonUtil.cancelCall(uploadFrontCall);
        showProgressDialog();
        uploadFrontCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "userImage", UploadFileUtils.getImageFilesMap(file));
//        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 0, fileZone, UploadFileUtils.getFilesMap(bitmapFile.getAbsolutePath()));
        uploadFrontCall.enqueue(new MyCallBack(IdentityAuthenActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    String imageUrl = resultData.getItem("imageUrlList", String.class);
                    Log.d("120", "imageUrl == " + imageUrl);
                    try {
                        JSONArray jsonArray = new JSONArray(imageUrl);
                        String url = jsonArray.getString(0);
                        if (!TextUtils.isEmpty(url)) {
                            frontImgFile = url;
                            tjrImageLoaderUtil.displayImage(frontImgFile, ivFrontImg);//原图要这样显示
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void startUploadBack(String file) {
        CommonUtil.cancelCall(uploadBackCall);
        showProgressDialog();
        uploadBackCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "identityImage", UploadFileUtils.getImageFilesMap(file));
        uploadBackCall.enqueue(new MyCallBack(IdentityAuthenActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    String imageUrl = resultData.getItem("imageUrlList", String.class);
                    Log.d("120", "imageUrl == " + imageUrl);
                    try {
                        JSONArray jsonArray = new JSONArray(imageUrl);
                        String url = jsonArray.getString(0);
                        if (!TextUtils.isEmpty(url)) {
                            backImgFile = url;
                            tjrImageLoaderUtil.displayImage(backImgFile, ivBackImg);//原图要这样显示
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 0x123:
                try {
                    Log.d("onActivityResult"," frontTake=="+frontTake);
                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(frontTake, true);
                    File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmapOfFile, true, getUserId());
                    startUploadFront(f.getAbsolutePath());
//                    frontImgFile = f.getAbsolutePath();
//                    ivFrontImg.setImageURI(Uri.fromFile(new File(frontImgFile)));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("onActivityResult","e=="+e);
                }

                break;
            case 0x456:
                try {
                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(backTake, true);
                    File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmapOfFile, true, getUserId());
                    startUploadBack(f.getAbsolutePath());
//                    backImgFile = f.getAbsolutePath();
//                    ivBackImg.setImageURI(Uri.fromFile(new File(backImgFile)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
//            case 0x789:
//                try {
//                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(holdTake, true);
//                    File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmapOfFile, true, getUserId());
//                    holdImgFile = f.getAbsolutePath();
//                    ivHoldImg.setImageURI(Uri.fromFile(new File(holdImgFile)));
//                } catch (Exception e) {
//
//                }
//
//                break;

        }

    }

//    //上传图片
//    private void startUpload(final String name, final String no) {
//        showProgressDialog();
//        CommonUtil.cancelCall(uploadCall);
//        uploadCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 5, FileZoneEnum.IMAGE_IDENTITY.type, UploadFileUtils.getIdentityAuthen(frontImgFile, backImgFile, holdImgFile));
//        uploadCall.enqueue(new MyCallBack(IdentityAuthenActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    String frontUrl = resultData.getItem("imgUrl", String.class);
//                    String backUrl = resultData.getItem("otherUrl", String.class);
////                    String holdUrl = resultData.getItem("videoUrl", String.class);
//                    startSubmitIdentityAuthen(name, no, frontUrl, backUrl);
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                dismissProgressDialog();
//
//            }
//        });
//    }

    private void startSubmitIdentityAuthen(String name, String no, String frontUrl, String backUrl) {
        CommonUtil.cancelCall(submitIdentityAuthenCall);
        submitIdentityAuthenCall = VHttpServiceManager.getInstance().getVService().submitIdentityAuthen(name, no, frontUrl, backUrl);
        submitIdentityAuthenCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    showSuccessDialog(resultData.msg);
                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                dismissProgressDialog();
            }
        });
    }

    TjrBaseDialog successDialog;

    private void showSuccessDialog(String msg) {
        if (successDialog == null) {
            successDialog = new TjrBaseDialog(this) {
                @Override
                public void onclickOk() {
                    dismiss();
                    PageJumpUtil.finishCurr(IdentityAuthenActivity.this);
                }

                @Override
                public void onclickClose() {
                    dismiss();
                }

                @Override
                public void setDownProgress(int progress) {

                }
            };
        }
        successDialog.setTvTitle("提示");
        successDialog.setMessage(msg);
        successDialog.setBtnColseVisibility(View.GONE);
        successDialog.show();


    }
}
