package com.example.administrator.kenya.ui.news;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;


public class DetailsDialog extends Dialog{


    private String details;

    public DetailsDialog(@NonNull Context context, String details) {
        super(context,R.style.FullScreenDialog);
        this.details = details;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_dialog);

        TextView tv_details = (TextView) findViewById(R.id.details);
        tv_details.setText(details);

    }



}
