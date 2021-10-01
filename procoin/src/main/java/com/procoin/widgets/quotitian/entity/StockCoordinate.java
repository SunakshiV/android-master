package com.procoin.widgets.quotitian.entity;

import com.procoin.http.base.TaojinluType;


/**
 * K线坐标
 *
 * @author Administrator
 *
 */
public class StockCoordinate implements TaojinluType {

	private float coordinateX;
	private float zgcjY;//最高坐标
	private float zdcjY;//最低坐标
	private float zjcjY;//最近坐标
	private float jrkpY;
	private long date;
	private float amt;// 收盘价与开盘价的差值(收盘价大于开盘价为1,相等为0,收盘价小于开盘价为-1)
	private float zrsp;
	private float M5Y;
	private float M10Y;
	private float M30Y;
	private float cjslY;
	private float VM5Y;
	private float VM10Y;
	private float kdjKY;
	private float kdjDY;
	private float kdjJY;
	private float difY;
	private float deaY;
	private float barY;

	public StockCoordinate() {

	}

	public float getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(float coordinateX) {
		this.coordinateX = coordinateX;
	}

	public float getZgcjY() {
		return zgcjY;
	}

	public void setZgcjY(float zgcjY) {
		this.zgcjY = zgcjY;
	}

	public float getZdcjY() {
		return zdcjY;
	}

	public void setZdcjY(float zdcjY) {
		this.zdcjY = zdcjY;
	}

	public float getZjcjY() {
		return zjcjY;
	}

	public void setZjcjY(float zjcjY) {
		this.zjcjY = zjcjY;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}


	public float getAmt() {
		return amt;
	}

	public void setAmt(float amt) {
		this.amt = amt;
	}

	public void setAmt(double zjcj, double jrkp) {
		if (zjcj == jrkp) amt = 0;
		else amt = zjcj > jrkp ? 1 : -1;
	}

	public float getZrsp() {
		return zrsp;
	}

	public void setZrsp(float zrsp) {
		this.zrsp = zrsp;
	}

	public float getJrkpY() {
		return jrkpY;
	}

	public void setJrkpY(float jrkpY) {
		this.jrkpY = jrkpY;
	}

	public float getM5Y() {
		return M5Y;
	}

	public void setM5Y(float m5y) {
		M5Y = m5y;
	}

	public float getM10Y() {
		return M10Y;
	}

	public void setM10Y(float m10y) {
		M10Y = m10y;
	}

	public float getM30Y() {
		return M30Y;
	}

	public void setM30Y(float m30y) {
		M30Y = m30y;
	}



	public float getCjslY() {
		return cjslY;
	}

	public void setCjslY(float cjslY) {
		this.cjslY = cjslY;
	}

	public float getVM5Y() {
		return VM5Y;
	}

	public void setVM5Y(float vM5Y) {
		VM5Y = vM5Y;
	}

	public float getVM10Y() {
		return VM10Y;
	}

	public void setVM10Y(float vM10Y) {
		VM10Y = vM10Y;
	}

	public float getKdjKY() {
		return kdjKY;
	}

	public void setKdjKY(float kdjKY) {
		this.kdjKY = kdjKY;
	}

	public float getKdjDY() {
		return kdjDY;
	}

	public void setKdjDY(float kdjDY) {
		this.kdjDY = kdjDY;
	}

	public float getKdjJY() {
		return kdjJY;
	}

	public void setKdjJY(float kdjJY) {
		this.kdjJY = kdjJY;
	}

	public float getDifY() {
		return difY;
	}

	public void setDifY(float difY) {
		this.difY = difY;
	}

	public float getDeaY() {
		return deaY;
	}

	public void setDeaY(float deaY) {
		this.deaY = deaY;
	}

	public float getBarY() {
		return barY;
	}

	public void setBarY(float barY) {
		this.barY = barY;
	}

}