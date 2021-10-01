package com.procoin.module.circle.util;

/**
 * Created by zhengmj on 16-3-23.
 */
public enum TjrSoftkeyBoardStateEnum {

//    private static final int SHOW_NOTHING = -1;// 下面什么都不显示
//    private static final int SHOW_VOICE = 0;// 下面显示发语音
//    private static final int SHOW_FACE = 1;// 下面显示发表情
//    private static final int SHOW_MORE = 2;// 下面显示发更多

    showNothing(-1), showVoice(0), showFace(1), showMore(2);
    private final int currShow;

    TjrSoftkeyBoardStateEnum(int currShow) {
        this.currShow = currShow;
    }

    public int getCurrShow() {
        return currShow;
    }

    public static TjrSoftkeyBoardStateEnum getDefault(){
        return showNothing;
    }

}
