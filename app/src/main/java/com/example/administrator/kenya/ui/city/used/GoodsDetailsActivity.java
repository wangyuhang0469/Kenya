package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Goods;
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
import de.hdodenhof.circleimageview.CircleImageView;

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
    @Bind(R.id.avatar)
    CircleImageView avatar;
    private Goods goods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.goods_detail));
        goods = (Goods) getIntent().getExtras().getSerializable("goods");
        initBanner(goods.getImageUrlList());
        goodsname.setText(goods.getGoodsname());
        goodsprice.setText("KSh " + goods.getGoodsprice());
        goodsdesc.setText(goods.getGoodsdesc());
        goodsusername.setText(goods.getGoodsusername());
        goodsphone.setText(getResources().getString(R.string.phone_no_) + goods.getGoodsphone());

        Glide.with(this).load(AppConstants.BASE_URL + goods.getUser().getUserPortrait())
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
                new PreviewDialog(GoodsDetailsActivity.this, goods.getImageUrlList(), position).show();
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
                new CallPhoneDialog(this, goods.getGoodsphone()).show();
                break;
        }
    }
}
