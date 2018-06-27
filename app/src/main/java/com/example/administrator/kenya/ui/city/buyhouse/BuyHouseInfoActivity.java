package com.example.administrator.kenya.ui.city.buyhouse;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.BuyHouse;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.model.image_selector.MultiImageSelectorActivity;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.view.MyRadioGroup;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.administrator.kenya.R.id.radioButton1;

public class BuyHouseInfoActivity extends BaseActivity {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.buy_house_info_title)
    EditText buyHouseInfoTitle;
    @Bind(R.id.buy_house_info_types)
    TextView buyHouseInfoTypes;
    @Bind(R.id.buy_house_info_square)
    EditText buyHouseInfoSquare;
    @Bind(R.id.buy_house_info_address)
    TextView buyHouseInfoAddress;
    @Bind(R.id.buy_house_info_price)
    EditText buyHouseInfoPrice;
    @Bind(R.id.buy_house_info_person)
    EditText buyHouseInfoPerson;
    @Bind(R.id.buy_house_info_phone)
    EditText buyHouseInfoPhone;
    @Bind(R.id.buy_house_info_desc)
    EditText buyHouseInfoDesc;
    @Bind(R.id.buy_house_info_img5)
    ImageView buyHouseInfoImg5;
    @Bind({R.id.buy_house_info_img1, R.id.buy_house_info_img2, R.id.buy_house_info_img3, R.id.buy_house_info_img4, R.id.buy_house_info_img5})
    List<ImageView> imageViews;
    @Bind(R.id.buy_house_inter)
    ImageView buyHouseInter;
    @Bind(R.id.buy_house_iv_type)
    ImageView buyHouseIvType;
    @Bind(R.id.ll_buy_house_info)
    AutoRelativeLayout llBuyHouseInfo;

    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<File> compressFile = new ArrayList<>();
    String housetype;
    private PopupWindow popupWindow;
    String province;
    String city;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_house_info);
        ButterKnife.bind(this);
        title.setText(getResources().getText(R.string.Housing_release));
        housetype = (String) getIntent().getExtras().get("housetype");
        buyHouseInfoPerson.setText(User.getInstance().getUserName());
        buyHouseInfoPhone.setText(User.getInstance().getUserPhonenumber());
        initPopupWindow();
    }

    @OnClick({R.id.back, R.id.buy_house_info_release, R.id.ll_buy_house_address, R.id.ll_buy_house_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.buy_house_info_release:
                if (buyHouseInfoTitle.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_your_title));
                } else if (buyHouseInfoTypes.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.please_choose_the_type_of_hoose_e_g__one_bedroom));
                } else if (buyHouseInfoSquare.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_the_size_of_house_in_square_metres));
                } else if (buyHouseInfoAddress.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_the_exact_location));
                } else if (buyHouseInfoPrice.getText().length() == 0) {
                    toast("" + getResources().getText(R.string.Please_enter_the_total_sale_price));
                } else if (buyHouseInfoPerson.getText().length() == 0) {
                    toast("" + getResources().getText(R.string.enter_personal_contacts));
                } else if (buyHouseInfoPhone.getText().length() == 0) {
                    toast("" + getResources().getText(R.string.enter_phone_number));
                } else if (buyHouseInfoDesc.getText().length() == 0) {
                    toast("" + getResources().getText(R.string.Please_enter_the_description_information_of_the_house));
                } else if (mResults == null || mResults.size() <= 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.please_select_at_least_one_picture));
                } else {
                    //弹出框
                    final LoadingDialog loadingDialog = new LoadingDialog(BuyHouseInfoActivity.this);
                    loadingDialog.show();
                    for (int i = 0; i < mResults.size(); i++) {
                        Luban.with(this)
                                .load(mResults.get(i))
                                .ignoreBy(150)
                                .setTargetDir(getExternalCacheDir().toString())
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        compressFile.add(file);
                                        if (compressFile.size() == mResults.size()) {
                                            send(loadingDialog);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                        toast(getResources().getString(R.string.post_fail));
                                    }
                                }).launch();
                    }
                }
                break;
            case R.id.ll_buy_house_address:
                startActivityForResult(new Intent(BuyHouseInfoActivity.this, ProvinceCityDetailsActivity.class), 1);
                break;
            case R.id.ll_buy_house_info:
                llBuyHouseInfo.setClickable(false);
                popupWindow.showAsDropDown(view);
                break;
        }
    }

    @OnClick({R.id.buy_house_info_img1, R.id.buy_house_info_img2, R.id.buy_house_info_img3, R.id.buy_house_info_img4, R.id.buy_house_info_img5})
    public void onViewClicked2(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        Intent intent = new Intent(BuyHouseInfoActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 5);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
        // 默认选择
        if (mResults != null && mResults.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mResults);
        }
        startActivityForResult(intent, 2);
    }

    private void send(final LoadingDialog loadingDialog) {
        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        for (int i = 0; i < compressFile.size(); i++) {
            postFormBuilder.addFile("files", compressFile.get(i).getName(), compressFile.get(i));
        }
        postFormBuilder.url(AppConstants.BASE_URL + "/kenya/House/saveHouse")
                .addParams("housename", buyHouseInfoTitle.getText().toString())
                .addParams("housedesc", buyHouseInfoDesc.getText().toString())
                .addParams("houseprice", buyHouseInfoPrice.getText().toString())
                .addParams("housephone", buyHouseInfoPhone.getText().toString())
                .addParams("houseUser", buyHouseInfoPerson.getText().toString())
                .addParams("housesquare", buyHouseInfoSquare.getText().toString())
                .addParams("househome", buyHouseInfoTypes.getText().toString())
                .addParams("houseaddress", buyHouseInfoAddress.getText().toString())
                .addParams("cityprovince", province)
                .addParams("cityname", city)
                .addParams("housetype", housetype)
                .addParams("userid", User.getInstance().getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        loadingDialog.dismiss();
                        toast(getResources().getString(R.string.post_fail));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                BuyHouse buyhouse = JSON.parseObject(jsonObject.getString("result"), BuyHouse.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("buyhouse", buyhouse);
                                toast(getResources().getString(R.string.post_success));
                                startActivity(BuyHouseDetailActivity.class, bundle);
                                finish();
                            } else {
                                toast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    /**
     * 发布合并前
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra("select_result");
                assert mResults != null;
                // show results in textview
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for (String result : mResults) {
                    sb.append(result).append("\n");
                }
                for (int i = 0; i < 5; i++) {
                    if (i < mResults.size()) {
                        Glide.with(this)
                                .load(mResults.get(i))
                                .into(imageViews.get(i));
                    } else {
                        Glide.with(this)
                                .load(R.drawable.city_used_camera)
                                .into(imageViews.get(i));
                    }
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle result = data.getExtras();
                province = result.get("province").toString();
                city = result.get("city").toString();
                content = result.get("content").toString();
                buyHouseInfoAddress.setText(province + city + content);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                startCamera();
            }
        }
    }


    private void initPopupWindow() {
        final View popContentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_buy_house_type, null);
        popupWindow = new PopupWindow(popContentView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llBuyHouseInfo.setClickable(true);
                    }
                }, 100);
            }
        });
        MyRadioGroup myRadioGroup = (MyRadioGroup) popContentView.findViewById(R.id.myRadioGroup);
        myRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case radioButton1:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Please_select_your_account_information));
                        break;
                    case R.id.radioButton2:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Room_1));
                        break;
                    case R.id.radioButton3:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Room_1_Hall));
                        break;
                    case R.id.radioButton4:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Room_1_Hall_2));
                        break;
                    case R.id.radioButton5:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Room_2_Hall_2));
                        break;
                    case R.id.radioButton6:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Room_1_Hall_3));
                        break;
                    case R.id.radioButton7:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Room_2_Hall_3));
                        break;
                    case R.id.radioButton8:
                        buyHouseInfoTypes.setText(getResources().getText(R.string.Room_1_Hall_4));
                        break;
                }
                popupWindow.dismiss();
            }
        });
    }
}
