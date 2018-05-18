package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.MessageEvent2;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnReUsernameSuccessfulListener;
import com.example.administrator.kenya.interfaces.OnSuccessfulListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;


public class ReUsernameDialog extends Dialog{

    private EditText information;
    private TextView yes;
    private TextView no;
    private OnReUsernameSuccessfulListener onReUsernameSuccessfulListener;


    public ReUsernameDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_username_dialog);

        information =(EditText) findViewById(R.id.information);
        yes =(TextView) findViewById(R.id.yes);
        no =(TextView) findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (information.getText().length()!=0){
                    send();
                }else {
                    Toast.makeText(getContext(), "Please enter a new name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReUsernameDialog.this.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void send(){

        final String userName = information.getText().toString();
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/updateuserName")
                .addParams("id", User.getInstance().getUserId())
                .addParams("userName",userName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Modify Fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")){
                                Toast.makeText(getContext(), "Modify Success", Toast.LENGTH_SHORT).show();
                                User.getInstance().setUserName(userName);
                                if (onReUsernameSuccessfulListener != null)
                                    onReUsernameSuccessfulListener.success(userName);
                                EventBus.getDefault().post(new MessageEvent2(userName));
                                User.getInstance().setUserName(userName);
                                ReUsernameDialog.this.dismiss();
                            }else {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }


    public void setOnReUsernameSuccessfulListener(OnReUsernameSuccessfulListener onReUsernameSuccessfulListener){
        this.onReUsernameSuccessfulListener = onReUsernameSuccessfulListener;
    }


}
