package com.example.administrator.kenya.ui.city.job;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
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
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        ButterKnife.bind(this);
        initdata();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new TabFragment1().newInstance("Page1"), getResources().getString(R.string.job_information));
        adapter.addFragment(new TabFragment2().newInstance("Page2"), getResources().getString(R.string.recruitment_information));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick({R.id.back, R.id.job_title_bar, R.id.city_home_job_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.job_title_bar:
                Intent intent = null;
                if (type == 0) {
                    intent = new Intent(this, JobSearchActivity.class);
                } else if (type == 1) {
                    intent = new Intent(this, CompanySeachActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.city_home_job_release:
                Intent intent1 = new Intent(this, ResumeinfoActivity.class);
                startActivity(intent1);
                break;
        }
    }

    public void initdata() {
        if (pager != null) {
            setupViewPager(pager);
        }
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                type = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
