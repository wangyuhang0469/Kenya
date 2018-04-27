package com.example.administrator.kenya.ui.city.findmoney;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.Funds;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.ui.main.PreviewDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

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
    @Bind(R.id.banner)
    Banner banner;
    private Funds funds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_monydetail);
        ButterKnife.bind(this);
        title.setText("资金详情");
        funds = (Funds) getIntent().getExtras().getSerializable("funds");
        initBanner(funds.getFundsImageUrlList());
        findMoneyDetailTitle.setText(funds.getFundsname());
        findMoneyCompanyInfo.setText(funds.getFundsdesc());
        finduser.setText(funds.getFundsuser());
        findphone.setText(funds.getFundsphone());
    }

    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                new PreviewDialog(FindMonydetailActivity.this, funds.getFundsImageUrlList(), position).show();
            }
        });
    }

    @OnClick({R.id.back, R.id.find_money_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.find_money_call:
                new CallPhoneDialog(this,funds.getFundsphone()).show();
                break;
        }
    }
}
