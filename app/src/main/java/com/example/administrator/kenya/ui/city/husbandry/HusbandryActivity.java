package com.example.administrator.kenya.ui.city.husbandry;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Husbandry;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.life.LifeDetailsActivity;
import com.example.administrator.kenya.ui.city.life.LifeReleaseActivity;
import com.example.administrator.kenya.view.MyRadioGroup;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.autolayout.AutoRelativeLayout;
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

public class HusbandryActivity extends BaseActivity {

    @Bind(R.id.title_bar)
    AutoRelativeLayout titleBar;
    @Bind(R.id.black)
    View black;
    @Bind(R.id.classification)
    TextView classification;
    @Bind(R.id.xiala)
    ImageView xiala;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;


    private PopupWindow popupWindow;
    private String keyword = "不限";


    private int cpageNum = 1;

    private MyAdapter myAdapter;
    private List<Husbandry> husbandryList = new ArrayList<>();
    private PostFormBuilder postFormBuilder;
    private StringCallback StringCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husbandry);
        ButterKnife.bind(this);

        initView();

        initOKHttp();

        initPopupWindow();

        postFormBuilder.addParams("framType", keyword).addParams("pn", cpageNum + "").build().execute(StringCallback);
    }

    //初始化组件
    private void initView() {

        myAdapter = new MyAdapter(husbandryList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myAdapter);
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);

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
                .url(AppConstants.BASE_URL + "/kenya/Fram/selectbytype")
                .addParams("framType", keyword)
                .addParams("pn", cpageNum + "");

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
                    myAdapter.notifyDataSetChanged();


                    pullToRefreshLayout.finishLoadMore();
                }
            }
        };

    }


    private void initPopupWindow() {
        final View popContentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_husbandry, null);
        popupWindow = new PopupWindow(popContentView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                black.setVisibility(View.GONE);
                xiala.setImageDrawable(getDrawable(R.drawable.icon_xiala0));
                //解决点击分类按钮 关闭且迅速打开问题
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        classification.setClickable(true);
                    }
                }, 100);
            }
        });

        MyRadioGroup myRadioGroup = (MyRadioGroup) popContentView.findViewById(R.id.myRadioGroup);
        myRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) popContentView.findViewById(checkedId);
                keyword = radioButton.getText().toString();
                husbandryList.clear();
                myAdapter.notifyDataSetChanged();
                cpageNum = 1;
                postFormBuilder.addParams("framType", keyword).addParams("pn", cpageNum + "").build().execute(StringCallback);
                popupWindow.dismiss();
            }
        });
    }


    @OnClick({R.id.back, R.id.classification, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.classification:
                xiala.setImageDrawable(getDrawable(R.drawable.icon_xiala1));
                classification.setClickable(false);
                popupWindow.showAsDropDown(titleBar);
                black.setVisibility(View.VISIBLE);
                break;
            case R.id.release:
                startActivity(HusbandryReleaseActivity.class, null);
                break;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        private List<Husbandry> list;

        public MyAdapter(List<Husbandry> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView framname, framphone;
            ImageView framimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                framname = (TextView) itemView.findViewById(R.id.framname);
                framphone = (TextView) itemView.findViewById(R.id.framphone);
                framimgs = (ImageView) itemView.findViewById(R.id.framimgs);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_husbandry, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.framname.setText(list.get(position).getFramname());
            holder.framphone.setText("手机：" + list.get(position).getFramphone());
//            holder.framimgs.setTag(list.get(position).getFramimgs());
//
            Glide.with(HusbandryActivity.this)
                    .load(AppConstants.BASE_URL + list.get(position).getFramimgs())
                    .centerCrop()
                    .placeholder(R.drawable.img_loading)
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
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

    }
}
