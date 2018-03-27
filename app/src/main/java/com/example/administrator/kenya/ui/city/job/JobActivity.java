package com.example.administrator.kenya.ui.city.job;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.activity.ResumeinfoActivity;
import com.example.administrator.kenya.adapter.MyViewPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JobActivity extends FragmentActivity {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.city_home_job_release)
    TextView cityHomeJobRelease;
    private MyViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        ButterKnife.bind(this);
        if (pager != null) {
            setupViewPager(pager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new TabFragment1().newInstance("Page1"), "我要求职");
        adapter.addFragment(new TabFragment2().newInstance("Page2"), "我要招聘");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.city_home_job_release)
    public void onViewClicked() {
        Intent intent = new Intent(this, ResumeinfoActivity.class);
        startActivity(intent);
    }
}
