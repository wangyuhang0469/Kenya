package com.example.administrator.kenya.ui.news;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.MyFragmentPagerAdapter;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.view.ScaleTransitionPagerTitleView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment {


    @Bind(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.poster)
    TextView poster;
    @Bind(R.id.linearLayout)
    AutoLinearLayout linearLayout;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        titleList.add("All");
        titleList.add("National");
        titleList.add("Opinion");
        titleList.add("Letters");
        titleList.add("County");
        titleList.add("Classified");
        titleList.add("Transition");
        titleList.add("Sports");
        addFragment("All");
        addFragment("National");
        addFragment("Opinion");
        addFragment("Letters");
        addFragment("County");
        fragmentList.add(new ClassifiedFragment());
        fragmentList.add(new TransitionFragment());
        addFragment("Sports");


        initMagicIndicator();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            linearLayout.setPadding(0, StatusUtil.getStatusBarHeight(this.getActivity()), 0, 0);
        }


        // 使用FragmentPagerAdapter作为中介将fragmentList、titleList 绑定给 viewPager
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(myFragmentPagerAdapter);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 5 || position == 6) {
                    poster.setVisibility(View.VISIBLE);
                } else {
                    poster.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }


    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titleList.get(index));
                simplePagerTitleView.setTextSize(17);
                simplePagerTitleView.setNormalColor(Color.GRAY);
//                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getContext().getResources().getColor(R.color.textgreen1));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
//                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));
                indicator.setColors(Color.parseColor("#029f8c"), Color.parseColor("#05B2CC")
                        , Color.parseColor("#DBFE1C"), Color.parseColor("#3CD173"),
                        Color.parseColor("#44A6CD"), Color.parseColor("#BFFEF9")
                        , Color.parseColor("#555555"), Color.parseColor("#4D69CC"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void addFragment(String type) {
        TypeNewsFragment typeNewsFragment = new TypeNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        typeNewsFragment.setArguments(bundle);
        fragmentList.add(typeNewsFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.poster)
    public void onViewClicked() {
        if (viewPager.getCurrentItem() == 5) {
            startActivity(ClassifiedReleaseActivity.class, null);
        } else if (viewPager.getCurrentItem() == 6) {
            startActivity(TransitionReleaseActivity.class, null);
        }

    }
}
