package com.example.administrator.kenya.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Job;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.job.DatePickerDialog;
import com.example.administrator.kenya.ui.city.job.OnBooleanListener;
import com.example.administrator.kenya.utils.DateUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ResumeinfoActivity extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener {

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
    @Bind(R.id.resume_tv_recm_choose)
    TextView resumeTvRecmChoose;
    @Bind(R.id.resume_info_photo)
    ImageView resumeInfoPhoto;
    @Bind(R.id.resume_info_detail)
    TextView resumeInfoDetail;
    @Bind(R.id.resume_info_men)
    ImageView resumeInfoMen;
    @Bind(R.id.resume_info_women)
    ImageView resumeInfoWomen;
    @Bind(R.id.resume_info_phone)
    EditText resumeInfoPhone;
    @Bind(R.id.resume_info_jobwant)
    EditText resumeInfoJobwant;
    @Bind(R.id.resume_tv_recm)
    EditText resumeTvRecm;
    @Bind(R.id.resume_info_jobname)
    EditText resumeInfoJobname;
    private Dialog dateDialog;
    private PopupWindow popupWindow;


    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    public Uri imageUriFromCamera;
    public Uri cropImageUri;
    public final int GET_IMAGE_BY_CAMERA_U = 5001;
    public final int CROP_IMAGE_U = 5003;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;

    private String s;
    private String sexvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumeinfo);
        resumeTvRecm.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    resumeTvRecmChoose.setVisibility(View.GONE);
                } else {
                }
            }
        });
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.pick_time, R.id.resume_recm, R.id.ruseme_work_time, R.id.resume_info_photo, R.id.resume_info_detail, R.id.resume_info_men, R.id.resume_info_women})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.pick_time:
                showDateDialog(DateUtil.getDateForString("2000-01-01"), "A");
                break;
            case R.id.resume_recm:
                break;
            case R.id.ruseme_work_time:
                showDateDialog(DateUtil.getDateForString("2010-01-01"), "B");
                break;
            case R.id.resume_info_photo:
                openPopupWindow(view);
                break;
            case R.id.resume_info_detail:
                if (s == null || s.length() == 0) {
                    toast("请上传头像");
                } else if (resumeInfoJobname.getText().length() == 0) {
                    toast("请输入姓名");
                } else if (sexvalue.equals("")) {
                    toast("请选择性别");
                } else if (resumeInfoJobwant.getText().length() == 0) {
                    toast("请输入求职意向");
                } else if (resumeInfoPhone.getText().length() == 0) {
                    toast("请输入手机号");
                } else if (resumeTimeBirday.getText().length() == 0) {
                    toast("请选择出生年月");
                } else if (resumeTvRecmChoose.getText().length() == 0) {
                    toast("请输入个人介绍");
                } else if (resumeTvTime.getText().length() == 0) {
                    toast("请选择开始工作时间");
                } else {
                    Log.d("kang", "aaaaaaaaaa" + s);
//                    Luban.with(this)
//                            .load(s)
//                            .ignoreBy(150)
//                            .setTargetDir(getExternalCacheDir().toString())
//                            .setCompressListener(new OnCompressListener() {
//                                @Override
//                                public void onStart() {
//                                }
//
//                                @Override
//                                public void onSuccess(File file) {
//                                    Log.d("kang", "BBBBBBBBBBBB" + file);
//                                    s = String.valueOf(file);
//                                    Log.d("kang", "CCCCCCCCCCCCC" + s);
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                }
//                            }).launch();
                    File f2 = new File(s);
                    Log.d("kang", "DDDDDDDDDDDDDD" + f2);
                    final PostFormBuilder postFormBuilder = OkHttpUtils.post()
                            .addFile("logoFile", "tupian.png", f2);
                    postFormBuilder.url(AppConstants.BASE_URL + "/kenya/jobSeeker/saveJobWant")
                            .addParams("sex", sexvalue)
                            .addParams("name", resumeInfoJobname.getText().toString())
                            .addParams("jobwant", resumeInfoJobwant.getText().toString())
                            .addParams("phone", resumeInfoPhone.getText().toString())
                            .addParams("birthday", resumeTimeBirday.getText().toString())
                            .addParams("jointime", resumeTvTime.getText().toString())
                            .addParams("persondesc", resumeTvRecm.getText().toString())
                            .addParams("userId", User.getInstance().getUserId())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    toast("加载失败");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("kang", "11111111111111111111" + response);
                                    log(response);
                                    toast("加载成功");
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("code").equals("000")) {
                                            Job job = JSON.parseObject(jsonObject.getString("data"), Job.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("job", job);
                                            toast("发布成功");
                                            startActivity(ResumeDetilActivity.class, bundle);
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
            case R.id.resume_info_men:
                resumeInfoMen.setImageResource(R.mipmap.resume_sex_click);
                resumeInfoWomen.setImageResource(R.mipmap.resume_sex2);
                sexvalue = "男";
                break;
            case R.id.resume_info_women:
                resumeInfoMen.setImageResource(R.mipmap.resume_sex);
                resumeInfoWomen.setImageResource(R.mipmap.resume_sex_click2);
                sexvalue = "女";
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
                    resumeTimeBirday.setText(dates[0] + "/" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "/"
                            + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));
                    resumeTimeBirdayChoose.setVisibility(View.GONE);
                } else {
                    resumeTvTime.setText(dates[0] + "/" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "/"
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
                camer();
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

    public void camer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                @Override
                public void onClick(boolean bln) {
                    if (bln) {
                        Log.d("MainActivity", "进入权限");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = createImagePathFile(ResumeinfoActivity.this);
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        imageUriFromCamera = FileProvider.getUriForFile(ResumeinfoActivity.this,
                                "com.example.administrator.kenya.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);

                        startActivityForResult(intent, GET_IMAGE_BY_CAMERA_U);
                    } else {
                        Toast.makeText(ResumeinfoActivity.this, "拍照无法正常使用", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            imageUriFromCamera = createImagePathUri(ResumeinfoActivity.this);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    imageUriFromCamera);
            startActivityForResult(intent, GET_IMAGE_BY_CAMERA_U);
        }

    }

    public File createImagePathFile(Activity activity) {
        Uri imageFilePath;
        File file = new File(activity.getExternalCacheDir(), USER_IMAGE_NAME);
        imageFilePath = Uri.fromFile(file);
        return file;
    }

    public Uri createImagePathUri(Activity activity) {
        Uri imageFilePath;
        File file = new File(activity.getExternalCacheDir(), USER_IMAGE_NAME);
        imageFilePath = Uri.fromFile(file);
        return imageFilePath;
    }

    @Override
    protected void onResume() {
        onPermissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
            @Override
            public void onClick(boolean bln) {
                if (bln) {
                } else {
                    Toast.makeText(ResumeinfoActivity.this, "文件读写无法正常使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        System.out.println("数据" + resultCode + "" + this.RESULT_OK);
        if (resultCode != this.RESULT_CANCELED) {
            switch (requestCode) {
                case GET_IMAGE_BY_CAMERA_U:
                    if (imageUriFromCamera != null) {
                        cropImage(imageUriFromCamera, 1, 1, CROP_IMAGE_U);
                        break;
                    }
                    break;
                case CROP_IMAGE_U:
                    s = getExternalCacheDir() + "/" + USER_CROP_IMAGE_NAME;
                    Log.d("kang", "zzzzzzzzz" + s);
                    Bitmap imageBitmap = GetBitmap(s, 320, 320);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
                    resumeInfoPhoto.setImageBitmap(imageBitmap);
                    break;
                case ALBUM_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        cropPhoto(uri);
                    }
                    break;
                case CROP_REQUEST_CODE:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap image = bundle.getParcelable("data");
                        resumeInfoPhoto.setImageBitmap(image);
                        s = saveImage("crop", image);
                        Log.d("kang", "path" + s);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    public void cropImage(Uri imageUri, int aspectX, int aspectY,
                          int return_flag) {
        File file = new File(this.getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileProvider.getUriForFile(ResumeinfoActivity.this, "com.example.administrator.kenya.fileprovider", file);
        }
        cropImageUri = Uri.fromFile(file);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        startActivityForResult(intent, return_flag);
    }

    public Bitmap GetBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
                BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
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

    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        Log.d("kang", "fileName" + fileName);
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
