package com.example.administrator.kenya.ui.city.findmoney;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.MyViewPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindMoneyActivity extends FragmentActivity {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.find_money_back)
    ImageView findMoneyBack;
    private MyViewPagerAdapter adapter;
    String findmoneyvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_money);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        findmoneyvalue = bundle.getString("findmoney");
        if (pager != null) {
            setupViewPager(pager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        if (findmoneyvalue.equals("A")) {
            adapter.addFragment(new FindMoneyFragment1().newInstance("Page1"), "项目找资金");
        } else {
            adapter.addFragment(new FindMoneyFragment2().newInstance("Page2"), "资金找项目");
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.find_money_back)
    public void onViewClicked() {
        finish();
    }
}
