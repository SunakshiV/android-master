package com.procoin.common.entity;

import android.text.TextUtils;
import android.util.Log;

import com.procoin.util.JsonParserUtils;
import com.google.gson.Gson;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by zhengmj on 16-6-21.
 */
public class ResultData implements TaojinluType {
    public String msg;
    public int code;
    public String data;//json格式
    public boolean success;

    public boolean isSuccess() {
        return success;
    }

    public JSONObject returnJSONObject() throws Exception {
        if (TextUtils.isEmpty(data)) throw new Exception("数据错误");
        return new JSONObject(data);
    }

    public JSONArray returnJsonArray() throws Exception {
        if (TextUtils.isEmpty(data)) throw new Exception("数据错误");
        return new JSONArray(data);
    }

    public JSONArray returnJSONArray() throws Exception {
        if (TextUtils.isEmpty(data)) throw new Exception("数据错误");
        return new JSONArray(data);
    }

    public int getPageSize(int defaultSize) {
        int pageSize = defaultSize;
        if (!success) return pageSize;
        try {
            if (!TextUtils.isEmpty(data)) {
                JSONObject jsonObject = returnJSONObject();
                if (JsonParserUtils.hasNotNullAndIsIntOrLong(jsonObject, "pageSize")) {
                    pageSize = jsonObject.getInt("pageSize");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);
        }
        return pageSize;
    }

    /**
     * 当data里面的key为实体列表的时候用到
     *
     * @param key
     * @return
     */
    public <T extends TaojinluType> Group<T> getGroup(String key, Type typeOfT) {
        if (!success) return null;
        Group<T> group = null;
        try {
            if (!TextUtils.isEmpty(data)) {
                JSONObject jsonObject = returnJSONObject();
                if (JsonParserUtils.hasAndNotNull(jsonObject, key)) {
                    group = new Gson().fromJson(jsonObject.getString(key), typeOfT);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);

        }
        return group;
    }

    /**
     * 当data直接就是json数组的时候
     *
     * @param typeOfT
     * @param <T>
     * @return
     */
    public <T extends TaojinluType> Group<T> getGroup(Type typeOfT) {
        if (!success) return null;
        Group<T> group = null;
        try {
            if (!TextUtils.isEmpty(data)) {
                group = new Gson().fromJson(data, typeOfT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);

        }
        return group;
    }

    /**
     * 当data里面的key为String数组的时候用到
     *
     * @param key
     * @return
     */
    public String[] getStringArray(String key) {
        if (!success) return null;
        try {
            if (!TextUtils.isEmpty(data)) {
                JSONObject jsonObject = returnJSONObject();
                if (JsonParserUtils.hasAndNotNull(jsonObject, key)) {
                    JSONArray ja = jsonObject.getJSONArray(key);
                    String[] str = new String[ja.length()];
                    for (int i = 0, m = ja.length(); i < m; i++) {
                        str[i] = ja.getString(i);
                    }
                    return str;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);

        }
        return null;
    }


    /**
     * 当data里面的直接为字符串数组，需要传入原生字符串进来
     * @param rawString 原生字符串，jsonObject格式
     * @param key
     * @return
     */
    public String[] getStringArray(String rawString,String key) {
        if (!success) return null;
        try {
            if (!TextUtils.isEmpty(rawString)) {
                JSONObject jsonObject = new JSONObject(rawString);
                if (JsonParserUtils.hasAndNotNull(jsonObject, key)) {
                    JSONArray ja = jsonObject.getJSONArray(key);
                    String[] str = new String[ja.length()];
                    for (int i = 0, m = ja.length(); i < m; i++) {
                        str[i] = ja.getString(i);
                    }
                    return str;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);

        }
        return null;
    }



    /**
     * 获取data里面的对象
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T extends TaojinluType> T getObject(String key, Class<T> type) {
        if (!success) return null;
        T t = null;
        try {
            if (!TextUtils.isEmpty(data)) {
                JSONObject jsonObject = returnJSONObject();
                if (JsonParserUtils.hasAndNotNull(jsonObject, key)) {
                    t = new Gson().fromJson(jsonObject.getString(key), type);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);
        }
        return t;
    }

    /**
     * 当data就是一个实体对象的时候
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T extends TaojinluType> T getObject(Class<T> type) {
        if (!success) return null;
        T t = null;
        try {
            if (!TextUtils.isEmpty(data)) {
                t = new Gson().fromJson(data, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);
        }
        return t;
    }

    /**
     * 获取data里面的基本类型
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getItem(String key, Class<T> type) {
        Log.d("200", "ResultData key == " + key + "data == " + data);
//        if (!success) return null;
        T t = null;
        try {
            if (!TextUtils.isEmpty(data)) {
                JSONObject jsonObject = returnJSONObject();
                if (JsonParserUtils.hasAndNotNull(jsonObject, key)) {
                    Log.d("200", "ResultData key result == " + jsonObject.getString(key));
                    if (type == String.class) {
                        return (T) jsonObject.getString(key);
                    } else if (type == Integer.class) {
                        return (T) (Integer) jsonObject.getInt(key);
                    } else if (type == Long.class) {
                        return (T) (Long) jsonObject.getLong(key);
                    } else if (type == Double.class) {
                        return (T) (Double) jsonObject.getDouble(key);
                    } else if (type == Boolean.class) {
                        return (T) (Boolean) jsonObject.getBoolean(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ResultData", "e==" + e);
        }

        //如果为null,返回默认值，否则把null强转T会报空指针
        if (t == null) {
            if (type == String.class) {
                return (T) "";
            } else if (type == Integer.class) {
                return (T) (Integer) 0;
            } else if (type == Long.class) {
                return (T) (Long) 0l;
            } else if (type == Double.class) {
                return (T) (Double) 0d;
            } else if (type == Boolean.class) {
                return (T) (Boolean) false;
            }
        }

        return t;
    }

}
