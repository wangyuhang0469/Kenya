package com.example.administrator.kenya.ui.city;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.Banners;
import com.example.administrator.kenya.classes.Funds;
import com.example.administrator.kenya.classes.News;
import com.example.administrator.kenya.classes.Project2;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.BannerItemClickListener;
import com.example.administrator.kenya.interfaces.MyLocationListener;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.city.buyhouse.BuyHouseActivity;
import com.example.administrator.kenya.ui.city.buyhouse.ProvinceCityDetailsActivity;
import com.example.administrator.kenya.ui.city.findmoney.FindMoneyActivity;
import com.example.administrator.kenya.ui.city.findmoney.FindMonydetailActivity;
import com.example.administrator.kenya.ui.city.findmoney.FindProjectdetailActivity;
import com.example.administrator.kenya.ui.city.friends.FriendsActivity;
import com.example.administrator.kenya.ui.city.house.HouseActivity;
import com.example.administrator.kenya.ui.city.husbandry.HusbandryActivity;
import com.example.administrator.kenya.ui.city.job.JobActivity;
import com.example.administrator.kenya.ui.city.life.LifeActivity;
import com.example.administrator.kenya.ui.city.news.NewsWebActivity;
import com.example.administrator.kenya.ui.city.news.NewsinfoActivity;
import com.example.administrator.kenya.ui.city.used.UsedActivity;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.ui.main.YesOrNoDialog;
import com.example.administrator.kenya.utils.MyLocationUtil;
import com.example.administrator.kenya.view.TextBannerView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.example.administrator.kenya.MyApplication.MyApplication.mContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityHomeFragment extends BaseFragment {
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.marqueeView1)
    TextBannerView marqueeView1;
    private PostFormBuilder postFormBuilder;
    private StringCallback StringCallback;
    List<News> newsList = new ArrayList<>();
    List<String> data;
    //轮播图
    List<Banners> BannersList = new ArrayList<>();
    List<String> imageList;
    
    //定义一个list，用于存储需要申请的权限
    ArrayList<String> permissionList = new ArrayList<String>();
    public static final int MY_PERMISSIONS_REQUEST = 3000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_home, container, false);
        ButterKnife.bind(this, view);


        permissionList.add(Manifest.permission.CAMERA);
        permissionList.add(Manifest.permission.RECORD_AUDIO);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        EventBus.getDefault().register(this);
        title.setText(getResources().getString(R.string.same_city));
        back.setVisibility(View.GONE);
        initOKHttp();
        postFormBuilder.addParams("page", 1 + "").build().execute(StringCallback);
        imageList = new ArrayList<>();
        data = new ArrayList<>();
        initOkhttpBanner();

        MyLocationUtil.getInstance(getContext()).getLocationInformation(new MyLocationListener() {
            @Override
            public void success(String province, String city) {
                toast(province + city);
            }

            @Override
            public void failed(String message) {
                toast(message);
            }
        });
        return view;
    }

    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (BannersList.get(position).getCategory().equals("funds-1")) {
                    final LoadingDialog loadingDialog = new LoadingDialog(getContext());
                    loadingDialog.show();
                    OkHttpUtils.get().url(AppConstants.BASE_URL + "/kenya/Funds/selectById?fundsid=" + BannersList.get(position).getCategoryId()).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            toast(getString(R.string.load_fail));
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("000")) {
                                    Funds funds = JSON.parseObject(jsonObject.getString("result"), Funds.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("funds", funds);
                                    startActivity(FindMonydetailActivity.class, bundle);
                                } else {
                                    toast(jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                } else if (BannersList.get(position).getCategory().equals("project-1")) {
                    final LoadingDialog loadingDialog = new LoadingDialog(getContext());
                    loadingDialog.show();
                    OkHttpUtils.get().url(AppConstants.BASE_URL + "/kenya/Project/selectById?projectid=" + BannersList.get(position).getCategoryId()).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            toast(R.string.load_fail);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("000")) {
                                    Project2 project2 = JSON.parseObject(jsonObject.getString("result"), Project2.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("project2", project2);
                                    startActivity(FindProjectdetailActivity.class, bundle);
                                } else {
                                    toast(jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                } else if (BannersList.get(position).getCategory().equals("url")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("content", BannersList.get(position).getContent());
                    startActivity(NewsWebActivity.class, bundle);
                } else {
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.city_home_job, R.id.city_home_used, R.id.city_home_house, R.id.city_home_friends, R.id.city_home_life, R.id.city_home_husbandry, R.id.tv1, R.id.city_home_buy_house})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Bundle bundle = new Bundle();
                bundle.putString("findmoney", "A");
                startActivity(FindMoneyActivity.class, bundle);
                break;
            case R.id.btn2:
                Bundle bundle1 = new Bundle();
                bundle1.putString("findmoney", "");
                startActivity(FindMoneyActivity.class, bundle1);
                break;
            case R.id.city_home_job:
                startActivity(JobActivity.class, null);
                break;
            case R.id.city_home_used:
                startActivity(UsedActivity.class, null);
                break;
            case R.id.city_home_house:
                startActivity(HouseActivity.class, null);
                break;
            case R.id.city_home_friends:
                // startActivity(FriendsActivity.class, null);
                toast(getString(R.string.under_development));
                break;
            case R.id.city_home_life:
                startActivity(LifeActivity.class, null);
                break;
            case R.id.tv1:
                startActivity(NewsinfoActivity.class, null);
                break;
            case R.id.city_home_husbandry:
                startActivity(HusbandryActivity.class, null);
                break;
            case R.id.city_home_buy_house:
                startActivity(BuyHouseActivity.class, null);
                break;
        }
    }

    private void initOKHttp() {
        postFormBuilder = OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/news/pageQuery")
                .addParams("page", 1 + "");
        StringCallback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //防止因Activity释放导致内部控件空指针
                toast(getString(R.string.load_fail));
            }

            @Override
            public void onResponse(String response, int id) {
                //防止因Activity释放导致内部控件空指针
                List<News> addList = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("000")) {
                    } else {
                    }
                    response = jsonObject.getString("rows");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addList = JSON.parseArray(response, News.class);
                newsList.addAll(addList);
                for (int i = 0; i < addList.size(); i++) {
                    String str = addList.get(i).getNewstitle();
                    data.add(str);
                }
                marqueeView1.setDatas(data);
                marqueeView1.setItemOnClickListener(new BannerItemClickListener() {
                    @Override
                    public void onItemClick(String data, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("news", newsList.get(position));
                        startActivity(NewsWebActivity.class, bundle);
                    }
                });
            }
        };
    }

    private void initOkhttpBanner() {
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/content/pageQuery")
                .addParams("page", 1 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toast(getString(R.string.load_fail));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        List<Banners> addList = null;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            response = jsonObject.getString("rows");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BannersList = JSON.parseArray(response, Banners.class);
                        if (BannersList != null) {
                            for (int i = 0; i < BannersList.size(); i++) {
                                imageList.add(AppConstants.BASE_URL + BannersList.get(i).getPic());
                            }
                            initBanner(imageList);
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        marqueeView1.stopViewAnimator();
    }

    @Override
    public void onResume() {
        super.onResume();
        marqueeView1.startViewAnimator();
    }

    @Subscribe
    public void onEvent(String str) {
        if (marqueeView1 != null) {
            if (str.equals("1")) {
                marqueeView1.stopViewAnimator();
            } else {
                marqueeView1.startViewAnimator();
            }
        }
    }
    // @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST: {
//                //If request is cancelled, the result arrays are empty.
//                int length = grantResults.length;
//                boolean re_request = false;//标记位：如果需要重新授予权限，true；反之，false。
//                for (int i = 0; i < length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                        Log.d("kang", "权限授予成功:" + permissions[i]);
//                    } else {
//                        Log.e("kang", "权限授予失败:" + permissions[i]);
//                        re_request = true;
//                    }
//                }
//                if (re_request) {
//                    //弹出对话框，提示用户重新授予权限
//                    //关于弹出自定义对话框，可以查看本博文开头的知识扩展
//                    final YesOrNoDialog permissionDialog = new YesOrNoDialog(mContext);
//                    permissionDialog.setCanceledOnTouchOutside(false);
//                    permissionDialog.setMeesage("请授予相关权限，否则程序无法运行。\n\n点击确定，重新授予权限。\n点击取消，立即终止程序。\n");
//                    permissionDialog.setCallback(new YesOrNoDialog.YesOrNoDialogCallback() {
//                        @Override
//                        public void onClickButton(YesOrNoDialog.ClickedButton button, String message) {
//                            if (button == YesOrNoDialog.ClickedButton.POSITIVE) {
//                                permissionDialog.dismiss();
//                                //此处需要弹出手动修改权限的系统界面
//                                checkAndRequestPermissions(permissionList);
//                            } else if (button == YesOrNoDialog.ClickedButton.NEGATIVE) {
//                                permissionDialog.dismiss();
//                            }
//                        }
//                    });
//                    permissionDialog.show();
//                }
//                break;
//            }
//            default:
//                break;
//        }
//    }
//    private void checkAndRequestPermissions(ArrayList<String> permissionList) {
//
//        ArrayList<String> list = new ArrayList<>(permissionList);
//        Iterator<String> it = list.iterator();
//        while (it.hasNext()) {
//            String permission = it.next();
//            //检查权限是否已经申请
//            int hasPermission = ContextCompat.checkSelfPermission(this.getContext(), permission);
//            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
//                it.remove();
//            }
//        }
//        if (list.size() == 0) {
//            return;
//        }
//        String[] permissions = list.toArray(new String[0]);
//        //正式请求权限
//        ActivityCompat.requestPermissions(this.getActivity(), permissions, MY_PERMISSIONS_REQUEST);
//    }
}
