package com.example.administrator.kenya.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.administrator.kenya.MainActivity;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.ui.myself.LoginActivity;


public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setBackgroundResource(R.drawable.linshi2);

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome2);


        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }, 3000);

    }
}


