package com.procoin.module.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.SysShareData;
import com.procoin.module.home.adapter.SearchUserAdapter;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.entity.SubUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 搜索
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class SearchActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.llCLear)
    LinearLayout llCLear;
    @BindView(R.id.tvDelHistory)
    TextView tvDelHistory;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;

    private Handler handler = new Handler();

    private SearchUserAdapter searchUserAdapter;
    private SearchUserAdapter historyAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.search;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);
        llCLear.setOnClickListener(this);
        searchUserAdapter = new SearchUserAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, false));
        rvList.setAdapter(searchUserAdapter);
        searchUserAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                SubUser data = (SubUser) t;
                SysShareData.appendSearchHistory(SearchActivity.this, data, getUserId());
            }
        });

        historyAdapter = new SearchUserAdapter(this);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.addItemDecoration(new SimpleRecycleDivider(this, false));
        rvHistory.setAdapter(historyAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString().trim();
                if (TextUtils.isEmpty(key)) {
                    searchUserAdapter.clearAllItem();
                    showHistory();
                } else {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 300);
                }
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(etSearch, 0);
            }
        }, 500);

        tvDelHistory.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCLear:
                etSearch.setText("");
                break;
            case R.id.tvDelHistory:
                SysShareData.cleanSearchHistory(SearchActivity.this, getUserId());
                showHistory();
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startSearch(etSearch.getText().toString());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        showHistory();
    }


    private void showHistory() {
        Group historyGroup = SysShareData.getSearchHistory(this, getUserId());
        Log.d("searchHistory", "searchHistory==" + (historyGroup == null ? "null" : historyGroup.size()));
        if (historyGroup == null || historyGroup.size() == 0) {
            llHistory.setVisibility(View.GONE);
        } else {
            llHistory.setVisibility(View.VISIBLE);
            historyAdapter.setGroup(historyGroup);
        }
    }

    Call<ResponseBody> searchCall;

    private void startSearch(String value) {
        CommonUtil.cancelCall(searchCall);
        searchCall = VHttpServiceManager.getInstance().getVService().search(value);
        searchCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    SubUser subUser = resultData.getObject("search", SubUser.class);
                    if (subUser != null) {
                        Group<SubUser> group = new Group<>();
                        group.add(subUser);
                        searchUserAdapter.setGroup(group);
                    } else {
                        searchUserAdapter.setGroup(null);
                    }
                }
            }

        });
    }

}
