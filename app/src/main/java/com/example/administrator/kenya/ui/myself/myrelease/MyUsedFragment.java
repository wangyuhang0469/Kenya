package com.example.administrator.kenya.ui.myself.myrelease;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.DeleteSuccessfulListener;
import com.example.administrator.kenya.ui.city.used.GoodsDetailsActivity;
import com.example.administrator.kenya.ui.main.DeleteDialog;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyUsedFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;


    private MyUsedAdapter myUsedAdapter;
    private List<Goods> goodsList = new ArrayList<>();

    private int cpageNum = 1;

    private PostFormBuilder postFormBuilder;
    private StringCallback stringCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_used, container, false);
        ButterKnife.bind(this, view);

        initOKHttp();
        initView();
        postFormBuilder.addParams("pn", cpageNum + "").build().execute(stringCallback);

        return view;
    }


    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/selectByUserId")
                .addParams("pn", cpageNum + "")
                .addParams("Type", "二手")
                .addParams("userid", User.getInstance().getUserId());

        stringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    toast("加载失败");
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("kang", "111111" + response);
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    List<Goods> addList = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("code").equals("000")) {
                            pullToRefreshLayout.setCanLoadMore(true);
                        } else {
                            pullToRefreshLayout.setCanLoadMore(false);
                        }
                        response = jsonObject.getString("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    addList = JSON.parseArray(response, Goods.class);
                    goodsList.addAll(addList);
                    myUsedAdapter.notifyDataSetChanged();
                    if (goodsList.size() == 0) {
                        nothing.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                    } else {
                        nothing.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                    }

                    pullToRefreshLayout.finishLoadMore();
                }
            }
        };

    }

    //初始化组件
    private void initView() {

        myUsedAdapter = new MyUsedAdapter(goodsList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myUsedAdapter);
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);

        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
                postFormBuilder.addParams("pn", cpageNum + "").tag(this).build().execute(stringCallback);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public class MyUsedAdapter extends RecyclerView.Adapter<MyUsedAdapter.ViewHolder> {


        private List<Goods> list;

        public MyUsedAdapter(List<Goods> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView goodsname, goodsphone, goodsprice, delete;
            ImageView goodsimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                goodsname = (TextView) itemView.findViewById(R.id.goodsname);
                goodsphone = (TextView) itemView.findViewById(R.id.goodsphone);
                goodsprice = (TextView) itemView.findViewById(R.id.goodsprice);
                delete = (TextView) itemView.findViewById(R.id.delete);
                goodsimgs = (ImageView) itemView.findViewById(R.id.goodsimgs);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mygoods, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.goodsname.setText(list.get(position).getGoodsname());
            holder.goodsphone.setText("手机：" + list.get(position).getGoodsphone());
            holder.goodsprice.setText("$" + list.get(position).getGoodsprice());

//            holder.goodsimgs.setTag(list.get(position).getGoodsimgs());

            Glide.with(getContext())
                    .load(AppConstants.BASE_URL + list.get(position).getGoodsimgs())
                    .centerCrop()
                    .placeholder(R.drawable.img_loading)
                    .into(holder.goodsimgs);

//                    .asBitmap()
//                    .placeholder(R.drawable.bg4dp_grey)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            String tag = (String) holder.goodsimgs.getTag();
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getGoodsimgs())) {
//                                holder.goodsimgs.setBackground(new BitmapDrawable(resource));   //设置背景
//                            }
//                        }
//                    });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", list.get(position));
                    startActivity(GoodsDetailsActivity.class, bundle);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Type", "二手");
                    params.put("id", list.get(position).getGoodsid());
                    DeleteDialog deleteDialog = new DeleteDialog(getContext(), AppConstants.BASE_URL + "/kenya/user/deleteByUserId", params);
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
}
