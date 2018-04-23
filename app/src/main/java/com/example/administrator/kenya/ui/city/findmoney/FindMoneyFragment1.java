package com.example.administrator.kenya.ui.city.findmoney;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.FundsAdapter;
import com.example.administrator.kenya.classes.Funds;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.constants.AppConstants;
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
public class FindMoneyFragment1 extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    private String mParam1;

    private FundsAdapter fundsAdapter;
    private List<Funds> fundsList;

    //请求数据参数
    private PostFormBuilder postFormBuilder;
    private int cpageNum = 1;
    private com.zhy.http.okhttp.callback.StringCallback StringCallback;

    public static FindMoneyFragment1 newInstance(String param1) {
        FindMoneyFragment1 fragment = new FindMoneyFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    public FindMoneyFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_money_fragment1, container, false);
        ButterKnife.bind(this, view);

        fundsList = new ArrayList<>();
        initOKHttp();
        initView();
        postFormBuilder.addParams("pn", cpageNum + "").build().execute(StringCallback);

        return view;
    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/Funds/selectbyfile")
                .addParams("pn", cpageNum + "");

        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("kang", "111111" + response);
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    List<Funds> addList = null;
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
                    addList = JSON.parseArray(response, Funds.class);
                    fundsList.addAll(addList);
                    fundsAdapter.notifyDataSetChanged();
                    pullToRefreshLayout.finishLoadMore();
                }
            }
        };

    }

    //初始化组件
    private void initView() {
        fundsAdapter = new FundsAdapter(getActivity(), fundsList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(fundsAdapter);
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
                postFormBuilder.addParams("pn", cpageNum + "").tag(this).build().execute(StringCallback);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
