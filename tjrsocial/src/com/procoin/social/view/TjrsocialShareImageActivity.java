package com.procoin.social.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.procoin.social.R;
import com.procoin.social.baseui.AbstractBaseActivity;
import com.procoin.social.common.TjrSocialShareConfig;
import com.procoin.social.util.CommonUtil;

public class TjrsocialShareImageActivity extends AbstractBaseActivity {
	private String filename;// 图片的文件名
	private String filepath;// 图片的路径
	private View view;
	private Button btnAdd, btnBack;
	private Bundle bundle;
	private OnClick onClick;
	private ImageView buImg;// 现在
	private boolean Other = false;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = getIntent().getExtras();
		if (bundle != null) {
			filename = bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_BITMAP);
			filepath = bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_PHOTO_PATH);
			Other = bundle.getBoolean("weibo_other");

			setContentView(showView());
		} else {
			goback();
			// CommonUtil.pageJumpToData(TjrsocialShareImageActivity.this,
			// TheShareWeiboActivity.class, false, true, bundle);
		}
	}

	private View showView() {
		if (view == null) {
			view = this.getLayoutInflater().inflate(R.layout.tjr_social_imageactivity, null);
			onClick = new OnClick();
			buImg = (ImageView) view.findViewById(R.id.ivImage);
			btnBack = (Button) view.findViewById(R.id.btnBack);
			btnAdd = (Button) view.findViewById(R.id.btnAdd);
			btnAdd.setOnClickListener(onClick);
			btnBack.setOnClickListener(onClick);
			CommonUtil.LogLa(2, "==filename==" + filename + "  filepath====" + filepath + "   Other===" + Other);
			if (filename != null && filepath != null && !Other) {
				// TODO 改错~
				// try {
				// bitmap =
				// getApplicationContext().getRemoteResourceChatManager().getNoCache(filename);
				// CommonUtil.LogLa(2, "==bitmap==" + bitmap);
				// buImg.setImageBitmap(bitmap);
				// } catch (IOException e) {
				// buImg.setImageBitmap(null);
				// }
			} else if (filename != null && filepath != null && Other) {
				CommonUtil.LogLa(2, "==filephoto==" + filepath);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 2;

				buImg.setImageBitmap(BitmapFactory.decodeFile(filepath, opts));
			}
		}
		return view;
	}

	class OnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.btnBack) {
				goback();
			} else if (id == R.id.btnAdd) {

				bundle.remove(TjrSocialShareConfig.KEY_EXTRAS_BITMAP);
				bundle.remove(TjrSocialShareConfig.KEY_EXTRAS_PHOTO_PATH);
				setResult(553, getIntent().putExtras(bundle));
				if (bitmap != null && !bitmap.isRecycled()) {
					buImg.setImageBitmap(null);
					bitmap.recycle();
					System.gc(); // 提醒系统及时回收
				}
				TjrsocialShareImageActivity.this.finish();
			}
		}
	}

	public void goback() {
		if (bitmap != null && !bitmap.isRecycled()) {
			buImg.setImageBitmap(null);
			bitmap.recycle();
		}
		this.finish();
	}

}
