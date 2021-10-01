package com.procoin.module.myhome.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p/>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;

/**
 * 一级GridView中每个item的数据模型
 *
 * @author likebamboo
 */
public class ImageGroup implements TaojinluType {
    /**
     * 文件夹名
     */
    private String dirName = "";

    /**
     * 文件夹下所有图片
     */
    private Group<ImageSelectGroup> images = new Group<ImageSelectGroup>();

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    /**
     * 获取第一张图片的路径(作为封面)
     *
     * @return
     */
    public String getFirstImgPath() {
        if (images.size() > 0) {
            return images.get(0).getPathStr();
//            if (images.get(0).getType() == 0) {
//                return images.get(0).getPathStr();
//            } else if (images.size() > 1) {
//                return images.get(1).getPathStr();
//            }
        }
        return "";
    }

    /**
     * 获取图片数量
     *
     * @return
     */
    public int getImageCount() {
        return images.size();
    }

    public Group<ImageSelectGroup> getImages() {
        return images;
    }

    /**
     * 添加一张图片
     *
     * @param image
     */
    public void addImage(String image) {
        if (images == null) {
            images = new Group<ImageSelectGroup>();
        }
        ImageSelectGroup selItem = new ImageSelectGroup();
//        selItem.setPathStr("file://" + image);
        selItem.setPathStr(image);
        images.add(selItem);
    }

//    public void addCameraImage() {
//        if (images == null) {
//            images = new Group<ImageSelectGroup>();
//        }
//        ImageSelectGroup selItem = new ImageSelectGroup();
//        selItem.setType(1);
//        selItem.setPathStr("拍摄照片");
//        images.add(selItem);
//    }

    @Override
    public String toString() {
        return "ImageGroup [firstImgPath=" + getFirstImgPath() + ", dirName=" + dirName + ", imageCount=" + getImageCount() + "]";
    }

    /**
     * <p/>
     * 重写该方法
     * <p/>
     * 使只要图片所在的文件夹名称(dirName)相同就属于同一个图片组
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageGroup)) {
            return false;
        }
        return dirName.equals(((ImageGroup) o).dirName);
    }
}
