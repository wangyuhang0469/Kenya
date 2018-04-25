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
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.findmoney.FindMonydetailActivity;
import com.example.administrator.kenya.ui.city.house.HouseDetailActivity;

import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class FundsAdapter extends RecyclerView.Adapter<FundsAdapter.ViewHolder> {

    private List<Funds> list;
    private Context context;

    public FundsAdapter(Context context, List<Funds> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView funds_Title, funds_phone, funds_price;
        ImageView funds_image;

        public ViewHolder(View itemView) {
            super(itemView);
            funds_Title = (TextView) itemView.findViewById(R.id.funds_Title);
            funds_phone = (TextView) itemView.findViewById(R.id.funds_phone);
            funds_price = (TextView) itemView.findViewById(R.id.funds_price);
            funds_image = (ImageView) itemView.findViewById(R.id.funds_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_funds, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.funds_Title.setText(list.get(position).getFundsname());
        holder.funds_phone.setText("手机：" + list.get(position).getFundsphone());
        holder.funds_price.setText("$" + list.get(position).getFundsprice());
//        holder.funds_image.setTag(list.get(position).getFundsimgs());

        Glide.with(context)
                .load(AppConstants.BASE_URL + list.get(position).getFundsimgs())
                .centerCrop()
                .placeholder(R.drawable.img_loading)
                .into(holder.funds_image);

//                .asBitmap()
//                .placeholder(R.drawable.bg4dp_grey)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        String tag = (String) holder.funds_image.getTag();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getFundsimgs())) {
//                            holder.funds_image.setBackground(new BitmapDrawable(resource));   //设置背景
//                        }
//                    }
//                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("funds", list.get(position));
                Intent intent = new Intent(context, FindMonydetailActivity.class);
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
