package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;


public class MainNoticeDialog extends Dialog{

    private String information;

    public MainNoticeDialog(@NonNull Context context, String information) {
        super(context,R.style.FullScreenDialog);
        this.information = information;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_notice_dialog);

        TextView tv_information = (TextView) findViewById(R.id.information);

        tv_information.setText(information);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainNoticeDialog.this.dismiss();
            }
        });

    }



}
