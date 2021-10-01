//package com.cropyme.social.view;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//import com.cropyme.social.R;
//import CommonUtil;
//import com.tencent.tjrtauth.TAuthView;
//
//public class TJRTAuthView extends TAuthView {
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		View view = CommonUtil.inflateView(R.layout.tjr_social_qq_webview, this);
//		Button btnButton = (Button) view.findViewById(R.id.btnBack);
//		btnButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				TJRTAuthView.this.finish();
//			}
//		});
//		getRootView().addView(view, 0);
//	}
//}
