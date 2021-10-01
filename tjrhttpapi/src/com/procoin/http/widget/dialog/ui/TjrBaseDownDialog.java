package com.procoin.http.widget.dialog.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.http.widget.dialog.base.AbstractBaseDialog;
import com.procoin.http.R;

public abstract class TjrBaseDownDialog extends AbstractBaseDialog {
	private TextView tvTitle; // 标题
	private Button btnClose;
	private ProgressBar pbProgress; // 进度条

	public TjrBaseDownDialog(Context context) {
		super(context);
		initTheme(context);
	}

	public void setTitle(CharSequence str) {
		tvTitle.setText(str);
	}

	public void setProgressSource(int i) {
		pbProgress.setProgress(i);

	}

	/**
	 * 初始化
	 */
	public void setPbProgressinit() {
		pbProgress.setProgress(0);
	}

	/**
	 * 关闭按钮
	 * 
	 * @param str
	 */
	public void setBtnColseText(CharSequence str) {
		btnClose.setText(str);
	}

	private void initTheme(Context context) {
		OnClick onClick = new OnClick();
		this.setContentView(R.layout.base_abstract_down_dialog);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		btnClose = (Button) this.findViewById(R.id.btnClose);
		pbProgress = (ProgressBar) this.findViewById(R.id.tjrpbProgress);
		btnClose.setOnClickListener(onClick);
	}

	private class OnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			TjrBaseDownDialog.this.dismiss();
			if (v.getId() == R.id.btnOk) {
			} else if (v.getId() == R.id.btnClose) {
				onclickClose();
			}
		}

	}

}
