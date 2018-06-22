package com.example.administrator.kenya.ui.city.buyhouse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.BuyHouse;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.ui.main.PreviewDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyHouseDetailActivity extends BaseActivity {
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
    @Bind(R.id.house_detail_name)
    TextView houseDetailName;
    @Bind(R.id.house_detail_square)
    TextView houseDetailSquare;
    @Bind(R.id.avatar)
    ImageView avatar;
    private BuyHouse buyHouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_house_detail);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.house_details));
        buyHouse = (BuyHouse) getIntent().getExtras().getSerializable("buyhouse");
        initBanner(buyHouse.getBuyHouseImageUrlList());
        houseDetailName.setText(buyHouse.getHousename());
        houseDetailDesc.setText(buyHouse.getHousedesc());
        houseDetailPrice.setText("KSh " + buyHouse.getHouseprice() + "/Month");
        houseDetailHome.setText(buyHouse.getHousehome());
        houseDetailSquare.setText(buyHouse.getHousesquare() + "㎡");
        houseDetailAddress.setText(buyHouse.getHouseaddress());
        houseUsername.setText(buyHouse.getHouseuser());
        housePhone.setText(getResources().getString(R.string.phone_no_) + buyHouse.getHousephone());

        Glide.with(this).load(AppConstants.BASE_URL + buyHouse.getUser().getUserPortrait())
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);
    }

    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                new PreviewDialog(BuyHouseDetailActivity.this, buyHouse.getBuyHouseImageUrlList(), position).show();
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
                new CallPhoneDialog(this, buyHouse.getHousephone()).show();
                break;
        }
    }
}
