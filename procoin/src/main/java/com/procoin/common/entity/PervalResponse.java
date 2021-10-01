package com.procoin.common.entity;

/**
 * 接口很多,所以返回泛型,让后面自己去解析
 * Created by zhengmj on 17-5-23.
 */

public class PervalResponse<T> {
    private T result;

    public PervalResponse(T var2) {
        this.result = var2;
    }

    public T getResult() {
        return this.result;
    }
}
