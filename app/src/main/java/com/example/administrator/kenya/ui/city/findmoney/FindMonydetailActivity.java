package com.example.administrator.kenya.ui.city.findmoney;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindMonydetailActivity extends Activity {

    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_monydetail);
        ButterKnife.bind(this);
        title.setText("资金详情");
    }

    @OnClick({R.id.back, R.id.find_money_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.find_money_call:
                break;
        }
    }
}
