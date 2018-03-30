package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Goods;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsedActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private MyAdapter myAdapter;
    private List<Goods> goodsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used);
        ButterKnife.bind(this);


        goodsList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Goods goods = new Goods();
            goods.setTitle("二手IPone6S 9成新");
            goodsList.add(goods);
        }

        myAdapter = new MyAdapter(goodsList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myAdapter);
    }

    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                startActivity(GoodsReleaseActivity.class, null);
                break;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        private List<Goods> list;

        public MyAdapter(List<Goods> list) {
            this.list = list;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.goodsTitle.setText(list.get(position).getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(GoodsDetailsActivity.class, null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

    }
}
