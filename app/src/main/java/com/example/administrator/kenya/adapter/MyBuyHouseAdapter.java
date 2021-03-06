package com.example.administrator.kenya.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.BuyHouse;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnSuccessfulListener;
import com.example.administrator.kenya.ui.city.buyhouse.BuyHouseDetailActivity;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.ui.main.DeleteDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2018/3/27.
 */

public class MyBuyHouseAdapter extends RecyclerView.Adapter<MyBuyHouseAdapter.ViewHolder> {
    private List<BuyHouse> list;
    private Context context;

    public MyBuyHouseAdapter(Context context, List<BuyHouse> list) {
        this.list = list;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView house_Title, house_square, house_address, house_home, house_price, call, house_delete;
        ImageView house_image;

        public ViewHolder(View itemView) {
            super(itemView);
            house_Title = (TextView) itemView.findViewById(R.id.house_Title);
            house_home = (TextView) itemView.findViewById(R.id.house_home);
            house_square = (TextView) itemView.findViewById(R.id.house_square);
            house_address = (TextView) itemView.findViewById(R.id.house_address);
            house_price = (TextView) itemView.findViewById(R.id.house_price);
            call = (TextView) itemView.findViewById(R.id.house_call);
            house_image = (ImageView) itemView.findViewById(R.id.house_image);
            house_delete = (TextView) itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mybuyhouse, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.house_Title.setText(list.get(position).getHousename());
        holder.house_home.setText(list.get(position).getHousehome());
        holder.house_square.setText(list.get(position).getHousesquare() + "㎡");
        holder.house_address.setText(list.get(position).getHouseaddress());
        holder.house_price.setText("KSh " + list.get(position).getHouseprice());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CallPhoneDialog(context, list.get(position).getHousephone()).show();
            }
        });

//        holder.house_image.setTag(list.get(position).getLeaseimgs());

        Glide.with(context)
                .load(AppConstants.BASE_URL + list.get(position).getHouseimgs())
                .centerCrop()
                .placeholder(R.drawable.img_loading1)
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
                bundle.putSerializable("buyhouse", list.get(position));
                Intent intent = new Intent(context, BuyHouseDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.house_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Type", "买房");
                params.put("id", list.get(position).getHouseid());
                DeleteDialog deleteDialog = new DeleteDialog(context, AppConstants.BASE_URL + "/kenya/user/deleteByUserId", params);
                deleteDialog.setOnSuccessfulListener(new OnSuccessfulListener() {
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
