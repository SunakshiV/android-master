package com.procoin.widgets.quotitian.entity;

import com.procoin.http.base.TaojinluType;


/**
 * 股票行情的数据结构
 * 
 * @author Administrator
 * 
 */
public class StockRateAndAmtDomain implements TaojinluType {

	public String fdm; // 证券代码，带有sh、sz
	public String dm; // 证券代码1
	public String jc; // 证券简称2
	public double zrsp; // 昨日收盘价3
	public double jrkp; // 今日开盘价4
	public double zjcj; // 最近成交价、最新价5
	public double cjsl; // 成交数量、总成交量6
	public double cjje; // 成交金额、总成交额7
	public double zgcj; // 最高成交9
	public double zdcj; // 最低成交10
	public double amt; // 涨跌
	public double rate;// 涨幅
	public String time; // 时间:09:05:02
	public String date = "2011-12-27";// 日期

	private int stockPlateType;// 板块那边也有用这个数据结构。这个就是板块那边才用得到


	public StockRateAndAmtDomain() {
	}

	public StockRateAndAmtDomain(String fdm, String jc,double rate) {
		this.fdm = fdm;
		this.jc = jc;
		this.rate = rate;
	}

	/**
	 * 板块类型
	 * 
	 * @return
	 */
	public int getStockPlateType() {
		return stockPlateType - 1;
	}

	public void setStockPlateType(int stockPlateType) {
		this.stockPlateType = stockPlateType + 1;
	}

}