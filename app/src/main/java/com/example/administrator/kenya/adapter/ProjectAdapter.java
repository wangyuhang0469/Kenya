package com.example.administrator.kenya.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.Funds;
import com.example.administrator.kenya.classes.Project2;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.findmoney.FindProjectdetailActivity;

import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private List<Project2> list;
    private Context context;

    public ProjectAdapter(Context context, List<Project2> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView project_Title, project_phone, project_price;
        ImageView project_image;

        public ViewHolder(View itemView) {
            super(itemView);
            project_Title = (TextView) itemView.findViewById(R.id.project_Title);
            project_phone = (TextView) itemView.findViewById(R.id.project_phone);
            project_price = (TextView) itemView.findViewById(R.id.project_price);
            project_image = (ImageView) itemView.findViewById(R.id.project_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.project_Title.setText(list.get(position).getProjectname());
        holder.project_phone.setText("手机：" + list.get(position).getProjectphone());
        holder.project_price.setText("$" + list.get(position).getProjectprice());
//        holder.project_image.setTag(list.get(position).getProjectimgs());

        Glide.with(context)
                .load(AppConstants.BASE_URL + list.get(position).getProjectimgs())
                .centerCrop()
                .placeholder(R.drawable.img_loading)
                .into(holder.project_image);
//                .asBitmap()
//                .placeholder(R.drawable.bg4dp_grey)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        String tag = (String) holder.project_image.getTag();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getProjectimgs())) {
//                            holder.project_image.setBackground(new BitmapDrawable(resource));   //设置背景
//                        }
//                    }
//                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("project2", list.get(position));
                Intent intent = new Intent(context, FindProjectdetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
