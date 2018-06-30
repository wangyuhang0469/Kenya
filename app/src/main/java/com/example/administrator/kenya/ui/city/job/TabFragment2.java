package com.example.administrator.kenya.ui.city.job;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.activity.AdvertyinfoActivity;
import com.example.administrator.kenya.adapter.CompanyAdapter;
import com.example.administrator.kenya.classes.Company;
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
import butterknife.OnClick;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;
    private String mParam1;

    private CompanyAdapter companyAdapter;
    private List<Company> companyList;
    protected boolean isVisible;

    //请求数据参数
    private PostFormBuilder postFormBuilder;
    private int cpageNum = 1;
    private StringCallback StringCallback;


    public static TabFragment2 newInstance(String param1) {
        TabFragment2 fragment = new TabFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        ButterKnife.bind(this, myview);
        companyList = new ArrayList<>();
        initOKHttp();
        initView();
        postFormBuilder.addParams("currPage", cpageNum + "").build().execute(StringCallback);
        return myview;
    }


    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/recruit/pageQuery")
                .addParams("currPage", cpageNum + "");

        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    Toast.makeText(getActivity(), getString(R.string.load_fail), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    List<Company> addList = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("code").equals("000")) {
                            pullToRefreshLayout.setCanLoadMore(true);
                        } else {
                            pullToRefreshLayout.setCanLoadMore(false);
                        }
                        response = jsonObject.getString("lists");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    addList = JSON.parseArray(response, Company.class);
                    companyList.addAll(addList);
                    companyAdapter.notifyDataSetChanged();
                    if (companyList.size() == 0) {
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
        companyAdapter = new CompanyAdapter(getActivity(), companyList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(companyAdapter);
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
                postFormBuilder.addParams("currPage", cpageNum + "").tag(this).build().execute(StringCallback);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.city_home_job_fr2_release)
    public void onViewClicked() {
        if (isVisible = true) {
            Intent intent = new Intent(this.getActivity(), AdvertyinfoActivity.class);
            startActivity(intent);
        } else {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }
}
