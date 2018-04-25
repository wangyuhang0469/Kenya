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
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.classes.News;
import com.example.administrator.kenya.ui.city.house.HouseDetailActivity;
import com.example.administrator.kenya.ui.city.news.NewsWebActivity;

import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> list;
    private Context context;

    public NewsAdapter(Context context, List<News> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView news_title, news_time;
        ImageView news_image;

        public ViewHolder(View itemView) {
            super(itemView);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_time = (TextView) itemView.findViewById(R.id.news_time);
            news_image = (ImageView) itemView.findViewById(R.id.news_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.news_title.setText(list.get(position).getNewstitle());
        holder.news_time.setText(list.get(position).getNewscreatetime());
        holder.news_image.setTag(list.get(position).getNewsimg0());

        Glide.with(context)
                .load(list.get(position).getNewsimg0())
                .asBitmap()
                .placeholder(R.drawable.bg4dp_grey)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String tag = (String) holder.news_image.getTag();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getNewsimg0())) {
                            holder.news_image.setBackground(new BitmapDrawable(resource));   //设置背景
                        }
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", list.get(position));
                Intent intent = new Intent(context, NewsWebActivity.class);
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
