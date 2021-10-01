package com.procoin.module.dialog;

import com.procoin.http.base.TaojinluType;

public class ManagerCircleItemType implements TaojinluType {


    public String text;
    public int type;//

    public ManagerCircleItemType(String text,int type){
        this.text=text;
        this.type=type;
    }


}
