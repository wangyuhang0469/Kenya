package com.example.administrator.kenya.ui.city.buyhouse;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.interfaces.MyLocationListener;
import com.example.administrator.kenya.ui.main.YesOrNoDialog;
import com.example.administrator.kenya.utils.MyLocationUtil;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.administrator.kenya.MyApplication.MyApplication.mContext;

public class ProvinceCityDetailsActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.buy_house_info_title)
    TextView buyHouseInfoTitle;
    @Bind(R.id.buy_house_province_city_sure)
    TextView buyHouseProvinceCitySure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_city_details);
        ButterKnife.bind(this);
        title.setText("省市详情");
        MyLocationUtil.getInstance(this).getLocationInformation(new MyLocationListener() {
            @Override
            public void success(String province, String city) {
                toast(province + city);
            }

            @Override
            public void failed(String message) {
                toast(message);
            }
        });
    }

    @OnClick({R.id.back, R.id.buy_house_province_city_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.buy_house_province_city_sure:
                break;
        }
    }
}
