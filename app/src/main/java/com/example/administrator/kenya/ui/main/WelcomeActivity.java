package com.example.administrator.kenya.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.example.administrator.kenya.MainActivity;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.myself.LoginActivity;
import com.example.administrator.kenya.utils.DeviceUuidFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setBackgroundResource(R.drawable.welcome);

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String userId =getPrefs.getString("userId" , "0");

        final User user = User.getInstance();

        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/loged")
                .addParams("deviceId", DeviceUuidFactory.getUUID(this))
                .addParams("userId" , userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                        if (jsonObject.getString("code").equals("000")) {
                            jsonObject = jsonObject.getJSONObject("result");
                            user.setUserId(jsonObject.getString("userId"));
                            user.setUserName(jsonObject.getString("userName"));
                            user.setUserPsw(jsonObject.getString("userPsw"));
                            user.setUserSex(jsonObject.getString("userSex"));
                            user.setUserPhonenumber(jsonObject.getString("userPhonenumber"));
                            user.setUserHavecar(jsonObject.getString("userHavecar"));
                            user.setUserBirthday(jsonObject.getString("userBirthday"));
                            user.setUserPortrait(jsonObject.getString("userPortrait"));
                            user.setStatus(true);
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user.getStatus()){
                    startActivity(MainActivity.class, null);
                }else {
                    startActivity(LoginActivity.class, null);
                }
                finish();
            }
        }, 3000);
//
    }

}


