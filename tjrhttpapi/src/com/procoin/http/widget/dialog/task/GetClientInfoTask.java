//package com.cropyme.http.widget.dialog.task;
//
//import android.app.Activity;
//
//import Components;
//import Placard;
//import com.cropyme.http.model.jsonparser.ComponentsParser;
//import com.cropyme.http.model.jsonparser.PlacardJsonParser;
//import com.cropyme.http.tjrcpt.BeebarHttp;
//import com.cropyme.http.tjrcpt.PervalHttp;
//import BaseAsyncTask;
//
//import org.json.JSONObject;
//
//public abstract class GetClientInfoTask extends BaseAsyncTask<Activity, Void, Activity> {
//    public abstract void getReslute(Components components, Placard placard, Activity rActivity,int msg_count);
//
//    private Components components;//
//    private Placard placard;
//    private String placardTime;
//    private String user_id;
//    private int msg_count;
//
//    public GetClientInfoTask(String placardTime,String user_id) {
//        this.placardTime = placardTime;
//        this.user_id = user_id;
//    }
//
//    @Override
//    protected void onPostExecute(Activity result) {
//        super.onPostExecute(result);
//        getReslute(components, placard, result,msg_count);
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected Activity doInBackground(Activity... params) {
//        try {
//            String result = BeebarHttp.getInstance().homeGet(placardTime, user_id);
//            if (result != null) {
//                JSONObject jsonObject = new JSONObject(result);
//                if (jsonObject.has("data") && !jsonObject.isNull("data")) {
//                    JSONObject jsonData = jsonObject.getJSONObject("data");
//                    if (jsonData.has("msgCount") && !jsonData.isNull("msgCount")){
//                     msg_count = jsonData.getInt("msgCount");
//                    }
//                    if (jsonData.has("version") && !jsonData.isNull("version")) {
//                        ComponentsParser componentsParser = new ComponentsParser();
//                        components = componentsParser.parse(jsonData.getJSONObject("version"));
//                    }
//                    if (jsonData.has("notice") && !jsonData.isNull("notice")) {
//                        PlacardJsonParser placardParser = new PlacardJsonParser();
//                        placard = placardParser.parse(jsonData.getJSONObject("notice"));
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//        return params[0];
//    }
//
//}
