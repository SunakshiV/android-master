package com.procoin.module.welcome.fragment;


import com.procoin.module.home.fragment.UserBaseFragment;

public class BaseWelComeFragment extends UserBaseFragment {

	protected boolean left, right;

	public void changeView(boolean left, boolean right) {
		this.right = right;
		this.left = left;
	}

}
