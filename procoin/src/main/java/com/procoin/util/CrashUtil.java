package com.procoin.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by zhengmj on 18-10-31.
 */

public class CrashUtil implements Thread.UncaughtExceptionHandler {
    private static CrashUtil instance = null;
    private Thread.UncaughtExceptionHandler handler;
    private Map<String,String> mMessages = new HashMap<>();
    private Context context;

    public static CrashUtil getInstance(){
        if (instance == null){
            synchronized (CrashUtil.class){
                if (instance == null){
                    instance = new CrashUtil();
                }
            }
        }
        return instance;
    }
    private CrashUtil(){}
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable)){
            handler.uncaughtException(thread,throwable);
        }else {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }
    public void init(Context context){
        this.context = context;
        handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    private boolean handleException(Throwable throwable){
        if (throwable == null){
            return false;
        }
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Log.d("Uncaught","捕捉到异常");
                Looper.loop();
            }
        }.start();
        collectErrorMessages();
        saveErrorMessages(throwable);
        return false;
    }
    private void collectErrorMessages(){
        PackageManager packageManager = context.getPackageManager();
        try{
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
            if (packageInfo!=null){
                String versionName = TextUtils.isEmpty(packageInfo.versionName) ? "Null" : packageInfo.versionName;
                String versionCode = String.valueOf(packageInfo.versionCode);
                mMessages.put("versionName",versionName);
                mMessages.put("versionCode",versionCode);
            }
            Field[] fields = Build.class.getFields();
            if (fields!=null && fields.length>0){
                for (Field field : fields){
                    field.setAccessible(true);
                    try{
                        mMessages.put(field.getName(),field.get(null).toString());
                    }catch (IllegalAccessException e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }
    private void saveErrorMessages(Throwable throwable){
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String,String> entry : mMessages.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null){
            cause.printStackTrace(printWriter);
            cause = throwable.getCause();
        }
        printWriter.close();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        String fileName = "crash-"+time+"-"+System.currentTimeMillis()+".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory().getPath()+"crash/";
            File dir = new File(path);
            if (!dir.exists())dir.mkdirs();
            FileOutputStream outputStream = null;
            try{
                outputStream = new FileOutputStream(path+fileName);
                outputStream.write(stringBuilder.toString().getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (outputStream != null){
                    try {
                        outputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
