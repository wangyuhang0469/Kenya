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
import com.example.administrator.kenya.adapter.ProjectAdapter;
import com.example.administrator.kenya.classes.Project2;
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
public class FindMoneyFragment2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    //请求数据参数
    private PostFormBuilder postFormBuilder;
    private int cpageNum = 1;
    private com.zhy.http.okhttp.callback.StringCallback StringCallback;

    private List<Project2> projectList;

    private ProjectAdapter projectAdapter;

    public static FindMoneyFragment2 newInstance(String param1) {
        FindMoneyFragment2 fragment = new FindMoneyFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FindMoneyFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_money_fragment2, container, false);
        ButterKnife.bind(this, view);
        projectList = new ArrayList<>();
        initOKHttp();
        initView();
        postFormBuilder.addParams("pn", cpageNum + "").build().execute(StringCallback);
        return view;
    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/Project/selectByFile")
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
                //防止因Activity释放导致内部控件空指针
                Log.d("kang", "11111" + response);
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    List<Project2> addList = null;
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
                    addList = JSON.parseArray(response, Project2.class);
                    projectList.addAll(addList);
                    projectAdapter.notifyDataSetChanged();
                    pullToRefreshLayout.finishLoadMore();
                }
            }
        };
    }

    //初始化组件
    private void initView() {
        projectAdapter = new ProjectAdapter(getActivity(), projectList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(projectAdapter);
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
