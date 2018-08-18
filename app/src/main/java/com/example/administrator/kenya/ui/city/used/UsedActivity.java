package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.CityProvince;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.buyhouse.DropDownMenu;
import com.example.administrator.kenya.ui.city.buyhouse.GirdDropDownAdapter;
import com.example.administrator.kenya.ui.city.buyhouse.ListDropDownAdapter;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.ui.main.LoadingDialog;
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

public class UsedActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.rootlayout)
    AutoRelativeLayout rootlayout;
    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;
    private MyAdapter myAdapter;
    private List<Goods> goodsList = new ArrayList<>();
    private int cpageNum = 1;
    private PostFormBuilder postFormBuilder;
    private StringCallback StringCallback;
    private StringCallback StringCallbackMore;

    private List<CityProvince> cityProvincesList = new ArrayList<>();
    private List<String> cityProvincesListstring = new ArrayList<>();
    private List<View> popupViews = new ArrayList<View>();
    private List<String> cityNameListstring = new ArrayList<>();
    private List<CityProvince> cityNameList = new ArrayList<>();
    View shengshiview;
    View popContentView;
    ListView squareView;
    ListView moneyView;
    ListView provinceView;
    ListView cityView;
    ListView popContentView2;
    private GirdDropDownAdapter provinceAdapter;
    MyRadioGroup myRadioGroup;
    MyRadioGroup myRadioGrouptext;
    private ListDropDownAdapter squareAdapter;
    private ListDropDownAdapter moneyAdapter;
    private GirdDropDownAdapter cityAdpter;
    private ListDropDownAdapter popContentView2adapter;
    private String[] headers;
    private String[] types;
    private String[] money;
    LoadingDialog loadingDialog;
    String cityprovince = "";
    String cityname = "";
    String goodsPrice = "";
    String goodsType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used);
        ButterKnife.bind(this);
        headers = new String[]{getString(R.string.position), getString(R.string.type), getString(R.string.Price)};
        types = new String[]{getString(R.string.Unlimited), getString(R.string.Mobile_phone_digital), getString(R.string.Household_appliances), getString(R.string.foodstuff), getString(R.string.Clothes_shoes), getString(R.string.vehicle), getString(R.string.general_merchandise), getString(R.string.Daily_chemical), getString(R.string.books), getString(R.string.other)};
        money = new String[]{getString(R.string.Unlimited), "KSh500", "KSh500-1000", "KSh1000-2000", "KSh2000-3000", "KSh3000-5000", "KSh5000-10000", "KSh10000-20000", "KSh20000-30000", "KSh30000-50000", "KSh50000-80000", "KSh80000-100000", getString(R.string.above_100000)};
        initOKHttp();
        initView();
        initProvinceCity();
        initData();
        initEvent();
        getRequest().addParams("pn", cpageNum + "").build().execute(StringCallback);
    }

    private PostFormBuilder getRequest() {
        return OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/Goods/selectByFile")
                .addParams("goodsType", goodsType)
                .addParams("cityprovince", cityprovince)
                .addParams("cityname", cityname)
                .addParams("goodsPrice", goodsPrice);
    }

    private void initOKHttp() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    toast(getResources().getString(R.string.load_fail));
                    e.printStackTrace();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                log(response);
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    List<Goods> addList = null;
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
                    addList = JSON.parseArray(response, Goods.class);
                    goodsList.clear();
                    goodsList.addAll(addList);
                    myAdapter.notifyDataSetChanged();
                    if (goodsList.size() == 0) {
                        nothing.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                    } else {
                        nothing.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                    }
                    pullToRefreshLayout.finishLoadMore();
                }
                loadingDialog.dismiss();
            }
        };
        StringCallbackMore = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    toast(getResources().getString(R.string.load_fail));
                    e.printStackTrace();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                //防止因Activity释放导致内部控件空指针
                if (pullToRefreshLayout != null) {
                    List<Goods> addList = null;
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
                    addList = JSON.parseArray(response, Goods.class);
                    goodsList.addAll(addList);
                    myAdapter.notifyDataSetChanged();
                    pullToRefreshLayout.finishLoadMore();
                }
                loadingDialog.dismiss();
            }
        };
    }

    //初始化组件
    private void initView() {
        myAdapter = new MyAdapter(goodsList);
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
        //init money
        moneyView = new ListView(this);
        moneyView.setDividerHeight(0);
        moneyAdapter = new ListDropDownAdapter(this, Arrays.asList(money));
        moneyView.setAdapter(moneyAdapter);
    }

    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                startActivity(GoodsReleaseActivity.class, null);
                break;
        }
    }

    @OnClick({R.id.search_bar, R.id.tv_search})
    public void onViewClicked2(View view) {
        startActivity(UsedSearchActivity.class, null);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Goods> list;

        public MyAdapter(List<Goods> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView goodsname, goodsphone, goodsprice, call, goods_type, goodsaddress;
            ImageView goodsimgs;

            public ViewHolder(View itemView) {
                super(itemView);
                goodsname = (TextView) itemView.findViewById(R.id.goodsname);
                goodsphone = (TextView) itemView.findViewById(R.id.goodsphone);
                goodsprice = (TextView) itemView.findViewById(R.id.goodsprice);
                call = (TextView) itemView.findViewById(R.id.call);
                goodsimgs = (ImageView) itemView.findViewById(R.id.goodsimgs);
                goods_type = itemView.findViewById(R.id.goods_type);
                goodsaddress = itemView.findViewById(R.id.goodsaddress);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.goodsname.setText(list.get(position).getGoodsname());
            holder.goodsphone.setText(getResources().getString(R.string.phone_no_) + list.get(position).getGoodsphone());
            holder.goodsprice.setText("KSh " + list.get(position).getGoodsprice());
            holder.goods_type.setText(list.get(position).getGoodstype());
            holder.goodsaddress.setText(list.get(position).getGoodsaddress());
//            holder.goodsimgs.setTag(list.get(position).getGoodsimgs());
//            holder.goodsimgs.setBackgroundResource(R.drawable.bg4dp_grey);

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CallPhoneDialog(UsedActivity.this, list.get(position).getGoodsphone()).show();
                }
            });
            Glide.with(UsedActivity.this)
                    .load(AppConstants.BASE_URL + list.get(position).getGoodsimgs())
                    .centerCrop()
                    .placeholder(R.drawable.img_loading1)
                    .into(holder.goodsimgs);

//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            String tag = (String) holder.goodsimgs.getTag();
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && tag != null && tag.equals(list.get(position).getGoodsimgs())) {
//                                holder.goodsimgs.setBackground(new BitmapDrawable(resource));   //设置背景
//                            }
//                        }
//                    });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", list.get(position));
                    startActivity(GoodsDetailsActivity.class, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
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

    private void initData() {
        //init popupViews
        popupViews.add(shengshiview);
        popupViews.add(popContentView2);
//        popupViews.add(squareView);
        popupViews.add(moneyView);
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
                cpageNum++;
                getRequest().addParams("pn", cpageNum + "").build().execute(StringCallbackMore);
            }
        });
        rootlayout.removeView(pullToRefreshLayout);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, pullToRefreshLayout);
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
                    getRequest().build().execute(StringCallback);
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
                cpageNum = 1;
                getRequest().build().execute(StringCallback);
            }
        });
        popContentView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popContentView2adapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : types[position]);
                // textView.append(position == 0 ? headers[2] : square[position] + "\n");
                if (position == 0) {
                    goodsType = "";
                } else {
                    goodsType = position == 0 ? headers[2] : types[position];
                    if (goodsType.equals(getString(R.string.Mobile_phone_digital))) {
                        goodsType = "Mobile phone digital";
                    } else if (goodsType.equals(getString(R.string.Household_appliances))) {
                        goodsType = "Domestic appliance";
                    } else if (goodsType.equals(getString(R.string.foodstuff))) {
                        goodsType = "Foodstuff";
                    } else if (goodsType.equals(getString(R.string.Clothes_shoes))) {
                        goodsType = "Clothes,Shoes.";
                    } else if (goodsType.equals(getString(R.string.vehicle))) {
                        goodsType = "Vehicle";
                    } else if (goodsType.equals(getString(R.string.general_merchandise))) {
                        goodsType = "General merchandise";
                    } else if (goodsType.equals(getString(R.string.Daily_chemical))) {
                        goodsType = "Daily chemical";
                    } else if (goodsType.equals(getString(R.string.books))) {
                        goodsType = "books";
                    } else if (goodsType.equals(getString(R.string.other))) {
                        goodsType = "Other";
                    } else {
                    }
                }
                mDropDownMenu.closeMenu();
                cpageNum = 1;
                getRequest().build().execute(StringCallback);
            }
        });
        moneyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moneyAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : money[position]);
                // textView.append(position == 0 ? headers[2] : sexs[position] + "\n");
                mDropDownMenu.closeMenu();
                if (position == 0) {
                    goodsPrice = "";
                } else {
                    // price = position == 0 ? headers[3] : money[position];
                    if (position == 1) {
                        goodsPrice = "0-500";
                    } else if (position == 2) {
                        goodsPrice = "500-1000";
                    } else if (position == 3) {
                        goodsPrice = "1000-2000";
                    } else if (position == 4) {
                        goodsPrice = "2000-3000";
                    } else if (position == 5) {
                        goodsPrice = "3000-5000";
                    } else if (position == 6) {
                        goodsPrice = "5000-10000";
                    } else if (position == 7) {
                        goodsPrice = "10000-20000";
                    } else if (position == 8) {
                        goodsPrice = "20000-30000";
                    } else if (position == 9) {
                        goodsPrice = "30000-50000";
                    } else if (position == 10) {
                        goodsPrice = "50000-80000";
                    } else if (position == 11) {
                        goodsPrice = "80000-100000";
                    } else if (position == 12) {
                        goodsPrice = "100000-0";
                    } else {
                    }
                }
                cpageNum = 1;
                getRequest().build().execute(StringCallback);
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
                        //Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                cityAdpter = new GirdDropDownAdapter(UsedActivity.this, cityNameListstring);
                cityView.setAdapter(cityAdpter);
            }
        });
    }
}
