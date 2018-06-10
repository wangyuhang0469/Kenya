package com.example.administrator.kenya.ui.myself.myrelease;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.House;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.ui.city.CityHomeFragment;
import com.example.administrator.kenya.ui.main.BlankFragment;
import com.example.administrator.kenya.view.MyRadioGroup;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MyReleaseActivity extends BaseActivity {

    @Bind(R.id.title_bar)
    AutoRelativeLayout titleBar;
    @Bind(R.id.classification)
    TextView classification;
    @Bind(R.id.xiala)
    ImageView xiala;
    @Bind(R.id.black)
    View black;
    private PopupWindow popupWindow;
    private Fragment[] fragments;
    private MyHouseFragment myHouseFragment;
    private MyUsedFragment myUsedFragment;
    private MyApplyJobFragment myApplyJobFragment;
    private MyRecruitFragment myRecruitFragment;
    private MyLifeFragment myLifeFragment;
    private MyHusbandryFragment myHusbandryFragment;
    private BlankFragment blankFragment7;
    private BuyHouseFragment buyHouseFragment;
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release);
        ButterKnife.bind(this);

        initView();
        initPopupWindow();
    }

    private void initPopupWindow() {
        final View popContentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_myrelease, null);
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
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (checkedId) {
                    case R.id.radioButton1:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[0]).commit();
                        currentTabIndex = 0;
                        classification.setText(getResources().getString(R.string.rental_services));
                        break;
                    case R.id.radioButton2:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[1]).commit();
                        currentTabIndex = 1;
                        classification.setText(getResources().getString(R.string.second_hand_goods));
                        break;
                    case R.id.radioButton3:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[2]).commit();
                        currentTabIndex = 2;
                        classification.setText(getResources().getString(R.string.job_wanted));
                        break;
                    case R.id.radioButton4:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[3]).commit();
                        currentTabIndex = 3;
                        classification.setText(getResources().getString(R.string.recruitment));
                        break;
                    case R.id.radioButton5:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[4]).commit();
                        currentTabIndex = 4;
                        classification.setText(getResources().getString(R.string.domestic_services));
                        break;
                    case R.id.radioButton6:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[5]).commit();
                        currentTabIndex = 5;
                        classification.setText(getResources().getString(R.string.farm_product));
                        break;
                    case R.id.radioButton7:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[6]).commit();
                        currentTabIndex = 6;
                        classification.setText("买房");
                        break;
                    case R.id.radioButton8:
                        fragmentTransaction.hide(fragments[currentTabIndex]).show(fragments[7]).commit();
                        currentTabIndex = 7;
                        classification.setText(getResources().getString(R.string.find_friends));
                        break;
                }

                popupWindow.dismiss();
            }
        });
    }

    protected void initView() {
        myHouseFragment = new MyHouseFragment();
        myUsedFragment = new MyUsedFragment();
        myApplyJobFragment = new MyApplyJobFragment();
        myRecruitFragment = new MyRecruitFragment();
        myLifeFragment = new MyLifeFragment();
        myHusbandryFragment = new MyHusbandryFragment();
        blankFragment7 = new BlankFragment();
        buyHouseFragment = new BuyHouseFragment();
        fragments = new Fragment[]{myHouseFragment, myUsedFragment, myApplyJobFragment, myRecruitFragment, myLifeFragment, myHusbandryFragment, buyHouseFragment, blankFragment7};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragments[0])
                .add(R.id.fragment_container, fragments[1])
                .add(R.id.fragment_container, fragments[2])
                .add(R.id.fragment_container, fragments[3])
                .add(R.id.fragment_container, fragments[4])
                .add(R.id.fragment_container, fragments[5])
                .add(R.id.fragment_container, fragments[6])
                .hide(fragments[6]).hide(fragments[5]).hide(fragments[4]).hide(fragments[3]).hide(fragments[2]).hide(fragments[1])
                .show(fragments[0]).commit();
    }

    @OnClick({R.id.back, R.id.classification})
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
        }
    }


}
