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
        imageList.add("http://img011.hc360.cn/k1/M0B/24/D0/wKhQwFdjdHSER6NQAAAAADZsFuo401.jpg");
        imageList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2351109249,2100272265&fm=27&gp=0.jpg");
        imageList.add("http://pic.90sjimg.com/design/00/14/38/18/5581393a30663.jpg");

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
