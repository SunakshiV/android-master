package com.procoin.util;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.procoin.common.constant.CommonConst;
import com.procoin.module.circle.entity.CircleChatTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengmj on 16-4-25.
 * <p/>
 * 圈子最后一句私聊 设置文字
 * <p/>
 * CircleHomeActivity   的私聊用到
 * CircleHome  CircleFragment list  用到
 * push用到
 */
public class CircleChatSetTextUtils {

    public static String formatText(String name, String say) {//为什么不传一个对象过来，因为有些对象不一样
        if (TextUtils.isEmpty(say)) return "";
        if (!TextUtils.isEmpty(name)) {
            name = name + ": ";
        } else {
            name = "";
        }
        String content = "";
        if (say.startsWith(CircleChatTypeEnum.SAY_VOICE.type)) {
            content = name + "[语音]";
        } else if (say.startsWith(CircleChatTypeEnum.SAY_IMG.type)) {
            content = name + "[图片]";
        } else if (say.startsWith(CircleChatTypeEnum.SAY_TEXT.type)) {//文字
            say = say.replace(CircleChatTypeEnum.SAY_TEXT.type, "");
            say = filterAtName(say);
            content = name + say;
        } else if (say.startsWith(CircleChatTypeEnum.SAY_FDM.type)) {//股票
//                    say = say.replace(CircleChatTypeEnum.SAY_FDM.type, "");
            content = name + "分享了一只[股票]";
        } else if (say.startsWith(CircleChatTypeEnum.SAY_TIP.type)) {
            content = say.replace(CircleChatTypeEnum.SAY_TIP.type, "");
            content = replaceAt(content);
//            MyClickableSpanUtil.getCustomText(say, StockTextUtils.parserTestToStock(say), false, context)
//            describes.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());

        } else {
            content = name + "发了新的消息,当前版本暂不支持";
        }
        return content;
    }

    public static String getJsonByKey(String json, String key, String defaultSay) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            if (jsonObject.containsKey(key)) {
                return jsonObject.getString(key);
            }
        } catch (Exception e) {

        }
        return defaultSay;
    }

    public static String replaceAt(String content) {
        // @阿青
        if (content == null) return content;
        final String regex3 = CommonConst.AT_MATCHES;//
        final Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
        final Matcher ma3 = pa3.matcher(content);
        Map<String, String> mkey = new HashMap<String, String>();
        while (ma3.find()) {
            if (ma3.groupCount() == 2) {
                // map.put(ma3.group(0), ma3.group(1)+"_"+ma3.group(2));
                mkey.put(ma3.group(0), ma3.group(1));
            }
        }
        for (Map.Entry<String, String> entry : mkey.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        return content;
    }

    /**
     * 这里只处理@和表情就好了
     *
     * @param say
     * @return
     */
    public static String filterAtName(String say) {
        Log.d("Matcher", "say==" + say);
        List<StockTextUtils.MatchEntity> map = new ArrayList<StockTextUtils.MatchEntity>();
        //@阿青
        String regex3 = CommonConst.AT_MATCHES;//
        Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
        Matcher ma3 = pa3.matcher(say);
        while (ma3.find()) {
            if (ma3.groupCount() == 2) {
                Log.d("Matcher", "ma3.group(0)==" + ma3.group(0) + " ma3.group(1)==" + ma3.group(1) + " ma3.group(2)==" + ma3.group(2));
                map.add(new StockTextUtils.MatchEntity(ma3.group(0), ma3.group(1)));
            }
        }
        int at_from_start = 0;
        SpannableStringBuilder ss = new SpannableStringBuilder(say);
        if (map.size() > 0) {
            for (StockTextUtils.MatchEntity entry : map) {
                if (entry.getValue() != null) {
                    if (entry.getValue().startsWith("@")) {
                        int vlen = entry.getKey().length();
                        int in = ss.toString().indexOf(entry.getKey(), at_from_start);
                        Log.d("at_from_start", "at_from_start==" + at_from_start);
                        String value = entry.getValue();
                        ss.replace(in, in + vlen, value);
                        at_from_start = in + value.length();
                        Log.d("at_from_start", "at_from_start==" + at_from_start);
                    }
                }
            }
        }
        return ss.toString();
    }


//    public static int getChatType(String say) {
//        int type = 0;
//        if (!TextUtils.isEmpty(say)) {
//            if (say.startsWith(CircleChatTypeEnum.SAY_TEXT.type)) {//文字
//                type = 0;
//            } else if (say.startsWith(CircleChatTypeEnum.SAY_IMG.type)) {//图片
//                type = 1;
//            } else if (say.startsWith(CircleChatTypeEnum.SAY_VOICE.type)) {//声音
//                type = 2;
//            } else if (say.startsWith(CircleChatTypeEnum.SAY_FDM.type)) {//股票
//                type = 3;
//            } else if (say.startsWith(CircleChatTypeEnum.SAY_TIP.type)) {//提示
//                type = 4;
//            } else if (say.startsWith(CircleChatTypeEnum.SAY_KLINE_BOX_JSON.type)) {//K线宝箱
//                type = 5;
//            }
//        }
//        return type;
//    }

  
}
