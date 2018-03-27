package com.example.administrator.kenya.activity;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.ui.city.job.DatePickerDialog;
import com.example.administrator.kenya.utils.DateUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResumeinfoActivity extends Activity implements View.OnClickListener, PopupWindow.OnDismissListener {

    @Bind(R.id.pick_time)
    AutoLinearLayout pickTime;
    @Bind(R.id.resume_time_birday)
    TextView resumeTimeBirday;
    @Bind(R.id.resume_recm)
    AutoLinearLayout resumeRecm;
    @Bind(R.id.ruseme_work_time)
    AutoLinearLayout rusemeWorkTime;
    @Bind(R.id.resume_tv_time)
    TextView resumeTvTime;
    @Bind(R.id.resume_time_birday_choose)
    TextView resumeTimeBirdayChoose;
    @Bind(R.id.resume_tv_choose)
    TextView resumeTvChoose;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.person_info)
    AutoRelativeLayout personInfo;
    @Bind(R.id.resume_tv_phone)
    TextView resumeTvPhone;
    @Bind(R.id.resume_tv_recm)
    TextView resumeTvRecm;
    @Bind(R.id.resume_tv_recm_choose)
    TextView resumeTvRecmChoose;
    @Bind(R.id.resume_info_photo)
    ImageView resumeInfoPhoto;
    @Bind(R.id.resume_info_detail)
    TextView resumeInfoDetail;
    private Dialog dateDialog;
    private PopupWindow popupWindow;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    //调用照相机返回图片文件
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumeinfo);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pick_time, R.id.resume_recm, R.id.ruseme_work_time, R.id.resume_info_photo, R.id.resume_info_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pick_time:
                showDateDialog(DateUtil.getDateForString("1990-01-01"), "A");
                break;
            case R.id.resume_recm:
                break;
            case R.id.ruseme_work_time:
                showDateDialog(DateUtil.getDateForString("1990-01-01"), "B");
                break;
            case R.id.resume_info_photo:
                openPopupWindow(view);
                break;
            case R.id.resume_info_detail:
                Intent intent = new Intent(this, ResumeDetilActivity.class);
                startActivity(intent);
                break;
        }
    }

    /* 用户生日时间*/
    private void showDateDialog(List<Integer> date, final String str) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                if (str.equals("A")) {
                    resumeTimeBirday.setText(dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                            + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));
                    resumeTimeBirdayChoose.setVisibility(View.GONE);
                } else {
                    resumeTvTime.setText(dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                            + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));
                    resumeTvChoose.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancel() {
            }
        })
                .setSelectYear(date.get(0) - 1)
                .setSelectMonth(date.get(1) - 1)
                .setSelectDay(date.get(2) - 1);

        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        dateDialog = builder.create();
        dateDialog.show();
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
            Uri contentUri = FileProvider.getUriForFile(ResumeinfoActivity.this, "com.hansion.chosehead", tempFile);
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
                        Uri contentUri = FileProvider.getUriForFile(ResumeinfoActivity.this, "com.hansion.chosehead", tempFile);
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
                    resumeInfoPhoto.setImageBitmap(image);
                }
                break;
        }

    }
}
