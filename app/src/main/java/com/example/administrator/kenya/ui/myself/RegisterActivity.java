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
    private String verificationCode = "113557dd";

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
                if (phone.getText().length() == 0) {
                    toast(getResources().getString(R.string.please) + getResources().getString(R.string.enter_phone_no_));
                } else {
                    toGetVerifyCode();
                    myCountDownTimer.start();
                }
                break;
            case R.id.register:
                if (lock) {
                } else if (phone.getText().length() == 0 || verifyCode.getText().length() == 0 || userName.getText().length() == 0 || password1.getText().length() == 0 || password2.getText().length() == 0) {
                    toast(getResources().getString(R.string.enter_complete));
                } else if (!password1.getText().toString().equals(password2.getText().toString())) {
                    toast(getResources().getString(R.string.passwords_not_math));
                } else if (password1.getText().length() < 6 || password1.getText().length() > 22) {
                    toast(getResources().getString(R.string.passwrod_6_22));
                } else if (!verifyCode.getText().toString().equals(verificationCode)) {
                    toast(getResources().getString(R.string.verification_incorrect));
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
                .addParams("userPhonenumber", phone.getText().toString())
                .addParams("userName", userName.getText().toString())
                .addParams("userPsw", password1.getText().toString())
//                .addParams("userSex", "6")
//                .addParams("userAge", "99")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toast(getResources().getString(R.string.register_failed));
                        lock = false;
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                toast(getResources().getString(R.string.register_successfully));
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

    private void toGetVerifyCode() {
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/getCode")
                .addParams("phone", phone.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myCountDownTimer.cancel();
                        myCountDownTimer.onFinish();
                        toast(getString(R.string.send_failed));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                verificationCode = jsonObject.getString("verificationCode");
                                toast(getString(R.string.send_successfully));
                            } else {
                                toast(getString(R.string.send_failed));
                                myCountDownTimer.onFinish();
                                myCountDownTimer.cancel();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                getVerifyCode.setText(getString(R.string.acquire));
                //设置可点击
                getVerifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg4dp_theme_btn));
                getVerifyCode.setClickable(true);
            } else myCountDownTimer.cancel();
        }
    }
}
