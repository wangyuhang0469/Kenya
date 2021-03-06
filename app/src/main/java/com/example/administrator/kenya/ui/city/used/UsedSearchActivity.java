package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.view.MyFootRefreshView;
import com.example.administrator.kenya.view.MyHeadRefreshView;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

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
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;

    private MyAdapter myAdapter;
    private List<Goods> goodsList = new ArrayList<>();

    private int cpageNum = 1;
    private String lastKeyword = "";

    private PostFormBuilder postFormBuilder;
    private StringCallback StringCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_search);
        ButterKnife.bind(this);

        //自动 弹出输入法切不挤压原布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        initOKHttp();

        initView();

    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/Goods/selectByFile")
                .addParams("pn", cpageNum + "")
                .addParams("goodsName", keyword.getText().toString());


        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    toast(getResources().getString(R.string.load_fail));
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    lastKeyword = keyword.getText().toString();
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
                    myAdapter.notifyDataSetChanged();
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

    private void initView() {
        myAdapter = new MyAdapter(goodsList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myAdapter);
        keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    searchEvent();
                }
                //关闭软键盘
                hideSoftInput(keyword.getWindowToken());
                return true;
            }
        });
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);
        pullToRefreshLayout.setHeaderView(new MyHeadRefreshView(this));
        pullToRefreshLayout.setFooterView(new MyFootRefreshView(this));
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
                postFormBuilder.addParams("pn", cpageNum + "").addParams("goodsName", lastKeyword).build().execute(StringCallback);
            }
        });
    }

    private void replacement() {
        goodsList.clear();
        cpageNum = 1;
    }

    private void searchEvent() {
        if (keyword.getText().length() == 0) {
            toast(getResources().getString(R.string.enter_keyword_first));
        } else if (lastKeyword.equals(keyword.getText().toString())) {
        } else {
            replacement();
            postFormBuilder.addParams("pn", cpageNum + "").addParams("goodsName", keyword.getText().toString()).build().execute(StringCallback);
        }
    }


    @OnClick({R.id.back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_search:
                searchEvent();
                //关闭软键盘
                hideSoftInput(keyword.getWindowToken());
                break;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<Goods> list;

        public MyAdapter(List<Goods> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView goodsname, goodsphone, goodsprice, call;
            ImageView goodsimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                goodsname = (TextView) itemView.findViewById(R.id.goodsname);
                goodsphone = (TextView) itemView.findViewById(R.id.goodsphone);
                goodsprice = (TextView) itemView.findViewById(R.id.goodsprice);
                call = (TextView) itemView.findViewById(R.id.call);
                goodsimgs = (ImageView) itemView.findViewById(R.id.goodsimgs);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.goodsname.setText(list.get(position).getGoodsname());
            holder.goodsphone.setText(getResources().getString(R.string.phone_no_) + list.get(position).getGoodsphone());
            holder.goodsprice.setText("KSh " + list.get(position).getGoodsprice());
//            holder.goodsimgs.setTag(list.get(position).getGoodsimgs());

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CallPhoneDialog(UsedSearchActivity.this, list.get(position).getGoodsphone()).show();
                }
            });

            Glide.with(UsedSearchActivity.this)
                    .load(AppConstants.BASE_URL + list.get(position).getGoodsimgs())
                    .centerCrop()
                    .placeholder(R.drawable.img_loading1)
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
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

    }

}
