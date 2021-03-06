package com.example.administrator.kenya.ui.city.house;

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
import com.example.administrator.kenya.adapter.HouseAdapter;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.CityProvince;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.buyhouse.DropDownMenu;
import com.example.administrator.kenya.ui.city.buyhouse.GirdDropDownAdapter;
import com.example.administrator.kenya.ui.city.buyhouse.ListDropDownAdapter;
import com.example.administrator.kenya.ui.main.HouseDialog;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.view.MyFootRefreshView;
import com.example.administrator.kenya.view.MyHeadRefreshView;
import com.example.administrator.kenya.view.MyRadioGroup;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.autolayout.AutoLinearLayout;
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

public class HouseActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @Bind(R.id.rootlayout)
    AutoRelativeLayout rootlayout;
    AutoLinearLayout house_type;
    @Bind(R.id.nothing)
    ImageView nothing;
    @Bind(R.id.text)
    TextView text;
    private PostFormBuilder postFormBuilder;
    private int cpageNum = 1;
    private StringCallback StringCallback;
    private StringCallback StringCallbackMore;
    private List<House> housesList = new ArrayList<>();
    private List<CityProvince> cityProvincesList = new ArrayList<>();
    private List<String> cityProvincesListstring = new ArrayList<>();
    private List<View> popupViews = new ArrayList<View>();
    private List<CityProvince> cityNameList = new ArrayList<>();
    private List<String> cityNameListstring = new ArrayList<>();
    private HouseAdapter houseadapter;
    private GirdDropDownAdapter provinceAdapter;
    private ListDropDownAdapter squareAdapter;
    private ListDropDownAdapter moneyAdapter;
    private GirdDropDownAdapter cityAdpter;
    private String[] headers;
    private String[] square;
    private String[] money;
    LoadingDialog loadingDialog;
    View shengshiview;
    View popContentView;
    ListView squareView;
    ListView moneyView;
    ListView provinceView;
    ListView cityView;
    MyRadioGroup myRadioGroup;
    MyRadioGroup myRadioGrouptext;
    String leaseName = "";
    String leaseSquare = "";
    String leaseHome = "";
    String hometype = "";
    String cityprovince = "";
    String cityname = "";
    String price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        ButterKnife.bind(this);
        headers = new String[]{getString(R.string.position), getString(R.string.type), getString(R.string.area), getString(R.string.rent)};
        square = new String[]{getString(R.string.Unlimited), getString(R.string.under_50m2), "50-70sqm", "70-90sqm", "90-110sqm", "110-130sqm", "130-150sqm", "150-200sqm", "200-300sqm", "300-500sqm", getString(R.string.Above_500m2)};
        money = new String[]{getString(R.string.Unlimited), "KSh1000/month", "KSh1000-5000/month", "KSh5000-10000/month", "KSh10000-15000/month", "KSh15000-20000/month", "KSh20000-30000/month", "KSh30000-50000/month", "KSh50000-80000/month", "KSh80000-100000/month", "KSh100000-150000/month", "KSh150000-200000/month", getString(R.string.rent_200000_above)
        };
        initOKHttp();
        initView();
        initProvinceCity();
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
                cityProvincesListstring.add(getString(R.string.Unlimited));
                for (int i = 0; i < cityProvincesList.size(); i++) {
                    String cityprovince = cityProvincesList.get(i).getCityprovince();
                    cityProvincesListstring.add(cityprovince);
                }
            }
        });
    }

    private PostFormBuilder getRequest() {
        return OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/Lease/selectByFile")
                .addParams("leaseName", leaseName)
                .addParams("leaseSquare", leaseSquare)
                .addParams("leasehome", leaseHome)
                .addParams("hometype", hometype)
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
                if (pullToRefreshLayout != null) {
                    List<House> addList = null;
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
                    addList = JSON.parseArray(response, House.class);
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
                if (pullToRefreshLayout != null) {
                    List<House> addList = null;
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
                    addList = JSON.parseArray(response, House.class);
                    housesList.addAll(addList);
                    houseadapter.notifyDataSetChanged();
                    pullToRefreshLayout.finishLoadMore();
                }
                loadingDialog.dismiss();
            }
        };
    }

    //初始化组件
    private void initView() {
        houseadapter = new HouseAdapter(this, housesList);
        shengshiview = this.getLayoutInflater().inflate(R.layout.sheng_shi_choose, null);
        provinceView = shengshiview.findViewById(R.id.lv_sheng);
        cityView = shengshiview.findViewById(R.id.lv_shi);
        provinceAdapter = new GirdDropDownAdapter(this, cityProvincesListstring);
        provinceView.setDividerHeight(0);//设置ListView条目间隔的距离
        provinceView.setAdapter(provinceAdapter);
        //init age menu
        popContentView = LayoutInflater.from(this).inflate(R.layout.house_type, null);
        house_type = popContentView.findViewById(R.id.ll_buy_house_type);
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
                leaseSquare = position == 0 ? headers[2] : square[position];
                mDropDownMenu.closeMenu();
                if (position == 0) {
                    leaseSquare = "";
                } else if (position == 1) {
                    // housesquare = position == 0 ? headers[2] : square[position];
                    leaseSquare = "0-50";
                } else if (position == 2) {
                    leaseSquare = "50-70";
                } else if (position == 3) {
                    leaseSquare = "70-90";
                } else if (position == 4) {
                    leaseSquare = "90-110";
                } else if (position == 5) {
                    leaseSquare = "110-130";
                } else if (position == 6) {
                    leaseSquare = "130-150";
                } else if (position == 7) {
                    leaseSquare = "150-200";
                } else if (position == 8) {
                    leaseSquare = "200-300";
                } else if (position == 9) {
                    leaseSquare = "300-500";
                } else if (position == 10) {
                    leaseSquare = "500-0";
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
                        price = "0-1000";
                    } else if (position == 2) {
                        price = "1000-5000";
                    } else if (position == 3) {
                        price = "5000-10000";
                    } else if (position == 4) {
                        price = "10000-15000";
                    } else if (position == 5) {
                        price = "15000-20000";
                    } else if (position == 6) {
                        price = "20000-30000";
                    } else if (position == 7) {
                        price = "30000-50000";
                    } else if (position == 8) {
                        price = "50000-80000";
                    } else if (position == 9) {
                        price = "80000-100000";
                    } else if (position == 10) {
                        price = "100000-150000";
                    } else if (position == 11) {
                        price = "150000-200000";
                    } else if (position == 12) {
                        price = "200000-0";
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
                        leaseHome = "";
                        break;
                    case R.id.radioButton2:
                        mDropDownMenu.closeMenu();
                        leaseHome = "1 Bedroom";
                        break;
                    case R.id.radioButton3:
                        mDropDownMenu.closeMenu();
                        leaseHome = "2 Bedroom";
                        break;
                    case R.id.radioButton4:
                        mDropDownMenu.closeMenu();
                        leaseHome = "3 Bedroom";
                        break;
                    case R.id.radioButton5:
                        mDropDownMenu.closeMenu();
                        leaseHome = "4 Bedroom";
                        break;
                    case R.id.radioButton6:
                        mDropDownMenu.closeMenu();
                        leaseHome = "5 Bedroom";
                        break;
                    case R.id.radioButton7:
                        mDropDownMenu.closeMenu();
                        leaseHome = "6 Bedroom";
                        break;
                    case R.id.radioButton8:
                        mDropDownMenu.closeMenu();
                        leaseHome = "Villas";
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
                        hometype = "";
                        house_type.setVisibility(View.GONE);
                        mDropDownMenu.closeMenu();
                        leaseHome = "";
                        break;
                    case R.id.rg_tv2:
                        hometype = "Residence";
                        house_type.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rg_tv3:
                        hometype = "Office Building";
                        leaseHome = "";
                        house_type.setVisibility(View.GONE);
                        mDropDownMenu.closeMenu();
                        break;
                    case R.id.rg_tv4:
                        hometype = "Factory";
                        leaseHome = "";
                        house_type.setVisibility(View.GONE);
                        mDropDownMenu.closeMenu();
                        break;
                }
                cpageNum = 1;
                getRequest().build().execute(StringCallback);
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

    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                HouseDialog houseDialog = new HouseDialog(this);
                houseDialog.show();
                break;
        }
    }

    @OnClick({R.id.house_search_bar, R.id.tv_search})
    public void onViewClicked2(View view) {
        startActivity(HouseSearchActivity.class, null);
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
                cityAdpter = new GirdDropDownAdapter(HouseActivity.this, cityNameListstring);
                cityView.setAdapter(cityAdpter);
            }
        });
    }

}
