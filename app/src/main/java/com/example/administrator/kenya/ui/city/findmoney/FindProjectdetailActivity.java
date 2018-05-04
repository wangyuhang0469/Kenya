package com.example.administrator.kenya.ui.city.findmoney;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Project2;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.ui.main.PreviewDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindProjectdetailActivity extends BaseActivity {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.porject_user)
    TextView porjectUser;
    @Bind(R.id.project_phone)
    TextView projectPhone;
    @Bind(R.id.project_detail_name)
    TextView projectDetailName;
    @Bind(R.id.project_detail_price)
    TextView projectDetailPrice;
    @Bind(R.id.project_detail_type)
    TextView projectDetailType;
    @Bind(R.id.project_detail_desc)
    TextView projectDetailDesc;
    @Bind(R.id.banner)
    Banner banner;
    private Project2 project2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_projectdetail);
        ButterKnife.bind(this);
        title.setText("项目详情");
        project2 = (Project2) getIntent().getExtras().getSerializable("project2");
        initBanner(project2.getProjectImageUrlList());
        projectDetailName.setText(project2.getProjectname());
        projectDetailPrice.setText("投资额度:" + project2.getProjectprice());
        projectDetailDesc.setText(project2.getProjectdesc());
        porjectUser.setText(project2.getProjectuser());
        projectPhone.setText(project2.getProjectphone());
    }
    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                new PreviewDialog(FindProjectdetailActivity.this, project2.getProjectImageUrlList(), position).show();
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
                new CallPhoneDialog(this,project2.getProjectphone()).show();
                break;
        }
    }
}
