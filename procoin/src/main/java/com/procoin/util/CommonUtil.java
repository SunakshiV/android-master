package com.procoin.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.procoin.common.constant.CommonConst;
import com.procoin.module.logout.ReLoginRemindActivity;
import com.procoin.common.ShareActivity;
import com.procoin.http.TjrBaseApi;
import com.procoin.http.common.ConfigTjrInfo;
import com.procoin.common.base.BaseBarActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class CommonUtil {
    private static String TAG = "ddd";

    /**
     * 打出log
     *
     * @param type 0代表verbase 1代表debug 2代表info 3代表warn 4代表error
     * @param msg  要打印的message
     */
    public static void LogLa(int type, String msg) {
        if (!TjrBaseApi.isLog) return;
        switch (type) {
            case 0:
                Log.v(TAG, "ygq " + msg + "");
                break;
            case 1:
                Log.d(TAG, "ygq " + msg + "");
                break;
            case 2:
                Log.i(TAG, "ygq " + msg + "");
                break;
            case 3:
                Log.w(TAG, "ygq " + msg + "");
                break;
            case 4:
                Log.e(TAG, "ygq " + msg + "");
                break;
            default:
                Log.v(TAG, "ygq " + msg + "");
                break;
        }
    }

    /**
     * 打印Toast
     *
     * @param value msg
     */
    public static void showmessage(String value, Context conext) {
        Toast.makeText(conext, value, Toast.LENGTH_SHORT).show();
    }


    /**
     * 关闭asyncTask
     *
     * @param task
     */
    public static void cancelAsyncTask(AsyncTask<?, ?, ?> task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            while (!task.isCancelled() && !task.cancel(true)) {
                if (task.isCancelled()) break;
                else task.cancel(true);
            }
        }
    }

    public static void cancelCall(Call<ResponseBody> call) {
        if (call != null) call.cancel();
    }

    public static void logoutToLoginActity(int code, String msg) {//session过期登录框
        com.procoin.http.util.CommonUtil.LogLa(2, "OnLogoutReceiver code is " + code + " msg is " + msg);
        if (code == 40009) {
            if (ConfigTjrInfo.getInstance().getContext() != null) {
                Log.d("onReceive", "ConfigTjrInfo not null");
                //这里静态注册的广播，8.0的机子可能收不到
//                Intent intent = new Intent(TJRFilterConf.INTENT_ACTION_LOGGED_OUT);
//                intent.putExtra(TJRFilterConf.RESPONSE_CODE, code);
//                intent.putExtra(TJRFilterConf.RESPONSE_MSG, msg);
//                ConfigTjrInfo.getInstance().getContext().sendBroadcast(intent);
//                (new LogoutClearUser()).logoutClearUser(ConfigTjrInfo.getInstance().getContext(), LoginActivity.class);
                Intent intent = new Intent(ConfigTjrInfo.getInstance().getContext(), ReLoginRemindActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//不加这句，有些手机会报错
                ConfigTjrInfo.getInstance().getContext().startActivity(intent);
            } else {
                Log.d("onReceive", "ConfigTjrInfo null");
            }


        }
    }

    public static String setNewsCount(long count) {
        if (count > 99) {
            return 99 + "+";
        } else {
            return String.valueOf(count);
        }
    }


    /**
     * 分享统一调用这个方法
     *
     * @param context
     * @param type
     * @param requestCode
     */
    public static void gotoShareActivity(BaseBarActivity context, int type, int requestCode) {
        gotoShareActivity(context, type, requestCode, false);
    }


    /**
     * OlstarHomeActivity专用,因为弹出的分享框之后状态栏要白色,其他都是深色
     *
     * @param context
     * @param type
     * @param requestCode
     * @param isStatusBarWhite
     */
    public static void gotoShareActivity(BaseBarActivity context, int type, int requestCode, boolean isStatusBarWhite) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, type);
        if (isStatusBarWhite) {
            intent.putExtra("statusBarWhite", isStatusBarWhite);
        }
        context.startActivityForResult(intent, requestCode);
    }


    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void copyText(Context context, CharSequence text) {
        if (Build.VERSION.SDK_INT >= 11) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        }
        CommonUtil.showmessage("已复制到粘贴板", context);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static String fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(source).toString();
        }

    }


    public static String getOriginSymbol(String symbol) {
        if (!TextUtils.isEmpty(symbol) && symbol.contains("/")) {
            String[] s = symbol.split("/");
            if (s.length == 2) {
                return s[0];
            }
        }

        return symbol;
    }

    public static String getUnitSymbol(String symbol) {
        if (!TextUtils.isEmpty(symbol) && symbol.contains("/")) {
            String[] s = symbol.split("/");
            if (s.length == 2) {
                return s[1];
            }
        }
        return symbol;
    }


    /**
     * @param myUserId
     * @param targetUserId
     * @param orderId      用来区分订单的    如果不需要orderId就传0
     * @return
     */
    public static String getChatTop(long myUserId, long targetUserId, long orderId) {
        String chatTop;
        if (myUserId < targetUserId) {
            chatTop = myUserId + "-" + targetUserId;
        } else {
            chatTop = targetUserId + "-" + myUserId;
        }
        if (orderId > 0) chatTop = chatTop + "-" + orderId;
        return chatTop;
    }

    public static String getChatTop(long myUserId, long targetUserId) {
        return getChatTop(myUserId, targetUserId, 0);

    }

    public static boolean isMyself(String chatTopic) {//判断chatTopic是否都是自己的id
        if (!TextUtils.isEmpty(chatTopic)) {
            String[] topic = chatTopic.split("-");
            if (topic.length == 2) {
                if (!TextUtils.isEmpty(topic[0]) && !TextUtils.isEmpty(topic[1]) && topic[0].equals(topic[1])) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * 判断手机号
     *
     * @param phone
     * @param context
     * @return
     */
    public static boolean invalidatePhone(String phone, Context context) {
        if (TextUtils.isEmpty(phone)) {
            CommonUtil.showmessage("手机号码不能为空!", context);
            return false;
        }
//        if (!phone.matches("^(1)\\d{10}$")) {
//            CommonUtil.showmessage("请输入正确手机号码!", context);
//            return false;
//        }
        return true;
    }

    /**
     * 判断密码
     *
     * @param psd
     * @param psd2    确认密码
     * @param context
     * @return
     */
    public static boolean invalidataPsd(String psd, String psd2, Context context) {

        if (TextUtils.isEmpty(psd)) {
            CommonUtil.showmessage("请输入密码!", context);
            return false;
        }

        if (psd.length() < 8 || psd.length() > 16) {
            CommonUtil.showmessage("密码必须是8-16位数字、字母组合!", context);
            return false;
        }

        if (!psd.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$")) {
            CommonUtil.showmessage("密码必须是8-16位数字、字母组合!", context);
            return false;
        }

        if (TextUtils.isEmpty(psd2)) {
            CommonUtil.showmessage("请再次输入密码!", context);
            return false;
        }
        if (!psd2.equals(psd)) {
            CommonUtil.showmessage("两次输入的密码不相同", context);
            return false;
        }

        return true;
    }


}
