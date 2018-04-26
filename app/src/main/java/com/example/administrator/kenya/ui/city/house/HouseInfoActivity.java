package com.example.administrator.kenya.ui.city.house;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.model.image_selector.MultiImageSelectorActivity;
import com.example.administrator.kenya.ui.main.LoadingDialog;
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

public class HouseInfoActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind({R.id.house_info_img1, R.id.house_info_img2, R.id.house_info_img3, R.id.house_info_img4, R.id.house_info_img5})
    List<ImageView> imageViews;
    @Bind(R.id.house_info_desc)
    EditText houseInfoDesc;
    @Bind(R.id.house_info_img1)
    ImageView houseInfoImg1;
    @Bind(R.id.house_name)
    EditText houseName;
    @Bind(R.id.house_price)
    EditText housePrice;
    @Bind(R.id.house_penson)
    EditText housePenson;
    @Bind(R.id.house_phone)
    EditText housePhone;
    @Bind(R.id.house_square)
    EditText houseSquare;
    @Bind(R.id.house_address)
    EditText houseAddress;
    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<File> compressFile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_info);
        ButterKnife.bind(this);
        title.setText("发布");

        housePenson.setText(User.getInstance().getUserName());
        housePhone.setText(User.getInstance().getUserPhonenumber());
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

    @OnClick({R.id.house_info_img1, R.id.house_info_img2, R.id.house_info_img3, R.id.house_info_img4, R.id.house_info_img5})
    public void onViewClicked2(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        Intent intent = new Intent(HouseInfoActivity.this, MultiImageSelectorActivity.class);
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

    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                if (houseName.getText().length() == 0) {
                    toast("请输入标题");
                } else if (houseSquare.getText().length() == 0) {
                    toast("请输入户型信息");
                } else if (houseAddress.getText().length() == 0) {
                    toast("请输入房子位置");
                } else if (houseInfoDesc.getText().length() == 0) {
                    toast("请输入详情");
                } else if (housePrice.getText().length() == 0) {
                    toast("请输入租房的价格");
                } else if (houseName.getText().length() == 0) {
                    toast("请输入联系人");
                } else if (housePhone.getText().length() == 0) {
                    toast("请输入联系电话");
                } else if (mResults == null || mResults.size() <= 0) {
                    toast("请选择图片");
                } else {
                    //弹出框
                    final LoadingDialog loadingDialog = new LoadingDialog(HouseInfoActivity.this);
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
                                    }
                                }).launch();
                    }
                }
                break;
        }
    }

    private void send(final LoadingDialog loadingDialog) {
        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        for (int i = 0; i < mResults.size(); i++) {
            File file = new File(mResults.get(i));
            postFormBuilder.addFile("files", file.getName(), file);
        }

        postFormBuilder.url(AppConstants.BASE_URL + "/kenya/Lease/inserLease")
                .addParams("leasename", houseName.getText().toString())
                .addParams("leasedesc", houseInfoDesc.getText().toString())
                .addParams("leaseprice", housePrice.getText().toString())
                .addParams("leasephone", housePhone.getText().toString())
                .addParams("leasesquare", houseSquare.getText().toString())
                .addParams("leaseaddress", houseAddress.getText().toString())
                .addParams("userid", User.getInstance().getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        loadingDialog.dismiss();
                        toast("发布失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                House house = JSON.parseObject(jsonObject.getString("result"), House.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("house", house);
                                toast("发布成功");
                                startActivity(HouseDetailActivity.class, bundle);
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
}
