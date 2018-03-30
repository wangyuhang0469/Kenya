package com.example.administrator.kenya.ui.city.house;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.HouseAdapter;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.House;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HouseActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<House> houseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        ButterKnife.bind(this);
        houseList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            House house = new House();
            house.setTitle("出租，恒大御景半岛，精装两室");
            houseList.add(house);
        }
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        HouseAdapter adapter = new HouseAdapter(this, houseList);
        recyclerView.setAdapter(adapter);
    }


    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                startActivity(HouseInfoActivity.class, null);
                break;
        }
    }
}
