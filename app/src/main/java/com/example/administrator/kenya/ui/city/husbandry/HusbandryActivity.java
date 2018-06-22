package com.example.administrator.kenya.ui.city.husbandry;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Husbandry;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.view.MyFootRefreshView;
import com.example.administrator.kenya.view.MyHeadRefreshView;
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
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;


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
        pullToRefreshLayout.setHeaderView(new MyHeadRefreshView(this));
        pullToRefreshLayout.setFooterView(new MyFootRefreshView(this));
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
                    toast(getResources().getString(R.string.load_fail));
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
                    if (husbandryList.size() == 0) {
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


    private void initPopupWindow() {
        final View popContentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_husbandry, null);
        popupWindow = new PopupWindow(popContentView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);


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
                if (keyword.equals("不限")) {
                    keyword = "No Experience";
                } else if (keyword.equals("动植物种苗")) {
                    keyword = "Pets ＆ Seeds";
                } else if (keyword.equals("农作物")) {
                    keyword = "Crops";
                } else if (keyword.equals("畜禽养殖")) {
                    keyword = "Livestock ＆ Poultry";
                } else if (keyword.equals("农机设备")) {
                    keyword = "Agricultural Machinery";
                } else if (keyword.equals("农药肥料")) {
                    keyword = "Pesticides ＆ Fertilizer";
                } else if (keyword.equals("其他")) {
                    keyword = "Others";
                } else {
                }
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
            TextView framname, framphone, call;
            ImageView framimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                framname = (TextView) itemView.findViewById(R.id.framname);
                framphone = (TextView) itemView.findViewById(R.id.framphone);
                call = (TextView) itemView.findViewById(R.id.call);
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
            holder.framphone.setText(getResources().getString(R.string.phone_no_) + list.get(position).getFramphone());
//            holder.framimgs.setTag(list.get(position).getFramimgs());
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CallPhoneDialog(HusbandryActivity.this, list.get(position).getFramphone()).show();
                }
            });
//
            Glide.with(HusbandryActivity.this)
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
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

    }
}
