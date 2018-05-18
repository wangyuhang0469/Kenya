package com.example.administrator.kenya.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.classes.Friend;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.ui.city.house.HouseDetailActivity;

import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> list;
    private Context context;

    public FriendAdapter(Context context, List<Friend> list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView goodsTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsTitle = (TextView) itemView.findViewById(R.id.goodsTitle);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.goodsTitle.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
