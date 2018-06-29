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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.LifeServices;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnSuccessfulListener;
import com.example.administrator.kenya.ui.city.life.LifeDetailsActivity;
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
public class MyLifeFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;

    private PopupWindow popupWindow;
    private String keyword = "不限";

    private int cpageNum = 1;

    private MyLifeAdapter myLifeAdapter;
    private List<LifeServices> lifeServicesList = new ArrayList<>();
    private PostFormBuilder postFormBuilder;
    private StringCallback stringCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_life, container, false);
        ButterKnife.bind(this, view);

        initView();

        initOKHttp();
        postFormBuilder.addParams("livetype", keyword).addParams("pn", cpageNum + "").build().execute(stringCallback);

        return view;
    }


    private void initView() {

        myLifeAdapter = new MyLifeAdapter(lifeServicesList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myLifeAdapter);
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
                postFormBuilder.addParams("liveType", keyword).addParams("pn", cpageNum + "").build().execute(stringCallback);
            }
        });
    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/selectByUserId")
                .addParams("pn", cpageNum + "")
                .addParams("Type", "生活服务")
                .addParams("userid", User.getInstance().getUserId());

        stringCallback = new StringCallback() {
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
                    List<LifeServices> addList = null;
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

                    addList = JSON.parseArray(response, LifeServices.class);
                    lifeServicesList.addAll(addList);
                    myLifeAdapter.notifyDataSetChanged();
                    if (lifeServicesList.size() == 0) {
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

    public class MyLifeAdapter extends RecyclerView.Adapter<MyLifeAdapter.ViewHolder> {

        private List<LifeServices> list;

        public MyLifeAdapter(List<LifeServices> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView livename, livephone, delete;
            ImageView liveimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                livename = (TextView) itemView.findViewById(R.id.livename);
                livephone = (TextView) itemView.findViewById(R.id.livephone);
                delete = (TextView) itemView.findViewById(R.id.delete);
                liveimgs = (ImageView) itemView.findViewById(R.id.liveimgs);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mylifeservies, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.livename.setText(list.get(position).getLivename());
            holder.livephone.setText(getString(R.string.phone_no_) + list.get(position).getLivephone());
//            holder.liveimgs.setTag(list.get(position).getLiveimgs());
//
            Glide.with(getContext())
                    .load(AppConstants.BASE_URL + list.get(position).getLiveimgs())
                    .centerCrop()
                    .placeholder(R.drawable.img_loading1)
                    .into(holder.liveimgs);

//                    .asBitmap()
//                    .placeholder(R.drawable.bg4dp_grey)
//                    .centerCrop()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            String tag = (String) holder.liveimgs.getTag();
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getLiveimgs())) {
//                                holder.liveimgs.setBackground(new BitmapDrawable(resource));   //设置背景
//                            }
//                        }
//                    });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lifeServices", list.get(position));
                    startActivity(LifeDetailsActivity.class, bundle);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Type", "生活服务");
                    params.put("id", list.get(position).getLiveid());
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
