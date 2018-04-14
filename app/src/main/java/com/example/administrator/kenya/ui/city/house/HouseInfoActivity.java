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

public class HouseInfoActivity extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener {

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
    private PopupWindow popupWindow;

    private ArrayList<String> mResults = new ArrayList<>();
    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    public Uri imageUriFromCamera;
    public Uri cropImageUri;
    public final int GET_IMAGE_BY_CAMERA_U = 5001;
    public final int CROP_IMAGE_U = 5003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_info);
        ButterKnife.bind(this);
        title.setText("发布");
        houseName.setText(User.getInstance().getUserName());
        housePhone.setText(User.getInstance().getUserPhonenumber());
    }

    /*
    *打开底部选择框*/
    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_cancel;
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_pick_phone.setOnClickListener(this);
        tv_pick_zone.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pick_phone:
                // getPicFromCamera();
                popupWindow.dismiss();
                break;
            case R.id.tv_pick_zone:
                //getPicFromAlbm();
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
        }

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
                houseInfoDesc.setText(mResults.toString());
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
    // http://192.168.1.103:8080/kenya/Lease/inserLease?
    // leasename=ddd
    // &leaseprice=123
    // &leasephone=13931860236
    // &leasesquare=JJFLy
    // &leaseaddress=jjfly
    // &leasehome=jjfly
    // &leasedesc=详情
    // &leaseIms=图片路径
    // &leaseIm1=图片路径
    // &userid=2

    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                final LoadingDialog loadingDialog = new LoadingDialog(HouseInfoActivity.this);
                loadingDialog.show();
                File file = new File(mResults.get(0));
                PostFormBuilder postFormBuilder = OkHttpUtils.post()
                        .addFile("leaseIms", "tupian.png", new File(mResults.get(0)));
                for (int i = 1; i < mResults.size(); i++) {
                    file = new File(mResults.get(i));
                    postFormBuilder.addFile("leaseIm" + i, "tupian.png", new File(mResults.get(i)));
                }
                postFormBuilder.url(AppConstants.BASE_URL + "/kenya/Lease/inserLease")
                        .addParams("leasename", houseName.getText().toString())
                        .addParams("leasedesc", houseInfoDesc.getText().toString())
                        .addParams("leaseprice", housePrice.getText().toString())
                        .addParams("leasephone", housePhone.getText().toString())

                        .addParams("leasesquare", "120" + "m2")
                        .addParams("leaseaddress", "河北省石家庄市")
                        .addParams("leasehome", "2室2厅2卫")
                        .addParams("userid", User.getInstance().getUserId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                loadingDialog.dismiss();
                                toast("加载失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                log(response);
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
                break;
        }
    }
}
