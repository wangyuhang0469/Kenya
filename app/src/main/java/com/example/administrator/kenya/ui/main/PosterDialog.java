package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.administrator.kenya.R;


public class PosterDialog extends Dialog{


    public PosterDialog(@NonNull Context context) {
        super(context,R.style.FullScreenDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poster_dialog);

        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "0780051319");
                intent.setData(data);
                getContext().startActivity(intent);
                PosterDialog.this.dismiss();
            }
        });

    }



}
