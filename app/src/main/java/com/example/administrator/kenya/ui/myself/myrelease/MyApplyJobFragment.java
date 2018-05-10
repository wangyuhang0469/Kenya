package com.example.administrator.kenya.ui.myself.myrelease;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.activity.ResumeDetilActivity;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.Job;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnSuccessfulListener;
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
public class MyApplyJobFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;

    private MyJobAdapter myJobAdapter;
    private List<Job> jobList = new ArrayList<>();
    //请求数据参数
    private PostFormBuilder postFormBuilder;
    private int cpageNum = 1;
    private StringCallback stringCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_apply_job, container, false);
        ButterKnife.bind(this, view);

        initOKHttp();
        initView();
        postFormBuilder.addParams("currPage", cpageNum + "").build().execute(stringCallback);
        return view;
    }


    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/user/selectByUserId")
                .addParams("pn", cpageNum + "")
                .addParams("Type", "求职")
                .addParams("userid", User.getInstance().getUserId());

        stringCallback = new StringCallback() {
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
                if (pullToRefreshLayout != null) {
                    cpageNum++;
                    List<Job> addList = null;
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
                    addList = JSON.parseArray(response, Job.class);
                    jobList.addAll(addList);
                    myJobAdapter.notifyDataSetChanged();
                    if (jobList.size() == 0) {
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
        myJobAdapter = new MyJobAdapter(getActivity(), jobList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myJobAdapter);
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


    public class MyJobAdapter extends RecyclerView.Adapter<MyJobAdapter.ViewHolder> {
        private List<Job> list;
        private Context context;

        public MyJobAdapter(Context context, List<Job> list) {
            this.list = list;
            this.context = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView job_want, job_name, job_sex, job_age, delete;
            ImageView job_image;

            public ViewHolder(View itemView) {
                super(itemView);
                job_want = (TextView) itemView.findViewById(R.id.job_want);
                job_name = (TextView) itemView.findViewById(R.id.job_name);
                job_sex = (TextView) itemView.findViewById(R.id.job_sex);
                job_age = (TextView) itemView.findViewById(R.id.job_age);
                delete = (TextView) itemView.findViewById(R.id.delete);
                job_image = (ImageView) itemView.findViewById(R.id.job_image);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myjob, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.job_want.setText(list.get(position).getJobwant());
            holder.job_name.setText(list.get(position).getName());
            holder.job_sex.setText(list.get(position).getSex());
            holder.job_age.setText(list.get(position).getAge() + "岁");
//            holder.job_image.setTag(list.get(position).getHeadimg());
            Glide.with(context)
                    .load(AppConstants.BASE_URL + list.get(position).getHeadimg())
                    .centerCrop()
                    .placeholder(R.drawable.img_loading1)
                    .into(holder.job_image);
//                    .asBitmap()
//                    .placeholder(R.drawable.bg4dp_grey)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            String tag = (String) holder.job_image.getTag();
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getHeadimg())) {
//                                holder.job_image.setBackground(new BitmapDrawable(resource));   //设置背景
//                            }
//                        }
//                    });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("job", list.get(position));
                    Intent intent = new Intent(context, ResumeDetilActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Type", "求职");
                    params.put("id", list.get(position).getJobid());
                    DeleteDialog deleteDialog = new DeleteDialog(context, AppConstants.BASE_URL + "/kenya/user/deleteByUserId", params);
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
