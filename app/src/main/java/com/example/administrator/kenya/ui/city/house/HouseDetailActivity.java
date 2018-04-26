package com.example.administrator.kenya.ui.city.house;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.main.PreviewDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HouseDetailActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.house_detail_desc)
    TextView houseDetailDesc;
    @Bind(R.id.house_detail_price)
    TextView houseDetailPrice;
    @Bind(R.id.house_detail_home)
    TextView houseDetailHome;
    @Bind(R.id.house_username)
    TextView houseUsername;
    @Bind(R.id.house_phone)
    TextView housePhone;
    @Bind(R.id.house_detail_address)
    TextView houseDetailAddress;
    private House house;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);
        ButterKnife.bind(this);
        title.setText("房屋详情");
        house = (House) getIntent().getExtras().getSerializable("house");
        initBanner(house.getHouseImageUrlList());
        houseDetailDesc.setText(house.getLeasedesc());
        houseDetailPrice.setText("$" + house.getLeaseprice() + "/月");
        houseDetailHome.setText(house.getLeasesquare());
        houseDetailAddress.setText(house.getLeaseaddress());
        houseUsername.setText(house.getLeasename());
        housePhone.setText("手机：" + house.getLeasephone());
    }

    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                new PreviewDialog(HouseDetailActivity.this, house.getHouseImageUrlList(), position).show();
            }
        });
    }

    @OnClick({R.id.back, R.id.call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.call:
                break;
        }
    }
}
