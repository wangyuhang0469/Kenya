package com.example.administrator.kenya.ui.city.job;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.JobAdapter;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Job;
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
import butterknife.OnClick;
import okhttp3.Call;

public class JobSearchActivity extends BaseActivity {
    @Bind(R.id.keyword)
    EditText keyword;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    private PostFormBuilder postFormBuilder;
    private int cpageNum = 1;
    private String lastKeyword = "";
    private List<Job> jobList = new ArrayList<>();
    private JobAdapter jobAdapter;
    private StringCallback StringCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        initView();
        initOKHttp();
    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/jobSeeker/pageQuery")
                .addParams("currPage", cpageNum + "")
                .addParams("pagetext", keyword.getText().toString());

        StringCallback = new StringCallback() {
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
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    lastKeyword = keyword.getText().toString();
                    List<Job> addList = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("code").equals("000")) {
                            pullToRefreshLayout.setCanLoadMore(true);
                        } else if (jsonObject.getString("totalCount").equals("0")) {
                            toast("暂时还没有求职者求职相关职位");
                        } else {
                            pullToRefreshLayout.setCanLoadMore(false);
                        }
                        response = jsonObject.getString("lists");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addList = JSON.parseArray(response, Job.class);
                    jobList.addAll(addList);
                    jobAdapter.notifyDataSetChanged();
                    pullToRefreshLayout.finishLoadMore();
                }
            }
        };
    }

    //初始化组件
    private void initView() {
        jobAdapter = new JobAdapter(this, jobList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(jobAdapter);

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
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
                postFormBuilder.addParams("currPage", cpageNum + "").addParams("pagetext", lastKeyword).build().execute(StringCallback);
            }
        });
    }

    private void replacement() {
        jobList.clear();
        cpageNum = 1;
    }

    private void searchEvent() {
        if (keyword.getText().length() == 0) {
            toast("请输入搜索内容");
        } else if (lastKeyword.equals(keyword.getText().toString())) {
        } else {
            replacement();
            postFormBuilder.addParams("currPage", cpageNum + "").addParams("pagetext", keyword.getText().toString()).build().execute(StringCallback);
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
}
