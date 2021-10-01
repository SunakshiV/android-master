package com.procoin.http.model;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * DNS
 */
public class DnsInfo implements TaojinluType {

    public String quoteSocket;
    public String pushSocket;
    public String api;
//    public String predictSocket;

    public DnsInfo(String quoteSocket, String pushSocket, String api) {
        this.quoteSocket = quoteSocket;
        this.pushSocket = pushSocket;
        this.api = api;
//        this.predictSocket = predictSocket;
    }
}
