package com.procoin.subpush;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.procoin.common.constant.CommonConst;
import com.procoin.data.db.TaoJinLuDatabase;
import com.procoin.data.sharedpreferences.CircleSharedPreferences;
import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.module.circle.entity.CircleChatTypeEnum;
import com.procoin.module.circle.entity.CircleRel;
import com.procoin.module.circle.entity.SocketFail;
import com.procoin.module.circle.entity.parser.SocketFailParser;
import com.procoin.subpush.notify.NotifyManager;
import com.procoin.subpush.notify.NotifyModel;
import com.procoin.util.CommonUtil;
import com.procoin.util.JsonParserUtils;
import com.procoin.module.circle.entity.CircleConfig;
import com.procoin.module.circle.entity.parser.CircleChatEntityParser;
import com.procoin.MainApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.CircleChatHttp;
import com.procoin.http.tjrcpt.SubPushHttp;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.module.circle.entity.CircleInfo;
import com.procoin.subpush.notify.NotifyModelParser;
import com.procoin.subpush.notify.TPushSettingManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceivedManager extends Observable {
    private volatile boolean isRunService;
    private volatile static ReceivedManager instance;
    private volatile IRemoteService iRemoteService;
    private TaoJinLuDatabase db;
    private long userId;
    private Context context;
    //    private final BlockingQueue<ReceiveModel> dbQueue;
    private final CircleChatEntityParser chatParser;
    private final NotifyModelParser notifyModelParser;
    private final SocketFailParser socketFailParser;
    private Gson gson = null;


    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.d("ReceivedManager", "onServiceConnected.......user_name:" + name + "  service=" + service);
            iRemoteService = IRemoteService.Stub.asInterface(service);
            try {
                iRemoteService.registerReceivedCallback(mCallback);
            } catch (RemoteException e) {
//                Toast.makeText(context, "绑定回调数据失败:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ReceivedManager", "onServiceDisconnected.......user_name:" + name);
        }
    };
    private IReceivedCallback mCallback = new IReceivedCallback.Stub() {

        @Override
        public void onReceivedJson(String json) throws RemoteException {
            Log.d("ReceivedManager", "mCallback IReceivedCallback.......回调数据:" + json);
            doReceivedJson(json);
        }
    };

    /**
     * 总解析回调数据方法
     *
     * @param json
     * @throws RemoteException
     */
    public void doReceivedJson(final String json) throws RemoteException {
        Log.d("ReceivedManager", "doReceivedJson is " + json);
        if (Consts.CONNECTION_ERROR.equals(json)) {//TODO 链接失败
            sendNotify(new ReceiveModel(ReceiveModelTypeEnum.connection_error.type(), null));
        } else if (Consts.REQDO_GETDATA.equals(json)) {//TODO 服务器通知你获取数据 /data/command?
            //CircleHttp.getInstance().connectGetData(userId)
            if (iRemoteService != null)
                iRemoteService.send(SubPushHttp.getInstance().connectGetDataUrl(userId));
        } else if (Consts.REQDO_OK.equals(json)) {// TODO 链接成功
            sendNotify(new ReceiveModel(ReceiveModelTypeEnum.connection_suc.type(), null));
            Log.d("ReceivedManager", "11111111111Consts.REQDO_OK:" + json);
            Group<CircleInfo> g = db.getMyCircleOnBG(userId);
            StringBuffer sb = new StringBuffer();
            if (g != null && g.size() > 0) {
                for (CircleInfo circleInfo : g) {
                    TPushSettingManager.getInstance().resetCircleName(circleInfo.circleId, circleInfo.circleName);
                    if (sb.length() == 0)
                        sb.append(circleInfo.circleId + ":" + circleInfo.circleId);
                    else
                        sb.append("," + circleInfo.circleId + ":" + circleInfo.synMark);
                }
            }
            if (sb.length() == 0) sb.append("0");
            Log.d("ReceivedManager", "22222222222Consts.REQDO_OK:" + json + "  sb.toString()=" + sb.toString());
            //同步圈子mark
            if (iRemoteService != null)
                iRemoteService.send(CircleChatHttp.getInstance().sendSynjoin(userId, sb.toString()));
        } else {
            // TODO 统一解析回调数据,包括存数据库
            try {
                JSONObject jsonObject = new JSONObject(json);
                if (JsonParserUtils.hasNotNullAndIsIntOrLong(jsonObject, "type")) {
                    int type = jsonObject.getInt("type");
                    boolean success = jsonObject.getBoolean("success");
                    Log.d("receiveType", "type==" + type + "  success=" + success);
                    String receive = null;// 接收的数据，是字符串类型
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "receive")) {
                        receive = jsonObject.getString("receive");
                        Log.d("receiveType", "receive==" + receive);
                    }
                    if (!success) {//false 代表发送失败  {"receive":"{\"code\":40000,\"msg\":\"请求失败,请重新请求\",\"success\":false}","success":false,"type":214}
                        if (!TextUtils.isEmpty(receive)) {
                            SocketFail socketFail = socketFailParser.parse(new JSONObject(receive));
                            Log.d("SocketFail", " success:" + socketFail.success + " msg:" + socketFail.msg);
                            CommonUtil.LogLa(2, "SocketFail success:" + socketFail.success + " msg:" + socketFail.msg);
                            sendNotify(new ReceiveModel(type, socketFail));
                            CommonUtil.logoutToLoginActity(socketFail.code, socketFail.msg);
                        }
                    } else {
                        if (!TextUtils.isEmpty(receive)) {
                            Log.d("154", "success type == " + type);
                            switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(type)) {
                                case sys_auto_push:
                                    Log.d("sys_tjrpush", "sys_tjrpush ==" + receive);
                                    NotifyModel notifyModel = notifyModelParser.parse(new JSONObject(receive));
                                    sendNotify(new ReceiveModel(type, notifyModel));//
                                    NotifyManager.getInstance().notifyTjrPushReceiver(context, userId, receive);
                                    break;
                                case private_chat_record:// 一条私聊
                                    Log.d("private_chat_record", "receive==" + receive);
                                    CircleChatEntity privatechat = chatParser.parsePrivateChat(new JSONObject(receive));
//                                    CircleSharedPreferences.saveCircleSettingRemind(context, userId, privatechat.chatTopic, privatechat.isPush);
                                    if (privatechat.userId != userId) {
                                        db.saveOrUpdateChatInfo(userId, privatechat.userId, privatechat.headurl, privatechat.name, privatechat.chatTopic);
                                        String chatTopic = CircleSharedPreferences.getCircleSpChatRoomid(context);
                                        if (!chatTopic.equals(privatechat.chatTopic)) { //如果是当前房间的话就没必要添加私聊数了
                                            PrivateChatSharedPreferences.updatePriChatRecordNum(context, privatechat.chatTopic, userId, 1);
                                        }
//                                        PrivateChatSharedPreferences.addChatRoom(context,userId,privatechat.userId,privatechat.name,privatechat.headurl);
                                        if (CircleChatTypeEnum.isVoice(privatechat.say)) {
                                            privatechat.voiceIsRed = 1;
                                        } else if (CircleChatTypeEnum.isText(privatechat.say)) {
//                                            saveIsAtMe(privatechat);
                                        }
                                    }
                                    db.saveAndUpdateUserInfo(privatechat.userId, privatechat.name, privatechat.headurl);
                                    privatechat.type = CircleChatTypeEnum.gettValue(privatechat.say);
                                    db.insertPrivateChat(userId, privatechat);
                                    sendNotify(new ReceiveModel(type, privatechat));//
                                    NotifyManager.getInstance().notifyChatReceiver(context, privatechat, userId, true);

                                    break;
                                case circle_chat_record://圈子聊天
                                    Log.d("circle_chat_record", "receive==" + receive);
                                    CircleChatEntity circleChatEntity = chatParser.parsePrivateChat(new JSONObject(receive));
//                                    CircleSharedPreferences.saveCircleSettingRemind(context, userId, circleChatEntity.chatTopic, circleChatEntity.isPush);
                                    if (circleChatEntity.userId != userId) {
                                        String circleId = CircleSharedPreferences.getCircleSpChatRoomid(context);
                                        if (!circleId.equals(circleChatEntity.chatTopic)) {//如果是当前房间的话就没必要添加私聊数了
                                            CircleSharedPreferences.updateChatRecordNum(context, circleChatEntity.chatTopic, userId, 1);
                                        }
//                                        PrivateChatSharedPreferences.addChatRoom(context,userId,privatechat.userId,privatechat.name,privatechat.headurl);
                                        if (CircleChatTypeEnum.isVoice(circleChatEntity.say)) {
                                            circleChatEntity.voiceIsRed = 1;
                                        } else if (CircleChatTypeEnum.isText(circleChatEntity.say)) {
                                            saveIsAtMe(circleChatEntity);
                                        }
                                    }
                                    db.saveAndUpdateUserInfo(circleChatEntity.userId, circleChatEntity.name, circleChatEntity.headurl, circleChatEntity.isVip);
                                    db.updateCircleRelTableRecent_time(String.valueOf(userId), circleChatEntity.chatTopic, circleChatEntity.createTime);
                                    circleChatEntity.type = CircleChatTypeEnum.gettValue(circleChatEntity.say);
                                    long ret = db.insertCircleChat(userId, circleChatEntity);
                                    Log.d("circle_chat_record", "insertCircleChat ret==" + ret);
                                    sendNotify(new ReceiveModel(type, circleChatEntity));//
                                    NotifyManager.getInstance().notifyChatReceiver(context, circleChatEntity, userId, false);

                                    break;
                                case circle_list://用户订阅的圈子的圈子列表
                                    Log.d("circle_sub_list", "receive==" + receive);
                                    Group<CircleInfo> myAllCircleGroup = getGson().fromJson(receive, new TypeToken<Group<CircleInfo>>() {
                                    }.getType());
                                    if (myAllCircleGroup != null && myAllCircleGroup.size() > 0) {
                                        for (CircleInfo circleInfo : myAllCircleGroup) {
                                            long insertResult = db.insertMyCircle(circleInfo);
                                            //这里只是插入圈子，circleRel表在circle_role_list里面插入，所以在这里调用db.getMyCircle(userId)是查不出来的
//                                            long insertCircleRelResult = db.insertCircleRel(circleInfo.circleId, userId, CircleRoleEnum.member.role());//role这里默认传0，然后在circle_role_list在更新
                                            Log.d("ReceivedManager", "insertMyCircle insertResult==" + insertResult);
                                        }
//                                        Group<CircleInfo> group = db.getMyCircle(userId);
//                                        Log.d("ReceivedManager", "insertMyCircle getMyCircle ==group.size==" + (group == null ? 0 : group.size()));
                                        sendNotify(new ReceiveModel(type, myAllCircleGroup));
                                    }
                                    break;
                                case circle_role_list://用户订阅的圈子的角色变化 [{"circleId":31,"role":10,"roleName":"管理员","userId":80805}]
                                    Log.d("circle_role_list", "receive==" + receive);
                                    Group<CircleRel> myCircleRoleGroup = getGson().fromJson(receive, new TypeToken<Group<CircleRel>>() {
                                    }.getType());
                                    if (myCircleRoleGroup != null && myCircleRoleGroup.size() > 0) {
                                        for (CircleRel circleRel : myCircleRoleGroup) {
                                            db.insertAndUpdateCircleRel(circleRel.circleId, circleRel.userId, circleRel.role);
                                        }
                                        sendNotify(new ReceiveModel(type, myCircleRoleGroup));
                                    }
                                    break;
                                case circle_nosub_list://用户退出的圈子的圈子列表
                                    Log.d("circle_nosub_list", "receive==" + receive);
                                    String[] circleIdList = getGson().fromJson(receive, new TypeToken<String[]>() {
                                    }.getType());
                                    Log.d("circle_nosub_list", "circleIdList==" + circleIdList.length);
                                    if (circleIdList != null && circleIdList.length > 0) {
                                        for (String circleId : circleIdList) {
                                            int delCircleRel = db.delCircleRel(circleId, userId);
                                            Log.d("circle_nosub_list", "delCircleRel==" + delCircleRel);
                                            CircleSharedPreferences.clearChatRecordNum(context, circleId, userId);
                                        }
                                        sendNotify(new ReceiveModel(type, circleIdList));
                                    }
                                    break;
                                case circle_speak_status:
                                    Log.d("circle_speak_status", "receive==" + receive);
                                    sendNotify(new ReceiveModel(type, receive));
                                    break;
                                case circle_config_list:
                                    Log.d("circle_config_list", "receive==" + receive);
                                    Group<CircleConfig> circleConfigs = getGson().fromJson(receive, new TypeToken<Group<CircleConfig>>() {
                                    }.getType());
                                    for (CircleConfig circleConfig : circleConfigs) {
                                        if (circleConfig.userId == userId) {//这里只保存消息免打扰和成员申请数量
                                            CircleSharedPreferences.saveCircleSettingRemind(context, userId, circleConfig.circleId, circleConfig.msgAlert == 1 ? true : false);
                                            CircleSharedPreferences.saveApplyCount(context, circleConfig.circleId, userId, circleConfig.newApplyAmount);
                                        }
                                    }
                                    sendNotify(new ReceiveModel(type, circleConfigs));
                                    break;
                                case refresh_entrust_order_api:
                                    Log.d("refresh_entrust_order", "receive==" + receive);
                                    sendNotify(new ReceiveModel(type, receive));
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                Log.d("SubPushService", "doReceivedJson....Exception:" + e.getMessage());
                Log.d("push_record_list", "doReceivedJson....Exception:" + e.getMessage());
            }
        }

    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private void saveIsAtMe(CircleChatEntity chat) {
        if (chat == null) return;
        if (CircleSharedPreferences.getCircleAt(context, chat.chatTopic, userId) == 1)
            return;//已经有人@我了，就不用重复了
        //@阿青
        try {
            String regex3 = CommonConst.AT_MATCHES;//
            Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
            Matcher ma3 = pa3.matcher(chat.say);
            while (ma3.find()) {
                if (ma3.groupCount() == 2) {
                    Log.d("Matcher", "ma3.group(0)==" + ma3.group(0) + " ma3.group(1)==" + ma3.group(1) + " ma3.group(2)==" + ma3.group(2) + " circleName==" + chat.chatTopic);
                    String atJson = ma3.group(2);
                    if (!TextUtils.isEmpty(atJson)) {
                        JSONObject jsonObject1 = new JSONObject(atJson);
                        if (JsonParserUtils.hasAndNotNull(jsonObject1, "params")) {
                            String p = jsonObject1.getString("params");
                            Log.d("saveIsAtMe", "p==" + p);
                            JSONObject paramsJson = new JSONObject(p);
                            if (JsonParserUtils.hasNotNullAndIsIntOrLong(paramsJson, CommonConst.USERID)) {
                                long atUserId = paramsJson.getLong(CommonConst.USERID);
                                if (atUserId == userId) {//有人@我了
                                    CircleSharedPreferences.saveCircleAt(context, chat.chatTopic, userId, 1);
                                    break;//没必要重复了

                                }
                            }
                        }

                    }
                }
            }
        } catch (JSONException e) {
            Log.d("saveIsAtMe", "exception==" + e.toString());
        }

    }

    public ReceivedManager(Context context) {
        this.context = context;
//        dbQueue = new ArrayBlockingQueue<ReceiveModel>(5000);
        db = ((MainApplication) context.getApplicationContext()).getmDb();
        chatParser = new CircleChatEntityParser();
        notifyModelParser = new NotifyModelParser();
        socketFailParser = new SocketFailParser();
//        startRunDbTask();
    }

    /**
     * 入库处理
     */
//    @SuppressWarnings("unchecked")
//    private void startRunDbTask() {
//        ThreadManager.submit(new Runnable() {
//            @Override
//            public void run() {
//                for (; ; ) {
//                    try {
//                        ReceiveModel receiveModel = dbQueue.take();
//                        if (receiveModel != null) {
//                            switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(receiveModel.type)) {
//                                case private_chat_record:// privateChat 一条私聊
//                                    if (receiveModel.obj instanceof CircleChatEntity) {
//                                        CircleChatEntity circleChatEntity = (CircleChatEntity) receiveModel.obj;
//                                        if (circleChatEntity != null) {
////                                                        Log.d("userInfo","type=="+ReceiveModelTypeEnum.chat_list.type()+"   useId=="+circleChatEntity.userId+"  userName=="+circleChatEntity.user_name);
//                                            if (userId != circleChatEntity.userId) {
//                                                db.saveOrUpdateChatInfo(userId, circleChatEntity.userId, circleChatEntity.headurl, circleChatEntity.name, circleChatEntity.chatTopic);
//                                            }
//                                            db.saveAndUpdateUserInfo(circleChatEntity.userId, circleChatEntity.name, circleChatEntity.headurl);
//                                            circleChatEntity.type = CircleChatTypeEnum.gettValue(circleChatEntity.say);
//                                            if (CircleChatTypeEnum.isVoice(circleChatEntity.say)) {
//                                                circleChatEntity.voiceIsRed = 1;
//                                            } else if (CircleChatTypeEnum.isText(circleChatEntity.say)) {
////                                                            saveIsAtMe(circleChatEntity);
//                                            }
//                                            db.insertPrivateChat(userId, circleChatEntity);
//                                        }
//                                    }
//                                    break;
//                                case circle_chat_record:
//                                    if (receiveModel.obj instanceof CircleChatEntity) {
//                                        CircleChatEntity entity = (CircleChatEntity) receiveModel.obj;
//                                        if (entity != null) {
////                                            Log.d("userInfo","type=="+ReceiveModelTypeEnum.chat_record.type()+"   useId=="+entity.userId+"  userName=="+entity.name);
//                                            db.saveAndUpdateUserInfo(entity.userId, entity.name, entity.headurl, entity.isVip);
//                                            entity.type = CircleChatTypeEnum.gettValue(entity.say);
//                                            if (CircleChatTypeEnum.isVoice(entity.say)) {
//                                                entity.voiceIsRed = 1;
//                                            } else if (CircleChatTypeEnum.isText(entity.say)) {
//                                                saveIsAtMe(entity);
//                                            }
//                                            long ret=db.insertCircleChat(userId, entity);
//                                            Log.d("circle_chat_record","insertCircleChat ret=="+ret);
//                                        }
//                                    }
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
//                }
//
//            }
//        });
//    }


    /**
     * 单例化一个CacheManager
     *
     * @return CacheManager对象
     */
    public static ReceivedManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ReceivedManager.class) {
                if (instance == null) {
                    instance = new ReceivedManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 通知
     *
     * @param obj
     */
    private void sendNotify(Object obj) {
        Log.d("sendNotify", "sendNotify////////");
//        if (obj instanceof ReceiveModel) dbQueue.add((ReceiveModel) obj);
        setChanged();
        notifyObservers(obj);
    }

    public synchronized void initRunService(final Context mContext, Long userId, String sessionid, String version) {
        Log.d("SubPushService", "==initRunService=====userId=" + userId + " myPid=" + android.os.Process.myPid() + " sessionid=" + sessionid + "  isRunService=" + isRunService);
        if (userId == null || userId == 0 || sessionid == null) return;
        TPushSettingManager.getInstance().resetUserId(String.valueOf(userId), mContext);
        if (isRunService) return;
        this.userId = userId;
        Intent intent = new Intent(mContext, SubPushService.class);
        intent.putExtra("userId", userId);
        intent.putExtra("sessionid", sessionid);
        intent.putExtra("version", version);
        mContext.startService(intent);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        isRunService = true;
    }

    public synchronized void stopRunService(final Context mContext) {
        Log.d("SubPushService", "==stopRunService=====isRun=" + isRunService + " myPid=" + android.os.Process.myPid());
        isRunService = false;
        mContext.unbindService(mServiceConnection);
        mContext.stopService(new Intent(mContext, SubPushService.class));
    }

    public IRemoteService getiRemoteService() {
        return iRemoteService;
    }

}
