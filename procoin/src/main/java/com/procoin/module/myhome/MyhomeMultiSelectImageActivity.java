package com.procoin.module.myhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.module.myhome.entity.ImageGroup;
import com.procoin.task.ImageLoadTask;
import com.procoin.widgets.SpaceItemDecoration;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.PermissionUtils;
import com.procoin.widgets.LogUtils;
import com.procoin.util.DynamicPermission;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.procoin.R;
import com.procoin.common.web.ImageUtil;
import com.procoin.http.base.Group;
import com.procoin.module.myhome.adapter.MyhomeMultiSelectAdapter;
import com.procoin.module.myhome.adapter.MyhomePopSelectAdapter;
import com.procoin.module.myhome.entity.ImageSelectGroup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyhomeMultiSelectImageActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    private final int PHOTOHRAPH = 0x111;// 拍照
    private final int SENDPIC_REQUESTCODE = 0x123;

    private static final int MAXPICCOUNT = 9;//默认9张图片
    private RecyclerView gridView;
    private TextView textView;
    private TextView tvOk;
    private TextView tvPreview;
    private ImageLoadTask imageloadTask;
    private MyhomeMultiSelectAdapter adapter;
    private PopupWindow pop;
    private ListView lvView;
    private MyhomePopSelectAdapter popAdapter;
    private String capturePath;
    private String title;
    private boolean showPop;
    private boolean showCarema = true;//是否显示拍照
    private boolean needCut = false;//是否需要裁剪
    private boolean artwork = false;//是否原图
    private String className = "";
    private int type = 0; // 当一个页面有多个地方需要调用这个页面的话，返回的时候就用这个来区分
    private DynamicPermission dynamicPermission;

    private int maxPicCount = MAXPICCOUNT;

    private Group<ImageSelectGroup> selectedGroup = new Group<>();


    @Override
    protected int setLayoutId() {
        return R.layout.myhome_selectimage;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    public static void pageJumpThis(Context context, int picCount, boolean showCarema, boolean needCut, String clsName) {
        pageJumpThis(context, picCount, showCarema, needCut, clsName, 0);
    }

    public static void pageJumpThis(Context context, int picCount, boolean showCarema, boolean needCut, String clsName, int type) {
        pageJumpThis(context, picCount, showCarema, needCut, clsName, type, false);
    }

    /**
     * @param context
     * @param picCount   最大图片数量
     * @param showCarema 是否显示拍照
     * @param needCut    是否需要裁剪(如果选了多张则无效)
     * @param clsName    目标Activity的className( XXXXXXXXActivity.class.getName())，需启用android:launchMode="singleTask",并在onNewIntent里面接收key为"picList"参数，类型为List<String>
     * @param type       当一个页面有多个地方需要调用这个页面的话，返回的时候就用这个来区分
     * @param artwork    是否是原图,如果是就不用压缩了，目前只用到实名认证（如果是原图的话，显示的时候要注意，否则容易内存溢出）
     */
    public static void pageJumpThis(Context context, int picCount, boolean showCarema, boolean needCut, String clsName, int type, boolean artwork) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.MAXPICCOUNT, picCount);
        bundle.putBoolean(CommonConst.SHOWCAREMA, showCarema);
        bundle.putString(CommonConst.CLASSNAME, clsName);
        bundle.putBoolean(CommonConst.NEEDCUT, needCut);
        bundle.putBoolean(CommonConst.ARTWORK, artwork);
        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
        PageJumpUtil.pageJump(context, MyhomeMultiSelectImageActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("capturePath")) {
                capturePath = savedInstanceState.getString("capturePath");
            }
        }
        Log.d("onActivityResult", "onCreate==  capturePath==" + capturePath);
        getApplicationContext().selectedGroup = null;//每次进来这个页面先清空
        getApplicationContext().allGroups = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            maxPicCount = bundle.getInt(CommonConst.MAXPICCOUNT);
            showCarema = bundle.getBoolean(CommonConst.SHOWCAREMA, true);
            className = bundle.getString(CommonConst.CLASSNAME, "");
            needCut = bundle.getBoolean(CommonConst.NEEDCUT);
            artwork = bundle.getBoolean(CommonConst.ARTWORK);
            type = bundle.getInt(CommonConst.KEY_EXTRAS_TYPE);
        }
        gridView = (RecyclerView) findViewById(R.id.gvView);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(this, 1);
        gridView.setLayoutManager(new GridLayoutManager(this, 3));
        gridView.addItemDecoration(spaceItemDecoration);
        adapter = new MyhomeMultiSelectAdapter(this, showCarema, maxPicCount);
        adapter.setHasStableIds(true);
        adapter.setCallBack(new MyhomeMultiSelectAdapter.CallBack() {
            @Override
            public void onSelectClick(int pos, ImageSelectGroup itemSelectGroup) {
                if (itemSelectGroup != null) {
                    if (itemSelectGroup.isCheck()) {
                        itemSelectGroup.setCheck(false);
                        if (selectedGroup.contains(itemSelectGroup))
                            selectedGroup.remove(itemSelectGroup);
                    } else {
                        if (selectedGroup != null && selectedGroup.size() >= maxPicCount) {
                            CommonUtil.showmessage("当前最多添加" + maxPicCount + "张图片", MyhomeMultiSelectImageActivity.this);
                            return;
                        }
                        itemSelectGroup.setCheck(true);
                        selectedGroup.add(itemSelectGroup);
                    }
                    adapter.notifyItemChanged(pos);
                    setButtonState();
                }
            }

            @Override
            public void onItemClick(int position, ImageSelectGroup item) {
                LogUtils.d("MyhomeSelectImageActivity", "showCarema==" + showCarema + "  pos==" + position);
                Group<ImageSelectGroup> groups = adapter.getGroup();
                getApplicationContext().allGroups = groups;
                getApplicationContext().selectedGroup = selectedGroup;
                SelectPicPreviewActivity.pageJumpThis(MyhomeMultiSelectImageActivity.this, showCarema ? position - 1 : position, maxPicCount, className, needCut, type, artwork);
            }

            @Override
            public void onCapture() {
                if (dynamicPermission != null)
                    dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 100);
            }
        });
        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                LogUtils.d("MyhomeSelectImageActivity", "showCarema==" + showCarema + "  pos==" + position);
//                if (showCarema && position == 0) {
//                    if (dynamicPermission != null)
//                        dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 100);
//                } else {
//                    Group<ImageSelectGroup> groups = adapter.getGroup();
//                    getApplicationContext().groups = groups;
//                    getApplicationContext().selectedGroup = selectedGroup;
//                    SelectPicPreviewActivity.pageJumpThis(MyhomeMultiSelectImageActivity.this, showCarema ? position - 1 : position, maxPicCount);
//
//                }
//
//            }
//        });
        textView = (TextView) findViewById(R.id.group_item_count_tv);
        tvOk = (TextView) findViewById(R.id.tvOk);
        tvOk.setOnClickListener(this);
        tvPreview = (TextView) findViewById(R.id.tvPreview);
        tvPreview.setOnClickListener(this);
        lvView = (ListView) View.inflate(this, R.layout.myhome_pop_selectimage, null);
        popAdapter = new MyhomePopSelectAdapter(this);
        lvView.setAdapter(popAdapter);
        initPopupMenu(this.getScreenWidth(), this.getScreenHeight() * 2 / 3);
        lvView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pop.dismiss();
                gridView.scrollToPosition(1);
                final int pos = position;
                gridView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pos == 0) {
                            setTitleName("全部图片");
                            adapter.setGroup(imageloadTask.getAllPic());
                        } else {
                            ImageGroup listitem = popAdapter.getItem(pos);
                            if (listitem != null) {
                                setTitleName(listitem.getDirName());
                                adapter.setGroup(listitem.getImages());
//                        if (imageloadTask.getmGruopList() != null && position < (imageloadTask.getmGruopList().size())) {
//                            Group<ImageSelectGroup> g = listitem.getImages();
////                            for (ImageSelectGroup iv : g) {
////                                Log.d("ImageSelect", "iv=" + iv.getPathStr());
////                            }
//                            adapter.setGroup(g);
//
//                        }
                            }
                        }
                    }
                }, 500);


            }
        });
        setButtonState();
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission(this, requestPermissionsCallBack);
        }
        dynamicPermission.checkSelfPermission(PermissionUtils.EXTERNAL_STORAGE, 101);
    }

    DynamicPermission.RequestPermissionsCallBack requestPermissionsCallBack = new DynamicPermission.RequestPermissionsCallBack() {
        @Override
        public void onRequestSuccess(String[] permissions, int requestCode) {
            Log.d("RequestPermissions", "onRequestSuccess requestCode==" + requestCode);
            if (requestCode == 101) {
                imageloadTask = new ImageLoadTask(MyhomeMultiSelectImageActivity.this) {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        showProgressDialog();
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                        if (result) {
                            if (imageloadTask.getmGruopList() != null && imageloadTask.getmGruopList().size() >= 1) {
                                adapter.setGroup(imageloadTask.getAllPic());
                                setTitleName("全部图片");
                            }
                            popAdapter.setData(imageloadTask.getmGruopList(), imageloadTask.getAllPic());
                            textView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showPopupMenu(v);
                                    showPop = true;
                                    setTitleName(title);

                                }
                            });
                        } else {

                        }
                        dismissProgressDialog();
                    }

                };
                imageloadTask.executeParams();
            } else if (requestCode == 100) {
                try {
                    capturePath = ImageUtil.getNewPhotoPath();
                    Intent caintent = ImageUtil.takeBigPicture(MyhomeMultiSelectImageActivity.this, capturePath);
                    PageJumpUtil.pageJumpResult(MyhomeMultiSelectImageActivity.this, caintent, PHOTOHRAPH);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("takeBigPicture", "e==" + e.toString());
                }


            }


        }

        @Override
        public void onRequestFail(String[] permissions, int requestCode) {
            Log.d("RequestPermissions", "onRequestFail requestCode==" + requestCode);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (dynamicPermission != null)
            dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setTitleName(String title) {
        this.title = title;
        if (showPop) {
            textView.setText(title + "▲");
        } else {
            textView.setText(title + "▼");
        }
    }

    private void setButtonState() {
        if (selectedGroup != null && selectedGroup.size() > 0) {
            tvOk.setText("确定(" + selectedGroup.size() + ")");
            tvPreview.setText("预览(" + selectedGroup.size() + ")");
            tvPreview.setEnabled(true);
        } else {
            tvOk.setText("确定");
            tvPreview.setText("预览");
            tvPreview.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getApplicationContext().selectedGroup != null) {
            selectedGroup = getApplicationContext().selectedGroup;
            Log.d("selectedGroup", "selectedGroup==" + selectedGroup.size());

            for (int i = 0, m = adapter.getItemCount(); i < m; i++) {
//            for (ImageSelectGroup imageSelectGroup : adapter.getGroup()) {
                ImageSelectGroup imageSelectGroup = adapter.getItem(i);//有可能为null,第0项可能是拍照
                if (imageSelectGroup != null) {
                    if (selectedGroup != null && selectedGroup.contains(imageSelectGroup)) {
                        if (!imageSelectGroup.isSelected()) {
                            imageSelectGroup.setCheck(true);
                            adapter.notifyItemChanged(i);
                        }
                    } else {
                        if (imageSelectGroup.isSelected()) {
                            imageSelectGroup.setCheck(false);
                            adapter.notifyItemChanged(i);
                        }
                    }
                }

            }
            setButtonState();

//            adapter.notifyItemChanged(i);
//            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {//退出清除数据
            if (getApplicationContext().allGroups != null) {
                getApplicationContext().allGroups.clear();
                getApplicationContext().allGroups = null;
            }
            if (getApplicationContext().selectedGroup != null) {
                getApplicationContext().selectedGroup.clear();
                getApplicationContext().selectedGroup = null;
            }
        }
    }

    public void initPopupMenu(int width, int height) {
        if (pop == null) {
            pop = new PopupWindow(lvView, width, height);//
            pop.setOutsideTouchable(false);
            pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));// 特别留意这个东东
            pop.setFocusable(true);// 如果不加这个，Grid不会响应ItemClick
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    showPop = false;
                    setTitleName(title);
                }
            });
// TODO           pop.setAnimationStyle(R.style.PopupFrdsAnimation);
        }
    }

    public void showPopupMenu(View parent) {
        if (pop != null && !pop.isShowing()) {
            pop.showAsDropDown(parent);
            // pop.showAtLocation(parent, gravity, x, y);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("onActivityResult", "onSaveInstanceState==");
        if (!TextUtils.isEmpty(capturePath)) {//经常被销毁，所以一定要保存
            outState.putString("capturePath", capturePath);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTOHRAPH) {//拍照回来
            Log.d("onActivityResult", "capturePath==" + capturePath + "  resultCode==" + resultCode + " data==" + data);
            if (TextUtils.isEmpty(capturePath) || !new File(capturePath).exists()) {
                CommonUtil.showmessage("获取图片失败", this);
                return;
            }
            try {
                if (needCut) {
                    MyHomeCropActivity.jumpThis(MyhomeMultiSelectImageActivity.this, capturePath, className, type);
                } else {
                    ArrayList list = new ArrayList();
                    if (artwork) {
                        list.add(capturePath);
                        Log.d("selectPicPreview", "拍照 原图 size ==" + new File(capturePath).length());
                    } else {
                        Bitmap bitmapOfFile = com.procoin.http.util.CommonUtil.getSmallBitmap(capturePath, true);
                        File f = com.procoin.http.util.CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmapOfFile);
                        Log.d("selectPicPreview", "拍照 非原图 size ==" + f.length());
                        list.add(f.getAbsolutePath());
                    }
                    Intent intent = new Intent();
                    intent.setClassName(getPackageName(), className);
                    intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, type);
                    intent.putStringArrayListExtra(CommonConst.PICLIST, list);
                    PageJumpUtil.pageJump(MyhomeMultiSelectImageActivity.this, intent);
                }


            } catch (Exception e) {
                e.printStackTrace();
                CommonUtil.showmessage("出错了", MyhomeMultiSelectImageActivity.this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPreview:
                getApplicationContext().allGroups = (Group<ImageSelectGroup>) selectedGroup.clone();
                getApplicationContext().selectedGroup = selectedGroup;
                SelectPicPreviewActivity.pageJumpThis(MyhomeMultiSelectImageActivity.this, 0, maxPicCount, className, needCut, type, artwork);
                break;
            case R.id.tvOk:
                if (selectedGroup == null || selectedGroup.size() == 0) {
                    CommonUtil.showmessage("请选择图片", MyhomeMultiSelectImageActivity.this);
                    return;
                }
                if (maxPicCount == 1 && needCut) {
                    MyHomeCropActivity.jumpThis(MyhomeMultiSelectImageActivity.this, selectedGroup.get(0).getPathStr(), className, type);
                } else {
                    if (selectedGroup.size() >= 4) showProgressDialog();
                    ArrayList<String> list = new ArrayList<>();
                    for (ImageSelectGroup imageSelectGroup : selectedGroup) {
                        try {
                            if (artwork) {
                                list.add(imageSelectGroup.getPathStr());
                                Log.d("selectPicPreview", "原图 size ==" + new File(imageSelectGroup.getPathStr()).length());
                            } else {
                                File f = com.procoin.http.util.CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), com.procoin.http.util.CommonUtil.getSmallBitmap(imageSelectGroup.getPathStr(), true));
                                Log.d("selectPicPreview", "非原图 size ==" + f.length());
                                list.add(f.getAbsolutePath());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (selectedGroup.size() >= 4) dismissProgressDialog();
                    Intent intent = new Intent();
                    intent.setClassName(getPackageName(), className);
                    intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, type);
                    intent.putStringArrayListExtra(CommonConst.PICLIST, list);
                    PageJumpUtil.pageJump(MyhomeMultiSelectImageActivity.this, intent);
                }

//                if (getApplicationContext().selectedGroup != null) {
//                    getApplicationContext().selectedGroup.clear();
//                    getApplicationContext().selectedGroup = null;
//                }

                break;
        }
    }
//    }


    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("deprecation")
    private Uri getOutputMediaFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = ImageLoader.getInstance().getDiskCache().get("IMG_TEMP_" + timeStamp + ".jpg");
        Uri mediaUri = Uri.fromFile(mediaFile);
        return mediaUri;
    }
}
