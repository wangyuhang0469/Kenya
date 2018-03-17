package com.example.administrator.kenya;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.ui.city.CityHomeFragment;
import com.example.administrator.kenya.ui.main.BlankFragment;
import com.example.administrator.kenya.ui.main.IntroActivity;
import com.example.administrator.kenya.ui.main.WelcomeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tab_1)
    LinearLayout tab1;
    @Bind(R.id.tab_2)
    LinearLayout tab2;
    @Bind(R.id.tab_3)
    LinearLayout tab3;
    @Bind(R.id.tab_4)
    LinearLayout tab4;

    private CityHomeFragment cityHomeFragment;
    private BlankFragment blankFragment2;
    private BlankFragment blankFragment3;
    private BlankFragment blankFragment4;
    private Fragment[] fragments;
    private LinearLayout[] linearLayouts;
    public int index;
    private int currentTabIndex;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //isFirstStart();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        log("aaaaaaaaaaaaaaaaaaaa");

    }


    protected void initView() {
        linearLayouts = new LinearLayout[4];
        linearLayouts[0] = tab1;
        linearLayouts[1] = tab2;
        linearLayouts[2] = tab3;
        linearLayouts[3] = tab4;
        linearLayouts[0].setSelected(true);

        cityHomeFragment = new CityHomeFragment();
        blankFragment2 = new BlankFragment();
        blankFragment3 = new BlankFragment();
        blankFragment4 = new BlankFragment();
        fragments = new Fragment[]{cityHomeFragment, blankFragment2, blankFragment3, blankFragment4};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, cityHomeFragment)
                .add(R.id.fragment_container, blankFragment2)
                .add(R.id.fragment_container, blankFragment3)
                .add(R.id.fragment_container, blankFragment4)
                .hide(blankFragment4).hide(blankFragment3).hide(blankFragment2)
                .show(cityHomeFragment).commit();
    }


    public void onTabSelect(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }

        linearLayouts[currentTabIndex].setSelected(false);
        linearLayouts[index].setSelected(true);
        currentTabIndex = index;
    }


    // 判断是否第一次启动来决定跳转不同的欢迎界面
    private void isFirstStart() {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

        if (isFirstStart) {

            startActivity(IntroActivity.class, null);

            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean("firstStart", false);
            e.apply();
        } else {
            startActivity(WelcomeActivity.class, null);
        }
    }

    //按两次返回键退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_1:
                index = 0;
                onTabSelect(index);
                break;
            case R.id.tab_2:
                index = 1;
                onTabSelect(index);
                break;
            case R.id.tab_3:
                index = 2;
                onTabSelect(index);
                break;
            case R.id.tab_4:
                index = 3;
                onTabSelect(index);
                break;
        }
    }


}
