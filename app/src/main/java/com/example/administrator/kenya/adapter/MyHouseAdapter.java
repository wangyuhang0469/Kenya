package com.example.administrator.kenya.adapter;

/**
 * Created by Administrator on 2018/4/19.
 */

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
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.DeleteSuccessfulListener;
import com.example.administrator.kenya.ui.city.house.HouseDetailActivity;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.ui.city.house.HouseDetailActivity;
import com.example.administrator.kenya.ui.city.used.UsedActivity;
import com.example.administrator.kenya.ui.main.DeleteDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2018/3/27.
 */

public class MyHouseAdapter extends RecyclerView.Adapter<MyHouseAdapter.ViewHolder>{

    private List<House> list;
    private Context context;

    public MyHouseAdapter(Context context, List<House> list) {
        this.list = list;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView house_Title, house_phone, house_home, house_price,house_delete;
        ImageView house_image;

        public ViewHolder(View itemView) {
            super(itemView);
            house_Title = (TextView) itemView.findViewById(R.id.house_Title);
            house_phone = (TextView) itemView.findViewById(R.id.house_phone);
            house_home = (TextView) itemView.findViewById(R.id.house_home);
            house_price = (TextView) itemView.findViewById(R.id.house_price);
            house_delete = (TextView) itemView.findViewById(R.id.delete);
            house_image = (ImageView) itemView.findViewById(R.id.house_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myhouse, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.house_Title.setText(list.get(position).getLeasename());
        holder.house_phone.setText("手机：" + list.get(position).getLeasephone());
        holder.house_home.setText(list.get(position).getLeasehome());
        holder.house_price.setText("$" + list.get(position).getLeaseprice());
//        holder.house_image.setTag(list.get(position).getLeaseimgs());

        Glide.with(context)
                .load(AppConstants.BASE_URL + list.get(position).getLeaseimgs())
                .placeholder(R.drawable.img_loading)
                .centerCrop()
                .into(holder.house_image);

//                .asBitmap()
//                .placeholder(R.drawable.bg4dp_grey)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        String tag = (String) holder.house_image.getTag();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getLeaseimgs())) {
//                            holder.house_image.setBackground(new BitmapDrawable(resource));   //设置背景
//                        }
//                    }
//                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("house", list.get(position));
                Intent intent = new Intent(context, HouseDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.house_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Type","租房");
                params.put("id",list.get(position).getLeaseid());
                DeleteDialog deleteDialog = new DeleteDialog(context, AppConstants.BASE_URL + "/kenya/user/deleteByUserId" , params);
                deleteDialog.setDeleteSuccessfulListener(new DeleteSuccessfulListener() {
                    @Override
                    public void success() {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                deleteDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}
