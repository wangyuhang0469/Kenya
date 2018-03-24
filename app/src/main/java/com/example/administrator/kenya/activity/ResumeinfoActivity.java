package com.example.administrator.kenya.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.kenya.R;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResumeinfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumeinfo);
        ButterKnife.bind(this);
    }
}
