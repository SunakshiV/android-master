package com.procoin.module.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.util.CommonUtil;
import com.procoin.R;

public class MyhomeInfoTextActivity extends TJRBaseToolBarSwipeBackActivity {
    //    private View view;
    private EditText etSay;
    private Intent intent;

    private InputMethodManager im;

    @Override
    protected int setLayoutId() {
        return R.layout.myhome_fix_text;
    }

    @Override
    protected String getActivityTitle() {
        return "个人简介";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getIntent() != null) {
            intent = this.getIntent();
//            setContentView(ShowView());
            ShowView();
        } else {
//            CommonUtil.LogLa(2, "MyhomeInfoTextActivity finish");
            finish();
            return;
        }
    }

    private void ShowView() {
//            view = View.inflate(this, R.layout.myhome_fix_text, null);
        TextView tvNum = (TextView) findViewById(R.id.tvNum);
        tvNum.setVisibility(View.GONE);
        etSay = (EditText) findViewById(R.id.etSay);
        if (intent != null) {
            String text = intent.getStringExtra(CommonConst.MYINFO);
            etSay.setText(text);
            etSay.setSelection(text.length());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finish_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                String text = etSay.getText().toString();
                if (text.length() > 140) {
                    CommonUtil.showmessage("文字太长了,不能超过140个字符", this);
                } else {
                    closeSoftKeyboard();
                    intent.putExtra(CommonConst.MYINFO, etSay.getText().toString());
                    setResult(0x252, intent);
                    onBackPressed();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void closeSoftKeyboard() {
        if (im == null) im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

}
