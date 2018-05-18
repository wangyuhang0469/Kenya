package com.example.administrator.kenya.ui.myself.myrelease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.MessageEvent;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnReUsernameSuccessfulListener;
import com.example.administrator.kenya.model.image_selector.MultiImageSelectorActivity;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.ui.main.ReUsernameDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        ButterKnife.bind(this);

        title.setText(getString(R.string.personal_information));
        userName.setText(user.getUserName());
        Glide.with(this).load(User.getInstance().getUserPortrait())
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);
        phone.setText(user.getUserPhonenumber());
    }

    @OnClick({R.id.back, R.id.avatarBar, R.id.userNameBar, R.id.birthdayBar, R.id.sexBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
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
                break;
            case R.id.sexBar:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                loadingDialog = new LoadingDialog(this);
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
                                send(file);
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


    private void send(File file) {

        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/updatePortrait")
                .addFile("file", file.getName(), file)
                .addParams("id", User.getInstance().getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        toast(R.string.post_fail);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        log(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            toast(jsonObject.getString("message"));
                            if (jsonObject.getString("code").equals("000")) {
                                Glide.with(MyInformationActivity.this)
                                        .load(mResults.get(0))
                                        .into(avatar);
                                EventBus.getDefault().post(new MessageEvent(mResults.get(0)));
                                User.getInstance().setUserPortrait(mResults.get(0));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingDialog.dismiss();
                        loadingDialog = null;
                    }
                });
    }
}
