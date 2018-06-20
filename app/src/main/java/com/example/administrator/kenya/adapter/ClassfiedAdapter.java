package com.example.administrator.kenya.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.Classified;
import com.example.administrator.kenya.classes.Transition;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.main.PreviewDialog;
import com.example.administrator.kenya.ui.news.DetailsDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class ClassfiedAdapter extends RecyclerView.Adapter<ClassfiedAdapter.ViewHolder> {

    private List<Classified> list;
    private Context context;

    public ClassfiedAdapter(Context context, List<Classified> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classified, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(context)
                .load(AppConstants.YJIP + list.get(position).getImg0())
                .centerCrop()
                .placeholder(R.drawable.img_loading3)
                .into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> url = new ArrayList<String>();
                url.add(AppConstants.YJIP + list.get(position).getImg0());
                new PreviewDialog(context ,url , 0 ).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
