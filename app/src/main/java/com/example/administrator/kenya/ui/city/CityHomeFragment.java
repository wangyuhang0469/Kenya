package com.example.administrator.kenya.ui.city;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.News;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.BannerItemClickListener;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.city.findmoney.FindMoneyActivity;
import com.example.administrator.kenya.ui.city.friends.FriendsActivity;
import com.example.administrator.kenya.ui.city.house.HouseActivity;
import com.example.administrator.kenya.ui.city.husbandry.HusbandryActivity;
import com.example.administrator.kenya.ui.city.job.JobActivity;
import com.example.administrator.kenya.ui.city.life.LifeActivity;
import com.example.administrator.kenya.ui.city.news.NewsWebActivity;
import com.example.administrator.kenya.ui.city.news.NewsinfoActivity;
import com.example.administrator.kenya.ui.city.used.UsedActivity;
import com.example.administrator.kenya.view.TextBannerView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_home, container, false);
        ButterKnife.bind(this, view);

        title.setText(getResources().getString(R.string.same_city));
        back.setVisibility(View.GONE);

        initOKHttp();
        postFormBuilder.addParams("page", 1 + "").build().execute(StringCallback);

        List<String> imageList = new ArrayList<>();
        imageList.add("http://img011.hc360.cn/k1/M0B/24/D0/wKhQwFdjdHSER6NQAAAAADZsFuo401.jpg");
        imageList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2351109249,2100272265&fm=27&gp=0.jpg");
        imageList.add("http://pic.90sjimg.com/design/00/14/38/18/5581393a30663.jpg");

        data = new ArrayList<>();

        initBanner(imageList);
        return view;
    }
    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                toast("点击了" + (position + 1));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.city_home_job, R.id.city_home_used, R.id.city_home_house, R.id.city_home_friends, R.id.city_home_life, R.id.city_home_husbandry, R.id.tv1})
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
                startActivity(FriendsActivity.class, null);
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
                toast("加载失败");
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
                        // Toast.makeText(getActivity(), String.valueOf(position) + "》》》》" + data, Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("news", newsList.get(position));
                        Intent intent = new Intent(getActivity(), NewsWebActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
    }
}
