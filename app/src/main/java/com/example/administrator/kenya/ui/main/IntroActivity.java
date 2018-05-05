package com.example.administrator.kenya.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.administrator.kenya.MainActivity;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.ui.myself.LoginActivity;
import com.github.paolorotolo.appintro.AppIntro;


public class IntroActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

//        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

//        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

//        if (isFirstStart) {

        addSlide(SlideFragment.newInstance(R.layout.fragment_splansh1));
        addSlide(SlideFragment.newInstance(R.layout.fragment_splansh2));
        addSlide(SlideFragment.newInstance(R.layout.fragment_splansh3));

        setColorDoneText(Color.parseColor("#313131"));
        setIndicatorColor(Color.parseColor("#555555"), Color.parseColor("#aaaaaa"));

        setSeparatorColor(getResources().getColor(R.color.touming));
        showStatusBar(false);
        showSkipButton(false);
        setDoneText("立即体验");

//            SharedPreferences.Editor e = getPrefs.edit();
//            e.putBoolean("firstStart", false);
//            e.apply();
//        }else {
//            Intent intent = new Intent(this, WelcomeActivity.class);
//            startActivity(intent);
//            finish();
//        }


    }

    private void startMain() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onSkipPressed() {
    }

    @Override
    public void onDonePressed() {
        startMain();
    }

    @Override
    public void onSlideChanged() {
    }

    @Override
    public void onNextPressed() {
    }

}
