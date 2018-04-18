package com.example.administrator.kenya.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Company;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AdvertyinfoActivity extends BaseActivity {

    int type = 0;
    boolean img;
    @Bind(R.id.adverty_info_choose)
    ImageView advertyInfoChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertyinfo);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.adverty_info_detail, R.id.adverty_info_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.adverty_info_detail:
                PostFormBuilder postFormBuilder = OkHttpUtils.post();
                postFormBuilder.url("http://192.168.1.107:8080/kenya/recruit/publish")
                        .addParams("companyname", "狮顺联合")
                        .addParams("companystationdesc", "主要销售汽车")
                        .addParams("companyphone", "15931127537")
                        .addParams("companyaddress", "河北省")
                        .addParams("companystation", "销售代表")
                        .addParams("companystationsalary", "300")
                        .addParams("userid", "2")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                toast("加载失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("kang", "1111111111111111" + response);
                                log(response);
                                toast("加载成功");
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("code").equals("000")) {
                                        Company company = JSON.parseObject(jsonObject.getString("data"), Company.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("company", company);
                                        toast("发布成功");
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
                break;
            case R.id.adverty_info_choose:
                type++;
                if (type % 2 == 0) {
                    advertyInfoChoose.setImageResource(R.mipmap.mianyi);
                    img = true;
                } else {
                    advertyInfoChoose.setImageResource(R.mipmap.mianyi2);
                    img = false;
                }
                break;
        }
    }
}
