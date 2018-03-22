package com.example.administrator.kenya.ui.myself;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.verifyCode)
    EditText verifyCode;
    @Bind(R.id.getVerifyCode)
    Button getVerifyCode;
    @Bind(R.id.userName)
    EditText userName;
    @Bind(R.id.password)
    EditText password;

    private MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        title.setText("注 册");
    }

    @OnClick({R.id.back, R.id.getVerifyCode, R.id.register, R.id.toLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.getVerifyCode:
                myCountDownTimer.start();
                break;
            case R.id.register:
                if (phone.getText().length() != 0 && verifyCode.getText().length() != 0 && userName.getText().length() != 0 && password.getText().length() != 0) {
                    toast("注册成功");
                    finish();
                } else {
                    toast("请填写完整信息");
                }
                break;
            case R.id.toLogin:
                finish();
                break;
        }
    }


    private void register(){
//        OkHttpUtils.get()
//                .url()

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
                getVerifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg20dp_grey));
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
                getVerifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg20dp_theme));
                getVerifyCode.setClickable(true);
            } else myCountDownTimer.cancel();
        }
    }
}
