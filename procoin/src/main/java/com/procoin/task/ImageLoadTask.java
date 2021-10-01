package com.procoin.task;

/**
 * ImageLoadTask.java
 * ImageSelector
 * <p/>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.procoin.module.myhome.entity.ImageGroup;
import com.procoin.http.base.Group;
import com.procoin.module.myhome.entity.ImageSelectGroup;

import java.io.File;

/**
 * 使用contentProvider扫描图片异步任务
 */
public class ImageLoadTask extends BaseAsyncTask<Void, Void, Boolean> {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 存放图片<文件夹,该文件夹下的图片列表>键值对
     */
    private Group<ImageGroup> mGruopList = new Group<ImageGroup>();

    private Group<ImageSelectGroup> allPic=new Group<>();

    public ImageLoadTask(Context context) {
        super();
        mContext = context;
    }

    public Group<ImageGroup> getmGruopList() {
        return mGruopList;
    }

    public Group<ImageSelectGroup> getAllPic() {
        return allPic;
    }

    public void setmGruopList(Group<ImageGroup> mGruopList) {
        this.mGruopList = mGruopList;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        long startTime=System.currentTimeMillis();
        Log.d("ImageLoadTask","startTime="+startTime);
        Uri mImageUri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();
        // 构建查询条件，且只查询jpeg和png的图片
        StringBuilder selection = new StringBuilder();
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");

        Cursor mCursor = null;
        try {
            // 初始化游标
            mCursor = mContentResolver.query(mImageUri, null, selection.toString(), new String[]{"image/jpeg", "image/png"}, Media.DATE_TAKEN + " DESC");
            // 遍历结果
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(Media.DATA));
                Log.d("ImageLoadTaskPath","path="+path);

                allPic.add(new ImageSelectGroup(path));

                // 获取该图片的所在文件夹的路径
                File file = new File(path);
                String parentName = "";
                if (file.getParentFile() != null) {
                    parentName = file.getParentFile().getName();
                } else {
                    parentName = file.getName();
                }
                // 构建一个imageGroup对象
                ImageGroup item = new ImageGroup();
                // 设置imageGroup的文件夹名称
                item.setDirName(parentName);

                // 寻找该imageGroup是否是其所在的文件夹中的第一张图片
                int searchIdx = mGruopList.indexOf(item);
                if (searchIdx >= 0) {
                    // 如果是，该组的图片数量+1
                    ImageGroup imageGroup = mGruopList.get(searchIdx);
                    imageGroup.addImage(path);
                } else {
                    // 否则，将该对象加入到groupList中
//                    item.addCameraImage();
                    item.addImage(path);
                    mGruopList.add(item);
                }
            }
            Log.d("ImageLoadTask","历时="+(System.currentTimeMillis()-startTime));

        } catch (Exception e) {
            // 输出日志
            return false;
        } finally {
            // 关闭游标
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }
        if (mGruopList.size() == 0) {
            // 构建一个imageGroup对象
            ImageGroup item = new ImageGroup();
//            item.addCameraImage();
            mGruopList.add(item);
        }
        return true;
    }
}
