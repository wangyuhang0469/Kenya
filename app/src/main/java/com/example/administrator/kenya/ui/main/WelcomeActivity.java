package com.example.administrator.kenya.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.ui.myself.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setBackgroundResource(R.drawable.welcome);

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);


        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class, null);
                finish();
            }
        }, 3000);
//
    }

}


