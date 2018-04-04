package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.example.administrator.kenya.R;


public class LoadingDialog extends Dialog{


    public LoadingDialog(@NonNull Context context) {
        super(context,R.style.FullScreenDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);

    }



}
