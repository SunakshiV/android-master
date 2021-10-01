package com.procoin.module.circle;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.circle.adapter.MentionMemberAdapter;
import com.procoin.module.circle.entity.CircleMemberUser;
import com.procoin.module.circle.entity.CircleRoleEnum;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.PinnedSectionListView;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.PageJumpUtil;
import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.model.User;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.circle.entity.PinnedMentionUser;
import com.procoin.widgets.index.FriendIndexUI;
import com.procoin.widgets.index.UserComparator;

import java.util.Collections;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 圈子聊天@成员页面
 *
 * @author kechenng
 */
public class MentionMemberActivity extends TJRBaseToolBarSwipeBackActivity {
    private PinnedSectionListView listView;
    private MentionMemberAdapter mentionMemberAdapter;

    private String circleId;
    private User user;

    private Group<CircleMemberUser> group;
    private Group<PinnedMentionUser> searchResultGroup;
    //    private View friendMainView;
    private UserComparator comparator;
    private FriendIndexUI friendIndexUI;
    private RelativeLayout rlContent;
    private Group<PinnedMentionUser> pmUserGroup;

    @Override
    protected int setLayoutId() {
        return R.layout.friend_main;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*----------- 初始化变量 -----------*/
        user = getApplicationContext().getUser();
        comparator = new UserComparator();
        mentionMemberAdapter = new MentionMemberAdapter(this);

        /*----------- 初始化视图 -----------*/

        if (getIntent().getExtras() != null) {
            circleId = getIntent().getExtras().getString(CommonConst.CIRCLEID);
            if (TextUtils.isEmpty(circleId)) {
                CommonUtil.showmessage("参数错误", this);
                finish();
                return;
            }
        }
//        friendMainView = View.inflate(this, R.layout.friend_main, null);
//        setContentView(friendMainView);
        mActionBar.setTitle("选择回复的人");
        listView = (PinnedSectionListView) findViewById(R.id.lvmyfriendlist);
        listView.setAdapter(mentionMemberAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PinnedMentionUser pmuser = (PinnedMentionUser) parent.getItemAtPosition(position);
                if (pmuser != null && pmuser.user != null) {
                    CircleMemberUser user = pmuser.user;
                    Intent intent = new Intent();
                    intent.putExtra(CommonConst.USERNAME, user.userName);
                    intent.putExtra(CommonConst.USERID, user.userId);
                    setResult(0x567, intent);
                    PageJumpUtil.finishCurr(MentionMemberActivity.this);
                }
            }
        });


        rlContent = (RelativeLayout) findViewById(R.id.rlContent);
        friendIndexUI = new FriendIndexUI(this, new FriendIndexUI.IndexUpdate() {

            @Override
            public void indexUpdate(int position) {
                listView.setSelection(position);
            }
        });
        rlContent.addView(friendIndexUI.showView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        startGetMemberList(circleId);

    }

    Call<ResponseBody> getMemberListCall;

    private void startGetMemberList(String circleId) {
        com.procoin.http.util.CommonUtil.cancelCall(getMemberListCall);
        getMemberListCall = VHttpServiceManager.getInstance().getVService().getMemberList(circleId, 1);
        getMemberListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    group = resultData.getGroup("data", new TypeToken<Group<CircleMemberUser>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        mActionBar.setTitle("所有成员(" + group.size() + ")");//现在后台是全部返回的

                        if (group != null && group.size() > 0) {    //过滤列表中的用户本身
                            int index = -1;
                            for (int i = 0, m = group.size(); i < m; i++) {
                                if (group.get(i).userId == user.getUserId()) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1 && index >= 0) {
                                group.remove(index);
                            }
                        }

                        //处理用户数据，按首字母排序
                        for (CircleMemberUser circleMemberUser : group) {
                            if (  CircleRoleEnum.isRootOrAdmin(circleMemberUser.role)) { //管理员
                                circleMemberUser.shiftKey = "#";
                            } else {
                                circleMemberUser.shiftKey = comparator.getPingYinSpell(circleMemberUser.userName);
                            }
                        }

                        Collections.sort(group, comparator);
                        CircleMemberUser user = null;
                        PinnedMentionUser pmUser = null;
                        pmUserGroup = new Group<>();
                        HashMap<String, Integer> hashMap = new HashMap<>();
                        int position = 0;
                        for (CircleMemberUser circleMemberUser : group) {
                            String shiftKey = circleMemberUser.shiftKey;
                            if (shiftKey == null || shiftKey.length() == 0) shiftKey = "#";
                            char startChar = shiftKey.charAt(0);
                            int startInt = (int) startChar;
                            if ((startInt >= 65 && startInt <= 90) || (startInt >= 97 && startInt <= 122)) {
                                if (!hashMap.containsKey(String.valueOf(startChar))) {
                                    user = new CircleMemberUser();
                                    user.shiftKey = String.valueOf(startChar);
                                    pmUser = new PinnedMentionUser(1, user);
                                    pmUserGroup.add(pmUser);
                                    hashMap.put(String.valueOf(startChar), position);
                                    position++;
                                }
                            } else {
                                if (!hashMap.containsKey(String.valueOf(startChar))) {
                                    user = new CircleMemberUser();
                                    user.shiftKey = "#";
                                    pmUser = new PinnedMentionUser(1, user);
                                    pmUserGroup.add(pmUser);
                                    hashMap.put(String.valueOf(startChar), position);
                                    position++;
                                }
                            }
                            position++;
                            pmUser = new PinnedMentionUser(0, circleMemberUser);
                            pmUserGroup.add(pmUser);
                        }
                        mentionMemberAdapter.setGroup(pmUserGroup);
                        friendIndexUI.setIndexMap(hashMap);


                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.circle_manager_member_menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("搜索成员");
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
                    mentionMemberAdapter.setGroup(pmUserGroup);
                } else {
                    searchResultGroup = searchUser(s);
                    mentionMemberAdapter.setGroup(searchResultGroup);
                }
                return false;
            }
        });

        return true;
    }

    public Group<PinnedMentionUser> searchUser(String s) {
        Group<PinnedMentionUser> pmuGroup = null;
        if (group != null && group.size() > 0) {
            pmuGroup = new Group<>();
            for (int i = 0; i < group.size(); i++) {
                CircleMemberUser user = group.get(i);
                if (user != null && user.userName.contains(s)) {
                    pmuGroup.add(new PinnedMentionUser(0, user));
                }
            }
        }
        return pmuGroup;

    }


}
