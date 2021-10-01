package com.procoin.widgets.multimedia;

import com.procoin.http.base.TaojinluType;

/**
 * 多媒体信息实体
 * Created by kechenng on 17-6-6.
 */

public class MultimediaEntity implements TaojinluType {
    public int media_type;  // 0:图片 1:视频
    public String imageUrl;  //图片链接
    public String videoUrl;  //视频链接

}
