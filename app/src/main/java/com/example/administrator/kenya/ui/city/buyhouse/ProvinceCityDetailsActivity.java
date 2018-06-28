package com.example.administrator.kenya.ui.city.buyhouse;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.interfaces.MyLocationListener;
import com.example.administrator.kenya.utils.MyLocationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProvinceCityDetailsActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.province_city_detail)
    TextView provinceCityDetail;
    @Bind(R.id.province_city_detail_et)
    EditText provinceCityDetailEt;
    @Bind(R.id.buy_house_province_city_sure)
    TextView buyHouseProvinceCitySure;
    Intent intent;
    String provinceName;
    String cityName;
    private LocationManager lm;//位置管理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_city_details);
        ButterKnife.bind(this);
        title.setText(getString(R.string.provinces_and_cities));
        intent = new Intent();
        permissionPosition();
    }

    @OnClick({R.id.back, R.id.buy_house_province_city_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.buy_house_province_city_sure:
                if (provinceCityDetailEt.getText().length() == 0) {
                    toast("" + getString(R.string.please_enter_the_detailed_address));
                } else if (provinceName.length() == 0 || cityName.length() == 0) {
                    toast("" + getString(R.string.Incorrect_information_of_provinces_and_cities));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("province", provinceName);
                    bundle.putString("city", cityName);
                    bundle.putString("content", provinceCityDetailEt.getText().toString());
                    intent.putExtras(bundle);
                    this.setResult(RESULT_OK, intent);
                    this.finish();
                }
                break;
        }
    }

    public void permissionPosition() {
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            //Toast.makeText(this, "请设置定位权限", Toast.LENGTH_SHORT).show();
        } else {
            // 有权限了
            pp();
            //  Toast.makeText(this, "有权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void pp() {
        MyLocationUtil.getInstance(this).getLocationInformation(new MyLocationListener() {
            @Override
            public void success(String province, String city) {
                //  toast(province + city);
                provinceCityDetail.setText(province + city);
                provinceName = province;
                cityName = city;
            }

            @Override
            public void failed(String message) {
                toast(message);
            }
        });
    }

}
