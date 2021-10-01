package com.procoin.module.login;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.login.adapter.CountryCodeAdapter;
import com.procoin.module.login.entity.CountryCode;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * 国家代码列表
 *
 * @author zhengmj
 */
public class AllCountryCodeListActivity extends TJRBaseToolBarSwipeBackActivity {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private CountryCodeAdapter countryCodeAdapter;

    private Group<CountryCode> group;
    private Group<CountryCode> searchResultGroup;


    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return "请选择国家或地区";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        countryCodeAdapter = new CountryCodeAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this));
        rvList.setAdapter(countryCodeAdapter);
        countryCodeAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                CountryCode countryCode = (CountryCode) t;
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name", countryCode.name);
                bundle.putString("code", countryCode.code);
                intent.putExtras(bundle);
                setResult(0x456, intent);
                finish();
            }
        });
        startGetCountrycodeinfolist();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.circle_manager_member_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("搜索国家或地区");
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("setOnQueryTextListener", "onQueryTextSubmit   s==" + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("setOnQueryTextListener", "onQueryTextChange   s==" + s);
                if (TextUtils.isEmpty(s)) {
                    countryCodeAdapter.setGroup(group);
                } else {
                    searchResultGroup = searchUser(s);
                    countryCodeAdapter.setGroup(searchResultGroup);
                }
                return false;
            }
        });

        return true;
    }

    public Group<CountryCode> searchUser(String s) {
        Group<CountryCode> searchResult = null;
        if (group != null && group.size() > 0) {
            searchResult = new Group<CountryCode>();
            for (int i = 0; i < group.size(); i++) {
                CountryCode countryCode = group.get(i);
                if (countryCode != null) {
                    if (countryCode.name.contains(s) || countryCode.code.contains(s))
                        searchResult.add(countryCode);
                }
            }
        }
        return searchResult;
    }


    Call<ResponseBody> getMemberListCall;

    private void startGetCountrycodeinfolist() {
        CommonUtil.cancelCall(getMemberListCall);
        getMemberListCall = VHttpServiceManager.getInstance().getVService().countrycodeinfolist();
        getMemberListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    group = resultData.getGroup("codeInfos", new TypeToken<Group<CountryCode>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        countryCodeAdapter.setGroup(group);
                    }
                }
            }
        });
    }


}
