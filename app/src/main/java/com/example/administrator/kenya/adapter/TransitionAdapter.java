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
import com.example.administrator.kenya.classes.News;
import com.example.administrator.kenya.classes.Transition;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.news.NewsWebActivity;
import com.example.administrator.kenya.ui.news.DetailsDialog;

import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class TransitionAdapter extends RecyclerView.Adapter<TransitionAdapter.ViewHolder> {

    private List<Transition> list;
    private Context context;

    public TransitionAdapter(Context context, List<Transition> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, details;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            details = (TextView) itemView.findViewById(R.id.details);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getName());
        holder.details.setText(list.get(position).getDiedReport());
//        holder.news_image.setTag(list.get(position).getNewsimg0());

        Glide.with(context)
                .load(AppConstants.BASE_URL + list.get(position).getImg0())
                .centerCrop()
                .placeholder(R.drawable.img_loading3)
                .into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DetailsDialog(context , list.get(position).getDiedReport()).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
