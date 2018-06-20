package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;


public class MainPosterDialog extends Dialog{

    private Bitmap poster;

    public MainPosterDialog(@NonNull Context context,Bitmap poster) {
        super(context,R.style.FullScreenDialog);
        this.poster = poster;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_poster_dialog);

        ImageView imageView = (ImageView)findViewById(R.id.poster);

        imageView.setImageBitmap(poster);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainPosterDialog.this.dismiss();
            }
        });

    }



}
