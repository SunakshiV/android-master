package com.procoin.subpush.notify;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.procoin.common.constant.CommonConst;
import com.procoin.data.sharedpreferences.CircleSharedPreferences;
import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.subpush.Consts;
import com.procoin.R;
import com.procoin.util.CircleChatSetTextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NotifyManager {
    private volatile static NotifyManager instance;
    private final NotifyModelParser notifyModelParser;
    private Vibrator vibrator;

    public NotifyManager() {
        notifyModelParser = new NotifyModelParser();
    }

    public static NotifyManager getInstance() {
        if (instance == null) {
            synchronized (NotifyManager.class) {
                if (instance == null) instance = new NotifyManager();
            }
        }
        return instance;
    }

    /**
     * @param context
     * @param notifyModel
     */
    public void notifySimple(Context context, NotifyModel notifyModel) {
        int defaults = Notification.DEFAULT_ALL;
        if (notifyModel.getRing() == 1 && notifyModel.getVibrate() == 1) {
            defaults = Notification.DEFAULT_ALL;
        } else if (notifyModel.getRing() == 1 && notifyModel.getVibrate() == 0) {
            defaults = Notification.DEFAULT_SOUND;
        } else if (notifyModel.getRing() == 0 && notifyModel.getVibrate() == 1) {
            defaults = Notification.DEFAULT_VIBRATE;
        }
        long time = System.currentTimeMillis();
        if (notifyModel.getTime() != 0) time = notifyModel.getTime();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo))
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(notifyModel.getHead())
                .setContentText(notifyModel.getBody())
                .setTicker(notifyModel.getBody())
                .setAutoCancel(true)
                .setWhen(time)
                .setDefaults(defaults);
        Log.d("154", "NotifyManager Pkg == " + notifyModel.getPkg() + " Cls == " + notifyModel.getCls());
        Intent mIntent = new Intent();
        Bundle bundle = new Bundle();
        int requestCode = 0;
        if (CommonConst.PUSH_PRIVATE_CHAT.equals(notifyModel.getT())) {//私聊需要参数
//            bundle.putString(CommonConst.CHAT_TOPIC, notifyModel.getCircleNum());//13456   21-173
            if (!TextUtils.isEmpty(notifyModel.getCircleNum())) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(CommonConst.CHAT_TOPIC, notifyModel.getCircleNum());
                    jsonObject.put(CommonConst.CHAT_NAME, notifyModel.getCircleName());
                    notifyModel.setP(jsonObject.toString());//所有通知都用params传参数
                } catch (JSONException e) {

                }
            }
            requestCode = 0;
        } else if (CommonConst.PUSH_CIRCLE.equals(notifyModel.getT())) {
            if (!TextUtils.isEmpty(notifyModel.getCircleNum())) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(CommonConst.CIRCLEID, notifyModel.getCircleNum());
                    jsonObject.put(CommonConst.CIRCLENAME, notifyModel.getCircleName());
                    notifyModel.setP(jsonObject.toString());//所有通知都用params传参数
                } catch (JSONException e) {

                }
            }
            requestCode = 1;
        } else {
            requestCode = 2;
        }
        Log.d("154", "requestCode == " + requestCode + " notifyModel.getP() == " + notifyModel.getP());
        if (!TextUtils.isEmpty(notifyModel.getPkg()) && !TextUtils.isEmpty(notifyModel.getCls())) {
            bundle.putString(CommonConst.KEY_EXTRAS_PKG, notifyModel.getPkg());
            bundle.putString(CommonConst.KEY_EXTRAS_CLS, notifyModel.getCls());
        }
        if (!TextUtils.isEmpty(notifyModel.getP())) {
            bundle.putString(CommonConst.MSG_PARAMS, notifyModel.getP());
        }
        mIntent.putExtras(bundle);
        ComponentName comp = new ComponentName("com.coingo", "HomeActivity");//先跳首页，然后再跳指定的Activity
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");


        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // // Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(cls);
        // // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(mIntent);
        //
        // // stackBuilder.startActivities();
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);//requestCode的值根据push的类别，如果requestCode一样的话FLAG_UPDATE_CURRENT就会更新上一条Intent
//		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notifyModel.getPid(), mBuilder.build());
    }

    /**
     * @param context
     * @param json
     */
    public void notifyTjrPushReceiver(Context context, long userId, String json) {
        try {
            if (!TPushSettingManager.getInstance().isTjrPush(context, userId)) return;
            NotifyModel notifyModel = notifyModelParser.parse(new JSONObject(json));
//            String className = getTopActivity(context);
//            if (className != null && className.startsWith("com.cropyme.chat") && "ch".equals(notifyModel.getT())) {
//                //广播一个刷新动作
//                Intent in = new Intent("com.cropyme.NOTIFICATION_SHOWNEWANDREFRESH");
//                in.putExtra("notificationMsgType", notifyModel.getT());
//                in.putExtra("notificationMsgLevel", 1);
//                context.sendBroadcast(in);
//                return;
//            }
            notifyModel.setRing(TPushSettingManager.getInstance().getRing());
            notifyModel.setVibrate(TPushSettingManager.getInstance().getVibrate());
//			notifyModel.setT(CommonConst.PUSH_TJR);
//            if (CommonConst.PUSH_CIRCLE.equals(notifyModel.getT())) {//圈子需要参数
//                if (notifyModel.getP() != null && notifyModel.getP().length() > 0) {
//                    JSONObject jsonObject = new JSONObject(notifyModel.getP());
//                    if (jsonObject.has(CommonConst.CIRCLENUM)) {
//                        notifyModel.setCircleNum(jsonObject.getString(CommonConst.CIRCLENUM));
//                    }
//                }
//            }
            notifySimple(context, notifyModel);
        } catch (Exception e) {
        }
    }

    public NotifyModel notifyModelBuilder(int pid, String head, String body, String t, String pkg, String cls, String p, long time, int ring, int vibrate, String circleNum, String circleName) {
        return new NotifyModel(pid, head, body, t, pkg, cls, p, time, ring, vibrate, circleNum, circleName);
    }

    /**
     * 这个确定是不是私聊
     *
     * @param context
     * @param chat
     * @param userId
     * @param isprivateChat
     */
    public void notifyChatReceiver(final Context context, CircleChatEntity chat, long userId, boolean isprivateChat) {
        try {
            if (userId == chat.userId) return;
            if (!chat.isPush) return;
            if (!TPushSettingManager.getInstance().isTjrPush(context, userId)) return;
            //对应某一个圈子的设置
            boolean chatRemind = TPushSettingManager.getInstance().getCircleReminde(context, userId, chat.chatTopic);
            if (chatRemind) return;
            if (isAppClsRun(context, "ChatRoomActivity")||isAppClsRun(context, "CircleChatRoomActivity")) {
                if (CircleSharedPreferences.getCircleSpChatRoomid(context).equals(chat.chatTopic)) {
                    if (TPushSettingManager.getInstance().getVibrate() == 1) {
                        if (vibrator == null)
                            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }
                    return;//如果当前正在聊天，并且房间号一样就不用往下走了，也不用生成push了
                }
            }
            String head = TPushSettingManager.getInstance().getCircleName(chat.chatTopic);
            if (head == null) head = CommonConst.APP_NAME;
            if (!isprivateChat) {//圈子
                notifySimple(context, notifyModelBuilder(Consts.PUSH_ID_CHAT, head, CircleChatSetTextUtils.formatText(chat.name, chat.say), CommonConst.PUSH_CIRCLE, "com.coingo", "CircleChatRoomActivity", null, 0, TPushSettingManager.getInstance().getRing(), TPushSettingManager.getInstance().getVibrate(), chat.chatTopic, head));
            } else {//私聊
                notifySimple(context, notifyModelBuilder(Consts.PUSH_ID_PRIVATECHAT, chat.name, CircleChatSetTextUtils.formatText("", chat.say), CommonConst.PUSH_PRIVATE_CHAT, "com.coingo", "ChatRoomActivity", null, 0, TPushSettingManager.getInstance().getRing(), TPushSettingManager.getInstance().getVibrate(), chat.chatTopic, chat.name));
            }
        } catch (Exception e) {

        }
    }


    /**
     * 判断当前app是否运行,并且最上面的页面是否是传进来的cls,微盘有个全局的通知用到WindowUtils
     *
     * @param context
     * @return
     */
    public boolean isAppClsRun(Context context, String cls) {
        if (TextUtils.isEmpty(cls)) return false;
        String className = getTopActivity(context);
//        CommonUtil.LogLa(2, "cls is " + className);
        if (className != null && className.startsWith(cls)) {
            return true;
        }
        return false;
    }

    /**
     * 现在这个方法使用来判断是不是主程序的包名
     */
    public boolean isAppPkgRun(Context context, String pkg) {
        if (TextUtils.isEmpty(pkg)) return false;
        String pkgName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo != null && tasksInfo.size() > 0) {
            ComponentName cn = tasksInfo.get(0).topActivity;
            if (cn != null) {
                pkgName = cn.getPackageName();
            }
        }
        if (pkgName != null && pkgName.startsWith(pkg)) {
            return true;
        }
        return false;
    }

    /**
     * 这个用来判断当前app是否在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 获取当前app最顶层activity
     *
     * @param context
     * @return
     */
    public String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo != null && tasksInfo.size() > 0) {
            ComponentName cn = tasksInfo.get(0).topActivity;
            if (cn != null) {
                return cn.getClassName();
            }
        }
        return null;
    }
}
