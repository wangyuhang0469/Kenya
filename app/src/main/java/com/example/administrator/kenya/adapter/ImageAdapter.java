package com.example.administrator.kenya.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.main.PreviewDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


    private List<String> imaUrls;
    private Context context;

    public ImageAdapter(Context context ,List<String> list) {
        this.imaUrls = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        holder.imageView.setTag(imaUrls.get(position));

        Glide.with(context)
                .load(imaUrls.get(position))
                .centerCrop()
                .into(holder.imageView);

//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        String tag = (String) holder.imageView.getTag();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(imaUrls.get(position))) {
//                            holder.imageView.setBackground(new BitmapDrawable(resource));   //设置背景
//                        }
//                    }
//                });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PreviewDialog(context ,imaUrls,position).show();

            }
        });



    }




    @Override
    public int getItemCount() {
        return imaUrls == null ? 0 : imaUrls.size();
    }


}