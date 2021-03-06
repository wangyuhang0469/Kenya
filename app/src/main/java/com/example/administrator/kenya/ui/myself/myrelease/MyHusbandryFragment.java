package com.example.administrator.kenya.ui.myself.myrelease;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.Husbandry;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnSuccessfulListener;
import com.example.administrator.kenya.ui.city.husbandry.HusbandryDetailsActivity;
import com.example.administrator.kenya.ui.main.DeleteDialog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyHusbandryFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;

    private String keyword = "不限";

    private int cpageNum = 1;

    private MyHusbandryAdapter myHusbandryAdapter;
    private List<Husbandry> husbandryList = new ArrayList<>();
    private PostFormBuilder postFormBuilder;
    private StringCallback StringCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_husbandry, container, false);
        ButterKnife.bind(this, view);

        initView();
        initOKHttp();
        postFormBuilder.addParams("pn", cpageNum + "").build().execute(StringCallback);

        return view;
    }

    //初始化组件
    private void initView() {

        myHusbandryAdapter = new MyHusbandryAdapter(husbandryList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myHusbandryAdapter);
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);
        pullToRefreshLayout.setHeaderView(new MyHeadRefreshView(getContext()));
        pullToRefreshLayout.setFooterView(new MyFootRefreshView(getContext()));
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
                postFormBuilder.addParams("framType", keyword).addParams("pn", cpageNum + "").build().execute(StringCallback);
            }
        });
    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/selectByUserId")
                .addParams("pn", cpageNum + "")
                .addParams("Type", "农林牧")
                .addParams("userid", User.getInstance().getUserId());

        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    toast(getString(R.string.load_fail));
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    List<Husbandry> addList = null;
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

                    addList = JSON.parseArray(response, Husbandry.class);
                    husbandryList.addAll(addList);
                    if (husbandryList.size() == 0) {
                        nothing.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                    } else {
                        nothing.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                    }
                    myHusbandryAdapter.notifyDataSetChanged();


                    pullToRefreshLayout.finishLoadMore();
                }
            }
        };

    }

    public class MyHusbandryAdapter extends RecyclerView.Adapter<MyHusbandryAdapter.ViewHolder> {


        private List<Husbandry> list;

        public MyHusbandryAdapter(List<Husbandry> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView framname, framphone, delete;
            ImageView framimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                framname = (TextView) itemView.findViewById(R.id.framname);
                framphone = (TextView) itemView.findViewById(R.id.framphone);
                delete = (TextView) itemView.findViewById(R.id.delete);
                framimgs = (ImageView) itemView.findViewById(R.id.framimgs);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myhusbandry, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.framname.setText(list.get(position).getFramname());
            holder.framphone.setText(getString(R.string.phone_no_) + list.get(position).getFramphone());
//            holder.framimgs.setTag(list.get(position).getFramimgs());
//
            Glide.with(getContext())
                    .load(AppConstants.BASE_URL + list.get(position).getFramimgs())
                    .centerCrop()
                    .placeholder(R.drawable.img_loading1)
                    .into(holder.framimgs);

//                    .asBitmap()
//                    .placeholder(R.drawable.bg4dp_grey)
//                    .centerCrop()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            String tag = (String) holder.framimgs.getTag();
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getFramimgs())) {
//                                holder.framimgs.setBackground(new BitmapDrawable(resource));   //设置背景
//                            }
//                        }
//                    });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Husbandry", list.get(position));
                    startActivity(HusbandryDetailsActivity.class, bundle);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Type", "农林牧");
                    params.put("id", list.get(position).getFramid());
                    DeleteDialog deleteDialog = new DeleteDialog(getContext(), AppConstants.BASE_URL + "/kenya/user/deleteByUserId", params);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
