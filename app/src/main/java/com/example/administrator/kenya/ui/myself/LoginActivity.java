package com.example.administrator.kenya.ui.myself;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.MainActivity;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        title.setText("登录");
        back.setVisibility(View.GONE);
    }


    @OnClick({R.id.back, R.id.login, R.id.forgetPassword, R.id.Register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:
                if (phone.getText().length() != 0 && password.getText().length() != 0) {
                    startActivity(MainActivity.class, null);
                    finish();
                    toast("登陆成功");
                } else {
                    toast("用户名或密码不能为空");
                }
                break;
            case R.id.forgetPassword:
                startActivity(ForgetPasswordActivity.class, null);
                break;
            case R.id.Register:
                startActivity(RegisterActivity.class, null);
                break;
        }
    }
}
