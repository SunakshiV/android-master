package com.procoin.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.procoin.R;
import com.procoin.common.constant.CommonConst;

import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;


public class PageJumpUtil {

    public static boolean pageJump(Context context, Class<?> cls, Bundle bundle) {
        try {
            if (context != null && cls != null) {
                Intent intent = new Intent();
                intent.setClass(context, cls);
                // 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (bundle != null) intent.putExtras(bundle);
                context.startActivity(intent);
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.perval_right_to_left, 0);
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }


    public static boolean pageJump(Context activity, Class<?> cls) {
        return pageJump(activity, cls, null);
    }

    public static boolean pageJump(Context context, Intent intent) {
        try {
            if (context != null) {
                // 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.perval_right_to_left, 0);
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }

    /**
     * 组件使用
     *
     * @throws Exception
     */
    public static boolean pageJumpNotry(Context context, Intent intent) throws Exception {
        if (context != null) {
            // 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).overridePendingTransition(R.anim.perval_right_to_left, 0);
            }
        }
        return false;
    }


    /**
     * 页面跳转,带参数
     *
     * @param activity 当前页面
     * @param cls      要跳转到的页面
     * @param bundle   参数
     * @return true表示跳转成功，false表示跳转不成功
     */
    public static boolean pageJumpToData(Context activity, Class<?> cls, Bundle bundle) {
        try {
            if (activity != null && cls != null) {
                Intent intent = new Intent();
                intent.setClass(activity, cls);
                // 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (bundle != null) intent.putExtras(bundle);
                activity.startActivity(intent);
                if (activity instanceof AppCompatActivity) {
//                    Log.d("154","pageJumpToData");
                    ((AppCompatActivity) activity).overridePendingTransition(R.anim.perval_right_to_left, 0);
                }
                // activity.overridePendingTransition(R.anim.in_left_to_right,
                // 0);// 进入动画
            }
//			else if (activity != null && cls == null) {
//				activity.finish();
//			}
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }


    /**
     * 页面跳转,带参数
     *
     * @param activity 当前页面
     * @param cls      要跳转到的页面
     * @param bundle   参数
     * @return true表示跳转成功，false表示跳转不成功
     */
    public static boolean pageJumpToDataNoClearTopFlag(Context activity, Class<?> cls, Bundle bundle) {
        try {
            if (activity != null && cls != null) {
                Intent intent = new Intent();
                intent.setClass(activity, cls);
//				// 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (bundle != null) intent.putExtras(bundle);
                activity.startActivity(intent);
                if (activity instanceof AppCompatActivity) {
                    ((AppCompatActivity) activity).overridePendingTransition(R.anim.perval_right_to_left, 0);
                }
                // activity.overridePendingTransition(R.anim.in_left_to_right,
                // 0);// 进入动画
            }
//			else if (activity != null && cls == null) {
//				activity.finish();
//			}
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }

    /**
     * 获取结果的
     *
     * @param activity
     * @param cls
     * @param intent
     * @return
     */
    public static boolean pageJumpResult(AppCompatActivity activity, Class<?> cls, Intent intent) {
        try {
            if (activity != null && cls != null && intent != null) {
                intent.setClass(activity, cls);
                activity.startActivityForResult(intent, 0x123);
                activity.overridePendingTransition(R.anim.perval_right_to_left, 0);
                // activity.overridePendingTransition(R.anim.in_left_to_right,
                // 0);
            } else if (activity != null && cls == null) {
                activity.finish();
            }
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }

    /**
     * 获取结果的
     *
     * @param activity
     * @param cls
     * @param intent
     * @return
     */
    public static boolean pageJumpResult(AppCompatActivity activity, Class<?> cls, Intent intent, int requestCode) {
        try {
            if (activity != null && cls != null && intent != null) {
                intent.setClass(activity, cls);
                activity.startActivityForResult(intent, requestCode);
                activity.overridePendingTransition(R.anim.perval_right_to_left, 0);
                // activity.overridePendingTransition(R.anim.in_left_to_right,
                // 0);
            } else if (activity != null && cls == null) {
                activity.finish();
            }
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }


    /**
     * 获取结果的
     *
     * @param intent
     * @return
     */
    public static boolean pageJumpResult(Fragment fragmen, Intent intent, int requestCode) {
        try {
            if (fragmen != null && intent != null) {
                fragmen.startActivityForResult(intent, requestCode);
                fragmen.getActivity().overridePendingTransition(R.anim.perval_right_to_left, 0);
            }
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }

    /**
     * 获取结果的
     *
     * @param activity
     * @param intent
     * @return
     */
    public static boolean pageJumpResult(AppCompatActivity activity, Intent intent, int requestCode) {
        try {
            if (activity != null && intent != null) {
//				intent.setClass(activity, cls);
                activity.startActivityForResult(intent, requestCode);
                activity.overridePendingTransition(R.anim.perval_right_to_left, 0);
            }
//			else if (activity != null && cls == null) {
//				activity.finish();
//			}
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }

    public static boolean pageJumpResult(AppCompatActivity activity, Class<?> cls, Bundle bundle) {
        try {
            if (activity != null && cls != null) {
                Intent intent = new Intent();
                intent.setClass(activity, cls);
                // 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (bundle != null) intent.putExtras(bundle);
                activity.startActivityForResult(intent, 0x123);
                activity.overridePendingTransition(R.anim.perval_right_to_left, 0);
            } else if (activity != null && cls == null) {
                activity.finish();
            }
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }

    public static boolean pageJumpResult(Fragment activity, Class<?> cls, Bundle bundle) {
        try {
            if (activity != null && cls != null) {
                Intent intent = new Intent();
                intent.setClass(activity.getContext(), cls);
                // 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (bundle != null) intent.putExtras(bundle);
                activity.startActivityForResult(intent, 0x123);
                activity.getActivity().overridePendingTransition(R.anim.perval_right_to_left, 0);
            } else if (activity != null && cls == null) {
//                activity.finish();
            }
        } catch (Exception e) {
            Log.e("Exception", "pageJump error!  " + e.toString());
        }
        return false;
    }

    // 跳转到其他程序的页面
    public static void startComponentActivity(AppCompatActivity activity, Intent mIntent) {
        activity.startActivity(mIntent);
        activity.overridePendingTransition(R.anim.perval_right_to_left, 0);
    }

    /**
     * 关闭当前页面     这样写方便以后加动画
     *
     * @param activity
     */
    public static void finishCurr(AppCompatActivity activity) {
        if (activity != null) {
            activity.finish();
        }

    }


    public static String getParameter(final String key, final Map<String, List<String>> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            List<String> keyval = parameters.get(key);
            if (keyval != null && keyval.size() > 0) {
                return keyval.get(0).trim();
            }
        }
        return null;
    }

    public static boolean pageJumpUrl(Context context, final String url) {
        Log.d("pageJumpUrl", "url==" + url);
        try {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);//
            Map<String, List<String>> parameters = queryStringDecoder.getParameters();
            String pkg = getParameter("pkg", parameters);
            String cls = getParameter("cls", parameters);
            Log.d("pageJumpUrl", "pkg==" + pkg + "    cls==" + cls);
            if (!TextUtils.isEmpty(pkg) && !TextUtils.isEmpty(cls)) {
                String params = getParameter("params", parameters);
                Log.d("pageJumpUrl", "params==" + params);
                Intent mIntent = new Intent();
                if (params != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConst.MSG_PARAMS, params);
                    mIntent.putExtras(bundle);
                }
                ComponentName comp = new ComponentName(pkg, cls);
                mIntent.setComponent(comp);
                mIntent.setAction("android.intent.action.VIEW");
                PageJumpUtil.pageJump(context, mIntent);
//            AppJc.jumpToComponetApp((TJRBaseActionBarActivity)context,pkg,cls,params);
//            ((TJRBaseActionBarActivity) context).jumpToComponet(pkg, cls, params);

                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 这个方法是用来挑
     *
     * @param activity
     * @param pkg
     * @param cls
     * @param params
     */
    public static void jumpByPkg(Context activity, String pkg, String cls, String params) {
        try {
            Intent mIntent = new Intent();
            Bundle bundle = new Bundle();
            if (params != null) {
                bundle.putString(CommonConst.MSG_PARAMS, params);
            }
            mIntent.putExtras(bundle);
            ComponentName comp = new ComponentName(pkg, cls);
            mIntent.setComponent(comp);
            mIntent.setAction("android.intent.action.VIEW");
            PageJumpUtil.pageJump(activity, mIntent);
        } catch (Exception e) {

        }

    }
}
