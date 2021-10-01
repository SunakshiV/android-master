package com.procoin.module.publish.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.RecyclerView;

import com.procoin.R;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.view.RoundAngleImageView;
import com.procoin.widgets.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengmj on 18-11-9.
 */

public class EditPhotoAdapter2 extends RecyclerView.Adapter {
    private Context context;
    private List<String> imgList = new ArrayList<>();
    private int maxPicCount;
    private Callback callback;
    private TjrImageLoaderUtil tjrImageLoaderUtil;

    public List<String> getImgList() {
        return imgList;
    }

    public List<String> getUploadImgList() {//更新项目的话用这个，因为要过滤http开头的
        List<String> uploadFile = new ArrayList<>();
        for (String file : imgList) {
            if (!file.startsWith("http")) {
                uploadFile.add(file);
            }
        }
        return uploadFile;
    }


    public List<String> getOldNetworkList() {//更新项目的话用这个，获取http开头的
        List<String> networkList = new ArrayList<>();
        for (String file : imgList) {
            if (file.startsWith("http")) {
                networkList.add(file);
            }
        }
        return networkList;
    }

    public EditPhotoAdapter2(Context context, int maxPicCount, Callback callback) {
        this.context = context;
        this.maxPicCount = maxPicCount;
        this.callback = callback;
    }


    private String getItem(int pos) {
        if (imgList.size() > 0) {
            return imgList.get(pos);
        }
        return "";
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
        notifyDataSetChanged();
    }

    public void addImgList(List<String> addImgList) {
        imgList.addAll(addImgList);
        notifyDataSetChanged();
    }

    public void addImg(String flist) {
        imgList.add(flist);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater.from(context).inflate(R.layout.item_edit_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PhotoHolder photoHolder = (PhotoHolder) holder;
        if (position == getItemCount() - 1 && imgList.size() < maxPicCount) {
            photoHolder.civ_delete.setVisibility(View.GONE);
            photoHolder.ivImg.setVisibility(View.GONE);
            photoHolder.ivSelect.setVisibility(View.VISIBLE);
            photoHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onSelectPicClick();
                    }
                }
            });
        } else {
            photoHolder.civ_delete.setVisibility(View.VISIBLE);
            photoHolder.ivImg.setVisibility(View.VISIBLE);
            photoHolder.ivSelect.setVisibility(View.GONE);
            final String fileStr = getItem(position);
            if (fileStr.startsWith("http")) {
                if (tjrImageLoaderUtil == null)
                    tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic);
                tjrImageLoaderUtil.displayImage(fileStr, photoHolder.ivImg);
            } else {
                photoHolder.ivImg.setImageURI(Uri.fromFile(new File(fileStr)));
            }
            photoHolder.civ_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setTitle("提示").setMessage("移除这张照片吗?").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (imgList.remove(fileStr)) {
                                notifyDataSetChanged();
                                File file = new File(fileStr);
                                if (file.exists()) {
                                    file.delete();
                                }
                            }

                        }
                    }).create().show();

                }
            });
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return Math.min(imgList.size() + 1, maxPicCount);
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private RoundAngleImageView ivImg;
        private CircleImageView civ_delete;
        private ImageView ivSelect;

        public PhotoHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.ivImg);
            civ_delete = itemView.findViewById(R.id.civ_delete);
            ivSelect = itemView.findViewById(R.id.ivSelect);
        }
    }

    public interface Callback {
        void onSelectPicClick();
    }
}
