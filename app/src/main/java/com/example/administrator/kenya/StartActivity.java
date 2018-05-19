package com.example.administrator.kenya;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.ui.main.IntroActivity;
import com.example.administrator.kenya.ui.main.WelcomeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartActivity extends BaseActivity {

    @Bind(R.id.start)
    ImageView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        isFirstStart();
        ButterKnife.bind(this);
    }

    // 判断是否第一次启动来决定跳转不同的欢迎界面
    private void isFirstStart() {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
        if (isFirstStart) {
            startActivity(IntroActivity.class, null);
            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean("firstStart", false);
            e.apply();
            finish();
        } else {
            startActivity(WelcomeActivity.class, null);
            finish();
        }
    }
}
