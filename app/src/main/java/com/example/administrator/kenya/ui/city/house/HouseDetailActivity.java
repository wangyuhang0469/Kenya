package com.example.administrator.kenya.ui.city.house;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HouseDetailActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.banner)
    Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);
        ButterKnife.bind(this);
        title.setText("房屋详情");

        List<String> imageList = new ArrayList<>();
        imageList.add("http://img10.soufunimg.com/viewimage/agents/2016_07/16/M02/07/B0/wKgHFFeJ14GIOQPPAACxeVCfl2gAAMBFgFiqCYAALGR861/722x542.jpg");
        imageList.add("http://bjstatic.centaline.com.cn/Images/20161029/055455_6f8228ae-5e10-4e08-b778-ddb3326776a1.jpg");
        imageList.add("http://img3n.soufunimg.com/viewimage/agents/2015_06/08/M01/0E/14/wKgFk1V1aaGIO2pVAAOJ_381io4AAWAJACRjpAAA4oX332/722x542.jpg");

        initBanner(imageList);
    }

    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                toast("点击了" + (position + 1));
            }
        });
    }
}
