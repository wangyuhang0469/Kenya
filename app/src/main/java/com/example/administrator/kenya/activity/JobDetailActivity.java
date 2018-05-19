package com.example.administrator.kenya.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Company;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JobDetailActivity extends BaseActivity {
    @Bind(R.id.job_detail_pay)
    TextView jobDetailPay;
    @Bind(R.id.job_detail_desc)
    TextView jobDetailDesc;
    @Bind(R.id.job_detail_person_name)
    TextView jobDetailPersonName;
    @Bind(R.id.job_detail_person_phone)
    TextView jobDetailPersonPhone;
    @Bind(R.id.job_detail_jobtype)
    TextView jobDetailJobtype;
    @Bind(R.id.job_detail_how_long)
    TextView jobDetailHowLong;
    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        ButterKnife.bind(this);
        company = (Company) getIntent().getExtras().getSerializable("company");
        jobDetailJobtype.setText(company.getCompanystation());
        if (company.getCompanystationsalary().equals("-1")) {
            jobDetailPay.setText(getResources().getString(R.string.monthly_salary) + ":" + getResources().getString(R.string.negotiable));
        } else {
            jobDetailPay.setText(getResources().getString(R.string.monthly_salary) + getResources().getString(R.string.ksh) + company.getCompanystationsalary() + "/" + getResources().getString(R.string.month));
        }
        jobDetailDesc.setText(company.getCompanystationdesc());
        jobDetailPersonName.setText(company.getCompanyname());
        jobDetailPersonPhone.setText(getString(R.string.phone_no_) + company.getCompanyphone());
        jobDetailHowLong.setText(company.getCompanyimg5());
    }

    @OnClick({R.id.back, R.id.job_detail_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.job_detail_release:
                new CallPhoneDialog(this, company.getCompanyphone()).show();
                break;
        }
    }
}
