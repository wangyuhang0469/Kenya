package com.example.administrator.kenya.ui.city.used;

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
import butterknife.OnClick;

public class GoodsDetailsActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.banner)
    Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);

        title.setText("物品详情");

        List<String> imageList = new ArrayList<>();
        imageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522661576&di=303c4b46e2f05da6b6363612b5673ee0&imgtype=jpg&er=1&src=http%3A%2F%2Fpic47.nipic.com%2F20140820%2F9666634_183159701000_2.jpg");
        imageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522066924716&di=bc984eb61d75ca2725bc794b1f31f6ca&imgtype=0&src=http%3A%2F%2Fimage.kejixun.com%2F2017%2F0220%2F20170220040005766.jpg");
        imageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522066961510&di=0461b7f9b07b81b33f888cc231c4d142&imgtype=0&src=http%3A%2F%2Fimg0.pconline.com.cn%2Fpconline%2F1712%2F14%2F10506108_6412_thumb.jpg");

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

    @OnClick(R.id.back)
    public void onViewClicked() {
    }
}
