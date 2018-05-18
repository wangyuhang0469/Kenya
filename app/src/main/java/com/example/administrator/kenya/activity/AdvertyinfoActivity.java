package com.example.administrator.kenya.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Company;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AdvertyinfoActivity extends BaseActivity {


    @Bind(R.id.adverty_info_choose)
    ImageView advertyInfoChoose;
    @Bind(R.id.adverty_info_work_name)
    EditText advertyInfoWorkName;
    @Bind(R.id.adverty_info_work_money)
    EditText advertyInfoWorkMoney;
    @Bind(R.id.adverty_info_work_quiret)
    EditText advertyInfoWorkQuiret;
    @Bind(R.id.adverty_info_company_name)
    EditText advertyInfoCompanyName;
    @Bind(R.id.adverty_info_company_phone)
    EditText advertyInfoCompanyPhone;
    @Bind(R.id.spinner)
    Spinner spinner;
    private String price;
    private int type = 0;
    boolean img = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertyinfo);
        ButterKnife.bind(this);
        List<String> types = new ArrayList<>();
        types.add(getResources().getString(R.string.Please_select_a_type));
        types.add(getResources().getString(R.string.unlimited));
        types.add(getResources().getString(R.string.recent_graduate));
        types.add(getResources().getString(R.string.one_year_below));
        types.add(getResources().getString(R.string.one_to_three_years));
        types.add(getResources().getString(R.string.three_to_five_years));
        types.add(getResources().getString(R.string.five_to_ten_years));
        types.add(getResources().getString(R.string.ten_years_and_above));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_simpleadverty, R.id.spinner_tv, types);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(adapter);
    }

    @OnClick({R.id.back, R.id.adverty_info_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.adverty_info_detail:
                if (img == true) {
                    price = advertyInfoWorkMoney.getText().toString();
                } else if (img == false) {
                    price = "-1";
                }
                if (advertyInfoWorkName.getText().length() == 0) {
                    toast(getString(R.string.please) + getString(R.string.enter_the_job));
                } else if (price.length() == 0) {
                    toast(getString(R.string.please) + getString(R.string.enter_monthly_salary));
                } else if (spinner.getSelectedItem().toString().equals(getString(R.string.Please_select_a_type))) {
                    toast(getString(R.string.Please_select_a_type));
                } else if (advertyInfoCompanyName.getText().length() == 0) {
                    toast(getString(R.string.please) + getString(R.string.please_enter_the_company_name));
                } else if (advertyInfoCompanyPhone.getText().length() == 0) {
                    toast(getString(R.string.please) + getString(R.string.enter_phone_no_));
                } else if (advertyInfoWorkQuiret.getText().length() == 0) {
                    toast(getString(R.string.please) + getString(R.string.please_enter_your_job_description));
                } else {
                    PostFormBuilder postFormBuilder = OkHttpUtils.post();
                    postFormBuilder.url(AppConstants.BASE_URL + "/kenya/recruit/publish")
                            .addParams("companyname", advertyInfoCompanyName.getText().toString())
                            .addParams("companystationdesc", advertyInfoWorkQuiret.getText().toString())
                            .addParams("companyphone", advertyInfoCompanyPhone.getText().toString())
                            .addParams("companyimg5", spinner.getSelectedItem().toString())//添加的是工作年限信息
                            .addParams("companystation", advertyInfoWorkName.getText().toString())
                            .addParams("companystationsalary", price)
                            .addParams("userid", User.getInstance().getUserId())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    toast(getString(R.string.post_fail));
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("code").equals("000")) {
                                            Company company = JSON.parseObject(jsonObject.getString("data"), Company.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("company", company);
                                            toast(getString(R.string.post_success));
                                            startActivity(JobDetailActivity.class, bundle);
                                            finish();
                                        } else {
                                            toast(jsonObject.getString("message"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                break;

        }
    }

    @OnClick({R.id.adverty_info_choose, R.id.resume_info_mianyi})
    public void onViewClicked2(View view) {
        type++;
        if (type % 2 == 0) {
            advertyInfoChoose.setImageResource(R.mipmap.mianyi);
            advertyInfoWorkMoney.setText("");
            advertyInfoWorkMoney.setFocusableInTouchMode(true);
            advertyInfoWorkMoney.setFocusable(true);
            advertyInfoWorkMoney.requestFocus();
            img = true;
        } else {
            advertyInfoChoose.setImageResource(R.mipmap.mianyi2);
            advertyInfoWorkMoney.setText(getString(R.string.negotiable));
            advertyInfoWorkMoney.setFocusable(false);
            advertyInfoWorkMoney.setFocusableInTouchMode(false);
            img = false;
        }

    }
}
