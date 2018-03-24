package com.example.administrator.kenya.ui.myself;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.MainActivity;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.zhy.autolayout.utils.ScreenUtils.getStatusBarHeight;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.password)
    EditText password;

    private boolean lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login, R.id.forgetPassword, R.id.Register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (lock) {
                } else if (phone.getText().length() == 0 || password.getText().length() == 0) {
                    toast("用户名或密码不能为空");
                } else {
                    login();
                    // startActivity(MainActivity.class, null);
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

    public void login() {
        lock = true;
        OkHttpUtils.get()
                .url("http://192.168.1.102:8080/kenYa-test/user/login")
                .addParams("userPhoneNumber", phone.getText().toString())
                .addParams("userPsw", password.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toast("登录失败");
                        lock = false;
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")) {
                                jsonObject = jsonObject.getJSONObject("data");
                                User user = User.getInstance();
                                user.setUserName(jsonObject.getString("userName"));
                                user.setStatus(true);
                                startActivity(MainActivity.class, null);
                                finish();
                                toast("登录成功");
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
}
