package com.procoin.subpush;

public enum ReceiveModelTypeEnum {
    default_0(0),
    sys_auto_push(10), // 官方推送
    connection_suc(200),
    connection_error(400), // 用户退出
    private_chat_record(2300), //    私聊返回码
    circle_chat_record(3000),// 圈子聊天信息记录返回码
    circle_list(3100), // 用户订阅的圈子的圈子列表(修改圈名也会返回)
    circle_role_list(3200),// 用户订阅的圈子的角色变化(比如设置/取消管理员)
    circle_nosub_list(3400), //用户退出的圈子的圈子列表(比如被T除)
    circle_speak_status(3500),// 圈子禁言状态
    circle_config_list(3600), //圈子配置信息(成员申请数、消息免打扰等等)(列表)

    refresh_entrust_order_api(5000), // 推送前端刷新当前委托列表（包括委托成功。撤销。成交都会返回这个状态，前端收到这个推送就进行相应的刷新）


    game_predict_main(20000), // 闪电预测主数据(分时数据、是否停止、往期预测结果、我是否预测等)
    game_predict_user(20001), // 闪电预用户信息(能力值、KBT数量)
    game_predict_tickets(20002), // 闪电预入场券列表
    game_predict_chat(20003),// 闪电预测聊天
//    game_predict_add_abilityvalue(20004),// 推送增加能力值
    game_kbt_not_enough(20005);// KBT购买刻豆时不足提示



    private final int type;

    ReceiveModelTypeEnum(int type) {
        this.type = type;
    }

    public int type() {
        return type;
    }

    public static ReceiveModelTypeEnum getReceiveModelTypeEnum(int type) {
        for (ReceiveModelTypeEnum receiveModelTypeEnum : values()) {
            if (receiveModelTypeEnum.type == type) return receiveModelTypeEnum;
        }
        return default_0;
    }
}