package com.procoin.module.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.collection.ArrayMap;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.data.db.TaoJinLuDatabase;
import com.procoin.data.sharedpreferences.CircleSharedPreferences;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.util.JsonParserUtils;
import com.procoin.module.chat.entity.ChatHomeEntity;
import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.module.circle.fragment.MoreFragment;
import com.procoin.module.circle.ui.PlayVoiceUtilView;
import com.procoin.module.circle.ui.RecordButton;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.VeDate;
import com.procoin.widgets.LogUtils;
import com.procoin.widgets.MyTextWatch;
import com.procoin.module.chat.util.ChatSmileyParser;
import com.procoin.module.circle.entity.CircleChatMore;
import com.procoin.module.circle.util.TjrSoftkeyBoardStateEnum;
import com.procoin.widgets.indicator.CirclePageIndicator;
import com.gyf.barlibrary.OnKeyboardListener;
import com.procoin.http.base.Group;
import com.procoin.http.model.User;
import com.procoin.http.resource.BaseRemoteResourceManager;
import com.procoin.http.retrofitservice.UploadFileUtils;
import com.procoin.http.tjrcpt.PrivateChatHttp;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.task.BaseAsyncTask;
import com.procoin.R;
import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.module.chat.adapter.ChatRoomAdapter;
import com.procoin.module.circle.entity.CircleChatTypeEnum;
import com.procoin.module.circle.fragment.FaceFragment;
import com.procoin.module.circle.ui.BmplitudeText;
import com.procoin.module.circle.ui.RoundProgressBar;
import com.procoin.module.myhome.MyhomeMultiSelectImageActivity;
import com.procoin.subpush.Consts;
import com.procoin.util.InflaterUtils;
import com.procoin.util.StockTextUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class ChatRoomActivity extends TJRBaseActionBarSwipeBackObserverActivity implements OnClickListener {

    private ImageView ivFace;
    private ImageView ivVoice;
    private ImageView ivMore;
    private Button tvSend;
    private LinearLayout layout;
    private FrameLayout flEdit;
    private EditText msgEdit;

    private ListView show;

    private LinearLayout llInfomation;

    private FrameLayout containerBottom;

    private LinearLayout llUnreadMessage;//点击跳到第一条未读消息
    private TextView tvUnReadMessageNum;//多少条未读消息

    private LinearLayout llNewMessage;
    private TextView tvNewMessageNum;


    private static final int MAX_UNREAD_MESSAGE = 10;//当未读消息大于这个值的时候就显示llUnreadMessage


    /**
     * chatRecordNumTemp 为什么要用这个值，因为比如我有50条新消息，我一直往上托托一页chatRecordNumTemp就-=NUM，这样一直拖也能定位到一下是你未读的消息，
     * 如果是直接点击llUnreadMessage 跳转到你有多少条未读消息，则需要用到chatRecordNum，比如chatRecordNum=50，
     * 那我只要确定adapter里面刚好有50条数据就可以了然后setSelection==0，但是这个时候有可能会有新的消息进来，
     * 所以 receiveChatNewsCount 这个参数表示新接受的消息
     * 那么点击的时候，只要在加载（chatRecordNum-adaper.getCount()+receiveChatNewsCount）条数据，然后在定位到setSelection==0就可以了
     * <p/>
     * 在1--10之间就显示一下然后自动消失并且不能点击
     */
    private boolean havaNews;//这个值表示是否显示你有多少条新消息
    private int chatRecordNum;//只要进入这个页面就要获取有多少条未读消息，如果大于MAX_UNREAD_MESSAGE就显示llUnreadMessage,havaNews=true;
    private int chatRecordNumTemp;
    private int receiveChatNewsCount;//
    private int showNewsCount;//跟receiveChatNewsCount不一样，这个参数表示你有多少条新消息，并且是有条件的，必须adapter.getCount-lastVisibilePos>3才有提示才++；
    private int lastVisibilePos;

    private volatile TjrSoftkeyBoardStateEnum currSoftState = TjrSoftkeyBoardStateEnum.getDefault();

    private ChatSmileyParser chatSmileyParser;

    private String chatTopic = "";//
    private String chatName = "";
    private String chatHeadUrl = "";
    //    private String circleLogo = "";
//    private int isVip;
    private ChatRoomAdapter adapter;

    private TaoJinLuDatabase db;
    private static final int NUM = 20;// 每页多少条
    private int firstVisibleItem;

    private View head;
    private ProgressBar pb;
    private TextView tvHeadText;
    //    private GetCircleChatHistoryTask getCircleChatHistoryTask;
    private boolean loadingHistory;// 正在加载历史数据
    private boolean noHistory;// 已经加载了所有的历史记录了


    private User user;

    private String mp3Path;//录音的地址
    private String fileName;
    private int recordTime;//录音时间

    private final int PHOTORESOULT = 3;// 结果

    private BaseRemoteResourceManager remoteResourceManagerTalkie;

    private MyTextWatch watcher;
    private ListView lvStock;

    private boolean isRunning = false;

    private static final Map<String, CircleChatEntity> map = new ConcurrentHashMap<String, CircleChatEntity>();
    private static final int interval = 6000;//5秒扫描一次hashmap
    private static final int overTime = 12000;

    public ArrayMap<String, String> namesMap = new ArrayMap<>();//这个item项替换时用到，因为不需要重复
    public List<StockTextUtils.MatchEntity> etNamesList = new ArrayList<StockTextUtils.MatchEntity>();//这个是在输入框里把 “@阿青” 变成drawable，因为可以重复@阿青，所以用list, setAtImageSpan用到这个

    private GetChatHistoryTask mGetChatHistoryTask;

    private ViewGroup home;

    public InputMethodManager im;
    private SharedPreferences mPreferences;
    protected static final int init_keyboard_height = 650, init_delayed = 300;
    protected volatile int keyboardHeight;

    //    private LinearLayout ivLiveRefresh;
//    private LinearLayout ivMinimize;
//    private LinearLayout ivClose;
    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:
                CommonUtil.LogLa(2, "private_chat_record 收到");
                Group<CircleChatEntity> otherSay = new Group<>();
                Group<CircleChatEntity> mySay = new Group<>();
                if (model.obj instanceof CircleChatEntity) {
                    CircleChatEntity entity = (CircleChatEntity) model.obj;
                    if (entity != null) {
                        if (chatTopic.equals(entity.chatTopic)) {
                            receiveChatNewsCount++;
                            if (entity.userId != getApplicationContext().getUser().getUserId()
                                    || CircleChatTypeEnum.isTip(entity.say)) {
                                otherSay.add(entity);
                            } else {// 如果是自己，发的时候就已经加入到adapter，说明发送成功，改变状态
                                // TODO
                                CommonUtil.LogLa(2, "entity is " + entity.toString());
                                //如果是自己发给别人的在这里生成房间,本来都是统一在收到消息那里生成的,但是自己发给别人的消息里面是没有toName和toHeadurl,所以放到这个页面来
                                if (!TextUtils.isEmpty(chatHeadUrl))
                                    getApplicationContext().getmDb().saveOrUpdateChatInfo(user.getUserId(), entity.toUid, chatHeadUrl, chatName, entity.chatTopic);
                                mySay.add(entity);
//                                    adapter.changeSendState(entity);
                                if (map.containsKey(entity.verify)) {
                                    map.remove(entity.verify);
                                }
                            }
                        }
                    }
                    if (otherSay.size() > 0) {
                        adapter.addItem(otherSay);
                        if (adapter.getCount() - lastVisibilePos >= 3 && lastVisibilePos != 0) {
                            showNewsCount = showNewsCount + otherSay.size();
                            showNewsCount();
                        } else {
                            scrollBottom();
                        }
                    }
                    if (mySay.size() > 0) {
                        adapter.changeSendState(mySay);
                    }
                    break;

                }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.circle_privatechat_room;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }


    protected void saveKeyboardHeight(int keyboardHeight) {
        if (mPreferences == null) return;
        this.keyboardHeight = keyboardHeight;
        containerBottom.getLayoutParams().height = keyboardHeight;
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("height", keyboardHeight);
        editor.commit();
    }

    /**
     * @param context
     * @param chatTopic
     * @param chatName
     * @param headurl   本来可以不要头像的，但是这里要生成房间
     */
    public static void pageJump(Context context, String chatTopic, String chatName, String headurl) {
        LogUtils.d("ChatRoomActivity", "chatTopic==" + chatTopic + "  chatName==" + chatName);
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.CHAT_TOPIC, chatTopic);
        bundle.putString(CommonConst.CHAT_NAME, chatName);
        bundle.putString(CommonConst.CHAT_HEADURL, headurl);
        PageJumpUtil.pageJump(context, ChatRoomActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyWebChromeClient", "onCreate.....");
        CommonUtil.LogLa(2, "chatRoomActiivty is oncreate");
        user = getApplicationContext().getUser();
        db = getApplicationContext().getmDb();
        im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mPreferences = getSharedPreferences("keyboard_height", MODE_PRIVATE);//Context.CONTEXT_IGNORE_SECURITY
        keyboardHeight = mPreferences.getInt("height", init_keyboard_height);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            parserParamsBack(bundle, new ParamsBack() {
                @Override
                public void paramsBack(Bundle bundle, JSONObject jsonObject) throws Exception {
                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.CHAT_TOPIC)) {
                        bundle.putString(CommonConst.CHAT_TOPIC, jsonObject.getString(CommonConst.CHAT_TOPIC));
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.CHAT_NAME)) {
                        bundle.putString(CommonConst.CHAT_NAME, jsonObject.getString(CommonConst.CHAT_NAME));
                    }

                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.CHAT_HEADURL)) {
                        bundle.putString(CommonConst.CHAT_HEADURL, jsonObject.getString(CommonConst.CHAT_HEADURL));
                    }

                }
            });
            chatTopic = bundle.getString(CommonConst.CHAT_TOPIC);
            chatName = bundle.getString(CommonConst.CHAT_NAME);
            chatHeadUrl = bundle.getString(CommonConst.CHAT_HEADURL);

            if (TextUtils.isEmpty(chatTopic)) {
                finish();
                return;
            }
            if (CommonUtil.isMyself(chatTopic)) {
                CommonUtil.showmessage("不能跟自己聊天", this);
                finish();
                return;
            }
        }
//        setContentView(showView());
        showView();
//        showCustomView();
//        attachKeyboardListeners((ViewGroup) findViewById(R.id.rootLayout), containerBottom, msgEdit);
        remoteResourceManagerTalkie = getApplicationContext().getRemoteResourceChatManager();
        chatRecordNum = PrivateChatSharedPreferences.getPriChatRecordNum(this, chatTopic, user.getUserId());
//        CommonUtil.LogLa(2, "getPriChatRecordNum " + chatRecordNum);
        Log.d("chatRoom", "chatRecordNum==" + chatRecordNum);
        chatRecordNumTemp = chatRecordNum;
        if (chatRecordNum > MAX_UNREAD_MESSAGE) {
//            CommonUtil.LogLa(2, "getPriChatRecordNum 显示未读消息");
            havaNews = true;
            llUnreadMessage.setVisibility(View.VISIBLE);
            llUnreadMessage.setOnClickListener(this);
            tvUnReadMessageNum.setText(String.format(getString(R.string.circle_unread_message), String.valueOf(chatRecordNum)));
        } else {
            if (chatRecordNum > 0) {//在1--10之间就显示一下然后自动消失不能点击
                llUnreadMessage.setVisibility(View.VISIBLE);
                llUnreadMessage.setOnClickListener(null);
                tvUnReadMessageNum.setText(String.format(getString(R.string.circle_unread_message), String.valueOf(chatRecordNum)));
                handler.postDelayed(autoDimissUnReadMessage, 2000);
            } else {
                llUnreadMessage.setVisibility(View.GONE);
            }

        }

        initDataFromDb(0, NUM);
        if (TextUtils.isEmpty(chatName)) {
            ChatHomeEntity chatHomeEntity = db.getChatInfo(getUserId(), chatTopic);
            if (chatHomeEntity != null)
                chatName = chatHomeEntity.name;
        }
        mActionBar.setTitle(chatName);
//        if (openKeyboard == null) openKeyboard = new OpenKeyboard();
        register();
//        immersionBar.keyboardEnable(true);
        immersionBar.setOnKeyboardListener(onKeyboardListener);
        CommonUtil.LogLa(2, "chatRoomActiivty is oncreate end");
    }


    public void playNext(int playPos) {
        Log.d("playNext", "playPos==" + playPos + "  size==" + show.getCount() + "  firstVisibile==" + show.getFirstVisiblePosition() + "lastVisibile==" + show.getLastVisiblePosition());
        View view = adapter.getView(playPos, null, show);
        if (view != null) {
            PlayVoiceUtilView playVoiceUtilView = (PlayVoiceUtilView) view.findViewById(R.id.rlVoice);
            if (playVoiceUtilView != null) {
                playVoiceUtilView.performClick();
                int lastVisibile = show.getLastVisiblePosition();
                if (playPos >= lastVisibile) {
                    show.setSelection(playPos);
                }
            }
        }


    }

    private Runnable autoDimissUnReadMessage = new Runnable() {
        @Override
        public void run() {
            hideNews();
        }
    };


    private View showView() {
//        home = (ViewGroup) InflaterUtils.inflateView(this, R.layout.circle_privatechat_room);
        home = (ViewGroup) findViewById(R.id.home);
        llUnreadMessage = (LinearLayout) findViewById(R.id.llUnreadMessage);
        tvUnReadMessageNum = (TextView) findViewById(R.id.tvUnReadMessageNum);


        llNewMessage = (LinearLayout) findViewById(R.id.llNewMessage);
        tvNewMessageNum = (TextView) findViewById(R.id.tvNewMessageNum);

        llNewMessage.setOnClickListener(this);


        chatSmileyParser = ChatSmileyParser.getInstance(this);
        show = (ListView) findViewById(R.id.showList);
        containerBottom = (FrameLayout) findViewById(R.id.containerBottom);
        containerBottom.getLayoutParams().height = keyboardHeight;
        show.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到顶部
                        if (show.getFirstVisiblePosition() == 0) {
                            startGetCircleChatHistoryTask(NUM, false);
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lastVisibilePos = show.getLastVisiblePosition();
                                Log.d("setOnScrollListener", "getLastVisiblePosition==" + show.getLastVisiblePosition() + " count==" + show.getCount() + "  show.getfirstVisibile==" + show.getFirstVisiblePosition() + "  ..." + (show.getLastVisiblePosition() - show.getFirstVisiblePosition()));
                                if (adapter.getCount() == lastVisibilePos) {
                                    hideShowNewsCount();
                                }
                            }
                        }, 300);

                        break;
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        resetShowState();
                        closeSoftKeyboard();
//                        llZone.closeMenu();
//                        hideInput();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ChatRoomActivity.this.firstVisibleItem = firstVisibleItem;
            }
        });
//        show.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                resetShowState();
//                closeSoftKeyboard();
//                CommonUtil.showmessage("onItemClick..", CircleChatRoomActivity.this);
//            }
//        });
        show.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetShowState();
                closeSoftKeyboard();
//                CommonUtil.showmessage("onTouch..", CircleChatRoomActivity.this);
                return false;
            }
        });
        show.addHeaderView(initHead());
        adapter = new ChatRoomAdapter(this, user, 0);
        show.setAdapter(adapter);
        ivFace = (ImageView) findViewById(R.id.ivFace);
        ivVoice = (ImageView) findViewById(R.id.ivVoice);
        layout = (LinearLayout) findViewById(R.id.rootLayout);
        flEdit = (FrameLayout) findViewById(R.id.flEdit);
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        // 有些手机editText padding属性在xml中设置无效 pxToSp(getResources(), 6)
//        msgEdit.setPadding(DensityUtil.px2sp(getApplicationContext(), 6), 0, DensityUtil.px2sp(getApplicationContext(), 40), DensityUtil.px2sp(getApplicationContext(), 3));
//        msgEdit.setFilters(new InputFilter[]{new MyInputFilter()});
        tvSend = (Button) findViewById(R.id.tvSend);
        ivMore = (ImageView) findViewById(R.id.ivMore);

        ivMore.setOnClickListener(this);
        ivFace.setOnClickListener(this);
        ivVoice.setOnClickListener(this);


        tvSend.setOnClickListener(this);

        lvStock = (ListView) findViewById(R.id.lvStock);

        watcher = new MyTextWatch(this, lvStock, msgEdit, 5000, new MyTextWatch.SurplusNum() {
            @Override
            public void callSurplusNum(int num) {
                scrollBottomDelayed();//换行的时候要滚到下面
                if (num < 5000) {
                    if (tvSend.getVisibility() == View.GONE) {
                        tvSend.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("MyTextWatch", "else//////////////");
                    tvSend.setVisibility(View.GONE);
                }


            }
        });
        msgEdit.addTextChangedListener(watcher);
        llInfomation = (LinearLayout) findViewById(R.id.llInfomation);
        llInfomation.setVisibility(View.GONE);
        initVoice(home);
        initFace(home);
        initMore(home);


        return home;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("MyWebChromeClient", "onConfigurationChanged....." + newConfig.orientation);
//        CommonUtil.showmessage("newConfig.orientation=="+newConfig.orientation,this);

        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            if (inCustomView()) {
//                hideCustomView();
//            }
        }
    }


    /**
     * 判断是否是全屏
     *
     * @return
     */
//    public boolean inCustomView() {
//        return (xCustomView != null);
//    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (inCustomView()) {
//                // webViewDetails.loadUrl("about:blank");
//                hideCustomView();
//                return true;
//            } else {
//                mWebView.loadUrl("about:blank");
//                CircleChatRoomActivity.this.finish();
//            }
//        }
//        return false;
//    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
//    public void hideCustomView() {
//        xwebchromeclient.onHideCustomView();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }


//    int lastX, lastY;
//
//    private class MyOnTouchListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    lastX = (int) event.getRawX();
//                    lastY = (int) event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    int nowX = (int) event.getRawX();
//                    int nowY = (int) event.getRawY();
//                    int moveX = nowX - lastX;
//                    int moveY = nowY - lastY;
//                    int top = v.getTop() + moveY;
//                    int bottom = v.getBottom() + moveY;
//                    int left = v.getLeft() + moveX;
//                    int right = v.getRight() + moveX;
//                    v.layout(left, top, right, bottom);
//                    lastX = (int) event.getRawX();
//                    lastY = (int) event.getRawY();
//
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//            }
//            return false;
//        }
//
//    }


    private LinearLayout llVoice;
    private RecordButton ibRecord;


    /**
     */
//    private void showCustomView() {
//        View menu = InflaterUtils.inflateView(this, R.layout.circle_chat_room_custom_menu);
//        llInfomation = (LinearLayout) menu.findViewById(R.id.llInfomation);
//        ivInfomation = (ImageView) menu.findViewById(R.id.ivInfomation);
//        ivInfomation.setImageResource(R.drawable.ic_circle_menu_info_black);
//        llInfomation.setOnClickListener(this);
//        badgeViewMyNews = new BadgeView(this, ivInfomation);
//        badgeViewMyNews.setBadgeMargin(5, 5);
//        badgeViewMyNews.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

//        ivLiveRefresh = (LinearLayout) menu.findViewById(R.id.ivLiveRefresh);
//        ivMinimize = (LinearLayout) menu.findViewById(R.id.ivMinimize);
//        ivClose = (LinearLayout) menu.findViewById(R.id.ivClose);
//
//        ivLiveRefresh.setOnClickListener(ChatRoomActivity.this);
//        ivMinimize.setOnClickListener(ChatRoomActivity.this);
//        ivClose.setOnClickListener(ChatRoomActivity.this);

//        ActionBar.LayoutParams mCustomViewLayoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);
//        mCustomViewLayoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
//        mActionBar.setCustomView(menu, mCustomViewLayoutParams);
//    }
    private void initVoice(View home) {
        llVoice = (LinearLayout) home.findViewById(R.id.llVoice);
        ibRecord = (RecordButton) home.findViewById(R.id.ibRecord);

        ibRecord.setTvBmplitude_left_max((BmplitudeText) home.findViewById(R.id.tvBmplitude_left_max));
        ibRecord.setTvBmplitude_left_mid((BmplitudeText) home.findViewById(R.id.tvBmplitude_left_mid));
        ibRecord.setTvBmplitude_left_min((BmplitudeText) home.findViewById(R.id.tvBmplitude_left_min));
        ibRecord.setTvBmplitude_right_max((BmplitudeText) home.findViewById(R.id.tvBmplitude_right_max));
        ibRecord.setTvBmplitude_right_mid((BmplitudeText) home.findViewById(R.id.tvBmplitude_right_mid));
        ibRecord.setTvBmplitude_right_min((BmplitudeText) home.findViewById(R.id.tvBmplitude_right_min));
        ibRecord.setRpb((RoundProgressBar) home.findViewById(R.id.roundProgressBar));

        ibRecord.setBtnRecordAgain((Button) home.findViewById(R.id.btnRecordAgain));
        ibRecord.setTvRecordState((TextView) home.findViewById(R.id.tvRecordState));
        ibRecord.setTvRecordTime((TextView) home.findViewById(R.id.tvRecordTime));
        ibRecord.setTvTimeWarn((TextView) home.findViewById(R.id.tvTimeWarn));
//		ibRecord.setBtnSend(tvSend);
        ibRecord.setUserId(user.getUserId());
        ibRecord.setRecordStateListener(recordStateListener);
    }

    private LinearLayout llMore;

//    private void setZoneNews() {
//        if (db != null && user != null) {
//            CircleNews circleNews = db.findCircleNews(circleNum, user.getUserId());
//            if (circleNews != null) {
//                llZone.setNewsCount(circleNews.infoNews, circleNews.partyNews, circleNews.gameNews);
//            }
//        }
//
//    }

//    private void setCircleNews() {
//        if (db != null && user != null) {
//            CircleNews circleNews = db.findCircleNews(chatTopic, user.getUserId());
//            if (circleNews != null) {
//                int newsNum = circleNews.applyNews + circleNews.sysPartyNews + circleNews.sysNews + circleNews.evalNews;
//                if (newsNum > 0) {
//                    if (badgeViewMyNews != null) {
//                        badgeViewMyNews.show();
//                        badgeViewMyNews.setText(CommonUtil.setNewsCount(newsNum));
//                    }
//                } else {
//                    if (badgeViewMyNews != null) {
//                        badgeViewMyNews.hide();
//                    }
//                }
//
//            }
//        }
//    }


    private ViewPager morePager;
    private MorePagerAdapter morePagerAdapter;
    private CirclePageIndicator moreIndicator;
    private static final int morePageSize = 8;


    private void initMore(View home) {

        llMore = (LinearLayout) home.findViewById(R.id.llMore);
        morePager = (ViewPager) home.findViewById(R.id.morePager);
        morePagerAdapter = new MorePagerAdapter(getSupportFragmentManager());
        morePagerAdapter.setMoreList(getMoreList());
        morePager.setAdapter(morePagerAdapter);
        moreIndicator = (CirclePageIndicator) home.findViewById(R.id.moreIndicator);
        moreIndicator.setViewPager(morePager);
        moreIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private List<CircleChatMore> getMoreList() {
        List<CircleChatMore> moreList = new ArrayList<CircleChatMore>();
        moreList.add(new CircleChatMore("图片", R.drawable.ic_circle_chat_fragment_more_pic, 0));
//        moreList.add(new CircleChatMore("股票", R.drawable.ic_circle_chat_fragment_more_stock, 1));
//        moreList.add(new CircleChatMore("k线挑战", R.drawable.ic_circle_chat_fragment_more_kline_pk, 2));
//        moreList.add(new CircleChatMore("宝箱", R.drawable.ic_circle_chat_fragment_more_lucky_box, 3));
//        moreList.add(new CircleChatMore("打赏", R.drawable.ic_circle_chat_fragment_more_areward, 2));
//        moreList.add(new CircleChatMore("名片", R.drawable.ic_circle_chat_fragment_more_visitingcard, 3));
//        if (CircleRoleEnum.isRootOrAdmin(role)) {
//            moreList.add(new CircleChatMore("文章", R.drawable.ic_circle_chat_fragment_more_article, 6));
//            moreList.add(new CircleChatMore("微电台", R.drawable.ic_circle_chat_fragment_more_microinterviews, 7));
//            moreList.add(new CircleChatMore("聚会", R.drawable.ic_circle_chat_fragment_more_party, 8));
//        }
        return moreList;
    }


    class MorePagerAdapter extends FragmentPagerAdapter {
        List<CircleChatMore> moreList;

        public MorePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setMoreList(List<CircleChatMore> moreList) {
            this.moreList = moreList;

        }

        @Override
        public Fragment instantiateItem(ViewGroup arg0, int arg1) {
            MoreFragment fragment = (MoreFragment) super.instantiateItem(arg0, arg1);
            fragment.setData(moreList.subList(morePageSize * arg1, Math.min(morePageSize * (arg1 + 1), moreList.size())), onMoreItemclick);//角色改变时，要这里设值才可以
            return fragment;
        }

        @Override
        public Fragment getItem(int arg0) {
            return MoreFragment.newInstance();
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentPagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return moreList == null ? 0 : (moreList.size() % morePageSize == 0 ? moreList.size() / morePageSize : (moreList.size() / morePageSize + 1));
        }

    }


    private void updateMore() {//当角色改变时候，显示也要改变
        if (morePager != null && moreIndicator != null && morePagerAdapter != null) {
            morePagerAdapter.setMoreList(getMoreList());
            morePagerAdapter.notifyDataSetChanged();
        }

    }

    private LinearLayout llFace;
    private ViewPager pager;
    private CirclePageIndicator indicator;
    private static final int pageSize = 17;

    private void initFace(View home) {

        llFace = (LinearLayout) home.findViewById(R.id.llFace);
        pager = (ViewPager) home.findViewById(R.id.pager);
        // pager.setOffscreenPageLimit(limit);
        pager.setAdapter(new FacePagerAdapter(getSupportFragmentManager()));
        indicator = (CirclePageIndicator) home.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    class FacePagerAdapter extends FragmentPagerAdapter {
        int pageCount;
        List<String> group;

        public FacePagerAdapter(FragmentManager fm) {
            super(fm);

            group = chatSmileyParser.getmSmileyList();

            int size = group.size();
            Log.d("face", "all size==" + size);
            pageCount = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
            pager.setOffscreenPageLimit(pageCount - 1);

        }

        @Override
        public Fragment getItem(int arg0) {
            return FaceFragment.newInstance(arg0, group.subList(pageSize * arg0, Math.min(pageSize * (arg0 + 1), group.size())), pageSize, chatSmileyParser, onFaceItemclick, onBackspaceClickedListener);
        }

        @Override
        public int getCount() {
            return pageCount;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ibRecord != null)
            ibRecord.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CircleSharedPreferences.saveCircleSpChatRoomid(this, chatTopic);
//        setZoneNews();
//        setCircleNews();
//        if (livePlayerUtils != null) {
//            livePlayerUtils.onresume();
//        }
    }

//    @Override
//    protected void onShowKeyboard(int keyboardHeight) {
//        super.onShowKeyboard(keyboardHeight);
//        scrollBottomDelayed();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        CircleSharedPreferences.saveCircleSpChatRoomid(this, "0");
        if (adapter != null) adapter.stop();
        if (isFinishing()) {
            try {
                PrivateChatSharedPreferences.clearPriChatRecordNum(this, chatTopic, user.getUserId());//
//            CircleSharedPreferences.saveCircleAt(this, chatTopic, user.getUserId(), 0);//清楚有人@我
                if (adapter != null) adapter.releaseMp();
                if (ibRecord != null) ibRecord.release();
                if (handler != null) handler.removeCallbacksAndMessages(null);
                if (clearPrivateChat != null) unregisterReceiver(clearPrivateChat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resetRecord() {
        if (ibRecord != null) {
            ibRecord.setState(RecordButton.RecordState.INITIALISE);// 不用删除
        }

    }

    /**
     * @param num
     * @param unreadMessage 是否是点击定位到第一条未读消息   一定是点击才为true
     */
    private void startGetCircleChatHistoryTask(int num, boolean unreadMessage) {
        if (!loadingHistory && !noHistory) {
            CommonUtil.cancelAsyncTask(mGetChatHistoryTask);
            mGetChatHistoryTask = (GetChatHistoryTask) new GetChatHistoryTask(num, unreadMessage).executeParams();
        }

    }

    class GetChatHistoryTask extends BaseAsyncTask<Void, Void, Group<CircleChatEntity>> {
        private int firstMark = 0;

        private int num;
        boolean unreadMessage;

        public GetChatHistoryTask(int num, boolean unreadMessage) {
            this.num = num;
            this.unreadMessage = unreadMessage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingHistory = true;
            if (adapter != null) {
                Group<CircleChatEntity> group = adapter.getGroup();
                if (group != null && group.size() > 0) {
                    for (CircleChatEntity entity : group) {
                        if (!entity.say.startsWith(CircleChatTypeEnum.SAY_UNREAD_MESSAGE.type)) {
                            firstMark = entity.chatMark;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        protected Group<CircleChatEntity> doInBackground(Void... params) {
            return db.getPrivateChat(user.getUserId(), chatTopic, firstMark, num);
        }

        @Override
        protected void onPostExecute(Group<CircleChatEntity> histoty) {
            super.onPostExecute(histoty);
            loadingHistory = false;
            Log.d("firstMark", "firstMark==" + firstMark + "   histoty==" + (histoty == null ? "null" : histoty.size()));
            if (histoty != null && histoty.size() > 0) {
                if (unreadMessage) {//是否是点击跳转到未读消息
                    CircleChatEntity chat = newUnderReadMsgCircleChatEntity();
                    histoty.add(0, chat);
                    adapter.addItemBefore(histoty);
                    adapter.notifyDataSetChanged();
                    show.setSelection(0);
                    startGetCircleChatHistoryTask(NUM, false);//这里不会触发滚动回调，所以手动调用
                } else {
                    int historySize = histoty.size();//这一句必须放到addItemBefore之前
                    if (havaNews) {
                        if (chatRecordNumTemp > historySize) {
                            chatRecordNumTemp -= historySize;
                        } else {
                            histoty.add(historySize - chatRecordNumTemp, newUnderReadMsgCircleChatEntity());
                        }
                    }
                    adapter.addItemBefore(histoty);
                    adapter.notifyDataSetChanged();
                    setHeadTextIfNoHistory(historySize);
                    show.setSelectionFromTop(firstVisibleItem + historySize + 1, head.getHeight() + show.getDividerHeight());// 这里要用historySize，因为有可能不够一页

                }

            } else {
                if (!unreadMessage) setHeadTextIfNoHistory(0);//这里是处理比如说数据库只有20条，加载时一条都加载不到
            }

        }

    }

    private CircleChatEntity newUnderReadMsgCircleChatEntity() {
        CircleChatEntity chat = new CircleChatEntity();
        chat.say = CircleChatTypeEnum.SAY_UNREAD_MESSAGE.type;
        return chat;

    }

    private View initHead() {
        head = InflaterUtils.inflateView(this, R.layout.circle_chat_head_load_history);
        pb = (ProgressBar) head.findViewById(R.id.pb);
        tvHeadText = (TextView) head.findViewById(R.id.tvHeadText);
        return head;
    }

    private void initDataFromDb(int mark, int num) {
        Group<CircleChatEntity> group = db.getPrivateChat(user.getUserId(), chatTopic, mark, num);
        Log.d("initDataFromDb", "group.size==" + (group == null ? "null" : group.size()));
        if (group != null && group.size() > 0) {
            if (havaNews) {
                if (chatRecordNumTemp <= NUM) {
                    group.add(NUM - chatRecordNumTemp, newUnderReadMsgCircleChatEntity());
                } else {
                    chatRecordNumTemp -= NUM;
                }
            }
            adapter.setGroup(group);
            setHeadTextIfNoHistory(group.size());
            scrollBottom();
        } else {
            show.removeHeaderView(head);
        }
    }


    /**
     * 隐藏  你有多少条未读消息
     */
    public void hideNews() {
        havaNews = false;
        llUnreadMessage.setVisibility(View.GONE);
    }

    /**
     * 显示你收到多少条新消息
     */
    public void showNewsCount() {
        llNewMessage.setVisibility(View.VISIBLE);
        tvNewMessageNum.setText(String.format(getString(R.string.circle_new_message), String.valueOf(showNewsCount)));
    }

    public void hideShowNewsCount() {
        llNewMessage.setVisibility(View.GONE);
        showNewsCount = 0;
        tvNewMessageNum.setText("");

    }


    /**
     * 如果没有历史记录了，就隐藏pb，显示Text
     */
    private void setHeadTextIfNoHistory(int size) {
        Log.d("setHeadTextIfNoHistory", "setHeadTextIfNoHistory///////////");
        if (size < NUM) {
            pb.setVisibility(View.GONE);
            tvHeadText.setVisibility(View.VISIBLE);
            tvHeadText.setText("没有更多历史记录了");
            noHistory = true;
        }
    }

    private void scrollBottom() {
        Log.d("scrollBottom", "scrollBottom......");
        if (show != null && show.getCount() > 0) {
            show.setSelection(show.getCount());
            if (llNewMessage.getVisibility() == View.VISIBLE)
                llNewMessage.setVisibility(View.GONE);//新消息提醒隐藏
            lastVisibilePos = adapter.getCount();//因为通过点击隐藏的lastVisibilePos不会重新赋值

        }
    }

    private void scrollBottomDelayed() {
        Log.d("scrollBottom", "scrollBottomDelayed......");
        if (show != null && show.getCount() > 0) {
            handler.removeCallbacks(scrollBottom);
            handler.postDelayed(scrollBottom, 300);

        }
    }

    Runnable scrollBottom = new Runnable() {
        @Override
        public void run() {
            show.setSelection(show.getCount());
            lastVisibilePos = adapter.getCount();//因为通过点击隐藏的lastVisibilePos不会重新赋值
            if (llNewMessage.getVisibility() == View.VISIBLE)
                llNewMessage.setVisibility(View.GONE);//新消息提醒隐藏
        }
    };


    RecordButton.RecordStateListener recordStateListener = new RecordButton.RecordStateListener() {
        @Override
        public void onRecordInitialise() {
            Log.d("onRecordInitialise", "onRecordInitialise...");
            if (currSoftState == TjrSoftkeyBoardStateEnum.showVoice) {//如果还是显示下面的voice，就隐藏
                tvSend.setVisibility(View.GONE);
            } else {//否则就看msgEditText里面有没有文字
                if (msgEdit.getText().toString().trim().length() == 0) {
                    tvSend.setVisibility(View.GONE);
                } else {
                    tvSend.setVisibility(View.VISIBLE);
                }
            }

        }

        @Override
        public void onRecording() {
            tvSend.setVisibility(View.GONE);
        }

        @Override
        public void onRecordEnd(String mp3Path, String fileName, int recordTime) {
            ChatRoomActivity.this.mp3Path = mp3Path;
            ChatRoomActivity.this.recordTime = recordTime;
            ChatRoomActivity.this.fileName = fileName;
            Log.d("onRecordEnd", "mp3Path==" + mp3Path + " recordTime==" + recordTime + "  fileName==" + fileName);
            tvSend.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPlaying() {
            tvSend.setVisibility(View.VISIBLE);
        }
    };
    // 这是点击退格
    FaceFragment.OnBackspaceClickedListener onBackspaceClickedListener = new FaceFragment.OnBackspaceClickedListener() {
        @Override
        public void onBackspaceClicked(View v) {
            onBackSapce();
        }
    };

    OnItemClickListener onFaceItemclick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            String faceStr = (String) arg0.getAdapter().getItem(arg2);
            if (!TextUtils.isEmpty(faceStr)) {
                CharSequence smileyStr = chatSmileyParser.replaceSamll(faceStr, 0.5);
                int index = msgEdit.getSelectionStart();// 获取光标所在位置
                if (index < 0 || index >= msgEdit.length()) {
                    msgEdit.append(smileyStr);
                } else {
                    msgEdit.getText().insert(index, smileyStr);
                }
            } else {

            }

        }
    };

    OnItemClickListener onMoreItemclick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            CircleChatMore circleChatMore = (CircleChatMore) arg0.getAdapter().getItem(arg2);
            switch (circleChatMore.pos) {
                case 0://图片
                    MyhomeMultiSelectImageActivity.pageJumpThis(ChatRoomActivity.this, 9, true, false, ChatRoomActivity.class.getName());
//                    Intent intent = new Intent();
//                    intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, "sendPic");
//                    PageJumpUtil.pageJumpResult(ChatRoomActivity.this, MyhomeSelectImageActivity.class, intent, PHOTORESOULT);
                    break;
//                case 1://股票
//                    showKeyBoard(home);
//                    break;
//                case 2://打赏页面
//                    long taUserId = -1;
//                    String taName = "";
//                    ChatHomeEntity chatHomeEntity = db.getChatInfo(user.getUserId(), chatTopic);
//                    if (chatHomeEntity != null) {
//                        taUserId = chatHomeEntity.taUserId;
//                        taName = chatHomeEntity.user_name;
//                    }
//
//                    Bundle bundleReward = new Bundle();
//
//                    bundleReward.putBoolean(CommonConst.ISPRIVATECHAT, true);
//                    bundleReward.putLong(CommonConst.TAUSERID, taUserId);
//                    bundleReward.putString(CommonConst.USERNAME, taName);
//                    PageJumpUtil.pageJumpToData(ChatRoomActivity.this, RewardActivity.class, bundleReward);
//                    CommonUtil.LogLa(2, "打赏页面 " + taUserId + " userName is " + taName);
//
//                    break;
//                case 3://分享名片
//                    Bundle bShare = new Bundle();
//                    bShare.putInt(CommonConst.KEY_EXTRAS_TYPE, 3);
//                    bShare.putString(CommonConst.CHAT_TOPIC, chatTopic);
//                    bShare.putBoolean(CommonConst.ISPRIVATECHAT, true);
//                    PageJumpUtil.pageJumpToData(ChatRoomActivity.this, CircleShareActivity.class, bShare);
//                    break;
            }

        }
    };

    private void onBackSapce() {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        msgEdit.dispatchKeyEvent(event);

    }

    boolean flag = false;//设置下面的高度等于键盘的高度,只设置一次即可
    boolean isPopup = false;//isPopup为true，软键盘弹出，为false，软键盘关闭
    //监听键盘弹出
    OnKeyboardListener onKeyboardListener = new OnKeyboardListener() {
        @Override
        public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
            Log.d("onKeyboardListener", "isPopup==" + isPopup + " keyboardHeight==" + keyboardHeight);
            if (!flag && keyboardHeight > 0) {
                saveKeyboardHeight(keyboardHeight);
                flag = true;
            }
            ChatRoomActivity.this.isPopup = isPopup;
            if (isPopup) {
//                    llZone.closeMenu();
                Log.d("scrollBottomDelayed", "onFocusChange.....");
                if (currSoftState != TjrSoftkeyBoardStateEnum.showNothing) {
                    resetShowState();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                resetShowState();
//                            }
//                        }, init_delayed);
                } else {
                    scrollBottomDelayed();
                }
            } else {
                setShowFragmentByType(currSoftState);
            }

        }
    };


    public void openSoftKeyboard() {
        if (im == null) return;
        im.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
    }

    public void closeSoftKeyboard() {
        if (im == null) return;
        if(this.getCurrentFocus()==null||this.getCurrentFocus().getWindowToken()==null)return;
        im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void show(TjrSoftkeyBoardStateEnum state) {
        if (!delayedDone) return;
        if (currSoftState == TjrSoftkeyBoardStateEnum.showNothing) {
            msgEdit.clearFocus();
            if (isPopup) {
                closeSoftKeyboard();
                currSoftState = state;//之前是用这个setShowFragmentByType(state);显示碎片统一放到onKeyboardListener方法,这样才能确保布局不会顶上去,解决了体验不好的问题
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//            immersionBar.keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);  //单独指定软键盘模式
            } else {//键盘没有弹出来,就直接显示
                setShowFragmentByType(state);
            }
        } else {
            if (currSoftState != state) {
                setShowFragmentByType(state);
            } else {
                msgEdit.requestFocus();
                openSoftKeyboard();
                delayedDone = false;
                handler.postDelayed(resetShowState, 500);
            }
        }
    }


    boolean delayedDone = true;//表示延迟已经完成，防止快速点击的时候体验不好
    Runnable resetShowState = new Runnable() {
        @Override
        public void run() {
            resetShowState();
            delayedDone = true;
        }
    };

    /**
     * 点击返回键 或者滚动listView，如果下面有显示声音，表情，更多，就隐藏全部，并且onClick设置true，否则点击不了
     */
    private void resetShowState() {
        setShowFragmentByType(TjrSoftkeyBoardStateEnum.showNothing);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        immersionBar.keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);  //单独指定软键盘模式
        resetRecord();
        flEdit.setVisibility(View.VISIBLE);
    }


    /**
     * 只能其中一种 private static final int SHOW_NOTHING = -1; private static
     * finalint SHOW_VOICE = 0; private static final int SHOW_FACE = 1; private
     * static final int SHOW_MORE = 2;
     */
    private void setShowFragmentByType(TjrSoftkeyBoardStateEnum enmu) {
        Log.d("setShowFragmentByType", "type==" + enmu.getCurrShow());
        if (enmu == TjrSoftkeyBoardStateEnum.showNothing) {
            if (containerBottom.getVisibility() == View.VISIBLE) {
                containerBottom.setVisibility(View.GONE);
            }
            ivFace.setSelected(false);
        } else {
            if (containerBottom.getVisibility() == View.GONE) {
                containerBottom.setVisibility(View.VISIBLE);
//                llZone.closeMenu();
            }
            llFace.setVisibility(enmu == TjrSoftkeyBoardStateEnum.showFace ? View.VISIBLE : View.GONE);
            llVoice.setVisibility(enmu == TjrSoftkeyBoardStateEnum.showVoice ? View.VISIBLE : View.GONE);
            llMore.setVisibility(enmu == TjrSoftkeyBoardStateEnum.showMore ? View.VISIBLE : View.GONE);
            ivFace.setSelected(enmu == TjrSoftkeyBoardStateEnum.showFace);
            flEdit.setVisibility(enmu == TjrSoftkeyBoardStateEnum.showVoice ? View.INVISIBLE : View.VISIBLE);
            if (enmu == TjrSoftkeyBoardStateEnum.showVoice) {
                tvSend.setVisibility(View.GONE);//显示voice就把按钮先隐藏
            }
            scrollBottomDelayed();
        }
        currSoftState = enmu;
        return;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFace:
//                if(isPopup){//如果键盘已经弹出,先隐藏
//                    closeSoftKeyboard();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            show(TjrSoftkeyBoardStateEnum.showFace);
//                        }
//                    },100);
//                }else{
                show(TjrSoftkeyBoardStateEnum.showFace);
//                }
                break;
            case R.id.ivVoice:
//                if(isPopup){//如果键盘已经弹出,先隐藏
//                    closeSoftKeyboard();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            show(TjrSoftkeyBoardStateEnum.showVoice);
//                        }
//                    },100);
//                }else{
                show(TjrSoftkeyBoardStateEnum.showVoice);
//                }

                break;
            case R.id.ivMore:
//                if(isPopup){//如果键盘已经弹出,先隐藏
//                    closeSoftKeyboard();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            show(TjrSoftkeyBoardStateEnum.showMore);
//                        }
//                    },100);
//                }else{
                show(TjrSoftkeyBoardStateEnum.showMore);
//                }
                break;
            case R.id.tvSend:
                sendContent();
                break;

//            case R.id.iv_zuhe://组合
//
//                break;
//            case R.id.iv_stock://自选股
//                break;


            case R.id.llUnreadMessage:
                if (chatRecordNum <= adapter.getCount()) {
                    int index = getUnreadMessageIndex();
                    if (index != -1) {
                        show.setSelection(index);
                        startGetCircleChatHistoryTask(NUM, false);//这里不会触发滚动回调，所以手动调用
                    }
                } else {
                    startGetCircleChatHistoryTask(chatRecordNum - adapter.getCount() + receiveChatNewsCount, true);
                }

                break;
            case R.id.llNewMessage:
                hideShowNewsCount();
                scrollBottom();
                break;

//            case R.id.llInfomation:
//                Bundle b = new Bundle();
//                b.putString(CommonConst.CHAT_TOPIC, chatTopic);
//                PageJumpUtil.pageJumpResult(this, ChatSettingActivity.class, b);
//                break;
//            case R.id.ivLiveRefresh:
//                break;
//            case R.id.ivMinimize:
//                break;
//            case R.id.ivClose:
//                break;

            default:
                break;
        }
    }

//    TjrBaseDialog wifiTipDialog;
//
//    protected void showWifiTipDialog() {
//        wifiTipDialog = new TjrBaseDialog(this) {
//            @Override
//            public void setDownProgress(int progress) {
//
//            }
//
//            @Override
//            public void onclickOk() {
//                dismiss();
////                showLiveWeb();
//            }
//
//            @Override
//            public void onclickClose() {
//                dismiss();
//
//            }
//        };
//        wifiTipDialog.setCancelable(false);
//        wifiTipDialog.setTvTitle("提示");
//        wifiTipDialog.setMessage("您当前处于非wifi网络环境下,观看直播将产生流量,确定继续使用？");
//        wifiTipDialog.setBtnColseText("关闭");
//        wifiTipDialog.setBtnOkText("观看直播");
//        if (!wifiTipDialog.isShowing()) wifiTipDialog.show();
//    }


//    private void setActionBarTitleOnLive() {
//        mActionBar.setTitle("直播");
//
//        ivLiveRefresh.setVisibility(View.VISIBLE);
//        ivMinimize.setVisibility(View.VISIBLE);
//        ivClose.setVisibility(View.VISIBLE);
//        llInfomation.setVisibility(View.GONE);
//
//
//    }
//
//    private void setActionBarTitleOnLiveOff() {
//        if (TextUtils.isEmpty(circleName)) circleName = db.getCircleName(circleNum);
//        mActionBar.setTitle(circleName);
//        ivLiveRefresh.setVisibility(View.GONE);
//        ivMinimize.setVisibility(View.GONE);
//        ivClose.setVisibility(View.GONE);
//        llInfomation.setVisibility(View.VISIBLE);
//
//    }

    private int getUnreadMessageIndex() {
        int index = -1;
        if (adapter != null && adapter.getCount() > 0) {
            for (int i = 0; i < adapter.getCount(); i++) {
                CircleChatEntity entity = adapter.getItem(i);
                if (entity != null) {
                    if (CircleChatTypeEnum.SAY_UNREAD_MESSAGE.type.equals(entity.say)) {
                        index = i;
                        break;
                    }
                }

            }
        }
        return index;
    }

    private void showKeyBoard(View v) {
//        TjrSocialMTAUtil.trackCustomKVEvent(this, TjrSocialMTAUtil.PROP_CLICKTYPE, TjrSocialMTAUtil.PROPVAlUE_CLICKTYPE, TjrSocialMTAUtil.EVENT_HOMEQUERYSTOCK);
//        openKeyboard.showKeyboard(this, this, v);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
            if (flist == null || flist.size() == 0) return;
//            String file = flist.get(0);
//            if (!TextUtils.isEmpty(file)) {
            try {
                resetShowState();
                for (String file : flist) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file, options);

                    String verifi = Consts.getRandomVerifi(7);
                    adapter.addItem(newCircleChatEntity(CircleChatTypeEnum.SAY_IMG.type + "file://" + file + "," + options.outHeight + "," + options.outWidth, verifi));

                    //这里发图片不是按照顺序的，所以可能发完之后顺序会有问题
                    startSendImgTask("img", options.outHeight, options.outWidth, file, verifi);
                }
                scrollBottom();

            } catch (Exception e) {
                CommonUtil.showmessage("参数错误", ChatRoomActivity.this);
            }
//            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTORESOULT && resultCode == 0x147) {//发一张图片  图片在Intent里面
            if (data != null && data.getExtras() != null) {
                String uri = data.getExtras().getString("uri");
                String path = Uri.parse(uri).getPath();
//                Log.d("onActivityResult", "uri==" + uri);
//                Log.d("onActivityResult", "path==" + path);
//                try {
//                    if (!TextUtils.isEmpty(uri)) {
//                        resetShowState();
////                        setShowFragmentByType(TjrSoftkeyBoardStateEnum.showNothing);//
//                        Bitmap bitmapOfFile = BitmapUtil.getSmallBitmap(path, true);
//                        File f = saveBitmapToFile(bitmapOfFile);
//                        if (f != null && f.exists()) {
//                            Log.d("onActivityResult", "filePath==" + f.getAbsolutePath());
//                            Log.d("onActivityResult", "f.user_name==" + f.getName());
////                            Log.d("onActivityResult", "fileSize==" + f.length());
//                            String verifi = Consts.getRandomVerifi(7);
//                            adapter.addItem(newCircleChatEntity(CircleChatTypeEnum.SAY_IMG.type + "file://" + f.getAbsolutePath() + "," + bitmapOfFile.getHeight() + "," + bitmapOfFile.getWidth(), verifi));
////                            adapter.notifyDataSetChanged();
//                            scrollBottom();
//                            startSendImgTask("img", bitmapOfFile.getHeight(), bitmapOfFile.getWidth(), f.getAbsolutePath(), f.getName(), verifi);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    CommonUtil.showmessage("参数错误", ChatRoomActivity.this);
//                }
            }

        } else if (resultCode == 0x789) {
            if (data == null) return;
            int type = data.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, -1);
            switch (type) {
//                case 0:// 微信
//                    shareWeixin(false);
//                    break;
//                case 1:// 微信好友圈
//                    shareWeixin(true);
//                    break;
//                case 2:// 淘金路好友
//                    if (changtoMyfriendsUI == null) {
//                        changtoMyfriendsUI = new TjrChangtoMyfriendsUI(ChatRoomActivity.this, getNeedFilterUserList(), user.getUserId(), ChatRoomActivity.this);
//                        changtoMyfriendsUI.initPopupMenu(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    }
//                    changtoMyfriendsUI.showPopupMenu(show, Gravity.BOTTOM, 0, 0);
//                    break;

                default:
                    break;
            }
        } else if (resultCode == 0x567) {
//            if (data != null) {
//                long userId = data.getLongExtra(CommonConst.USERID, -1);
//                String userName = data.getStringExtra(CommonConst.USERNAME);
//                addAt2EditText(userId, userName);
//
//                Log.d("resultCode", "userName==" + userName);
//            }
        } else if (0x101 == resultCode) {   //从CircleInformationActivity 返回的结果
//            if (null != data && data.hasExtra("circleLogo") && !TextUtils.isEmpty(data.getStringExtra("circleLogo"))) {
//                circleLogo = data.getStringExtra("circleLogo");
//
//                //将circleLog 返回给CircleFragment
//                setResult(0x101, data);
//                CommonUtil.LogLa(2, "CircleChatRoomActivity----->onActivityResault()");
//            }
        } else if (0x666 == resultCode) {//清除聊天记录
        }
    }

//    public void addAt2EditText(long userId, String userName) {
//        if (userId == this.user.getUserId()) return;
//        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
//        json.put(CommonConst.USERID, userId);
//        AtNeed atNeed = new AtNeed(1, json.toString());
//
//        String atName = "@" + userName;
//        namesMap.put(atName, atNeed.toAtNeed(atName));
//        etNamesList.add(new StockTextUtils.MatchEntity(atName, atNeed.toAtNeed(atName)));
//        Log.d("resultCode", "AtNeed==" + atNeed.toAtNeed(userName));
//        int curIndex = msgEdit.getSelectionStart();
//        msgEdit.getText().insert(curIndex, atName + " ");//后面加多个空格，以便drawable能够居中,因为EditText里面如果只有drawable的话无法居中，包括表情也一样
//        if (curIndex >= 1) {
//            msgEdit.getText().replace(curIndex - 1, curIndex, "");
//        }
//        setAtImageSpan();
//        msgEdit.requestFocus();
//        openSoftKeyboard();
//    }
//
//    private void setAtImageSpan() {
//        if (etNamesList == null || etNamesList.size() == 0) return;
//        String describes = msgEdit.getText().toString();
//        String tmp = describes;
//        SpannableString ss = new SpannableString(tmp);
//        String user_name = "";
//        int fromStart = 0;
//        Log.d("setAtImageSpan", "namesMap.szie==" + etNamesList.size());
//        for (StockTextUtils.MatchEntity entry : etNamesList) {
//            user_name = entry.getKey();
//            if (user_name != null && user_name.trim().length() > 0) {
//                Log.d("setAtImageSpan", "user_name==" + user_name + "   user_name.length==" + user_name.length() + "  fromStart==" + fromStart);
//                if (tmp.indexOf(user_name, fromStart) >= 0
//                        && (tmp.indexOf(user_name, fromStart) + user_name.length()) <= tmp
//                        .length()) {
//                    final Bitmap bmp = getNameBitmap(user_name);
//                    ss.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
//                                   @Override
//                                   public Drawable getDrawable() {
//                                       BitmapDrawable drawable = new BitmapDrawable(
//                                               getResources(), bmp);
//                                       drawable.setBounds(0, 0,
//                                               bmp.getWidth(),
//                                               bmp.getHeight());
//                                       return drawable;
//                                   }
//                               },
//                            tmp.indexOf(user_name, fromStart),
//                            tmp.indexOf(user_name, fromStart) + user_name.length(),
//                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    fromStart = tmp.indexOf(user_name, fromStart) + user_name.length();
//                }
//            }
//        }
//
//        try {
//            ss = chatSmileyParser.replaceSamll(ss, 0.5);//如果没有这句，用了@别人之后，表情又变回文字了
//        } catch (Exception e) {
//        }
//        msgEdit.setTextKeepState(ss);
//    }
//
//    private Bitmap getNameBitmap(String user_name) {
//
//
////        user_name = user_name+" ";//后面加多个空格加到drawable里面 以便用户按退格键一下就可以删除
//        Paint paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.circle_chat_at_color));
//        paint.setAntiAlias(true);
//        paint.setTextSize(msgEdit.getTextSize());
//        Rect rect = new Rect();
//
//        paint.getTextBounds(user_name, 0, user_name.length(), rect);
//
//        int width = (int) (paint.measureText(user_name));
//
//        final Bitmap bmp = Bitmap.createBitmap(width, rect.height(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bmp);
//
//        canvas.drawText(user_name, rect.left, rect.height() - rect.bottom, paint);
//
//        return bmp;
//    }

//
//    @Override
//    public void friendsUICallBackgoback() {
//
//    }
//
//    @Override
//    public void friendsUICallBackgonext(List<User> users) {
////        changtoMyfriendsUI.clearDate();
//        if (users == null || users.size() == 0) return;
//        StringBuffer userIds = new StringBuffer();
//        for (User u : users) {
//            userIds.append(u.getUserId() + ",");
//        }
//        String uids = userIds.toString();
//        userIds.setLength(0);
//        if (uids.endsWith(",")) {
//            uids = uids.substring(0, uids.length() - 1);
//            startApplyByInvitTask(uids);
//        }
//    }

//    private void startApplyByInvitTask(String targetIds) {
//        CommonUtil.cancelAsyncTask(applyByInvitTask);
//        applyByInvitTask = (ApplyByInvitTask) new ApplyByInvitTask(targetIds).executeParams();
//    }
//
//    /**
//     * 邀请淘金路好友
//     */
//    class ApplyByInvitTask extends BaseAsyncTask<Void, Void, Boolean> {
//        private String targetIds;
//        private String msg = "";
//        private Exception exception;
//
//        public ApplyByInvitTask(String targetIds) {
//            this.targetIds = targetIds;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                String result = CircleHttp.getInstance().applyByInvit(String.valueOf(user.getUserId()), chatTopic, targetIds);
//                Log.d("result", "result==" + result);
//                boolean ret = false;
//                if (!TextUtils.isEmpty(result)) {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "success")) {
//                        ret = jsonObject.getBoolean("success");
//                    }
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "msg")) {
//                        msg = jsonObject.getString("msg");
//                    }
//                }
//                return ret;
//            } catch (Exception e) {
//                exception = e;
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aVoid) {
//            super.onPostExecute(aVoid);
//            dismissProgressDialog();
//            if (aVoid) {
//                CommonUtil.showmessage(msg, ChatRoomActivity.this);
//            } else {
//                if (exception != null)
//                    NotificationsUtil.ToastReasonForFailure(ChatRoomActivity.this, exception);
//            }
//
//        }
//    }
//
//    private Group<User> getNeedFilterUserList() {
////        if(membersList!=null&&membersList.size()>0){
////            Group<User> users=new Group<User>();
////            for (CircleMemberUser memberUser:membersList) {
////                users.add(new User(memberUser.userId,memberUser.user_name));
////            }
////            return users;
////        }
//        return null;
//    }
//
//    private void shareWeixin(boolean timeline) {
////        String reContentStr = Html.fromHtml(newsPaper.getLead()).toString();
////        if (reContentStr != null && reContentStr.length() > 60) reContentStr = reContentStr.substring(0, 60) + "...";
////        TjrSocialShare.getInstance().shareToWeixinNoGui(this, "weixin-hotspot", VeJson.setArticleTitle(newsPaper.title), reContentStr, VeJson.getJsonRelId(String.valueOf(newsPaper.articleId), "news"), timeline, BitmapFactory.decodeResource(getResources(), R.drawable.ic_applogo), getApplicationContext().getUser().getUserId());
//
//        if (weChatShare == null) {
//            weChatShare = new WeChatShare(this);
//        }
//        weChatShare.setTimeline(timeline);
//        weChatShare.SendReqURL(inviUrl, shareTitle, shareContent, BitmapFactory.decodeResource(getResources(), R.drawable.ic_applogo));
//
//    }


//    private void startGetWeixinUrlTask() {
//        CommonUtil.cancelAsyncTask(getWeixinUrlTask);
//        getWeixinUrlTask = (GetWeixinUrlTask) new GetWeixinUrlTask().executeParams();
//
//    }

//    class GetWeixinUrlTask extends BaseAsyncTask<Void, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                String result = CircleHttp.getInstance().getWeixinUrl(String.valueOf(user.getUserId()), chatTopic);
//                boolean ret = false;
//                if (!TextUtils.isEmpty(result)) {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "inviUrl")) {
//                        inviUrl = jsonObject.getString("inviUrl");
//                    }
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "shareContent")) {
//                        shareContent = jsonObject.getString("shareContent");
//                    }
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "shareTitle")) {
//                        shareTitle = jsonObject.getString("shareTitle");
//                    }
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "success")) {
//                        ret = jsonObject.getBoolean("success");
//                    }
//                    return ret;
//                }
//
//            } catch (Exception exception) {
//
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            if (aBoolean) {
//                if (!TextUtils.isEmpty(inviUrl) && !TextUtils.isEmpty(shareContent) && !TextUtils.isEmpty(shareTitle)) {
//                    Intent intent = new Intent(ChatRoomActivity.this, CircleInviteActivity.class);
//                    startActivityForResult(intent, 0x456);
//                    overridePendingTransition(R.anim.hotnews_share_in, 0);
//                }
//            }
//        }
//    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isRunning = true;
            if (map.size() == 0) {
                handler.removeCallbacks(this);
                isRunning = false;
                Log.d("runnable", "removeCallbacks.......");
            } else {
                Iterator it = map.entrySet().iterator();
                Log.d("runnable", "iterator.......");
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    CircleChatEntity chat = (CircleChatEntity) entry.getValue();
                    if (chat != null) {
                        if (chat.overtime + interval >= overTime) {
                            chat.state = -1;
                            adapter.changeSendState(chat);
                            it.remove();
                        } else {
                            chat.overtime += interval;
                        }
                    }
                }

                if (map.size() == 0) {
                    handler.removeCallbacks(this);
                    isRunning = false;
                    Log.d("runnable", "removeCallbacks.......");
                } else {
                    handler.postDelayed(this, interval);
                    isRunning = true;
                }

            }
        }
    };


    private CircleChatEntity newCircleChatEntity(String say, String verify) {
        CircleChatEntity entity = new CircleChatEntity();
        entity.userId = getApplicationContext().getUser().getUserId();
        entity.chatTopic = chatTopic;
//        entity.isVip = getApplicationContext().getUser().getIsVip();
        entity.name = getApplicationContext().getUser().getUserName();
        entity.headurl = getApplicationContext().getUser().getHeadUrl();
        entity.state = 1;
        entity.verify = verify;
        entity.say = say;

        map.put(verify, entity);
        if (!isRunning) handler.postDelayed(runnable, interval);

        String currTime = VeDate.getyyyyMMddHHmmss(VeDate.getNow());
        entity.createTime = currTime;
        if (adapter != null && adapter.getCount() > 0) {
            String lastTime = adapter.getItem(adapter.getCount() - 1).createTime;
            if (VeDate.isShowTime(lastTime, currTime)) {
                entity.showTime = true;
            }
        }
        return entity;
    }

    /**
     * 保存图片到淘金目录
     *
     * @param bitmap
     * @return
     * @throws Exception
     */
    private File saveBitmapToFile(Bitmap bitmap) throws Exception {
        if (bitmap == null) return null;
        String fileName2 = System.currentTimeMillis() + "" + user.getUserId() + ".jpg";
        File file2 = remoteResourceManagerTalkie.getFile(fileName2);
        remoteResourceManagerTalkie.writeFile(file2, bitmap, true);
        if (file2 != null && file2.exists()) {
            return file2;
        }
        return null;
    }


    @Override
    public void onBackPressed() {
        if (currSoftState != TjrSoftkeyBoardStateEnum.showNothing) {
            resetShowState();
        } else {
            closeSoftKeyboard();
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//
    }

    public void reSendMsg(CircleChatEntity entity) {
        String say = entity.say;
        map.put(entity.verify, entity);
        if (!isRunning) handler.postDelayed(runnable, interval);
        entity.state = 1;
        adapter.changeSendState(entity);
        try {
            switch (CircleChatTypeEnum.getEnum(say)) {
                case SAY_TEXT:
                    iRemoteService.send(PrivateChatHttp.getInstance().privateChatsendText(getApplicationContext().getUser().getUserId(), chatTopic, say, entity.verify, "text"));
                    break;
                case SAY_IMG:
                    say = say.replace(CircleChatTypeEnum.SAY_IMG.type + "file://", "");
                    String[] sayImg = say.split(",");
                    String reSendFileAbsolutePath = sayImg[0];
                    int reSendBitmapHeight = Integer.parseInt(sayImg[1]);
                    int reSendBitmapWidth = Integer.parseInt(sayImg[2]);
//                    String reSendImgFileName = reSendFileAbsolutePath.substring(reSendFileAbsolutePath.lastIndexOf("/") + 1);
                    startSendImgTask("img", reSendBitmapHeight, reSendBitmapWidth, reSendFileAbsolutePath, entity.verify);
                    break;
                case SAY_VOICE:
                    say = say.replace(CircleChatTypeEnum.SAY_VOICE.type, "");
                    String[] s = say.split(",");
                    String reSendMp3Path = s[0];
                    int reSendRecordTime = Integer.parseInt(s[1]);
//                    String reSendFileName = reSendMp3Path.substring(reSendMp3Path.lastIndexOf("/") + 1);
//                    Log.d("reSendFileName", "reSendFileName==" + reSendFileName);
//                    startSendVoiceTask("voice", reSendRecordTime, reSendMp3Path, reSendFileName, entity.verifi);
                    iRemoteService.send(PrivateChatHttp.getInstance().privateChatsendVoice(getApplicationContext().getUser().getUserId(), chatTopic, reSendMp3Path, reSendRecordTime, entity.verify, "voice"));
                    break;
                case SAY_FDM:
                    iRemoteService.send(PrivateChatHttp.getInstance().sendFdm(getApplicationContext().getUser().getUserId(), chatTopic, say, entity.verify));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    private void sendContent() {

        if (flEdit.getVisibility() == View.INVISIBLE && !TextUtils.isEmpty(mp3Path) && !TextUtils.isEmpty(fileName)) {//发语音
            resetRecord();
//            tvSend.setVisibility(View.GONE);
//            setShowFragmentByType(TjrSoftkeyBoardStateEnum.showNothing);//
            String verifi = Consts.getRandomVerifi(7);
            adapter.addItem(newCircleChatEntity(CircleChatTypeEnum.SAY_VOICE.type + mp3Path + "," + recordTime, verifi));
//            adapter.notifyDataSetChanged();
            scrollBottomDelayed();
            startSendVoiceTask("voice", recordTime, mp3Path, verifi);
        } else {
            String say = msgEdit.getText().toString().trim();

            if (!TextUtils.isEmpty(say)) {// 文字类型
                if (namesMap.size() > 0) {
                    say = filterAtNames(say);
                }
                if (etNamesList != null) etNamesList.clear();
                try {
                    String verifi = Consts.getRandomVerifi(7);
                    Log.d("verifi", "verifi==" + verifi);
                    msgEdit.setText("");
//                    hideInput();
//                    setShowFragmentByType(SHOW_NOTHING);//
                    adapter.addItem(newCircleChatEntity(CircleChatTypeEnum.SAY_TEXT.type + say, verifi));
                    adapter.notifyDataSetChanged();
                    scrollBottomDelayed();
                    iRemoteService.send(PrivateChatHttp.getInstance().privateChatsendText(getApplicationContext().getUser().getUserId(), chatTopic, say, verifi, "text"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    //发送文字的时候，把@阿青换成完整格式  @\((.*?)\)\「(.*?)」
    private String filterAtNames(String say) {
        Log.d("filterAtNames", "say==" + say);
        for (Map.Entry<String, String> entry : namesMap.entrySet()) {
            Log.d("filterAtNames", "key==" + entry.getKey());
            Log.d("filterAtNames", "value==" + entry.getValue());
            say = say.replace(entry.getKey(), entry.getValue());
        }
//        namesMap.clear();//不能在这里清除，因为有可能发送失败
        Log.d("filterAtNames", "say==" + say);
        return say;
    }

    private void sendFdm(String fdm, String code, String currPrice, String rate) {
        try {
            String verifi = Consts.getRandomVerifi(7);
            String say = fdm + "," + code + "," + currPrice + "," + rate;
            adapter.addItem(newCircleChatEntity(CircleChatTypeEnum.SAY_FDM.type + say, verifi));
            adapter.notifyDataSetChanged();
            scrollBottom();
            iRemoteService.send(PrivateChatHttp.getInstance().sendFdm(getApplicationContext().getUser().getUserId(), chatTopic, say, verifi));
        } catch (Exception e) {

        }

    }


    private void startSendVoiceTask(final String type, final int second, String filePath, final String verifi) {

//        CommonUtil.cancelCall(sendVoiceCall);
        //青爷说私聊的语音图片fileZone全部传4
//        Call<ResponseBody> sendVoiceCall = UploadFileUtils.uploadFiles(RedzHttpServiceManager.UPLOADFILE_URL, 3, 0, 4, UploadFileUtils.getFilesMap(filePath));
        Call<ResponseBody> sendVoiceCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "file", "chatVoice", UploadFileUtils.getFilesMap(filePath));
        sendVoiceCall.enqueue(new MyCallBack(ChatRoomActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    String voiceUrl = resultData.getItem("imgUrl", String.class);
//                    try {
//                        iRemoteService.send(PrivateChatHttp.getInstance().privateChatsendVoice(getApplicationContext().getUser().getUserId(), chatTopic, voiceUrl, second, verifi, type));
//                    } catch (RemoteException e) {
//
//                    }
//                }

                if (resultData.isSuccess()) {
                    String[] str = resultData.getStringArray("fileUrlList");
                    if (str != null && str.length > 0) {
                        try {
                            iRemoteService.send(PrivateChatHttp.getInstance().privateChatsendVoice(getApplicationContext().getUser().getUserId(), chatTopic, str[0], second, verifi, type));
                        } catch (RemoteException e) {

                        }
                    }

                }
            }
        });
    }

    private void startSendImgTask(final String type, final int picHeight, final int picWidth, String filePath, final String verifi) {
//        CommonUtil.cancelCall(sendImgCall);
//        Call<ResponseBody> sendImgCall = UploadFileUtils.uploadFiles(RedzHttpServiceManager.UPLOADFILE_URL, 1, 0, 4, UploadFileUtils.getFilesMap(filePath));
        Call<ResponseBody> sendImgCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "chatImage", UploadFileUtils.getImageFilesMap(filePath));
        sendImgCall.enqueue(new MyCallBack(ChatRoomActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    String voiceUrl = resultData.getItem("imgUrl", String.class);
//                    try {
//                        iRemoteService.send(PrivateChatHttp.getInstance().privateChatsendImg(getApplicationContext().getUser().getUserId(), chatTopic, voiceUrl, picHeight, picWidth, verifi, type));
//                    } catch (RemoteException e) {
//
//                    }
//                }
                if (resultData.isSuccess()) {
                    String[] str = resultData.getStringArray("imageUrlList");
                    if (str != null && str.length > 0) {
                        try {
                            iRemoteService.send(PrivateChatHttp.getInstance().privateChatsendImg(getApplicationContext().getUser().getUserId(), chatTopic, str[0], picHeight, picWidth, verifi, type));
                        } catch (RemoteException e) {
                        }
                    }
                }


            }
        });
    }

    public void register() {//清除聊天记录

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConst.CLEAR_PRIVATE_CHAT);
        registerReceiver(clearPrivateChat, intentFilter);
    }

    private BroadcastReceiver clearPrivateChat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String clearChatTopic = intent.getStringExtra(CommonConst.CHAT_TOPIC);
            if (action != null) {
                if (CommonConst.CLEAR_PRIVATE_CHAT.equals(action) && clearChatTopic.equals(chatTopic)) {
                    adapter.clearAllItem();
                    adapter.notifyDataSetChanged();
                    llUnreadMessage.setVisibility(View.GONE);
                    llNewMessage.setVisibility(View.GONE);
                    initDataFromDb(0, NUM);
                }
            }
        }

    };


//    private GetVideoHtmlTextTask getVideoHtmlTextTask;
//
//
//    private void startGetVideoHtmlTextTask() {
//        CommonUtil.cancelAsyncTask(getVideoHtmlTextTask);
//        getVideoHtmlTextTask = (GetVideoHtmlTextTask) new GetVideoHtmlTextTask().executeParams();
//    }
//
//    class GetVideoHtmlTextTask extends BaseAsyncTask<Void, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                String result = WeipanHttp.getInstance().getVideoHtmlText(getDeviceId(), String.valueOf(user.getUserId()), circleNum);
//                Log.d("result", "result==" + result);
//            } catch (Exception e) {
//            }
//
//
//            return null;
//        }
//    }


//    private void startAnim() {
//        if (animatorSet == null) {
//            Log.d("startAnim", "startAnim//////////////");
//            animatorSet = new AnimatorSet();
//            animScale = ObjectAnimator.ofObject(viewAnim, "radius", new RadiusEvaluator(), CommonUtil.dpToPxFloat(getResources(), 4f), CommonUtil.dpToPxFloat(getResources(), 7f), CommonUtil.dpToPxFloat(getResources(), 4f));
//            animAlpha = ObjectAnimator.ofFloat(viewAnim, AnimatorCommonConst.ALPHA, 0.5f, 1f, 0.5f);
//            animatorSet.playTogether(animScale, animAlpha);
//            animAlpha.setRepeatCount(-1);
//            animScale.setRepeatCount(-1);
//            animatorSet.setDuration(1500);
//        }
//        animatorSet.start();
//    }

}
