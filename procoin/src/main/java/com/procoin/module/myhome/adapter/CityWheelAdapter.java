package com.procoin.module.myhome.adapter;

import com.procoin.widgets.wheelview.WheelAdapter;

/**
 * Adapter for countries
 */
public class CityWheelAdapter implements WheelAdapter {
	// private String countries[] = new String[] { "USA", "Canada", "Ukraine",
	// "France" };

	private String city[];

	public CityWheelAdapter(String city[]) {
		this.city = city;
	}

	@Override
	public int getItemsCount() {
		return city.length;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return city[index];
		}
		return null;
	}

	@Override
	public int getMaximumLength() {
		return city.length;
	}
}