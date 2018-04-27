package com.example.administrator.kenya.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;


public class CallPhoneDialog extends Dialog {

    private String phone;
    private TextView information;
    private TextView yes;
    private TextView no;

    public CallPhoneDialog(@NonNull Context context, String phone) {
        super(context, R.style.FullScreenDialog);
        this.phone = phone;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_phone_dialog);

        information =(TextView) findViewById(R.id.information);
        yes =(TextView) findViewById(R.id.yes);
        no =(TextView) findViewById(R.id.no);

        information.setText("您确定要给"+phone+"拨打电话吗？");

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallPhoneDialog.this.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                getContext().startActivity(intent);
                CallPhoneDialog.this.dismiss();
            }
        });



    }



}
