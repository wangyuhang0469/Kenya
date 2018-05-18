package com.example.administrator.kenya.ui.city.used;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.classes.Issuer;
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

public class GoodsReleaseActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;

    @Bind({R.id.images, R.id.image1, R.id.image2, R.id.image3, R.id.image4})
    List<ImageView> imageViews;
    @Bind(R.id.goodsname)
    EditText goodsname;
    @Bind(R.id.goodsdesc)
    EditText goodsdesc;
    @Bind(R.id.goodsprice)
    EditText goodsprice;
    @Bind(R.id.goodsusername)
    EditText goodsusername;
    @Bind(R.id.goodsphone)
    EditText goodsphone;

    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<File> compressFile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_release);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.post));

        goodsusername.setText(User.getInstance().getUserName());
        goodsphone.setText(User.getInstance().getUserPhonenumber());

        goodsname.requestFocus();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                if (goodsphone.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_your_title));
                } else if (goodsdesc.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.describe_in_details_about_your_product));
                } else if (goodsprice.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_your_selling_price));
                } else if (goodsusername.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_personal_contacts));
                } else if (goodsphone.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_phone_number));
                } else if (mResults == null || mResults.size() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.please_select_at_least_one_picture));
                } else {
                    final LoadingDialog loadingDialog = new LoadingDialog(GoodsReleaseActivity.this);
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

    @OnClick({R.id.images, R.id.image1, R.id.image2, R.id.image3, R.id.image4})
    public void onViewClicked2(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        Intent intent = new Intent(GoodsReleaseActivity.this, MultiImageSelectorActivity.class);
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
            postFormBuilder.addFile("logoFil", compressFile.get(i).getName(), compressFile.get(i));
        }
        postFormBuilder.url(AppConstants.BASE_URL + "/kenya/saveSurvey")
                .addParams("userId", User.getInstance().getUserId())
                .addParams("goodsName", goodsname.getText().toString())
                .addParams("goodsDesc", goodsdesc.getText().toString())
                .addParams("goodsPrice", goodsprice.getText().toString())
                .addParams("goodUserName", goodsusername.getText().toString())
                .addParams("goodsPhone", goodsphone.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        toast(getResources().getString(R.string.post_fail));
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            log(response);
                            if (jsonObject.getString("code").equals("000")) {
                                Goods goods = JSON.parseObject(jsonObject.getString("data1"), Goods.class);
                                Issuer issuer = JSON.parseObject(jsonObject.getString("data"), Issuer.class);
                                goods.setUser(issuer);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("goods", goods);
                                toast(getResources().getString(R.string.post_success));
                                startActivity(GoodsDetailsActivity.class, bundle);
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
