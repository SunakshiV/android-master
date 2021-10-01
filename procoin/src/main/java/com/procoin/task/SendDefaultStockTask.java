//package com.tjr.perval.task;
//
//import android.text.TextUtils;
//import android.util.Log;
//
//import TjrStockHttp;
//import com.cropyme.stock.model.StockRateAndAmtDomain;
//import com.cropyme.stock.model.jsonparser.StockRateAndAmtDomainParser;
//import BaseAsyncTask;
//
///**
// * 公用task都放这个包下
// * <p>
// * 获取某支股票代码信息
// * <p>
// * <p>
// * Created by zhengmj on 17-1-11.
// */
//
//public class SendDefaultStockTask extends BaseAsyncTask<String, Void, String> {
//    private StockRateAndAmtDomainParser stockRateAndAmtDomainParser;
//    private String s_fdm;
//    private sendDefaultStockCallBack sendDefaultStockCallBack;
//
//
//    public SendDefaultStockTask(StockRateAndAmtDomainParser stockRateAndAmtDomainParser, sendDefaultStockCallBack sendDefaultStockCallBack) {
//        this.stockRateAndAmtDomainParser = stockRateAndAmtDomainParser;
//        this.sendDefaultStockCallBack = sendDefaultStockCallBack;
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        String result = "";
//        try {
//            s_fdm = params[0];
//            result = TjrStockHttp.getInstance().sendDefaultStock(s_fdm);
//            Log.d("result", "result==" + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//        if (!TextUtils.isEmpty(result)) {
//            try {
////                StockRateAndAmtDomain p = stockRateAndAmtDomainParser.parse(new JSONObject(result), s_fdm);
////                Log.d("result", "result==" + p.jc + "," + p.fdm + "," + p.zjcj + "," + p.rate + "," + p.date + " " + p.time);
////                if (sendDefaultStockCallBack != null) sendDefaultStockCallBack.callBack(p);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    public interface sendDefaultStockCallBack {
//        void callBack(StockRateAndAmtDomain stockRateAndAmtDomain);
//    }
//}
