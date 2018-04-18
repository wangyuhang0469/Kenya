package com.example.administrator.kenya.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.GlideCircleTransform;
import com.example.administrator.kenya.classes.Job;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResumeDetilActivity extends Activity {

    @Bind(R.id.resume_detail_name)
    TextView resumeDetailName;
    @Bind(R.id.resume_detail_price)
    TextView resumeDetailPrice;
    @Bind(R.id.resume_detial_jobwant)
    TextView resumeDetialJobwant;
    @Bind(R.id.resume_detail_experience)
    TextView resumeDetailExperience;
    @Bind(R.id.resume_detail_desc)
    TextView resumeDetailDesc;
    @Bind(R.id.resume_detail_phone)
    TextView resumeDetailPhone;
    @Bind(R.id.resume_detil_img)
    ImageView resumeDetilImg;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_detil);
        ButterKnife.bind(this);

        job = (Job) getIntent().getExtras().getSerializable("job");
        resumeDetailName.setText(job.getName());
        resumeDetailPrice.setText("期望薪资:" + job.getHopesalary());
        resumeDetialJobwant.setText(job.getJobwant());
        resumeDetailExperience.setText(job.getJointime());
        resumeDetailDesc.setText(job.getPersondesc());
        resumeDetailPhone.setText(job.getPhone());
        Glide.with(this).load("http://192.168.1.107:8080" + job.getHeadimg()).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(this)).into(resumeDetilImg);
    }

    @OnClick({R.id.back, R.id.resume_detail_job_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.resume_detail_job_release:
                break;
        }
    }
}
