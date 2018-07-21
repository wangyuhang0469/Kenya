package com.example.administrator.kenya.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.CityProvince;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.buyhouse.GirdDropDownAdapter;
import com.example.administrator.kenya.ui.city.used.UsedActivity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class SelectAddressActivity extends BaseActivity {

    @Bind(R.id.location_address)
    TextView locationAddress;
    @Bind(R.id.choose_other)
    TextView chooseOther;
    @Bind(R.id.province_view)
    AutoLinearLayout provinceView;
    @Bind(R.id.city_view)
    AutoLinearLayout cityView;


    private List<CityProvince> provinceList = new ArrayList<>();
    private List<CityProvince> cityList = new ArrayList<>();
    private String provinceStr;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);

        initProvince();

    }


    private void initProvince() {
        OkHttpUtils.get().url(AppConstants.BASE_URL + "/kenya/city/findByCountCityprovince").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toast(getString(R.string.load_fail));
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("000")) {
                        response = jsonObject.getString("result");
                        provinceList = JSON.parseArray(response, CityProvince.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0 ; i < provinceList.size() ; i++){
                    CityProvince province = provinceList.get(i);
                    addProvince(province , i);
                }

            }
        });
    }


    private void loadCity() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        OkHttpUtils.post().
                url(AppConstants.BASE_URL + "/kenya/city/findByCityprovince" )
                .addParams("cityprovince" , provinceStr )
                .build()
                .execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toast(getString(R.string.load_fail));
                loadingDialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("000")) {
                        response = jsonObject.getString("result");
                        cityList = JSON.parseArray(response, CityProvince.class);
                        for (int i = 0 ; i < cityList.size() ; i++){
                            CityProvince city = cityList.get(i);
                            addCity(city , i);
                        }
                        provinceView.setVisibility(View.GONE);
                        cityView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingDialog.dismiss();
            }
        });
    }


    private void addProvince(final CityProvince province , final int position){
        View itemView = View.inflate(this , R.layout.item_choose_province_city , null);
        TextView tv_province = itemView.findViewById(R.id.text);
        tv_province.setText(province.getCityprovince());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provinceStr = provinceList.get(position).getCityprovince();
                showCitys();
            }
        });
        provinceView.addView(itemView);
    }


    private void addCity(final CityProvince province , final int position){
        View itemView = View.inflate(this , R.layout.item_choose_province_city , null);
        TextView tv_city = itemView.findViewById(R.id.text);
        tv_city.setText(province.getCityname());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("province"  ,  provinceStr);
                intent.putExtra("city"  , province.getCityname());
                setResult(200 , intent);
                finish();
            }
        });
        cityView.addView(itemView);
    }


        private void showCitys(){
            chooseOther.setText("当前选择的城市为:" + provinceStr);
            loadCity();
    }
}
