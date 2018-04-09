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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_release);
        ButterKnife.bind(this);
        title.setText("发布");

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
                goodsdesc.setText(mResults.toString());
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
                final LoadingDialog loadingDialog = new LoadingDialog(GoodsReleaseActivity.this);
                loadingDialog.show();

                PostFormBuilder postFormBuilder = OkHttpUtils.post();
                for (int i = 0; i < mResults.size(); i++) {
                    File file = new File(mResults.get(i));
                    postFormBuilder.addFile("logoFil", file.getName(), file);
                }
                postFormBuilder.url(AppConstants.BASE_URL + "/kenYa-test/saveSurvey")
                        .addParams("userId", "1")
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
                                loadingDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                Log.d("kang", "11111111" + response);
                                log(response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("code").equals("000")) {
                                        Log.d("kang", "11111111" + jsonObject.getString("code"));
                                        Goods goods = JSON.parseObject(jsonObject.getString("data"), Goods.class);
                                        Log.d("kang", "222222222" + jsonObject.getString("data"));
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("goods", goods);
                                        toast("发布成功");
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

////                File file = new File(mResults.get(0));
//               PostFormBuilder postFormBuilder = OkHttpUtils.post()
//                       .addFile("leaseIms" ,"tupian.png",new File(mResults.get(0)));
//                for (int i = 1 ; i < mResults.size() ; i ++){
////                    file = new File(mResults.get(i));
//                    postFormBuilder.addFile("leaseIm"+i ,"tupian.png",new File(mResults.get(i)));
//                }
//                postFormBuilder.url("http://192.168.1.108:8080/kenya/Lease/inserLease")
//                        .addParams("leasename", goodsname.getText().toString())
//                        .addParams("leasedesc",goodsdesc.getText().toString())
//                        .addParams("leaseprice" , goodsprice.getText().toString())
//                        .addParams("leasephone" , goodsphone.getText().toString())
//
//                        .addParams("leasesquare" , "面积")
//                        .addParams("leaseaddress" , "地址")
//                        .addParams("leasehome" , "户型")
//                        .addParams("userid" , "1")
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//                                e.printStackTrace();
//                                loadingDialog.dismiss();
//                                toast("加载失败");
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                log(response);
//
////                                try {
////                                    JSONObject jsonObject = new JSONObject(response);
////                                    if (jsonObject.getString("code").equals("000")){
////                                        Goods goods = JSON.parseObject(jsonObject.getString("result"),Goods.class);
////                                        Bundle bundle = new Bundle();
////                                        bundle.putSerializable("goods",goods);
////                                        toast("发布成功");
////                                        startActivity(GoodsDetailsActivity.class,bundle);
////                                        finish();
////                                    }else {
////                                        toast(jsonObject.getString("message"));
////                                    }
////                                } catch (JSONException e) {
////                                    e.printStackTrace();
////                                }

//                            }
//                        });
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

}
