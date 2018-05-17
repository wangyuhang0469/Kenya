package com.example.administrator.kenya.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.kenya.R;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ((TextView) findViewById(R.id.detail_tv)).setText(getIntent().getStringExtra("content"));
    }
}
