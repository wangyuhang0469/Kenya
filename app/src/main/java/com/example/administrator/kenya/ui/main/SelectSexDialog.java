package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.interfaces.OnSexSelectListener;
import com.example.administrator.kenya.interfaces.OnSuccessfulListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;


public class SelectSexDialog extends Dialog{


    private TextView male;
    private TextView female;
    private OnSexSelectListener onSexSelectListener;

    public SelectSexDialog(@NonNull Context context,OnSexSelectListener onSexSelectListener) {
        super(context, R.style.FullScreenDialog);
        this.onSexSelectListener = onSexSelectListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sex_dialog);

        male =(TextView) findViewById(R.id.male);
        female =(TextView) findViewById(R.id.female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSexSelectListener.selete("0");
                SelectSexDialog.this.dismiss();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSexSelectListener.selete("1");
                SelectSexDialog.this.dismiss();
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



}
