package com.procoin.module.myhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.util.BitmapUtil;
import com.procoin.util.PageJumpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.procoin.http.util.CommonUtil;
import com.procoin.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


@SuppressLint("SimpleDateFormat")
public class MyHomeCropActivity extends TJRBaseToolBarSwipeBackActivity implements PhotoViewAttacher.OnMatrixChangedListener {
    private float matixTop;
    private float matrixHeight;
    private float matrixLeft;
    private float matrixWidth;
    private View operationBar;
    private TextView tvTitle;
    private Bitmap bitmap;
    private Intent intent;
    private String strPath;
    private String clsName;//
    private int type;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * @param context
     * @param filePath
     * @param clsName  目标Activity的className，需启用android:launchMode="singleTask",并在onNewIntent里面接收参数
     */
    public static void jumpThis(Context context, String filePath, String clsName, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_EXTRAS_BITMAP, filePath);
        bundle.putString(CommonConst.CLASSNAME, clsName);
        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
        PageJumpUtil.pageJump(context, MyHomeCropActivity.class, bundle);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.myhome_crop;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mActionBar.hide();
        intent = getIntent();
        if (intent != null) {
            strPath = intent.getStringExtra(CommonConst.KEY_EXTRAS_BITMAP);
            clsName = intent.getStringExtra(CommonConst.CLASSNAME);
            type = intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, 0);
        }
        if (strPath == null) {
            CommonUtil.showmessage("没有获取到图片", MyHomeCropActivity.this);
            finish();
            return;
        }
        if (!"".equals(strPath)) {
            strPath = Uri.decode(strPath).toString();
        }
        // bitmap = ImageLoader.getInstance().loadImageSync(strPath);

        try {
            bitmap = BitmapUtil.getSmallBitmap(Uri.parse(strPath).getPath(), true);

        } catch (Exception e) {
        } catch (OutOfMemoryError e) {
        }

        if (bitmap == null) {
            CommonUtil.showmessage("没有获取到图片", MyHomeCropActivity.this);
            finish();
            return;
        }
//        setContentView(R.layout.myhome_crop);
        final PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        tvTitle = (TextView) findViewById(R.id.tvCroptitle);
        operationBar = findViewById(R.id.operation_bar);

        new Thread(new Runnable() {

            @Override
            public void run() {
                MyHomeCropActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tvTitle.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                // TODO Auto-generated method stub
                                tvTitle.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        photoView.setPadding(0, MyHomeCropActivity.this.tvTitle.getMeasuredHeight(), 0, MyHomeCropActivity.this.operationBar.getMeasuredHeight());
                                        photoView.setOnMatrixChangeListener(MyHomeCropActivity.this);
                                        photoView.setImageBitmap(bitmap);
                                    }
                                }, 300);
                            }
                        });
                    }
                });
            }
        }).start();

        findViewById(R.id.crop_ok).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                cropImage();
            }
        });
        findViewById(R.id.crop_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void cropImage() {
        float bmWidth = this.bitmap.getWidth();
        float bmHeight = this.bitmap.getHeight();
        float startx = (int) (bmWidth / this.matrixWidth * this.matrixLeft);
        float starty = (int) (bmHeight / this.matrixHeight * this.matixTop);
        float maxSize = getOutputSquareSideLength(this);//
        float minSize = Math.min((int) Math.min(bmWidth, maxSize * (bmWidth / this.matrixWidth)), (int) Math.min(bmHeight, maxSize * (bmHeight / this.matrixHeight)));
        if (startx + minSize > bmWidth) minSize = bmWidth - startx;
        if (starty + minSize > bmHeight) minSize = bmHeight - starty;
        Matrix localMatrix = new Matrix();
        float scale = maxSize / minSize;
        localMatrix.setScale(scale, scale);
        Bitmap localBitmap = Bitmap.createBitmap(this.bitmap, Math.round(startx), Math.round(starty), Math.round(minSize), Math.round(minSize), localMatrix, true);
        if (localBitmap != null) {
            try {
//                Intent localIntent = new Intent();
//                File file = saveBitmapToFile(localBitmap, true);
//                localIntent.setData(Uri.fromFile(file));
//                setResult(Activity.RESULT_OK, localIntent);
//                finish();
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), clsName);
                intent.putExtra(CommonConst.KEY_EXTRAS_TYPE,type);
                File file = saveBitmapToFile(localBitmap, true);
                ArrayList<String> list = new ArrayList<>();
                list.add(file.getAbsolutePath());
                intent.putStringArrayListExtra(CommonConst.PICLIST, list);
                PageJumpUtil.pageJump(MyHomeCropActivity.this, intent);
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
    }

    private File getOutputMediaFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = ImageLoader.getInstance().getDiskCache().get("IMG_" + timeStamp + ".png");
        return mediaFile;
    }


    private File saveBitmapToFile(Bitmap bitmap, boolean reyle) throws Exception {
        if (bitmap == null) return null;
        String fileName2 = System.currentTimeMillis() + "" + getUserId() + ".png";
        File file2 = getApplicationContext().getRemoteResourceChatManager().getFile(fileName2);
        getApplicationContext().getRemoteResourceChatManager().writeFile(file2, bitmap, reyle);
        if (file2 != null && file2.exists()) {
            return file2;
        }
        return null;
    }


    /**
     * 如果小于640就取640
     *
     * @param paramContext
     * @return
     */
    public static int getOutputSquareSideLength(Context paramContext) {
        int i = Math.max(paramContext.getResources().getDisplayMetrics().widthPixels, 640);
        return i;
    }

    public void onMatrixChanged(RectF paramRectF) {

        this.matrixWidth = (paramRectF.right - paramRectF.left);
        this.matrixHeight = (paramRectF.bottom - paramRectF.top);
        this.matrixLeft = Math.abs(paramRectF.left);
        this.matixTop = Math.abs(paramRectF.top);
        Log.i("ddd", "onMatrixChanged matrixWidth is" + matrixWidth + " matrixHeight is " + matrixHeight + " matrixLeft is" + matrixLeft + " matixTop" + matixTop);
    }

    protected static class ExifInfo {
        public final boolean flipHorizontal;
        public final int rotation;

        protected ExifInfo() {
            this.rotation = 0;
            this.flipHorizontal = false;
        }

        protected ExifInfo(int paramInt, boolean paramBoolean) {
            this.rotation = paramInt;
            this.flipHorizontal = paramBoolean;
        }
    }

}
