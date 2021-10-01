package com.procoin.module.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.procoin.util.LanguageUtils;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-10.
 */

public class LanguageSettingActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.cbWay1)
    RadioButton cbWay1;
    @BindView(R.id.cbWay2)
    RadioButton cbWay2;
    @BindView(R.id.cbWay3)
    RadioButton cbWay3;
    @BindView(R.id.rg)
    RadioGroup rg;

    @BindView(R.id.tvMenu)
    TextView tvMenu;

    private String language = LanguageUtils.AUTO;

    @Override
    protected int setLayoutId() {
        return R.layout.language_setting;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.languageset);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.language_setting);
        ButterKnife.bind(this);
        tvMenu.setText(R.string.save);
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LanguageUtils.changeAppLanguage(LanguageSettingActivity.this, language);
//                LanguageUtils.changeAppLanguage(App.getContext(), newLanguage);
                Intent mIntent = new Intent();
                ComponentName comp = new ComponentName("com.cropyme", "LanguageTestActivity");//先跳首页，然后再跳指定的Activity
                mIntent.setComponent(comp);
                mIntent.setAction("android.intent.action.VIEW");
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
//                recreate();
            }
        });
//        final SharedPreferences sp = getSharedPreferences("language_switch", Context.MODE_PRIVATE);
        final SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext().getApplicationContext());
        language = sp.getString("language", "auto");
        if (language.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            cbWay2.setChecked(true);
        } else if (language.equals(Locale.ENGLISH.getLanguage())) {
            cbWay3.setChecked(true);
        } else {
            cbWay1.setChecked(true);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cbWay1) {
                    language = "auto";
                } else if (checkedId == R.id.cbWay2) {
                    language = Locale.SIMPLIFIED_CHINESE.getLanguage();
                } else if (checkedId == R.id.cbWay3) {
                    language = Locale.ENGLISH.getLanguage();
                }
                sp.edit().putString("language", language).commit();
            }
        });
    }


}
