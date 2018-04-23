package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.city.used.GoodsDetailsActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.List;


public class PreviewDialog extends Dialog{

    private String url;

    public PreviewDialog(@NonNull Context context,String url) {
        super(context,R.style.FullScreenDialog);
        this.url = url;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_dialog);

        ImageView imageView = findViewById(R.id.image);
        Glide.with(getContext()).load(url).fitCenter().into(imageView);



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
