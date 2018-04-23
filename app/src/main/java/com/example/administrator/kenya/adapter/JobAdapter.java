package com.example.administrator.kenya.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.activity.ResumeDetilActivity;
import com.example.administrator.kenya.classes.Job;
import com.example.administrator.kenya.constants.AppConstants;

import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<Job> list;
    private Context context;

    public JobAdapter(Context context, List<Job> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView job_want, job_name, job_sex, job_age;
        ImageView job_image;

        public ViewHolder(View itemView) {
            super(itemView);
            job_want = (TextView) itemView.findViewById(R.id.job_want);
            job_name = (TextView) itemView.findViewById(R.id.job_name);
            job_sex = (TextView) itemView.findViewById(R.id.job_sex);
            job_age = (TextView) itemView.findViewById(R.id.job_age);
            job_image = (ImageView) itemView.findViewById(R.id.job_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.job_want.setText(list.get(position).getJobwant());
        holder.job_name.setText(list.get(position).getName());
        holder.job_sex.setText(list.get(position).getSex());
        holder.job_age.setText(list.get(position).getAge() + "岁");
        holder.job_image.setTag(list.get(position).getHeadimg());
        Glide.with(context)
                .load(AppConstants.BASE_URL + list.get(position).getHeadimg())
                .asBitmap()
                .placeholder(R.drawable.bg4dp_grey)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String tag = (String) holder.job_image.getTag();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getHeadimg())) {
                            holder.job_image.setBackground(new BitmapDrawable(resource));   //设置背景
                        }
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("job", list.get(position));
                Intent intent = new Intent(context, ResumeDetilActivity.class);
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
