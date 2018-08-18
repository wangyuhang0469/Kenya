package com.example.administrator.kenya.ui.city.life;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.CityProvince;
import com.example.administrator.kenya.classes.LifeServices;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.buyhouse.DropDownMenu;
import com.example.administrator.kenya.ui.city.buyhouse.GirdDropDownAdapter;
import com.example.administrator.kenya.ui.city.buyhouse.ListDropDownAdapter;
import com.example.administrator.kenya.ui.city.husbandry.HusbandryActivity;
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
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class LifeActivity extends BaseActivity {

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
    @Bind(R.id.rootlayout)
    AutoRelativeLayout rootlayout;
    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;


    private PopupWindow popupWindow;
    private String keyword = "不限";


    private int cpageNum = 1;

    private MyAdapter myAdapter;
    private List<LifeServices> lifeServicesList = new ArrayList<>();



    String cityprovince = "";
    String cityname = "";
    String type = "";

    View shengshiview;
    ListView provinceView;
    ListView cityView;
    ListView popContentView2;


    private GirdDropDownAdapter cityAdpter;
    private ListDropDownAdapter popContentView2adapter;
    private GirdDropDownAdapter provinceAdapter;

    private List<CityProvince> cityProvincesList = new ArrayList<>();
    private List<String> cityProvincesListstring = new ArrayList<>();
    private List<View> popupViews = new ArrayList<View>();
    private List<String> cityNameListstring = new ArrayList<>();
    private List<CityProvince> cityNameList = new ArrayList<>();

    private String[] headers;
    private String[] types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        ButterKnife.bind(this);

        headers = new String[]{getString(R.string.position), getString(R.string.type)};
        types = new String[]{getString(R.string.Unlimited), getString(R.string.cleaning_service), getString(R.string.moving_service), getString(R.string.repairing_services), getString(R.string.recycle_items), getString(R.string.others)};

        initView();

        initProvinceCity();

//        initPopupWindow();

        initEvent();

        loadList();

    }

    //初始化组件
    private void initView() {
        myAdapter = new MyAdapter(lifeServicesList);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(myAdapter);


        shengshiview = this.getLayoutInflater().inflate(R.layout.sheng_shi_choose, null);
        provinceView = shengshiview.findViewById(R.id.lv_sheng);
        cityView = shengshiview.findViewById(R.id.lv_shi);
        provinceAdapter = new GirdDropDownAdapter(this, cityProvincesListstring);
        provinceView.setDividerHeight(0);//设置ListView条目间隔的距离
        provinceView.setAdapter(provinceAdapter);
//init type menu
        popContentView2 = new ListView(this);
        popContentView2.setDividerHeight(0);
        popContentView2adapter = new ListDropDownAdapter(this, Arrays.asList(types));
        popContentView2.setAdapter(popContentView2adapter);


        //init popupViews
        popupViews.add(shengshiview);
        popupViews.add(popContentView2);



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
                loadList();
            }
        });

        rootlayout.removeView(pullToRefreshLayout);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, pullToRefreshLayout);
    }

    private void loadList() {
        log(type);
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/Live/findByLive")
                .addParams("livetype", type)
                .addParams("pn", cpageNum + "")
                .addParams("cityprovince",  cityprovince)
                .addParams("cityname", cityname)
                .build()
                .execute(new StringCallback() {
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
                        log(response);
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
                            myAdapter.notifyDataSetChanged();
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
                });


    }

    private void initPopupWindow() {
        final View popContentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_life, null);
        popupWindow = new PopupWindow(popContentView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                black.setVisibility(View.GONE);
                xiala.setImageDrawable(getResources().getDrawable(R.drawable.icon_xiala0));
                classification.setTextColor(getResources().getColor(R.color.textBlack2));
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
                } else if (keyword.equals("保洁")) {
                    keyword = "Cleaning Service";
                } else if (keyword.equals("搬家")) {
                    keyword = "Moving Service";
                } else if (keyword.equals("维修")) {
                    keyword = "Repairing Services";
                } else if (keyword.equals("回收")) {
                    keyword = "Recycle Items";
                } else if (keyword.equals("其他")) {
                    keyword = "Others";
                } else {

                }
                replacement();
                loadList();
                popupWindow.dismiss();
            }
        });
    }

    private void replacement(){
        lifeServicesList.clear();
        myAdapter.notifyDataSetChanged();
        cpageNum = 1;
    }


    @OnClick({R.id.back, R.id.classification, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.classification:
                xiala.setImageDrawable(getResources().getDrawable(R.drawable.icon_xiala1));
                classification.setTextColor(getResources().getColor(R.color.textgreen1));
                classification.setClickable(false);
                popupWindow.showAsDropDown(titleBar);
                black.setVisibility(View.VISIBLE);
                break;
            case R.id.release:
                startActivity(LifeReleaseActivity.class, null);
                break;
        }
    }


    /**
     * 设置条件选择的点击事件
     */
    private void initEvent() {
        provinceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provinceAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : cityProvincesListstring.get(position));
                if (position == 0) {
                    mDropDownMenu.closeMenu();
                    cityprovince = "";
                    cityname = "";
                    cpageNum = 1;
                    replacement();
                    loadList();
                } else {

                    cityprovince = position == 0 ? headers[0] : cityProvincesListstring.get(position);
                }
                //textView.append(position == 0 ? headers[0] : citys[position] + "\n");
                initcityname(cityProvincesListstring.get(position));
            }
        });
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDropDownMenu.setTabText(i == 0 ? headers[0] : cityView.getItemAtPosition(i).toString());
                cityAdpter.setCheckItem(i);
                mDropDownMenu.closeMenu();
                if (i == 0) {
                    cityname = "";
                } else {
                    cityname = i == 0 ? headers[0] : cityView.getItemAtPosition(i).toString();
                }
                replacement();
//                getRequest().build().execute(StringCallback);
                loadList();
            }
        });
        popContentView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popContentView2adapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : types[position]);
                // textView.append(position == 0 ? headers[2] : square[position] + "\n");
                if (position == 0) {
                    type = "";
                } else {
                    type = position == 0 ? headers[2] : types[position];

                    if (type.equals("保洁")) {
                        type = "Cleaning Service";
                    } else if (type.equals("搬家")) {
                        type = "Moving Service";
                    } else if (type.equals("维修")) {
                        type = "Repairing Services";
                    } else if (type.equals("回收")) {
                        type = "Recycle Items";
                    } else if (type.equals("其他")) {
                        type = "Others";
                    }



                }
                mDropDownMenu.closeMenu();
                replacement();

                loadList();
            }
        });

    }


    //init city
    private void initcityname(String cityprovince) {
        cityNameListstring.clear();
        cityNameList.clear();
        OkHttpUtils.get().url(AppConstants.BASE_URL + "/kenya/city/findByCityprovince?cityprovince=" + cityprovince).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toast(getString(R.string.load_fail));
                // Toast.makeText(context, "城市名称获取失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                List<CityProvince> addList = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("000")) {
                    } else {
                        toast(jsonObject.getString("message"));
                    }
                    response = jsonObject.getString("result");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addList = JSON.parseArray(response, CityProvince.class);
                cityNameList.addAll(addList);
                cityNameListstring.add(getString(R.string.Unlimited));
                for (int i = 0; i < cityNameList.size(); i++) {
                    String cityname = cityNameList.get(i).getCityname();
                    cityNameListstring.add(cityname);
                }
                cityAdpter = new GirdDropDownAdapter(LifeActivity.this, cityNameListstring);
                cityView.setAdapter(cityAdpter);
            }
        });
    }


    private void initProvinceCity() {
        OkHttpUtils.get().url(AppConstants.BASE_URL + "/kenya/city/findByCountCityprovince").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toast(getString(R.string.load_fail));
            }

            @Override
            public void onResponse(String response, int id) {
                List<CityProvince> addList = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("000")) {
                    } else {
                        toast(jsonObject.getString("message"));
                    }
                    response = jsonObject.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addList = JSON.parseArray(response, CityProvince.class);
                cityProvincesList.addAll(addList);
                cityProvincesListstring.add(getString(R.string.Unlimited));
                for (int i = 0; i < cityProvincesList.size(); i++) {
                    String cityprovince = cityProvincesList.get(i).getCityprovince();
                    cityProvincesListstring.add(cityprovince);
                }
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<LifeServices> list;

        public MyAdapter(List<LifeServices> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView livename, livephone, call;
            ImageView liveimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                livename = (TextView) itemView.findViewById(R.id.livename);
                livephone = (TextView) itemView.findViewById(R.id.livephone);
                call = (TextView) itemView.findViewById(R.id.call);
                liveimgs = (ImageView) itemView.findViewById(R.id.liveimgs);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lifeservies, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.livename.setText(list.get(position).getLivename());
            holder.livephone.setText(getResources().getString(R.string.phone_no_) + list.get(position).getLivephone());
//            holder.liveimgs.setTag(list.get(position).getLiveimgs());

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CallPhoneDialog(LifeActivity.this, list.get(position).getLivephone()).show();
                }
            });
//
            Glide.with(LifeActivity.this)
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
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }
}
