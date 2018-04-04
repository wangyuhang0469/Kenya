package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsDetailsActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.goodsprice)
    TextView goodsprice;
    @Bind(R.id.goodsdesc)
    TextView goodsdesc;
    @Bind(R.id.goodsusername)
    TextView goodsusername;
    @Bind(R.id.goodsphone)
    TextView goodsphone;
    @Bind(R.id.goodsname)
    TextView goodsname;

    private Goods goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);

        title.setText("物品详情");

        goods = (Goods) getIntent().getExtras().getSerializable("goods");


        initBanner(goods.getImageUrlList());

        goodsname.setText(goods.getGoodsname());
        goodsprice.setText("$" + goods.getGoodsprice());
        goodsdesc.setText(goods.getGoodsdesc());
        goodsusername.setText(goods.getGoodsusername());
        goodsphone.setText("手机号：" + goods.getGoodsphone());


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
        finish();
    }
}
