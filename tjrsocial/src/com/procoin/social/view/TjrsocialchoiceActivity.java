package com.procoin.social.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.procoin.social.common.TjrSocialShareConfig;
import com.procoin.social.R;
import com.procoin.social.baseui.AbstractBaseActivity;
import com.procoin.social.ui.adapter.ChoiceListViewLayout;
import com.procoin.social.ui.adapter.ChoiceListViewLayout.ViewHolder;
import com.procoin.social.util.CommentWatcher;
import com.procoin.social.util.CommentWatcher.SurplusNum;

/**
 * 这个是weibo第二个页面
 * 
 * @author zhengmj
 * 
 */

public class TjrsocialchoiceActivity extends AbstractBaseActivity implements SurplusNum {
	private View view;
	private OnClick onClick;
	// private ArrayList<View> array = new ArrayList<View>();// 0.1.2.32.
	private TextView tvNum;
	private CommentWatcher watcher;
	private String surplusNum, surplusNumPass140;// 字数没有超过140
	private ScrollView mylist;
	private ChoiceListViewLayout adapter;
	private RadioButton radio1, radio2;
	private Bundle bundle;
	private EditText etcontent;
	private LinearLayout lview;
	private ArrayList<String> QuestionArray;
	private String Question;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_QUESTION_ARRAY)) QuestionArray = bundle.getStringArrayList(TjrSocialShareConfig.KEY_EXTRAS_QUESTION_ARRAY);
			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_QUESTION)) Question = bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_QUESTION);
		}
		setContentView(ShowView());
	}

	private View ShowView() {
		if (view == null) {
			lview = new LinearLayout(this);
			lview.setOrientation(LinearLayout.VERTICAL);
			view = this.getLayoutInflater().inflate(R.layout.tjr_social_chage_acticty, null);
			onClick = new OnClick();
			adapter = new ChoiceListViewLayout(this);
			surplusNum = getString(R.string.surplusNum);
			surplusNumPass140 = getString(R.string.surplusNumPass140);
			mylist = (ScrollView) view.findViewById(R.id.lvchoice);
			View footview = this.getLayoutInflater().inflate(R.layout.tjr_social_chage_foot, null);
			Button btnGroupAdd = (Button) footview.findViewById(R.id.btnGroupAdd);
			lview.addView(getHeader());
			lview.addView(adapter);
			lview.addView(footview);
			mylist.addView(lview);
			adapter.createView();
			adapter.createView();// 创建2个
			Button btnblack = (Button) view.findViewById(R.id.btnBack);
			Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
			btnblack.setOnClickListener(onClick);
			btnAdd.setOnClickListener(onClick);
			btnGroupAdd.setOnClickListener(onClick);
			if (QuestionArray != null && Question != null) {
				int count = QuestionArray.size() - 1;
				etcontent.setText(QuestionArray.get(count));
				if (Question.toString().equals("1")) {
					radio1.setChecked(true);
				} else {
					radio2.setChecked(true);
				}
				if (count <= 2) {
					ViewHolder viewH = (ViewHolder) adapter.getView().get(0).getTag();
					ViewHolder viewH2 = (ViewHolder) adapter.getView().get(1).getTag();
					viewH.getEditText().setText(QuestionArray.get(0));
					viewH2.getEditText().setText(QuestionArray.get(1));
				} else {
					ViewHolder viewH = (ViewHolder) adapter.getView().get(0).getTag();
					ViewHolder viewH2 = (ViewHolder) adapter.getView().get(1).getTag();
					viewH.getEditText().setText(QuestionArray.get(0));
					viewH2.getEditText().setText(QuestionArray.get(1));
					for (int i = 2; i < count; i++) {
						adapter.createView();
						ViewHolder theview = (ViewHolder) adapter.getView().get(i).getTag();
						theview.getEditText().setText(QuestionArray.get(i));
					}
				}
			}
		}
		return view;
	}

	private View getHeader() {
		View header = this.getLayoutInflater().inflate(R.layout.tjr_social_chage_header, null);
		watcher = new CommentWatcher(140, this);
		radio1 = (RadioButton) header.findViewById(R.id.radioButton1);
		radio2 = (RadioButton) header.findViewById(R.id.radioButton2);
		tvNum = (TextView) header.findViewById(R.id.tvNum);
		etcontent = (EditText) header.findViewById(R.id.etWeico);
		etcontent.addTextChangedListener(watcher);
		tvNum.setText(String.format(surplusNum, 140));// 限制的最大字数
		radio1.setChecked(true);
		return header;
	}

	@SuppressLint("ParserError")
	class OnClick implements OnClickListener {

		public void onClick(View v) {
			int myid = v.getId();
			if (myid == R.id.btnBack) {
				goback();
			} else if (myid == R.id.btnGroupAdd) {
				if (adapter.getCount() < 10) {
					adapter.createView();
				} else {
					Toast.makeText(TjrsocialchoiceActivity.this, "最多只能添加十项", Toast.LENGTH_SHORT).show();
				}
			} else if (myid == R.id.btnAdd) {
				pageJump();
			}
		}
	}

	private void pageJump() {
		Editable content = etcontent.getText();
		ViewHolder viewH = (ViewHolder) adapter.getView().get(0).getTag();
		ViewHolder viewH2 = (ViewHolder) adapter.getView().get(1).getTag();
		if (content != null && content.length() > 0) {
			String sContent = content.toString().trim().replaceAll(" ", "");
			if (sContent.length() <= 0) {
				Toast.makeText(TjrsocialchoiceActivity.this, "内容不能全为空格!", Toast.LENGTH_SHORT).show();
				return;
			} else if (!watcher.getNumber()) {
				Toast.makeText(TjrsocialchoiceActivity.this, "内容字数超过140个中文字符,或280个英文字符!", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			Toast.makeText(TjrsocialchoiceActivity.this, "问题不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (viewH.getTextContent().equals("") || viewH2.getTextContent().equals("")) {
			Toast.makeText(TjrsocialchoiceActivity.this, "至少前两个答案不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < adapter.getCount(); i++) {
			String thecontent = ((ViewHolder) adapter.getView().get(i).getTag()).getTextContent();
			if (!thecontent.equals("")) {
				list.add(thecontent);
			}
		}
		list.add(content.toString());
		if (radio1.isChecked()) {
			bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_QUESTION, "1");
		} else {
			bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_QUESTION, "2");
		}
		bundle.putStringArrayList(TjrSocialShareConfig.KEY_EXTRAS_QUESTION_ARRAY, list);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		this.setResult(554, intent);
		this.finish();
	}

	@Override
	public void callSurplusNum(int num) {
		tvNum.setText(Html.fromHtml(String.format(num < 0 ? surplusNumPass140 : surplusNum, num)));
		if (num < 0) Toast.makeText(TjrsocialchoiceActivity.this, "输入内容超过140个中文字符(280个英文字符)!", Toast.LENGTH_SHORT).show();
	}

	public void goback() {
		this.finish();
	}

}
