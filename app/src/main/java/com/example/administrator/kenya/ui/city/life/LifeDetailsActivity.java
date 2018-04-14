package com.example.administrator.kenya.ui.city.life;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.ImageAdapter;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.LifeServices;
import com.example.administrator.kenya.view.ExStaggeredGridLayoutManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LifeDetailsActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.livename)
    TextView livename;
    @Bind(R.id.livetype)
    TextView livetype;
    @Bind(R.id.livedesc)
    TextView livedesc;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.liveusername)
    TextView liveusername;
    @Bind(R.id.livephone)
    TextView livephone;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.call)
    TextView call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_details);
        ButterKnife.bind(this);

        title.setText("详情");
        LifeServices lifeServices = (LifeServices) getIntent().getExtras().getSerializable("lifeServices");
        livename.setText(lifeServices.getLivename());
        livetype.setText("服务类型  " + lifeServices.getLivetype());
        livedesc.setText(lifeServices.getLivedesc());
        liveusername.setText(lifeServices.getLiveuser());
        livephone.setText(lifeServices.getLivephone());


        log(lifeServices.getImageUrlList().toString());
        ExStaggeredGridLayoutManager exStaggeredGridLayoutManager = new ExStaggeredGridLayoutManager(2, ExStaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(exStaggeredGridLayoutManager);
        ImageAdapter imageAdapter = new ImageAdapter(this, lifeServices.getImageUrlList());
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @OnClick({R.id.back, R.id.call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.call:
                break;
        }
    }


}
