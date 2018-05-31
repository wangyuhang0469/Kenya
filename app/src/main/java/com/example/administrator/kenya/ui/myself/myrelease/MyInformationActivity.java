package com.example.administrator.kenya.ui.myself.myrelease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.MessageEvent;
import com.example.administrator.kenya.classes.MessageEvent2;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnReUsernameSuccessfulListener;
import com.example.administrator.kenya.interfaces.OnSexSelectListener;
import com.example.administrator.kenya.model.image_selector.MultiImageSelectorActivity;
import com.example.administrator.kenya.ui.city.job.DatePickerDialog;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.ui.main.ReUsernameDialog;
import com.example.administrator.kenya.ui.main.SelectSexDialog;
import com.example.administrator.kenya.ui.myself.LoginActivity;
import com.example.administrator.kenya.utils.DateUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class MyInformationActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.birthday)
    TextView birthday;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.phone)
    TextView phone;


    private ArrayList<String> mResults;
    private LoadingDialog loadingDialog;
    private User user = User.getInstance();
    private boolean isfirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(this);
        title.setText(getString(R.string.personal_information));
        userName.setText(user.getUserName());
        Glide.with(this).load(User.getInstance().getUserPortrait())
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);
        phone.setText(user.getUserPhonenumber());

        String birthdayStr = user.getUserBirthday();
        if (birthdayStr != null && !birthdayStr.equals("null") && !birthdayStr.equals("")) {
            birthday.setText(birthdayStr);
        }
        if (user.getUserSex().equals("0")) {
            sex.setText(getString(R.string.male));
        } else if (user.getUserSex().equals("1")) {
            sex.setText(getString(R.string.female));
        }
    }

    @OnClick({R.id.back, R.id.log_out, R.id.avatarBar, R.id.userNameBar, R.id.birthdayBar, R.id.sexBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.log_out:
                User.getInstance().setStatus(false);
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor e = getPrefs.edit();
                e.putString("userId", "0");
                e.apply();

                Intent i = new Intent();
                setResult(4, i);
                finish();
                startActivity(LoginActivity.class, null);
                break;
            case R.id.avatarBar:
                Intent intent = new Intent(this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                // 默认选择
                if (mResults != null && mResults.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mResults);
                }
                startActivityForResult(intent, 2);
                break;
            case R.id.userNameBar:
                ReUsernameDialog reUsernameDialog = new ReUsernameDialog(this);
                reUsernameDialog.setOnReUsernameSuccessfulListener(new OnReUsernameSuccessfulListener() {
                    @Override
                    public void success(String newUsername) {
                        userName.setText(newUsername);
                    }
                });
                reUsernameDialog.show();
                break;
            case R.id.birthdayBar:
                String birthdayStr = user.getUserBirthday();
                if (birthdayStr != null && !birthdayStr.equals("null") && !birthdayStr.equals("")) {
                    showDateDialog(DateUtil.getDateForString(birthdayStr));
                } else {
                    showDateDialog(DateUtil.getDateForString("2000-01-01"));
                }
                break;
            case R.id.sexBar:
                new SelectSexDialog(this, new OnSexSelectListener() {
                    @Override
                    public void selete(String sex) {
                        send("userSex", sex);
                    }
                }).show();
                break;
        }
    }

    private void showDateDialog(List<Integer> date) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                String birString = dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2]));
                send("birthday", birString);
            }

            @Override
            public void onCancel() {
            }
        }).setSelectYear(date.get(0) - 1)
                .setSelectMonth(date.get(1) - 1)
                .setSelectDay(date.get(2) - 1);

        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        builder.create().show();
//        dateDialog = builder.create();
//        dateDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                loadingDialog.show();
                Luban.with(this)
                        .load(mResults.get(0))
                        .ignoreBy(150)
                        .setTargetDir(getExternalCacheDir().toString())
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                sendAvatar(file);
                            }

                            @Override
                            public void onError(Throwable e) {
                                loadingDialog.dismiss();
                            }
                        }).launch();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void sendAvatar(File file) {
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/updatePortrait")
                .addFile("file", file.getName(), file)
                .addParams("id", User.getInstance().getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        toast(R.string.modify_failed);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        log(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                Glide.with(MyInformationActivity.this)
                                        .load(mResults.get(0))
                                        .into(avatar);
                                String msg = jsonObject.getJSONObject("result").getString("userPortrait");
                                User.getInstance().setUserPortrait(msg);
                                EventBus.getDefault().post(new MessageEvent(msg));
                                toast(getString(R.string.modify_successfully));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingDialog.dismiss();
                        loadingDialog = null;
                    }
                });
    }

    private void send(final String key, final String word) {
        loadingDialog.show();
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/updateUser")
                .addParams("userId", User.getInstance().getUserId())
                .addParams(key, word)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        toast(getString(R.string.modify_failed));
                        loadingDialog.dismiss();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                toast(getString(R.string.modify_successfully));
                                if (key.equals("birthday")) {
                                    User.getInstance().setUserBirthday(word);
                                    birthday.setText(word);
                                } else if (key.equals("userSex")) {
                                    String sexStr = null;
                                    if (word.equals("0")) {
                                        sexStr = getString(R.string.male);
                                        User.getInstance().setUserSex(word);
                                        sex.setText(sexStr);
                                    } else if (word.equals("1")) {
                                        sexStr = getString(R.string.female);
                                        User.getInstance().setUserSex(word);
                                        sex.setText(sexStr);
                                    }
                                }
                            } else {
                                toast(getString(R.string.modify_failed));
                            }
                            loadingDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
