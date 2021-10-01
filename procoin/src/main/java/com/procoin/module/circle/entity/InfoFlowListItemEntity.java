package com.procoin.module.circle.entity;


import com.procoin.http.base.TaojinluType;

public class InfoFlowListItemEntity implements TaojinluType {

    public long infoId;// 对应数据库里key
    public String circleNum; // 圈号,唯一
    public long userId; // 即user_id
    public String name; // 用户名
    public String headurl; // 头像路径
    public int isVip; // 0代表不是v，1代表是v
    public String title;// 标题
    public String content;// 内容
    public String picUrl;// 图片路径,json格式
    public String fileUrl;// 文件路径,json
    public String pkg; // 哪个包的下 如com.cropyme
    public String cls; // 跳到哪个activity
    public String pview; // iphone跳转页面名称
    public String params; // 要求json格式
    public int infoType;// 资讯类型   1 是微访谈   0是资讯
    public int isDelete;// 是否已删除
    public String createTime;
    public boolean isPush; //授权前端可用push处理

}






















