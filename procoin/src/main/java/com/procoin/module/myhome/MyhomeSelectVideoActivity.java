package com.procoin.module.myhome;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.module.myhome.entity.VideoEntity;
import com.procoin.util.DensityUtil;
import com.procoin.util.DynamicPermission;
import com.procoin.util.PermissionUtils;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.module.myhome.adapter.MyVideoAdapter;
import com.procoin.module.publish.RecordVideo2Activity;
import com.procoin.util.CommonUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.util.PageJumpUtil;
import com.procoin.http.base.Group;
import com.procoin.http.base.baseadapter.BaseImageLoaderAdapter;
import com.procoin.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengmj on 18-11-15.
 */

public class MyhomeSelectVideoActivity extends TJRBaseToolBarSwipeBackActivity {
    private RecyclerView rv_list;
    private MyVideoAdapter adapter;
    private int VIDEORESOULT;
    private DynamicPermission dynamicPermission;
    private HashMap<String, Group<VideoEntity>> map;
    private TextView group_item_count_tv;
    private TextView textview;
    private PopupWindow pop;
    private ListView lvView;
    private boolean showPop;
    private String title = "全部视频";
    private PopListAdapter popListAdapter;
    private Group<VideoEntity> group;

    private static final long VIDEOMAXSIZE = 50;//视频最大尺寸，单位MB


    @Override
    protected int setLayoutId() {
        return R.layout.myhome_selectvideo;
    }

    @Override
    protected String getActivityTitle() {
        return null;
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
        }
    }

    private void setTitleName(String title) {
        this.title = title;
        if (showPop) {
            textview.setText(title + "▲");
        } else {
            textview.setText(title + "▼");
        }
    }

    public void showPopupMenu(View parent) {
        if (pop != null && !pop.isShowing()) {
            pop.showAsDropDown(parent);
            // pop.showAtLocation(parent, gravity, x, y);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        handler = new MyHandler(this);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        textview = (TextView) findViewById(R.id.group_item_count_tv);
        lvView = (ListView) View.inflate(this, R.layout.myhome_pop_selectimage, null);
        lvView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setTitleName(popListAdapter.getTitleInList(i));
                if (i == 0) {
                    if (group != null && group.size() > 0) adapter.setGroup(group);
                } else {
                    Group<VideoEntity> group = popListAdapter.getVideoGroup(i);
                    adapter.setGroup(group);
                }
                pop.dismiss();
            }
        });
        popListAdapter = new PopListAdapter();
        lvView.setAdapter(popListAdapter);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
                showPop = true;
                setTitleName(title);
            }
        });
        initPopupMenu(this.getScreenWidth(), this.getScreenHeight() * 2 / 3);
        setTitleName(title);
        rv_list.addItemDecoration(new SpaceItemDecoration());
        rv_list.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new MyVideoAdapter(this);
        rv_list.setAdapter(adapter);
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission(this, requestPermissionsCallBack);
        }
        dynamicPermission.checkSelfPermission(PermissionUtils.EXTERNAL_STORAGE, 101);
        adapter.setOnItemclickCallback(new MyVideoAdapter.OnItemclickCallback() {
            @Override
            public void onitemClick(String path) {
                if (path.equals("default")) {
                    dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_RECORDAUDIO_EXTERNALSTORAGE, 0xbbc);
                } else {
                    File file =new File(path);
                    if(!file.exists()){
                        CommonUtil.showmessage("视频不存在",MyhomeSelectVideoActivity.this);
                        return;
                    }
                    if (!getFileSizeEnable(file)) {
                        CommonUtil.showmessage("视频大小不能超过"+VIDEOMAXSIZE+"M",MyhomeSelectVideoActivity.this);
                        return;
                    }

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConst.VIDEOOUTPUTPATH, path);
                    intent.putExtras(bundle);
                    setResult(0x789, intent);
                    PageJumpUtil.finishCurr(MyhomeSelectVideoActivity.this);
                }
            }
        });
//        adapter.setOnFailThumbCallback(new MyVideoAdapter.OnFailThumbCallback() {
//            @Override
//            public Bitmap onFail(String path) {
//                Bitmap bitmap = null;
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                try{
//                    CommonUtil.showmessage("进入else",MyhomeSelectVideoActivity.this);
//                    retriever.setDataSource(path);
//                    bitmap = retriever.getFrameAtTime();
//                }catch (Exception e){
//
//                }finally {
//                    try {
//                        retriever.release();
//                    }catch (Exception e){
//
//                    }
//                }
//                return bitmap;
//            }
//        });
    }

    private boolean getFileSizeEnable(File file) {
        if(file==null)return false;
        long size = 0;
        FileInputStream fis = null;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                // Log.e("获取文件大小", "文件不存在!");
            }
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        Log.d("getFileSizeEnable","size=="+size);
        return size <= 1024 * 1024 * VIDEOMAXSIZE;
    }

    DynamicPermission.RequestPermissionsCallBack requestPermissionsCallBack = new DynamicPermission.RequestPermissionsCallBack() {
        @Override
        public void onRequestSuccess(String[] permissions, int requestCode) {
            Log.d("RequestPermissions", "onRequestSuccess requestCode==" + requestCode);
            Log.d("200", "Permission -> " + requestCode);
            if (requestCode == 101) {
                getList();
            } else if (requestCode == 0xbbc) {
                PageJumpUtil.pageJumpResult(MyhomeSelectVideoActivity.this, RecordVideo2Activity.class, new Intent(), VIDEORESOULT);
            }
        }

        @Override
        public void onRequestFail(String[] permissions, int requestCode) {
            Log.d("RequestPermissions", "onRequestFail requestCode==" + requestCode);
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (dynamicPermission != null)
            dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEORESOULT && resultCode == 0x789) {
            String videoPath = data.getExtras().getString(CommonConst.VIDEOOUTPUTPATH);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(CommonConst.VIDEOOUTPUTPATH, videoPath);
            intent.putExtras(bundle);
            setResult(0x789, intent);
            PageJumpUtil.finishCurr(MyhomeSelectVideoActivity.this);
        }
    }

    private void getList() {
        showProgressDialog();
        group = new Group<>();
        VideoEntity defaultEntity = new VideoEntity();
        defaultEntity.coverPath = "default";
        defaultEntity.videoPath = "default";
        defaultEntity.duration = 0;
        group.add(defaultEntity);

        Observable.create(new ObservableOnSubscribe<Group<VideoEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<Group<VideoEntity>> e) throws Exception {
                Cursor cursor = null;
                String[] mediaColumns = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};
                String[] thumbColumns = new String[]{MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails._ID};
                String sort = MediaStore.Video.Media.DATE_TAKEN + " DESC";
                try {
                    cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, sort);
                    if (cursor == null) {
                        Log.d("200", "query is null");
                        return;
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            VideoEntity entity = new VideoEntity();
                            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                            Cursor thumbCursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                                    thumbColumns,
                                    MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null);

                            if (thumbCursor.moveToFirst()) {
                                entity.coverPath = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                            }
                            entity.videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media
                                    .DATA));
                            entity.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video
                                    .Media.DURATION));
//                            entity.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                            group.add(entity);
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            packagePath(path, entity);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception exception) {
                    Log.d("getList", "exception==" + exception);
                } finally {
                    if (cursor != null) cursor.close();
                }
                e.onNext(group);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Group<VideoEntity>>() {

                    @Override
                    public void accept(Group<VideoEntity> entity) throws Exception {
                        dismissProgressDialog();
                        if (entity != null) {
                            adapter.setGroup(group);
                            popListAdapter.setGroup(map);
                        }
                    }
                });

    }

    private void packagePath(String path, VideoEntity entity) {
        File file = new File(path);
        String parentName = "";
        if (file.getParentFile() != null) {
            parentName = file.getParentFile().getName();
        } else {
            parentName = file.getName();
        }
        if (map == null) map = new HashMap<>();
        Log.d("200", "path string == " + path + "\n parent name == " + parentName);
        if (map.containsKey(parentName)) {
            Group<VideoEntity> group = map.get(parentName);
            group.add(entity);
        } else {
            Group<VideoEntity> group = new Group<>();
            group.add(entity);
            map.put(parentName, group);
        }
        Log.d("200", "map size == " + map.size());
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = DensityUtil.dip2px(MyhomeSelectVideoActivity.this, 1);
            outRect.right = DensityUtil.dip2px(MyhomeSelectVideoActivity.this, 1);
            outRect.top = DensityUtil.dip2px(MyhomeSelectVideoActivity.this, 1);
            outRect.bottom = DensityUtil.dip2px(MyhomeSelectVideoActivity.this, 1);

        }
    }

    private class PopListAdapter extends BaseImageLoaderAdapter<VideoEntity> {
        private ArrayList<String> titleList;
        private ArrayList<Group<VideoEntity>> groups;

        public void setGroup(HashMap<String, Group<VideoEntity>> map) {
            titleList = new ArrayList<>();
            titleList.add("全部视频");
            groups = new ArrayList<>();
            if (map != null && map.size() > 0) {
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    titleList.add(key);
                    Group<VideoEntity> group = map.get(key);
                    groups.add(group);
                }
            }
        }

        public Group<VideoEntity> getVideoGroup(int position) {
            return groups.get(position - 1);
        }

        public String getTitleInList(int position) {
            return titleList.get(position);
        }

        @Override
        public int getCount() {
            return titleList == null ? 0 : titleList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = InflaterUtils.inflateView(MyhomeSelectVideoActivity.this, R.layout.myhome_pop_select_item);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.setData(position);

            return convertView;
        }

        class ViewHolder {

            ImageView ivPhoto;
            TextView tvName;
            TextView tvDesc;

            private ViewHolder(View view) {
                ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
                ivPhoto.setVisibility(View.GONE);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            }

            public void setData(int position) {
                tvName.setText(titleList.get(position));
                if (position == 0) {
                    tvDesc.setText("共" + (MyhomeSelectVideoActivity.this.group.size()-1) + "个");
                } else {
                    tvDesc.setVisibility(View.VISIBLE);
                    tvDesc.setText("共" + groups.get(position - 1).size() + "个");
                }
            }
        }
    }
}
