package com.example.administrator.kenya.ui.city.findmoney;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.kenya.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindProjectdetailActivity extends Activity {

    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_projectdetail);
        ButterKnife.bind(this);
        title.setText("项目详情");
    }
}
