package com.example.administrator.kenya.ui.myself;

import android.os.Bundle;
import android.view.View;
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

public class LoginActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.back)
    ImageView back;

    private boolean lock=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        title.setText("登 录");
        back.setVisibility(View.GONE);
    }


    @OnClick({R.id.back, R.id.login, R.id.forgetPassword, R.id.Register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:
                if (lock) {
                } else if (phone.getText().length() == 0 || password.getText().length() == 0){
                    toast("用户名或密码不能为空");
                }else {
                    login();
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

    public void login(){
        lock = true;
        OkHttpUtils.get()
                .url("http://192.168.1.102:8080/kenYa-test/user/login")
                .addParams("userName",phone.getText().toString())
                .addParams("userPsw",password.getText().toString())
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
                            if (jsonObject.getString("code").equals("040003")){
                                jsonObject = jsonObject.getJSONObject("data");
                                User user = User.getInstance();
                                user.setUserName(jsonObject.getString("userName"));
                                user.setStatus(true);
                                startActivity(MainActivity.class, null);
                                finish();
                                toast("登陆成功");
                            }else {
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
