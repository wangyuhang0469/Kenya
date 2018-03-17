package com.example.administrator.kenya.ui.city;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityHomeFragment extends BaseFragment {


    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_home, container, false);
        ButterKnife.bind(this, view);

        title.setText("同城");
        back.setVisibility(View.GONE);

        List<String> imageList = new ArrayList<>();
        imageList.add("http://img011.hc360.cn/k1/M0B/24/D0/wKhQwFdjdHSER6NQAAAAADZsFuo401.jpg");
        imageList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2351109249,2100272265&fm=27&gp=0.jpg");
        imageList.add("http://pic.90sjimg.com/design/00/14/38/18/5581393a30663.jpg");

        initBanner(imageList);

        return view;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
