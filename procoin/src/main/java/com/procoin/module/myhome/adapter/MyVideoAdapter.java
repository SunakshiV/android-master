package com.procoin.module.myhome.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.myhome.entity.VideoEntity;
import com.procoin.http.base.Group;
import com.procoin.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengmj on 18-11-15.
 */

public class MyVideoAdapter extends BaseImageLoaderRecycleAdapter<VideoEntity> {
    private Context context;
    private OnItemclickCallback onItemclickCallback;
    public MyVideoAdapter(Context context){
        this.context = context;
        Log.d("200","videoList adapter被创建了");
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("200","videoList onCreateViewHolder size == "+getGroup().size());
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.myhome_video_item,parent,false));
    }

    @Override
    public void setGroup(Group<VideoEntity> g) {
        Log.d("200","videoList setGroup被调用了");
        super.setGroup(g);
        Log.d("200","videoList setGroup size == "+getGroup().size()+" getItemCount == "+getItemCount());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("200","videoList onBindViewViewHolder size == "+getGroup().size());
        final VideoEntity entity = getItem(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        if (!TextUtils.isEmpty(entity.coverPath)){
            if (entity.coverPath.equals("default")){
                viewHolder.bg.setVisibility(View.GONE);
                viewHolder.ivView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_redz_shooting_2));
            }else {
                viewHolder.bg.setVisibility(View.VISIBLE);
//                viewHolder.ivView.setImageURI(Uri.fromFile(new File(entity.coverPath)));
                displayImage("file://"+entity.coverPath,viewHolder.ivView);
            }
        }
        else {
            viewHolder.ivView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_common_mic));
                Observable.create(new ObservableOnSubscribe<Bitmap>() {
                    @Override
                    public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                        Bitmap bitmap = null;
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        try {
                            //根据url获取缩略图
                            retriever.setDataSource(entity.videoPath);
                            //获得第一帧图片
                            bitmap = retriever.getFrameAtTime();
                        } catch (Exception ex) {

                        } finally {
                            retriever.release();
                        }

                        if (bitmap != null) e.onNext(bitmap);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Bitmap>() {
                            @Override
                            public void accept(Bitmap bitmap) throws Exception {
                                if (bitmap != null) {
                                    viewHolder.ivView.setImageDrawable(new BitmapDrawable(bitmap));
                                }
                            }
                        });


        }
        long time = entity.duration/1000;
        String timeLabel = String.valueOf(time);
        if (time < 60){
            //少于一分钟
            if (timeLabel.length() == 1){
                timeLabel = "0"+timeLabel;
            }
            timeLabel = "0:"+timeLabel;
        }else {
            if (time<60*60){//小于一小时
                String seconds = String.valueOf(time%60);
                if (seconds.length() == 1){
                    seconds = seconds + "0";
                }
                timeLabel = time/60+":"+seconds;
            }
        }
        viewHolder.tv_duration.setText(timeLabel);
        viewHolder.ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemclickCallback!=null)
                {
                    onItemclickCallback.onitemClick(entity.videoPath);
                }
            }
        });


    }


    private class ViewHolder extends RecyclerView.ViewHolder{
        private FrameLayout bg;
        private ImageView ivView;
        private TextView tv_duration;
        public ViewHolder(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            ivView = itemView.findViewById(R.id.ivView);
            tv_duration = itemView.findViewById(R.id.tv_duration);
        }
    }
    public void setOnItemclickCallback(OnItemclickCallback onItemclickCallback) {
        this.onItemclickCallback = onItemclickCallback;
    }
    public interface OnItemclickCallback {
        void onitemClick(String path);
    }

}
