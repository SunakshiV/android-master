package com.procoin.widgets.quotitian.entity;

import com.procoin.http.base.TaojinluType;


/**
 * 股票行情的数据结构
 * 
 * @author Administrator
 * 
 */
public class StockDomain implements TaojinluType {

	public String fdm; // 证券代码，带有sh、sz
	public String dm; // 证券代码1
	public String jc; // 证券简称2
	public double zrsp; // 昨日收盘价3
	public double jrkp; // 今日开盘价4
	public double zjcj; // 最近成交价、最新价5
	public double cjsl; // 成交数量、总成交量6
	public double cjje; // 成交金额、总成交额7
	public double cjbs; // 成交笔数8，只有深圳才有的
	public double zgcj; // 最高成交9
	public double zdcj; // 最低成交10
	public String syl1; // 市盈率1 11
	public String syl2; // 市盈率2 12
	public double sjw5; // 卖价五16
	public double ssl5; // 卖量五17
	public double sjw4; // 卖价四18
	public double ssl4; // 卖量四19
	public double sjw3; // 卖价三20
	public double ssl3; // 卖量三21
	public double sjw2; // 卖价二22
	public double ssl2; // 卖量二23
	public double sjw1; // 卖价一24
	public double ssl1; // 卖量一25
	public double bjw1; // 买价一26
	public double bsl1; // 买量一27
	public double bjw2; // 买价二28
	public double bsl2; // 买量二29
	public double bjw3; // 买价三30
	public double bsl3; // 买量三31
	public double bjw4; // 买价四32
	public double bsl4; // 买量四33
	public double bjw5; // 买价五34
	public double bsl5; // 买量五35
	public String date; // 日期:2011-12-01
	public String time; // 时间:09:05:02

//	/**
//	 * 证券代码，带有sh、sz 如sh600004
//	 * 
//	 * @return
//	 */
//	public String getFdm() {
//		return fdm;
//	}
//
//	public void setFdm(String fdm) {
//		this.fdm = fdm;
//	}
//
//	/**
//	 * 证券代码
//	 * 
//	 * 如600004
//	 * 
//	 * @return
//	 */
//	public String getDm() {
//		return dm;
//	}
//
//	public void setDm(String dm) {
//		this.dm = dm;
//	}
//
//	/**
//	 * 证券简称
//	 * 
//	 * @return
//	 */
//	public String getJc() {
//		return jc;
//	}
//
//	public void setJc(String jc) {
//		this.jc = jc;
//	}
//
//	/**
//	 * 昨日收盘价
//	 * 
//	 * @return
//	 */
//	public double getZrsp() {
//		return zrsp;
//	}
//
//	public void setZrsp(double zrsp) {
//		this.zrsp = zrsp;
//	}
//
//	/**
//	 * 今日开盘价
//	 * 
//	 * @return
//	 */
//	public double getJrkp() {
//		return jrkp;
//	}
//
//	public void setJrkp(double jrkp) {
//		this.jrkp = jrkp;
//	}
//
//	/**
//	 * 最近成交价、最新价
//	 * 
//	 * @return
//	 */
//	public double getZjcj() {
//		return zjcj;
//	}
//
//	public void setZjcj(double zjcj) {
//		this.zjcj = zjcj;
//	}
//
//	/**
//	 * 成交数量、总成交量
//	 * 
//	 * @return
//	 */
//	public double getCjsl() {
//		return cjsl;
//	}
//
//	public void setCjsl(double cjsl) {
//		this.cjsl = cjsl;
//	}
//
//	/**
//	 * 成交金额、总成交额
//	 * 
//	 * @return
//	 */
//	public double getCjje() {
//		return cjje;
//	}
//
//	public void setCjje(double cjje) {
//		this.cjje = cjje;
//	}
//
//	/**
//	 * 成交笔数8，只有深圳才有的
//	 * 
//	 * @return
//	 */
//	public double getCjbs() {
//		return cjbs;
//	}
//
//	public void setCjbs(double cjbs) {
//		this.cjbs = cjbs;
//	}
//
//	/**
//	 * 最高成交
//	 * 
//	 * @return
//	 */
//	public double getZgcj() {
//		return zgcj;
//	}
//
//	public void setZgcj(double zgcj) {
//		this.zgcj = zgcj;
//	}
//
//	/**
//	 * 最低成交
//	 * 
//	 * @return
//	 */
//	public double getZdcj() {
//		return zdcj;
//	}
//
//	public void setZdcj(double zdcj) {
//		this.zdcj = zdcj;
//	}
//
//	/**
//	 * 市盈率1
//	 * 
//	 * @return
//	 */
//	public String getSyl1() {
//		return syl1;
//	}
//
//	public void setSyl1(String syl1) {
//		this.syl1 = syl1;
//	}
//
//	/**
//	 * 市盈率2
//	 * 
//	 * @return
//	 */
//	public String getSyl2() {
//		return syl2;
//	}
//
//	public void setSyl2(String syl2) {
//		this.syl2 = syl2;
//	}
//
//	/**
//	 * 卖价五
//	 * 
//	 * @return
//	 */
//	public double getSjw5() {
//		return sjw5;
//	}
//
//	public void setSjw5(double sjw5) {
//		this.sjw5 = sjw5;
//	}
//
//	/**
//	 * 卖量五
//	 * 
//	 * @return
//	 */
//	public double getSsl5() {
//		return ssl5;
//	}
//
//	public void setSsl5(double ssl5) {
//		this.ssl5 = ssl5;
//	}
//
//	/**
//	 * 卖价四
//	 * 
//	 * @return
//	 */
//	public double getSjw4() {
//		return sjw4;
//	}
//
//	public void setSjw4(double sjw4) {
//		this.sjw4 = sjw4;
//	}
//
//	/**
//	 * 卖量四
//	 * 
//	 * @return
//	 */
//	public double getSsl4() {
//		return ssl4;
//	}
//
//	public void setSsl4(double ssl4) {
//		this.ssl4 = ssl4;
//	}
//
//	/**
//	 * 卖价三
//	 * 
//	 * @return
//	 */
//	public double getSjw3() {
//		return sjw3;
//	}
//
//	public void setSjw3(double sjw3) {
//		this.sjw3 = sjw3;
//	}
//
//	/**
//	 * 卖量三
//	 * 
//	 * @return
//	 */
//	public double getSsl3() {
//		return ssl3;
//	}
//
//	public void setSsl3(double ssl3) {
//		this.ssl3 = ssl3;
//	}
//
//	/**
//	 * 卖价二
//	 * 
//	 * @return
//	 */
//	public double getSjw2() {
//		return sjw2;
//	}
//
//	public void setSjw2(double sjw2) {
//		this.sjw2 = sjw2;
//	}
//
//	/**
//	 * 卖量二
//	 * 
//	 * @return
//	 */
//	public double getSsl2() {
//		return ssl2;
//	}
//
//	public void setSsl2(double ssl2) {
//		this.ssl2 = ssl2;
//	}
//
//	/**
//	 * 卖价一
//	 * 
//	 * @return
//	 */
//	public double getSjw1() {
//		return sjw1;
//	}
//
//	public void setSjw1(double sjw1) {
//		this.sjw1 = sjw1;
//	}
//
//	/**
//	 * 卖量一
//	 * 
//	 * @return
//	 */
//	public double getSsl1() {
//		return ssl1;
//	}
//
//	public void setSsl1(double ssl1) {
//		this.ssl1 = ssl1;
//	}
//
//	/**
//	 * 买价一
//	 * 
//	 * @return
//	 */
//	public double getBjw1() {
//		return bjw1;
//	}
//
//	public void setBjw1(double bjw1) {
//		this.bjw1 = bjw1;
//	}
//
//	/**
//	 * 买量一
//	 * 
//	 * @return
//	 */
//	public double getBsl1() {
//		return bsl1;
//	}
//
//	public void setBsl1(double bsl1) {
//		this.bsl1 = bsl1;
//	}
//
//	/**
//	 * 买价二
//	 * 
//	 * @return
//	 */
//	public double getBjw2() {
//		return bjw2;
//	}
//
//	public void setBjw2(double bjw2) {
//		this.bjw2 = bjw2;
//	}
//
//	/**
//	 * 买量二
//	 * 
//	 * @return
//	 */
//	public double getBsl2() {
//		return bsl2;
//	}
//
//	public void setBsl2(double bsl2) {
//		this.bsl2 = bsl2;
//	}
//
//	/**
//	 * 买价三
//	 * 
//	 * @return
//	 */
//	public double getBjw3() {
//		return bjw3;
//	}
//
//	public void setBjw3(double bjw3) {
//		this.bjw3 = bjw3;
//	}
//
//	/**
//	 * 买量三
//	 * 
//	 * @return
//	 */
//	public double getBsl3() {
//		return bsl3;
//	}
//
//	public void setBsl3(double bsl3) {
//		this.bsl3 = bsl3;
//	}
//
//	/**
//	 * 买价四
//	 * 
//	 * @return
//	 */
//	public double getBjw4() {
//		return bjw4;
//	}
//
//	public void setBjw4(double bjw4) {
//		this.bjw4 = bjw4;
//	}
//
//	/**
//	 * 买量四
//	 * 
//	 * @return
//	 */
//	public double getBsl4() {
//		return bsl4;
//	}
//
//	public void setBsl4(double bsl4) {
//		this.bsl4 = bsl4;
//	}
//
//	/**
//	 * 买价五
//	 * 
//	 * @return
//	 */
//	public double getBjw5() {
//		return bjw5;
//	}
//
//	public void setBjw5(double bjw5) {
//		this.bjw5 = bjw5;
//	}
//
//	/**
//	 * 买量五
//	 * 
//	 * @return
//	 */
//	public double getBsl5() {
//		return bsl5;
//	}
//
//	public void setBsl5(double bsl5) {
//		this.bsl5 = bsl5;
//	}
//
//	/**
//	 * 日期
//	 * 
//	 * @return
//	 */
//	public String getDate() {
//		return date;
//	}
//
//	public void setDate(String date) {
//		this.date = date;
//	}
//
//	/**
//	 * 时间
//	 * 
//	 * @return
//	 */
//	public String getTime() {
//		return time;
//	}
//
//	public void setTime(String time) {
//		this.time = time;
//	}
}