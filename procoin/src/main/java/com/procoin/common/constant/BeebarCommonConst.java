package com.procoin.common.constant;

/**
 * Created by zhengmj on 18-8-17.
 * 用来储存错综复杂的常量
 */

public class BeebarCommonConst {
    public final static int DEFAULT_REQUEST_CODE = 0x000;//这个请求码只是用作填充requestCode的，与后续代码没有任何关联
    public final static int RESULT_REFRESH_ARTICLE_LIST = 0x001;//从文章内容页(ShowArticleActivity或者ShowQuestionActivity)作出操作行为后返回到HomeActivity的resultCode
    public final static int REQUEST_OPEN_ARTICLE_EDIT = 0x002;//列表item下方的评论数点击时的请求码，收到该请求码时内容页会弹出输入框
    public final static int REQUEST_OPEN_SHARE = 0x003;//列表item下方的分享点击时的请求码，收到该请求码时内容页会弹出分享框
    public final static int RESULT_REFRESH_BLOCK = 0x004;//当用户订阅的版块数量或者顺序发生变化时会返回该返回码，HomeFragment就会调用接口刷新用户的订阅列表
    public final static int RESULT_PARSE_BLOCK = 0x005;//从NewSelectBlockActivity跳到SearchBlockActivity后，点选了一个Block之后返回该返回码，NewSelectBlockActivity会对应增加一个Block
    public final static int RESULT_FOLLOW_BLOCK = 0x006;//在SearchBlockActivity跳到版块详情，点击关注版块后返回该返回码
    public final static int RESULT_PARSE_VLIST = 0x007;//在选择大V界面将列表传回AskForQuestionActivity时用到
    public final static int RESULT_FINISH_SEND = 0x008;//没用的
    public final static int RESULT_APPEND_V = 0x009;//追加大V会返回该返回码
    public final static int RESULT_REFRESH_REVIEW_LIST = 0x010;
    public final static int RESULT_HTTP_REFRESH_REVIEW_LIST = 0x011;
    public final static int TYPE_SHIELD_SOMEONE = 0x012;//显示屏蔽功能
    public final static int RESULT_MOGUL_REFRESH = 0x013;//大佬说的返回码
}
