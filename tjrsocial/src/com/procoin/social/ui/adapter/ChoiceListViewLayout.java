package com.procoin.social.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.social.R;

import java.util.ArrayList;

@SuppressLint({ "ViewConstructor", "ShowToast", "ShowToast" })
public class ChoiceListViewLayout extends LinearLayout {
	private AppCompatActivity activity;
	private ViewHolder viewHolder;
	private ArrayList<View> viewlist = new ArrayList<View>();

	public ChoiceListViewLayout(AppCompatActivity activity) {
		super(activity);
		this.activity = activity;
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public void createView() {
		View convertView = activity.getLayoutInflater().inflate(R.layout.tjr_social_thechoice_view, null);
		viewHolder = new ViewHolder(convertView);
		int size = viewlist.size();
		viewHolder.setData(size);
		convertView.setTag(viewHolder);
		this.addView(convertView);
		viewlist.add(convertView);
	}

	public void delView(int position) {
		View convertView = viewlist.get(position);
		viewlist.remove(convertView);
		this.removeView(convertView);
		resetView();
	}

	private void resetView() {
		int i = 0;
		for (View view : viewlist) {
			((ViewHolder) view.getTag()).setData(i);
			i++;
		}
	}

	class Onclick implements View.OnClickListener {
		private int position;

		@Override
		public void onClick(View v) {
			if (ChoiceListViewLayout.this.getChildCount() <= 2) {
				Toast.makeText(activity, "至少要保留两个选项", Toast.LENGTH_SHORT).show();
				return;
			}
			delView(position);
		}

		public void setPosition(int position) {
			this.position = position;
		}

	}

	public ArrayList<View> getView() {
		return viewlist;
	}

	public int getCount() {
		return viewlist.size();
	}

	public class ViewHolder {
		private EditText edContent;
		private TextView tvTag;
		private Button btnDelete;
		private Onclick onclick;

		public ViewHolder(View view) {
			edContent = (EditText) view.findViewById(R.id.etWeicontent);
			tvTag = (TextView) view.findViewById(R.id.thecount);
			btnDelete = (Button) view.findViewById(R.id.btnDelete);
			onclick = new Onclick();
			btnDelete.setOnClickListener(onclick);
		}

		public void setData(int position) {
			char s = (char) (65 + position);
			tvTag.setText(s + ".");
			onclick.setPosition(position);
		}

		public EditText getEditText() {
			return edContent;
		}

		public String getTextContent() {
			return edContent.getText().toString();
		}
	}

}
