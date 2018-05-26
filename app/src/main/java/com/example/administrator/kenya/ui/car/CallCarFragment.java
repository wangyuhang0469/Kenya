package com.example.administrator.kenya.ui.car;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.MyViewPagerAdapter;
import com.example.administrator.kenya.ui.city.job.TabFragment1;
import com.example.administrator.kenya.ui.city.job.TabFragment2;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallCarFragment extends Fragment {

    @Bind(R.id.call_car_title)
    TextView callCarTitle;
    @Bind(R.id.call_car_tab_layout)
    TabLayout callCarTabLayout;
    @Bind(R.id.call_car_pager)
    ViewPager callCarPager;
    private MyViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_car, container, false);
        ButterKnife.bind(this, view);
        initdata();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager(), this.getContext());
        adapter.addFragment(new CarTabFragment1().newInstance("Page1"), "打车");
        adapter.addFragment(new CarTabFragment2().newInstance("Page2"), "拉货");
        viewPager.setAdapter(adapter);
        callCarTabLayout.setupWithViewPager(viewPager);
    }

    public void initdata() {
        if (callCarPager != null) {
            setupViewPager(callCarPager);
        }

    }
}
