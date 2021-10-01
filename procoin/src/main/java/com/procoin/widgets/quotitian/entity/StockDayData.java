package com.procoin.widgets.quotitian.entity;

import android.text.TextUtils;
import android.util.Log;

import com.procoin.http.base.TaojinluType;

import java.io.Serializable;


/**
 * 股票行情的数据结构
 *
 * @author Administrator
 */
public class StockDayData implements TaojinluType, Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 80551566881860845L;

    private String fdm; // 股票全代号

    private String jc; // 股票名字

    private double jrkp; // 今日开盘价

    private double zrsp; // 昨日收盘价

    private double zjcj; // 当前价格 相当于今日收盘

    private double zgcj; // 今日最高价

    private double zdcj; // 今日最低价

    private String cjsl; // 成交量

    private long cjje; // 成交金额

    private long date; // 股票更新日期(年-月-日)转化成20010111,

    private String time; // 股票更新时间(时:分:秒)

    private double ema12; // EMA12的初始值为第一天的收盘价 ,EMA12 =0.1538*今天的收盘价+昨天的EMA12*11/13;

    private double ema26; // EMA26的初始值为第一天的收盘价,EMA26 = 0.0741*今天的收盘价+昨天的EMA26*25/27;

    private double dif; // dif = EMA12-EMA26;

    private double dea; // dea = dif*0.2+dea*0.8;

    private double bar; // bar = 2*(dif-dea);

    private double m5; // 当前价的m5

    private String vm5; // 当前成交量的m5

    private double m10; // 当前价的m10

    private String vm10; // 当前成交量的m10

    private double m30; // 当前价的m30

    private double kdjk; // 当前价的kdjk=0.6667*昨天的K+0.3333*rsv;

    private double kdjd; // 当前价的kdjd=0.667*昨天的D+0.3333*今天的K;

    private double kdjj; // 当前价的kdjj=3*今天的K-2*今天的D;

    private double rate;

    private double amt;

    private boolean isHas;//代表是否有KDJ，MACD

    public StockDayData() {
        super();
        // TODO Auto-generated constructor stub
    }


    public StockDayData(double jrkp, double zjcj, double zgcj, double zdcj, double m5, double m10, double m30) {
        super();
        this.jrkp = jrkp;
        this.zjcj = zjcj;
        this.zgcj = zgcj;
        this.zdcj = zdcj;
        this.m5 = m5;
        this.m10 = m10;
        this.m30 = m30;
    }

    public StockDayData(double jrkp, double zjcj, double zgcj, double zdcj, String cjsl) {
        super();
        this.jrkp = jrkp;
        this.zjcj = zjcj;
        this.zgcj = zgcj;
        this.zdcj = zdcj;
        this.cjsl = cjsl;
        Log.d("StockDayData", "jrkp==" + jrkp + "  zjcj==" + zjcj + "  zgcj==" + zgcj + "  zdcj==" + zdcj + "  cjsl==" + cjsl);
    }


    public String getFdm() {
        return fdm;
    }

    public void setFdm(String fdm) {
        this.fdm = fdm;
    }

    public String getJc() {
        return jc;
    }

    public void setJc(String jc) {
        this.jc = jc;
    }

    public double getJrkp() {
        return jrkp;
    }

    public void setJrkp(double jrkp) {
        this.jrkp = jrkp;
    }

    public double getZrsp() {
        return zrsp;
    }

    public void setZrsp(double zrsp) {
        this.zrsp = zrsp;
    }

    public double getZjcj() {
        return zjcj;
    }

    public void setZjcj(double zjcj) {
        this.zjcj = zjcj;
    }

    public double getZgcj() {
        return zgcj;
    }

    public void setZgcj(double zgcj) {
        this.zgcj = zgcj;
    }

    public double getZdcj() {
        return zdcj;
    }

    public void setZdcj(double zdcj) {
        this.zdcj = zdcj;
    }

    public String getCjsl() {
        return TextUtils.isEmpty(cjsl) ? "0" : cjsl;
    }

    public void setCjsl(String cjsl) {
        this.cjsl = cjsl;
    }

    public long getCjje() {
        return cjje;
    }

    public void setCjje(long cjje) {
        this.cjje = cjje;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getEma12() {
        return ema12;
    }

    public void setEma12(double ema12) {
        this.ema12 = ema12;
    }

    public double getEma26() {
        return ema26;
    }

    public void setEma26(double ema26) {
        this.ema26 = ema26;
    }

    public double getDif() {
        return dif;
    }

    public void setDif(double dif) {
        this.dif = dif;
    }

    public double getDea() {
        return dea;
    }

    public void setDea(double dea) {
        this.dea = dea;
    }

    public double getBar() {
        return bar;
    }

    public void setBar(double bar) {
        this.bar = bar;
    }

    public double getM5() {
        return m5;
    }

    public void setM5(double m5) {
        Log.d("setM5","m5=="+m5);
        this.m5 = m5;
    }

    public String getVm5() {
        return TextUtils.isEmpty(vm5) ? "0" : vm5;
    }

    public void setVm5(String vm5) {
        this.vm5 = vm5;
    }

    public double getM10() {
        return m10;
    }

    public void setM10(double m10) {
        this.m10 = m10;
    }

    public String getVm10() {
        return TextUtils.isEmpty(vm10) ? "0" : vm10;
    }

    public void setVm10(String vm10) {
        this.vm10 = vm10;
    }

    public double getM30() {
        return m30;
    }

    public void setM30(double m30) {
        Log.d("setM30","m30=="+m30);
        this.m30 = m30;
    }

    public double getKdjk() {
        return kdjk;
    }

    public void setKdjk(double kdjk) {
        this.kdjk = kdjk;
    }

    public double getKdjd() {
        return kdjd;
    }

    public void setKdjd(double kdjd) {
        this.kdjd = kdjd;
    }

    public double getKdjj() {
        return kdjj;
    }

    public void setKdjj(double kdjj) {
        this.kdjj = kdjj;
    }


    public double getRate() {
        return rate;
    }


    public void setRate(double rate) {
        this.rate = rate;
    }


    public double getAmt() {
        return amt;
    }


    public void setAmt(double amt) {
        this.amt = amt;
    }


    public boolean getIsHas() {
        return isHas;
    }


    public void setIsHas(boolean isHas) {
        this.isHas = isHas;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

}