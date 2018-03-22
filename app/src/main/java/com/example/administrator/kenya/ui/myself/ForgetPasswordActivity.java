package com.example.administrator.kenya.ui.myself;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseActivity {


    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.verifyCode)
    EditText verifyCode;
    @Bind(R.id.getVerifyCode)
    Button getVerifyCode;

    private MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.getVerifyCode)
    public void onViewClicked(View view) {
        myCountDownTimer.start();
    }

    @OnClick(R.id.updatePassword)
    public void onViewClicked() {
        if (phone.getText().length() != 0 && verifyCode.getText().length() != 0) {
            startActivity(ForgetPasswordActivity.class, null);
            finish();
        } else {
            toast("请填写完整信息");
        }
    }

    /*
    *倒计时
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
