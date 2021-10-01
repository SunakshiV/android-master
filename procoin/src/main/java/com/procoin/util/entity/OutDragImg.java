package com.procoin.util.entity;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 19-4-17.
 */

public class OutDragImg implements TaojinluType {
//    locationy: "70",
//    smallImgName: "http://192.168.0.223/imredz/tempdragimg/small_5_70_f0935e4cd5920aa6.png",
//    bigImgName: "http://192.168.0.223/imredz/tempdragimg/big_5_70_f0935e4cd5920aa6.png",
//    sourceImgName: "http://192.168.0.223/imredz/sourcedragimg/5.png",

//    sourceImgWidth: "297",
//    smallImgHeight: "60",
//    smallImgWidth: "60",
//    sourceImgHeight: "185",

//    dragImgKey: "BtbIoxibmesC7mk5IbgP_goVGe5TlffrUl"

    public String locationy;
    public String smallImgName;
    public String bigImgName;
    public String sourceImgName;

    public String sourceImgWidth;
    public String smallImgHeight;
    public String smallImgWidth;
    public String sourceImgHeight;

    public String dragImgKey;

    @Override
    public String toString() {
        return "locationy=="+locationy+" smallImgName=="+smallImgName+" bigImgName=="+bigImgName+"  sourceImgName=="+sourceImgName+"  sourceImgWidth=="+sourceImgWidth+"  smallImgHeight=="+smallImgHeight+"  smallImgWidth=="+smallImgWidth+"  sourceImgHeight=="+sourceImgHeight+"  dragImgKey=="+dragImgKey;
    }
}
