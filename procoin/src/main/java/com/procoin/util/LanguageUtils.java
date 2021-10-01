package com.procoin.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;


/**
 * @author YanLu
 * @since 17/5/12
 */

public class LanguageUtils {

    // 简体中文
    public static final String SIMPLIFIED_CHINESE = "zh";
    // 英文
    public static final String ENGLISH = "en";
    //跟随系统
    public static final String AUTO = "auto";

    private static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(2) {{
        put(ENGLISH, Locale.ENGLISH);
        put(SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
    }};

    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // app locale
        Locale locale = getLocaleByLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // updateConfiguration
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    public static Context checkLanguage(Context context) {
        // index：本地保存的语言类型：0英语，1中文简体，2中文繁体
//        int index = DataRepository.getInstence().getSpValue(SPConstant.SP_LANGUAGE, SPConstant.KEY_LANGUAGE_INDEX, -1);
        String language= PreferenceManager.getDefaultSharedPreferences(context).getString("language", LanguageUtils.AUTO);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale locale;
        if (language.equals(LanguageUtils.ENGLISH)) {
            locale = Locale.ENGLISH;
        } else if (language.equals(LanguageUtils.SIMPLIFIED_CHINESE)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }else {
            // 获取系统默认语言，版本兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
//            locale = ((MainApplication)context.getApplicationContext()).currLocale();
//            if(!isSupportLanguage(locale.getLanguage())){//如果不支持就用中文
//                locale= Locale.SIMPLIFIED_CHINESE;
//            }
        }
        // 设置语言，版本做兼容
        // 这个updateConfiguration方法已废弃，官方建议用createConfigurationContext。但是仍然可以用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, displayMetrics);
        }
        return context;
    }

    private static boolean isSupportLanguage(String language) {
        return mAllLanguages.containsKey(language);
    }

    public static String getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return language;
        }
        return SIMPLIFIED_CHINESE;
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #mAllLanguages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #mAllLanguages}，返回英语
     *
     * @param language language
     * @return
     */
    public static Locale getLocaleByLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mAllLanguages.get(language);
        } else {
//            Locale locale = Locale.getDefault();
            Locale locale=null;
            // 获取系统默认语言，版本兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
            Log.d("getLocaleByLanguage", "locale.getLanguage()==" + locale.getLanguage());
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = LanguageUtils.getLocaleByLanguage(language);
        Log.d("getLocaleByLanguage", "updateResources    locale=" + locale.getLanguage());
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}
