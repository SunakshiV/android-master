package com.procoin.widgets;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;


import com.procoin.module.chat.util.ChatSmileyParser;
import com.procoin.module.chat.util.CommentWatcher;

import java.util.regex.Pattern;

public class MyTextWatch extends CommentWatcher {
	private ListView lv;
	private EditText etSay;
//	private StockAdapter adapter;
	private int startIndex = -1;// 点击item项替换的时候用到
	private int endIndex = -1;// 记录上输入的index
	private StringBuilder changeText = new StringBuilder();// 最终要查询的数字
//	private KeyboardDB keyboardDB;
	private boolean fromClick;
//	private Context context;
	private LayoutInflater inflater;
	private int type = -1;
	private StockLvListen stockLvListen;
	private ChatSmileyParser chatSmileyParser;

	public MyTextWatch(Context context, ListView lv, EditText etSay,int num, SurplusNum surplusNum) {
		super(num,surplusNum);
//		this.context = context;
		this.lv = lv;
		this.etSay = etSay;
		lv.setOnItemClickListener(new MyOnItemCllick());
//		keyboardDB = KeyboardDB.getInstance(context);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		chatSmileyParser=ChatSmileyParser.getInstance(context);
	}

	public void setStockLvGoneListen(StockLvListen stockLvListen) {
		this.stockLvListen = stockLvListen;
	}

	class MyOnItemCllick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			fromClick = true;
//			StockInformation stockInfo = (StockInformation) parent.getItemAtPosition(position);
//			StringBuilder sb = new StringBuilder(etSay.getText().toString());
//			sb=sb.replace(Math.max(startIndex, 0), endIndex, "$"+stockInfo.getJc() + "(" + stockInfo.getFdm().toUpperCase() + ")$");
//			etSay.setText(chatSmileyParser.replaceSamll(sb, 0.5));
//			etSay.setSelection(startIndex=etSay.length());
//			endIndex = -1;
//			setLvGone(true);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		super.afterTextChanged(s);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		super.beforeTextChanged(s, start, count, after);
//		Log.d("beforeTextChanged", "s==" + s.toString() + "  start==" + start + "  count==" + count + "  after==" + after);

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		super.onTextChanged(s, start, before, count);
		Log.d("onTextChanged", "start=="+start+"   count==" + count + "  s==" + s.toString());
		if (fromClick) {
			fromClick = false;
			return;
		}
		if (count > 0) {// 添加
			String text = s.subSequence(start, start +count).toString();
			boolean isNum = isNumeric(text);
			boolean isZm = isZm(text);
//			Log.d("onTextChanged", "text=="+text+"  isZm==" + isZm );
			if (type != -1) {
				if (getType(text) != type) {
					setLvGone(true);
					type=-1;
//					return;
				}
			}
			if (isNum || isZm) {
				if (startIndex == -1) {
					startIndex = start;
				}
//				if (endIndex != -1) {
//					if (endIndex != start) {
//						clearChangeText();
//					}
//				}
				type = getType(text);
				endIndex = start + count;
				changeText.append(text);
				int lenth=changeText.toString().length();
				if(lenth>s.length()){
					changeText.delete(0,lenth);
					changeText.append(s);
				}
//				Log.d("onTextChanged", "changText==" + changeText.toString());
				updateLv(changeText.toString());
			} else {
				setLvGone(true);
			}

		} else {// 删除
//			Log.d("delete", "endIndex==" + endIndex + "  start==" + start);
			if (endIndex - 1 != start) {// 不按常规删除
				setLvGone(true);
				endIndex = -1;
			} else {
//				Log.d("changeText", "changeText==" + changeText);
				if (changeText.length() == 0) return;// 按常规删除，但删除的不是数字或者不是要联想的数字return
				changeText = changeText.delete(changeText.length() - 1, changeText.length());
				endIndex = endIndex - 1;// 删除的时候endIndex要改变
//				Log.d("onTextChanged", "changText==" + changeText.toString());
				updateLv(changeText.toString());
			}

		}
	}

	private int getType(String text) {
		if (isNumeric(text)) {
			return 0;
		} else if (isZm(text)) {
			return 1;
		} else {
			return 2;
		}
	}

	private void clearChangeText() {
		startIndex = -1;// endIndex不用清除
		changeText.delete(0, changeText.length());
	}

	private void updateLv(String str) {
//		Group<StockInformation> stockInformations=null;
//		if(isNumeric(str)){
//			stockInformations = keyboardDB.queryDm(changeText.toString().replace("'",""), false, true, 20, null, new String[] { "sh000001" });
//		}else{
//			stockInformations = keyboardDB.queryPy(changeText.toString().replace("'",""), false, true, 20, null, new String[] { "sh000001" });
//		}
//		if (stockInformations == null || stockInformations.size() == 0) {
//			setLvGone(false);
//		} else {
//			setLvVisibile();
//			if (adapter == null) {
//				adapter = new StockAdapter(stockInformations);
//				lv.setAdapter(adapter);
//			} else {
//				adapter.setStocks(stockInformations);
//				adapter.notifyDataSetChanged();
//			}
//		}
	}

	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	private boolean isZm(String str) {
//		Log.d("isZm","str=="+str+" str.replace=="+str.replace("'",""));
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		return pattern.matcher(str.replace("'","")).matches();
	}

	public void setLvGone(boolean isClear) {
		if (lv.getVisibility() == View.VISIBLE) {
			lv.setVisibility(View.GONE);
			if(stockLvListen!=null){
				stockLvListen.whenStockLvGone();
			}
		}
		if (isClear) {
			clearChangeText();
		}

	}

	public void setLvVisibile() {
		if (lv.getVisibility() == View.GONE) {
			lv.setVisibility(View.VISIBLE);
			if(stockLvListen!=null){
				stockLvListen.whenStockLvVisibile();
			}
		}
	}

//	class StockAdapter extends BaseAdapter {
//
//		private Group<StockInformation> stocks;
//
//		public StockAdapter(Group<StockInformation> stockInformations) {
//			this.stocks = stockInformations;
//		}
//
//		public void setStocks(Group<StockInformation> stocks) {
//			this.stocks = stocks;
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public int getCount() {
//			return stocks == null ? 0 : stocks.size();
//		}
//
//		@Override
//		public StockInformation getItem(int position) {
//			if (getCount() == 0) return null;
//			return stocks.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if(convertView==null){
//				convertView= inflater.inflate(R.layout.lv_stock_leneovo_text, null);
//			}
//			TextView tv = (TextView) convertView.findViewById(R.id.tvStock);
//			tv.setText(getItem(position).getJc()+" "+getItem(position).getDm());
//			return convertView;
//		}
//
//	}

	/**
	 *  当listView隐藏时候的回调借口
	 * @author zt
	 *
	 */
	public interface StockLvListen{

		void whenStockLvGone();
		void whenStockLvVisibile();

	}
}