package com.example.administrator.kenya.ui.city.findmoney;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.Funds;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindMonydetailActivity extends Activity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.find_money_company_info)
    TextView findMoneyCompanyInfo;
    @Bind(R.id.finduser)
    TextView finduser;
    @Bind(R.id.findphone)
    TextView findphone;
    @Bind(R.id.find_money_detail_title)
    TextView findMoneyDetailTitle;
    private Funds funds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_monydetail);
        ButterKnife.bind(this);
        title.setText("资金详情");
        funds = (Funds) getIntent().getExtras().getSerializable("funds");
        findMoneyDetailTitle.setText(funds.getFundsname());
        findMoneyCompanyInfo.setText(funds.getFundsdesc());
        finduser.setText(funds.getFundsuser());
        findphone.setText(funds.getFundsphone());
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
