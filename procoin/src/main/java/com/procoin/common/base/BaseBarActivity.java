package com.procoin.common.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.procoin.util.LanguageUtils;
import com.procoin.util.PageJumpUtil;
import com.procoin.MainApplication;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

/**
 * Created by zhengmj on 17-6-21.
 */

public class BaseBarActivity extends AppCompatActivity {//ActionBarActivity
    private TjrBaseDialog exitTipsDialog;
    private String exitTipsMsg = "";
    private boolean exitTips;//退出提示，用于编辑页面

    private View emptyView;

    @Override
    protected void attachBaseContext(Context newBase) {
//        String language=PreferenceManager.getDefaultSharedPreferences(newBase.getApplicationContext()).getString("language", LanguageUtils.AUTO);
//        Log.d("getLocaleByLanguage", "BaseBarActivity language==" + language);
//        super.attachBaseContext(LanguageUtils.attachBaseContext(newBase, language));
        super.attachBaseContext(checkLanguage(newBase));
    }

    public static Context checkLanguage(Context context) {
        // index：本地保存的语言类型：0英语，1中文简体，2中文繁体
//        int index = DataRepository.getInstence().getSpValue(SPConstant.SP_LANGUAGE, SPConstant.KEY_LANGUAGE_INDEX, -1);
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", LanguageUtils.AUTO);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale locale;
        if (language.equals(Locale.ENGLISH.getLanguage())) {
            locale = Locale.ENGLISH;
        } else if (language.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            // 获取系统默认语言，版本兼容
            locale = ((MainApplication) context.getApplicationContext()).currLocale;
        }
        if (locale == null) return context;
        // 设置语言，版本做兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, displayMetrics);
        }
        return context;
    }

    protected void setExitTips(boolean exitTips, String exitTipsMsg) {
        this.exitTips = exitTips;
        this.exitTipsMsg = exitTipsMsg;
    }

    protected void setExitTips(boolean exitTips) {
        setExitTips(exitTips, "退出此次编辑?");//默认msg
    }

    public MainApplication getApplicationContext() {
        return (MainApplication) super.getApplicationContext();
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 获取底部导航栏高度
     *
     * @param context
     * @return
     */
    public int getNavigationBarSize(Context context) {

        Point point = null;

        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            point = new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            point = new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return point == null ? 0 : point.y;
    }

    public Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    private void showExitTipsDialog(String msg) {
        if (exitTipsDialog == null) {
            exitTipsDialog = new TjrBaseDialog(this) {
                @Override
                public void onclickOk() {
                    dismiss();
                    PageJumpUtil.finishCurr(BaseBarActivity.this);
                }

                @Override
                public void onclickClose() {
                    dismiss();
                }

                @Override
                public void setDownProgress(int progress) {

                }
            };
        }
        exitTipsDialog.setTitleVisibility(View.GONE);
        exitTipsDialog.setMessage(msg);
        exitTipsDialog.setBtnOkText("退出");
        exitTipsDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (exitTips) {
            showExitTipsDialog(exitTipsMsg);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        super.finish();
    }

}
