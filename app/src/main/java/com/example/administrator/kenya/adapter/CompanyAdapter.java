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
import com.example.administrator.kenya.activity.JobDetailActivity;
import com.example.administrator.kenya.classes.Company;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.ui.city.house.HouseDetailActivity;

import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    private List<Company> list;
    private Context context;

    public CompanyAdapter(Context context, List<Company> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView company_Title, company_phone, company_home, company_price;
        ImageView company_image;

        public ViewHolder(View itemView) {
            super(itemView);
            company_Title = (TextView) itemView.findViewById(R.id.company_Title);
            company_phone = (TextView) itemView.findViewById(R.id.company_phone);
            company_home = (TextView) itemView.findViewById(R.id.company_home);
            company_price = (TextView) itemView.findViewById(R.id.company_price);
            company_image = (ImageView) itemView.findViewById(R.id.company_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.company_Title.setText(list.get(position).getCompanystation() + ":");
        holder.company_phone.setText(list.get(position).getCompanyphone());
        holder.company_home.setText(list.get(position).getCompanyname());
        holder.company_price.setText(list.get(position).getCompanystationsalary() + "元/月");
        holder.company_image.setTag(list.get(position).getCompanyimg0());

        Glide.with(context)
                .load(list.get(position).getCompanyimg0())
                .asBitmap()
                .placeholder(R.drawable.bg4dp_grey)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String tag = (String) holder.company_image.getTag();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getCompanyimg0())) {
                            holder.company_image.setBackground(new BitmapDrawable(resource));   //设置背景
                        }
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("company", list.get(position));
                Intent intent = new Intent(context, JobDetailActivity.class);
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
