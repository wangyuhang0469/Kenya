package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GoodsReleaseActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_release);
        ButterKnife.bind(this);

        title.setText("发布");
    }
}
