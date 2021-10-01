package com.procoin.common.interfaces;

import com.procoin.common.entity.PervalResponse;
import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 17-5-23.
 */

public interface PervalDataCallBack<T> extends TaojinluType {
    void onResponse(PervalResponse<T> var1);
}
