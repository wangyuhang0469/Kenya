package com.example.administrator.kenya.ui.myself;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.constants.AppConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class RegisterActivity extends BaseActivity {


    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.verifyCode)
    EditText verifyCode;
    @Bind(R.id.getVerifyCode)
    Button getVerifyCode;
    @Bind(R.id.userName)
    EditText userName;
    @Bind(R.id.password1)
    EditText password1;
    @Bind(R.id.password2)
    EditText password2;

    private boolean lock = false;
    private MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.getVerifyCode, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getVerifyCode:
                myCountDownTimer.start();
                break;
            case R.id.register:
                if (lock) {
                } else if (phone.getText().length() == 0 || userName.getText().length() == 0 || password1.getText().length() == 0 || password2.getText().length() == 0) {
                    toast("请填写完整信息");
                } else if (!password1.getText().toString().equals(password2.getText().toString())) {
                    toast("密码输入不一致");
                } else {
                    register();
                }
                break;
        }
    }

    private void register() {
        lock = true;
        OkHttpUtils.get()
                .url(AppConstants.BASE_URL + "/kenya/user/register")
                .addParams("userPhoneNumber", phone.getText().toString())
                .addParams("userName", userName.getText().toString())
                .addParams("userPsw", password1.getText().toString())
                .addParams("userSex", "6")
                .addParams("userAge", "99")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toast("注册失败");
                        lock = false;
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                toast("注册成功");
                                finish();
                            } else {
                                toast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        lock = false;
                    }
                });

    }
    /*
    * 倒计时获取验证码
    * */

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止退出Activity造成空指针
            if (getVerifyCode != null) {
                getVerifyCode.setClickable(false);
                getVerifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg4dp_grey));
                getVerifyCode.setText(l / 1000 + "s");
            } else myCountDownTimer.cancel();
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            if (getVerifyCode != null) {
                getVerifyCode.setText("重新获取");
                //设置可点击
                getVerifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg4dp_theme_btn));
                getVerifyCode.setClickable(true);
            } else myCountDownTimer.cancel();
        }
    }
}
