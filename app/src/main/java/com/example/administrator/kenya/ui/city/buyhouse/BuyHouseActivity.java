package com.example.administrator.kenya.ui.city.buyhouse;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.BuyHouseAdapter;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.BuyHouse;
import com.example.administrator.kenya.classes.CityProvince;
import com.example.administrator.kenya.classes.Friend;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.main.BuyHouseDialog;
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

public class BuyHouseActivity extends BaseActivity {
    @Bind(R.id.buy_house_release)
    TextView buyHouseRelease;
    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.rootlayout)
    AutoRelativeLayout rootlayout;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;
    private PostFormBuilder postFormBuilder;
    private int cpageNum = 1;
    private StringCallback StringCallback;
    private StringCallback StringCallbackMore;
    private List<BuyHouse> housesList = new ArrayList<>();
    private BuyHouseAdapter houseadapter;
    private List<Friend> friendList;
    private List<CityProvince> cityProvincesList = new ArrayList<>();
    private List<String> cityProvincesListstring = new ArrayList<>();
    private List<CityProvince> cityNameList = new ArrayList<>();
    private List<String> cityNameListstring = new ArrayList<>();
    private List<View> popupViews = new ArrayList<View>();
    private GirdDropDownAdapter provinceAdapter;
    private GirdDropDownAdapter cityAdpter;
    private ListDropDownAdapter typeAdapter;
    private ListDropDownAdapter squareAdapter;
    private ListDropDownAdapter moneyAdapter;
    private String[] headers;
    private String[] square;
    private String[] money;
    LoadingDialog loadingDialog;
    View shengshiview;
    View housetypeview;
    View popContentView;
    MyRadioGroup myRadioGroup;
    MyRadioGroup myRadioGrouptext;
    ListView provinceView;
    ListView typeView;
    ListView squareView;
    ListView moneyView;
    ListView cityView;
    String cityprovince = "";
    String cityname = "";
    String housetype = "";
    String houseName = "";
    String housesquare = "";
    String househome = "";
    String price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_house);
        ButterKnife.bind(this);
        headers = new String[]{getString(R.string.position), getString(R.string.type), getString(R.string.area), getString(R.string.total)};
        square = new String[]{getString(R.string.Unlimited), getString(R.string.under_50m2), "50-70sqm", "70-90sqm", "90-110sqm", "110-130sqm", "130-150sqm", "150-200sqm", "200-30sqm", "300-500sqm", getString(R.string.Above_500m2)};
        money = new String[]{getString(R.string.Unlimited), getString(R.string.under_ksh100000), "KSh100000-200000", "KSh200000-300000", "KSh200000-300000", "KSh300000-400000", "KSh400000-500000", "KSh500000-600000", "KSh600000-700000", "KSh700000-800000", "KSh800000-900000", getString(R.string.above__ksh1000000)};
        initOKHttp();
        initProvinceCity();
        initView();
        initData();
        initEvent();
        getRequest().addParams("pn", cpageNum + "").build().execute(StringCallback);
    }

    //init province
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
                cityProvincesListstring.add((String) getResources().getText(R.string.Unlimited));
                for (int i = 0; i < cityProvincesList.size(); i++) {
                    String cityprovince = cityProvincesList.get(i).getCityprovince();
                    cityProvincesListstring.add(cityprovince);
                }
            }
        });
    }

    private PostFormBuilder getRequest() {
        return OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/House/findByHouse")
                .addParams("houseName", houseName)
                .addParams("houseSquare", housesquare)
                .addParams("househome", househome)
                .addParams("housetype", housetype)
                .addParams("cityprovince", cityprovince)
                .addParams("cityname", cityname)
                .addParams("Price", price);
    }

    private void initOKHttp() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    toast(getResources().getString(R.string.load_fail));
                    e.printStackTrace();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (pullToRefreshLayout != null) {
                    List<BuyHouse> addList = null;
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
                    addList = JSON.parseArray(response, BuyHouse.class);
                    housesList.clear();
                    housesList.addAll(addList);
                    houseadapter.notifyDataSetChanged();
                    if (housesList.size() == 0) {
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
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.finishLoadMore();
                    toast(getResources().getString(R.string.load_fail));
                    e.printStackTrace();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (pullToRefreshLayout != null) {
                    List<BuyHouse> addList = null;
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
                    addList = JSON.parseArray(response, BuyHouse.class);
                    housesList.addAll(addList);
                    houseadapter.notifyDataSetChanged();
                    pullToRefreshLayout.finishLoadMore();
                }
                loadingDialog.dismiss();
            }
        };
    }

    private void initView() {
        houseadapter = new BuyHouseAdapter(this, housesList);
        shengshiview = this.getLayoutInflater().inflate(R.layout.sheng_shi_choose, null);
        provinceView = shengshiview.findViewById(R.id.lv_sheng);
        cityView = shengshiview.findViewById(R.id.lv_shi);
        provinceAdapter = new GirdDropDownAdapter(this, cityProvincesListstring);
        provinceView.setDividerHeight(0);//设置ListView条目间隔的距离
        provinceView.setAdapter(provinceAdapter);
        //init age menu
        popContentView = LayoutInflater.from(this).inflate(R.layout.buy_house_type, null);
        myRadioGroup = (MyRadioGroup) popContentView.findViewById(R.id.myRadioGroup);
        myRadioGrouptext = (MyRadioGroup) popContentView.findViewById(R.id.myRadioGroupText);
        //init square
        squareView = new ListView(this);
        squareView.setDividerHeight(0);
        squareAdapter = new ListDropDownAdapter(this, Arrays.asList(square));
        squareView.setAdapter(squareAdapter);
        //init money
        moneyView = new ListView(this);
        moneyView.setDividerHeight(0);
        moneyAdapter = new ListDropDownAdapter(this, Arrays.asList(money));
        moneyView.setAdapter(moneyAdapter);
    }

    @OnClick({R.id.buy_house_back, R.id.buy_house_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buy_house_back:
                finish();
                break;
            case R.id.buy_house_release:
                BuyHouseDialog buyHouseDialog = new BuyHouseDialog(this);
                buyHouseDialog.show();
                break;
        }
    }

    private void initData() {
        //init popupViews
        popupViews.add(shengshiview);
        popupViews.add(popContentView);
        popupViews.add(squareView);
        popupViews.add(moneyView);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(houseadapter);
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
     * 设置四个条件选择的点击事件
     */
    private void initEvent() {
        popContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        //add item click event
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
        squareView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                squareAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : square[position]);
                // textView.append(position == 0 ? headers[2] : square[position] + "\n");
                housesquare = position == 0 ? headers[2] : square[position];
                mDropDownMenu.closeMenu();
                if (position == 0) {
                    housesquare = "";
                } else if (position == 1) {
                    // housesquare = position == 0 ? headers[2] : square[position];
                    housesquare = "0-50";
                } else if (position == 2) {
                    housesquare = "50-70";
                } else if (position == 3) {
                    housesquare = "70-90";
                } else if (position == 4) {
                    housesquare = "90-110";
                } else if (position == 5) {
                    housesquare = "110-130";
                } else if (position == 6) {
                    housesquare = "130-150";
                } else if (position == 7) {
                    housesquare = "150-200";
                } else if (position == 8) {
                    housesquare = "200-300";
                } else if (position == 9) {
                    housesquare = "300-500";
                } else if (position == 10) {
                    housesquare = "500-0";
                } else {
                }
                cpageNum = 1;
                getRequest().build().execute(StringCallback);
            }
        });
        moneyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moneyAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[3] : money[position]);
                // textView.append(position == 0 ? headers[2] : sexs[position] + "\n");
                mDropDownMenu.closeMenu();
                if (position == 0) {
                    price = "";
                } else {
                    // price = position == 0 ? headers[3] : money[position];
                    if (position == 1) {
                        price = "0-100000";
                    } else if (position == 2) {
                        price = "100000-200000";
                    } else if (position == 3) {
                        price = "200000-300000";
                    } else if (position == 4) {
                        price = "300000-400000";
                    } else if (position == 5) {
                        price = "400000-500000";
                    } else if (position == 6) {
                        price = "500000-600000";
                    } else if (position == 7) {
                        price = "600000-700000";
                    } else if (position == 8) {
                        price = "700000-800000";
                    } else if (position == 9) {
                        price = "800000-900000";
                    } else if (position == 10) {
                        price = "900000-1000000";
                    } else if (position == 11) {
                        price = "1000000-0";
                    } else {
                    }
                }
                cpageNum = 1;
                getRequest().build().execute(StringCallback);
            }
        });
        myRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        mDropDownMenu.closeMenu();
                        househome = "";
                        break;
                    case R.id.radioButton2:
                        mDropDownMenu.closeMenu();
                        househome = (String) getResources().getText(R.string.bedroom_1);
                        break;
                    case R.id.radioButton3:
                        mDropDownMenu.closeMenu();
                        househome = (String) getResources().getText(R.string.bedroom_2);
                        break;
                    case R.id.radioButton4:
                        mDropDownMenu.closeMenu();
                        househome = (String) getResources().getText(R.string.bedroom_3);
                        break;
                    case R.id.radioButton5:
                        mDropDownMenu.closeMenu();
                        househome = (String) getResources().getText(R.string.bedroom_4);
                        break;
                    case R.id.radioButton6:
                        mDropDownMenu.closeMenu();
                        househome = (String) getResources().getText(R.string.bedroom_5);
                        break;
                    case R.id.radioButton7:
                        mDropDownMenu.closeMenu();
                        househome = (String) getResources().getText(R.string.bedroom_6);
                        break;
                    case R.id.radioButton8:
                        mDropDownMenu.closeMenu();
                        househome = (String) getResources().getText(R.string.villas);
                        break;
                }
                cpageNum = 1;
                getRequest().build().execute(StringCallback);
            }
        });
        myRadioGrouptext.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_tv1:
                        housetype = "";
                        break;
                    case R.id.rg_tv2:
                        housetype = (String) getResources().getText(R.string.bridal_chamber);
                        break;
                    case R.id.rg_tv3:
                        housetype = (String) getResources().getText(R.string.second_hand_house);
                        break;
                }
                myRadioGroup.setOnCheckedChangeListener(null);
                myRadioGroup.clearCheck();
                myRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.radioButton1:
                                mDropDownMenu.closeMenu();
                                househome = "";
                                break;
                            case R.id.radioButton2:
                                mDropDownMenu.closeMenu();
                                househome = (String) getResources().getText(R.string.Room_1);
                                break;
                            case R.id.radioButton3:
                                mDropDownMenu.closeMenu();
                                househome = (String) getResources().getText(R.string.Room_1_Hall);
                                break;
                            case R.id.radioButton4:
                                mDropDownMenu.closeMenu();
                                househome = (String) getResources().getText(R.string.Room_1_Hall_2);
                                break;
                            case R.id.radioButton5:
                                mDropDownMenu.closeMenu();
                                househome = (String) getResources().getText(R.string.Room_2_Hall_2);
                                break;
                            case R.id.radioButton6:
                                mDropDownMenu.closeMenu();
                                househome = (String) getResources().getText(R.string.Room_1_Hall_3);
                                break;
                            case R.id.radioButton7:
                                mDropDownMenu.closeMenu();
                                househome = (String) getResources().getText(R.string.Room_2_Hall_3);
                                break;
                            case R.id.radioButton8:
                                mDropDownMenu.closeMenu();
                                househome = (String) getResources().getText(R.string.Room_1_Hall_4);
                                break;
                        }
                        cpageNum = 1;
                        getRequest().build().execute(StringCallback);
                    }
                });
            }
        });
    }
    /**
     * 监听点击回退按钮事件
     */
    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
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
                cityNameListstring.add((String) getResources().getText(R.string.Unlimited));
                for (int i = 0; i < cityNameList.size(); i++) {
                    String cityname = cityNameList.get(i).getCityname();
                    cityNameListstring.add(cityname);
                }
                cityAdpter = new GirdDropDownAdapter(BuyHouseActivity.this, cityNameListstring);
                cityView.setAdapter(cityAdpter);
            }
        });
    }

    @OnClick({R.id.buy_house_search_bar, R.id.buy_house_tv_search})
    public void onViewClicked2(View view) {
        startActivity(BuyHouseSearchActivity.class, null);
    }
}
