package com.example.administrator.kenya.ui.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.NewsAdapter;
import com.example.administrator.kenya.adapter.TransitionAdapter;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.News;
import com.example.administrator.kenya.classes.Transition;
import com.example.administrator.kenya.constants.AppConstants;
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
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransitionFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;
    private TransitionAdapter transitionAdapter;
    private List<Transition> transitionList = new ArrayList<>();
    private int cpageNum = 1;
    private PostFormBuilder postFormBuilder;
    private StringCallback StringCallback;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transition, container, false);
        ButterKnife.bind(this, view);


        initOKHttp();
        initView();
        postFormBuilder.addParams("page", cpageNum + "").build().execute(StringCallback);

        return view;
    }


    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/funeral/pageQuery")
                .addParams("page", cpageNum + "");

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
                    List<Transition> addList = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("code").equals("000")) {
                            pullToRefreshLayout.setCanLoadMore(true);
                        } else {
                            pullToRefreshLayout.setCanLoadMore(false);
                        }
                        response = jsonObject.getString("rows");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addList = JSON.parseArray(response, Transition.class);
                    if (addList != null)
                        transitionList.addAll(addList);
                    transitionAdapter.notifyDataSetChanged();
                    pullToRefreshLayout.finishLoadMore();
                    pullToRefreshLayout.finishRefresh();
                }
            }
        };
    }

    //初始化组件
    private void initView() {
        transitionAdapter = new TransitionAdapter(getContext(), transitionList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(transitionAdapter);
        pullToRefreshLayout.setCanLoadMore(false);
        pullToRefreshLayout.setHeaderView(new MyHeadRefreshView(getContext()));
        pullToRefreshLayout.setFooterView(new MyFootRefreshView(getContext()));
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                cpageNum = 1;
                transitionList.clear();
                postFormBuilder.addParams("page", cpageNum + "").build().execute(StringCallback);
            }

            @Override
            public void loadMore() {
                postFormBuilder.addParams("page", cpageNum + "").tag(this).build().execute(StringCallback);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
