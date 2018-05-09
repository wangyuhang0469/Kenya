package com.example.administrator.kenya.ui.myself;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnReUsernameSuccessfulListener;
import com.example.administrator.kenya.model.image_selector.MultiImageSelectorActivity;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.ui.main.ReUsernameDialog;
import com.example.administrator.kenya.ui.myself.myrelease.MyReleaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyselfFragment extends BaseFragment {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.my_center_name)
    TextView myCenterName;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.phone)
    TextView phone;

    private User user = User.getInstance();

    private ArrayList<String> mResults;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        ButterKnife.bind(this, view);
        title.setText("个人中心");
        back.setVisibility(View.GONE);
        myCenterName.setText(user.getUserName());
        Glide.with(this).load(AppConstants.BASE_URL + User.getInstance().getUserPortrait())
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);
        phone.setText("手机号：" + user.getUserPhonenumber());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.myName,R.id.myAddress, R.id.myRelease, R.id.myCar, R.id.myPoint, R.id.myVallet, R.id.avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myName:
                ReUsernameDialog reUsernameDialog = new ReUsernameDialog(getContext());
                reUsernameDialog.setOnReUsernameSuccessfulListener(new OnReUsernameSuccessfulListener() {
                    @Override
                    public void success(String newUsername) {
                        myCenterName.setText(newUsername);
                    }
                });
                reUsernameDialog.show();
                break;
            case R.id.myAddress:
                break;
            case R.id.myRelease:
                startActivity(MyReleaseActivity.class, null);
                break;
            case R.id.myCar:
                break;
            case R.id.myPoint:
                break;
            case R.id.myVallet:
                break;
            case R.id.avatar:
                break;

        }
    }

    @OnClick(R.id.avatar)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), MultiImageSelectorActivity.class);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                loadingDialog = new LoadingDialog(getContext());
                loadingDialog.show();
                Luban.with(getContext())
                        .load(mResults.get(0))
                        .ignoreBy(150)
                        .setTargetDir(getContext().getExternalCacheDir().toString())
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
                        toast("上传失败");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        log(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            toast(jsonObject.getString("message"));
                            if (jsonObject.getString("code").equals("000")) {
                                Glide.with(getContext())
                                        .load(mResults.get(0))
                                        .into(avatar);
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
