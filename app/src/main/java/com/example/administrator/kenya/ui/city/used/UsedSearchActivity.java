package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Goods;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class UsedSearchActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.keyword)
    EditText keyword;

    private MyAdapter myAdapter;
    private List<Goods> goodsList;

    private int cpageNum = 1;
    private String lastKeyword="";

    private PostFormBuilder postFormBuilder;
    private StringCallback StringCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_search);
        ButterKnife.bind(this);


        goodsList = new ArrayList<>();


        myAdapter = new MyAdapter(goodsList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myAdapter);

        initOKHttp();

        initPullToRresh();

    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url("http://192.168.1.101:8080/kenya/Goods/selectByFile")
                .addParams("pn", cpageNum + "")
                .addParams("goodsName", keyword.getText().toString());


        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                pullToRefreshLayout.finishLoadMore();
                toast("加载失败");
                if (cpageNum == 1)
                    pullToRefreshLayout.setCanLoadMore(false);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                cpageNum++;
                lastKeyword = keyword.getText().toString();
                log(cpageNum + response);

                List<Goods> addList = JSON.parseArray(response, Goods.class);

                if (addList == null) {
                    toast("加载失败");
                } else if (addList.size() > 0) {
                    goodsList.addAll(addList);
                    myAdapter.notifyDataSetChanged();
                    pullToRefreshLayout.setCanLoadMore(true);
                } else if (cpageNum == 2){
                    pullToRefreshLayout.setCanLoadMore(false);
                    toast("未查询到数据");
                    myAdapter.notifyDataSetChanged();
                }else {
                    pullToRefreshLayout.setCanLoadMore(false);
                    toast("已是最后一页");
                }

                pullToRefreshLayout.finishLoadMore();
            }
        };

    }

    private void replacement() {
        goodsList.clear();
        cpageNum = 1;
        pullToRefreshLayout.setCanLoadMore(true);
    }


    private void initPullToRresh() {
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);

        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
                postFormBuilder.addParams("pn", cpageNum + "").addParams("goodsName", keyword.getText().toString()).build().execute(StringCallback);
            }
        });
    }

    @OnClick({R.id.back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_search:
                if (keyword.getText().length() == 0){
                    toast("请输入搜索内容");
                }else if (lastKeyword.equals(keyword.getText().toString())){
                    postFormBuilder.addParams("pn", cpageNum + "").addParams("goodsName", keyword.getText().toString()).build().execute(StringCallback);
                }else {
                    replacement();
                    postFormBuilder.addParams("pn", cpageNum + "").addParams("goodsName", keyword.getText().toString()).build().execute(StringCallback);
                }
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
            holder.goodsTitle.setText(list.get(position).getGoodsname());

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
