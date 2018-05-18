package com.example.administrator.kenya.ui.city.husbandry;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.ImageAdapter;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Husbandry;
import com.example.administrator.kenya.classes.LifeServices;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.view.ExStaggeredGridLayoutManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HusbandryDetailsActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.view)
    View view;
    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.call)
    TextView call;
    @Bind(R.id.framname)
    TextView framname;
    @Bind(R.id.framtype)
    TextView framtype;
    @Bind(R.id.framdesc)
    TextView framdesc;
    @Bind(R.id.framuser)
    TextView framuser;
    @Bind(R.id.framphone)
    TextView framphone;

    private Husbandry husbandry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husbandry_details);
        ButterKnife.bind(this);

        title.setText(getResources().getString(R.string.details));
        husbandry = (Husbandry) getIntent().getExtras().getSerializable("Husbandry");
        framname.setText(husbandry.getFramname());
        framtype.setText( getResources().getString(R.string.service_details) +"  " + husbandry.getFramtype());
        framdesc.setText(husbandry.getFramdesc());
        framuser.setText(husbandry.getFramuser());
        framphone.setText(getResources().getString(R.string.phone_no_)+ husbandry.getFramphone());


        ExStaggeredGridLayoutManager exStaggeredGridLayoutManager = new ExStaggeredGridLayoutManager(2, ExStaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(exStaggeredGridLayoutManager);
        ImageAdapter imageAdapter = new ImageAdapter(this, husbandry.getImageUrlList());
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        Glide.with(this).load(AppConstants.BASE_URL + husbandry.getUser().getUserPortrait())
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);

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
                new CallPhoneDialog(this, husbandry.getFramphone()).show();
                break;
        }
    }


}
