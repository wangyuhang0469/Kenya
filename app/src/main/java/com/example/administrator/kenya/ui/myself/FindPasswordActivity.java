package com.example.administrator.kenya.ui.myself;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPasswordActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.updatePwd)
    EditText updatePwd;
    @Bind(R.id.updatePwdsure)
    EditText updatePwdsure;
    @Bind(R.id.updatePassword_conmit)
    Button updatePasswordConmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        title.setText("重置密码");
    }

    @OnClick({R.id.back, R.id.updatePassword_conmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.updatePassword_conmit:
                toast("更新成功");
                finish();
                break;
        }
    }
}
