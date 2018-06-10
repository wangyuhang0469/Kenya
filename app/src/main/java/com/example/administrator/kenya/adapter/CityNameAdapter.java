package com.example.administrator.kenya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.kenya.R;

import java.util.List;

/**
 * Created by 123 on 2018/6/4.
 */

public class CityNameAdapter extends BaseAdapter {

    Context context;
    List<String> list;

    public CityNameAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_city_name, null);
        TextView city_name = v.findViewById(R.id.city_name);
        for (int i = 0; i < list.size(); i++) {
            city_name.setText(list.get(position));
        }
        return v;
    }
}
