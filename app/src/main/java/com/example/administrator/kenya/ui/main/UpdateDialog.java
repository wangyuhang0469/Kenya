package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.interfaces.OnChooseListener;
import com.example.administrator.kenya.interfaces.OnSuccessfulListener;
import com.example.administrator.kenya.utils.OpenFileUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;


public class UpdateDialog extends Dialog{

    private TextView tv_information;
    private TextView tv_version;
    private TextView yes;
    private TextView no;
    private String path;
    private String version;
    private boolean forcedUpdate;
    private String information;
    private OnChooseListener onChooseListener;
    public static final String DOWNLOAD_PATH = "DOWNLOAD_PATH" ;

    public UpdateDialog(@NonNull Context context , String path ,String version,String information , boolean forcedUpdate) {
        super(context, R.style.FullScreenDialog);
        this.information = information;
        this.path = path;
        this.version = version;
        this.forcedUpdate = forcedUpdate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);

        tv_information =(TextView) findViewById(R.id.information);
        tv_version =(TextView) findViewById(R.id.tv_version);
        yes =(TextView) findViewById(R.id.yes);
        no =(TextView) findViewById(R.id.no);

        if (path.equals(DOWNLOAD_PATH))
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/apk/BL"+ version +".apk";
        if (forcedUpdate)
            no.setText("EXIT");

        tv_information.setText( information);
        tv_version.setText(version);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OpenFileUtil.openFile(getContext(),path);
                getContext().startActivity(intent);
                onChooseListener.yes("yes");
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseListener.no(String.valueOf(forcedUpdate));
                UpdateDialog.this.dismiss();
            }
        });
    }


    public UpdateDialog setOnChooseListener(OnChooseListener onChooseListener){
        this.onChooseListener = onChooseListener;
        return this;
    }

}
