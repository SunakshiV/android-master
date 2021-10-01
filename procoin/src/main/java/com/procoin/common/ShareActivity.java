package com.procoin.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareActivity extends AppCompatActivity implements OnClickListener {
    private GridView gvShare;
    private TextView tvTitle;
    private TextView btnCancle;
    //    private static final int[] imgs = new int[]{R.drawable.ic_hotnews_share_square, R.drawable.ic_hotnews_share_frdcircle, R.drawable.ic_hotnews_share_chat, R.drawable.ic_hotnews_share_circle, R.drawable.ic_hotnews_share_wechat, R.drawable.ic_hotnews_share_wechatfrds, R.drawable.ic_hotnews_share_weibo};
    private int[] imgs = null;
    private String[] text;
    private String title = "";
    private int type = -1;

    private boolean statusBarWhite;//只有OlstarHomeActivity跳过来才为true,因为这个页面是深色的,所以当弹出分享框的时候状态栏的颜色要白色的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(CommonConst.KEY_EXTRAS_TYPE, -1);
            statusBarWhite= bundle.getBoolean("statusBarWhite");
        }

        if (type == -1) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        switch (ShareTypeEnum.getShareTypeEnum(type)) {
            case WECHAT:
                title = "分享";
                imgs = new int[]{
                        R.drawable.ic_hotnews_share_wechat,
                        R.drawable.ic_hotnews_share_wechatfrds,
                };
                text = getResources().getStringArray(R.array.appwebshare);
                break;
        }


        setContentView(R.layout.hotnews_share);
        //沉浸式布局
        ImmersionBar.with(this)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
//                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
//                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarDarkFont(!statusBarWhite)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.white)  //修改flyme OS状态栏字体颜色
//                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .removeSupportAllView() //移除全部view支持
                .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
                .fixMarginAtBottom(true)   //已过时，当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
                .init();  //必须调用方可沉浸式
        gvShare = (GridView) findViewById(R.id.gvShare);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnCancle = (TextView) findViewById(R.id.btnCancle);

        tvTitle.setText(title);

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        String[] from = new String[]{"src", "text"};
        Map<String, Object> map = null;
        for (int i = 0; i < text.length; i++) {
            map = new HashMap<String, Object>();
            map.put(from[0], imgs[i]);
            map.put(from[1], text[i]);
            data.add(map);
        }
        gvShare.setAdapter(new SimpleAdapter(this, data, R.layout.hotnews_share_item, from, new int[]{R.id.ivShare, R.id.tvName}));
        btnCancle.setOnClickListener(this);
        findViewById(R.id.llRoot).setOnClickListener(this);
        gvShare.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, position);
                setResult(0x789, intent);
                finishActivity();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancle:
            case R.id.llRoot:
                finishActivity();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        finishActivity();
        return true;
    }

    private void finishActivity() {
        this.finish();
        overridePendingTransition(0,0);
//        overridePendingTransition(0, R.anim.hotnews_share_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下手机物理返回键
        if (event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    finishActivity();
                    return true;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     */
    public enum ShareTypeEnum {

        DEFAULT_0(-1),
        WECHAT(0);  //网页分享 ,


        private int type;

        ShareTypeEnum(int type) {
            this.type = type;
        }

        public int type() {
            return type;
        }


        public static ShareTypeEnum getShareTypeEnum(int type) {
            for (ShareTypeEnum shareTypeEnum : values()) {
                if (shareTypeEnum.type == type) return shareTypeEnum;
            }
            return DEFAULT_0;
        }

    }
}
