package com.example.administrator.kenya.ui.city.house;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.kenya.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HouseInfoActivity extends Activity implements View.OnClickListener, PopupWindow.OnDismissListener {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.house_info_img1)
    ImageView houseInfoImg1;
    @Bind(R.id.house_info_img2)
    ImageView houseInfoImg2;
    @Bind(R.id.house_info_img3)
    ImageView houseInfoImg3;
    @Bind(R.id.house_info_img4)
    ImageView houseInfoImg4;
    @Bind(R.id.house_info_img5)
    ImageView houseInfoImg5;
    private PopupWindow popupWindow;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    //调用照相机返回图片文件
    private File tempFile;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_info);
        ButterKnife.bind(this);
        title.setText("发布");
    }

    @OnClick({R.id.house_info_img1, R.id.house_info_img2, R.id.house_info_img3, R.id.house_info_img4, R.id.house_info_img5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.house_info_img1:
                img = "A";
                openPopupWindow(view);
                break;
            case R.id.house_info_img2:
                img = "B";
                openPopupWindow(view);
                break;
            case R.id.house_info_img3:
                img = "C";
                openPopupWindow(view);
                break;
            case R.id.house_info_img4:
                img = "D";
                openPopupWindow(view);
                break;
            case R.id.house_info_img5:
                img = "E";
                openPopupWindow(view);
                break;
        }
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
                getPicFromCamera();
                popupWindow.dismiss();
                break;
            case R.id.tv_pick_zone:
                getPicFromAlbm();
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
        }

    }

    /**
     * 从相机获取图片
     */

    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(HouseInfoActivity.this, "com.hansion.chosehead", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("dasd", contentUri.toString());
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(HouseInfoActivity.this, "com.hansion.chosehead", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    //设置到ImageView上
                    if (img.equals("A")) {
                        houseInfoImg1.setImageBitmap(image);
                    } else if (img.equals("B")) {
                        houseInfoImg2.setImageBitmap(image);
                    } else if (img.equals("C")) {
                        houseInfoImg3.setImageBitmap(image);
                    } else if (img.equals("D")) {
                        houseInfoImg4.setImageBitmap(image);
                    } else if (img.equals("E")) {
                        houseInfoImg5.setImageBitmap(image);
                    } else {
                    }
                }
                break;
        }
    }
}
