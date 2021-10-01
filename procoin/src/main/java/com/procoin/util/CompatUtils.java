package com.procoin.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 *
 * 专门处理兼容性的问题
 * Created by zhengmj on 17-8-14.
 */

public class CompatUtils {

    public static Spanned fromHtml(String text){
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }
}
