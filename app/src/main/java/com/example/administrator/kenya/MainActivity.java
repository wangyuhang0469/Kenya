package com.example.administrator.kenya;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnChooseListener;
import com.example.administrator.kenya.ui.car.CallCarFragment;
import com.example.administrator.kenya.ui.city.CityHomeFragment;
import com.example.administrator.kenya.ui.main.BlankFragment;
import com.example.administrator.kenya.ui.main.IntroActivity;
import com.example.administrator.kenya.ui.main.MainNoticeDialog;
import com.example.administrator.kenya.ui.main.MainPosterDialog;
import com.example.administrator.kenya.ui.main.UpdateDialog;
import com.example.administrator.kenya.ui.main.WelcomeActivity;
import com.example.administrator.kenya.ui.myself.MyselfFragment;
import com.example.administrator.kenya.ui.news.NewsFragment;
import com.example.administrator.kenya.utils.APKVersionCodeUtils;
import com.example.administrator.kenya.utils.OpenFileUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;
import okhttp3.Call;

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
    private CallCarFragment blankFragment2;
    private NewsFragment newsFragment;
    private MyselfFragment myselfFragment;
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

        // 第二个参数是状态栏色值, 第三个是兼容5.0到6.0之间状态栏颜色字体只能是白色。
        // 如果沉浸的颜色与状态栏颜色冲突, 设置一层浅色对比能显示出状态栏字体
        // 如果您的项目是6.0以上机型, 推荐使用两个参数的setUseStatusBarColor。
        StatusUtil.setUseStatusBarColor(this, getResources().getColor(R.color.theme), Color.parseColor("#33000000"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色
        StatusUtil.setSystemStatus(this, true, false);

//        initTitle();

        initView();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            getposterOrNotice();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                getposterOrNotice();
            }
        }
    }

//    private void initTitle() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            layoutParams.setMargins(0, StatusUtil.getStatusBarHeight(this), 0, 0);
//            //顶部距离
//            TextView titleView = new TextView(this);
//            titleView.setText("djopwijdioajdad");
//            titleView.setLayoutParams(layoutParams);
//        }
//    }

    protected void initView() {
        linearLayouts = new LinearLayout[4];
        linearLayouts[0] = tab1;
        linearLayouts[1] = tab2;
        linearLayouts[2] = tab3;
        linearLayouts[3] = tab4;
        linearLayouts[0].setSelected(true);

        cityHomeFragment = new CityHomeFragment();
        blankFragment2 = new CallCarFragment();
        newsFragment = new NewsFragment();
        myselfFragment = new MyselfFragment();
        fragments = new Fragment[]{cityHomeFragment, blankFragment2, newsFragment, myselfFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, cityHomeFragment)
                .add(R.id.fragment_container, blankFragment2)
                .add(R.id.fragment_container, newsFragment)
                .add(R.id.fragment_container, myselfFragment)
                .hide(myselfFragment).hide(newsFragment).hide(blankFragment2)
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
//                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                System.exit(0);
                Intent intent = new Intent();
// 为Intent设置Action、Category属性
                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
                startActivity(intent);
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
                StatusUtil.setUseStatusBarColor(this, getResources().getColor(R.color.theme), Color.parseColor("#33000000"));
                break;
            case R.id.tab_2:
                index = 1;
                onTabSelect(index);
                break;
            case R.id.tab_3:
                index = 2;
                onTabSelect(index);
                StatusUtil.setUseStatusBarColor(this, Color.parseColor("#4b4b4b"), Color.parseColor("#33000000"));
                break;
            case R.id.tab_4:
                index = 3;
                onTabSelect(index);
                StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT, Color.parseColor("#33000000"));
                break;
        }
        if (index != 0)
            EventBus.getDefault().postSticky("1");
        else
            EventBus.getDefault().postSticky("0");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
//            startActivity(LoginActivity.class,null);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void getposterOrNotice() {
        log("开始请求");
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/posterOrNotice/query")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            log(response);
                            if (jsonObject.getString("code").equals("000")) {
                                jsonObject = jsonObject.getJSONObject("data");
                                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                String lastPoster = getPrefs.getString("lastPoster", "0");
                                //每个海报只显示一次
                                if (!jsonObject.getString("id").equals(lastPoster)) {
                                    SharedPreferences.Editor e = getPrefs.edit();
                                    e.putString("lastPoster", jsonObject.getString("id"));
                                    e.apply();
                                    //如果是海报则...
                                    if (jsonObject.getString("type").equals("Poster")) {
                                        Glide.with(MainActivity.this)
                                                .load(AppConstants.BASE_URL + jsonObject.getString("img0"))
                                                .asBitmap()
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                        MainPosterDialog mainPosterDialog = new MainPosterDialog(MainActivity.this, resource);
                                                        mainPosterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                            @Override
                                                            public void onDismiss(DialogInterface dialogInterface) {
                                                                getNewVersions();   //执行是否有新版本逻辑
                                                            }
                                                        });
                                                        mainPosterDialog.setCancelable(false);
                                                        mainPosterDialog.show();
                                                    }
                                                });
                                        //如果是通知则...
                                    } else if (jsonObject.getString("type").equals("Notice")) {
                                        MainNoticeDialog mainNoticerDialog = new MainNoticeDialog(MainActivity.this, jsonObject.getString("article"));
                                        mainNoticerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                getNewVersions();     //执行是否有新版本逻辑
                                            }
                                        });
                                        mainNoticerDialog.setCancelable(false);
                                        mainNoticerDialog.show();
                                    }
                                } else {
                                    getNewVersions();    //执行是否有新版本逻辑
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    private void getNewVersions() {
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/version/query")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }


                    @Override
                    public void onResponse(String response, int id) {
                        log(response);
                        JSONObject jsonObject = null;
                        final SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        String newVersion = "";
                        final String lastDownloadVersion = getPrefs.getString("version", "1.0.0");
                        final String nowVersion = APKVersionCodeUtils.getVerName(MainActivity.this);
                        String apkUrl = "";
                        String information = "";
                        boolean forcedUpdate = false;

                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.getString("code").equals("000")) {
                                jsonObject = jsonObject.getJSONObject("data");
                                newVersion = jsonObject.getString("version");
                                apkUrl = jsonObject.getString("apkUrl");
                                information = jsonObject.getString("describes");
                                forcedUpdate = jsonObject.getBoolean("forcedUpdate");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //判断最新版本 与 当前版本
                        if (APKVersionCodeUtils.compareVersion(newVersion, nowVersion) > 0) {
                            log("网上大于现在");
                            //判断最新版本是否下载
                            if (APKVersionCodeUtils.compareVersion(newVersion, lastDownloadVersion) > 0) {
                                log("未下载");
                                //若WIFI为开启状态下载新版本
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                if (wifiManager != null && wifiManager.isWifiEnabled()) {
                                    log("wifi已开启 开始下载");
                                    final String finalInformation = information;
                                    final String finalNewVersion = newVersion;
                                    final boolean finalForcedUpdate = forcedUpdate;
                                    OkHttpUtils.get()
                                            .url(AppConstants.BASE_URL + apkUrl)
//                                            .url(AppConstants.BASE_URL + "/kenya/upload/-1547452445fd192b26f73b1b79a1a10b09c024a837.jpg")
                                            .build()
                                            .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath() + "/apk", "BL" + finalNewVersion + ".apk") {

                                                @Override
                                                public void onError(Call call, Exception e, int id) {
                                                    e.printStackTrace();
                                                }

                                                @Override
                                                public void onResponse(File response, int id) {
                                                    OpenFileUtil.deleteFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/apk", "BL" + lastDownloadVersion + ".apk"));
                                                    SharedPreferences.Editor e = getPrefs.edit();
                                                    e.putString("version", finalNewVersion);
                                                    e.apply();
                                                    log(response.getPath());
                                                    UpdateDialog updateDialog = new UpdateDialog(MainActivity.this, response.getPath(), finalNewVersion, finalInformation, finalForcedUpdate).setOnChooseListener(new OnChooseListener() {
                                                        @Override
                                                        public void yes(String message) {
                                                        }

                                                        @Override
                                                        public void no(String message) {
                                                            if (message.equals("true"))
                                                                finish();
                                                        }
                                                    });
                                                    updateDialog.setCancelable(false);
                                                    updateDialog.show();

                                                }
                                            });
                                }
                            } else {
                                UpdateDialog updateDialog = new UpdateDialog(MainActivity.this, UpdateDialog.DOWNLOAD_PATH, lastDownloadVersion, information, forcedUpdate).setOnChooseListener(new OnChooseListener() {
                                    @Override
                                    public void yes(String message) {
                                    }

                                    @Override
                                    public void no(String message) {
                                        if (message.equals("true"))
                                            finish();
                                    }
                                });

                                updateDialog.setCancelable(false);
                                updateDialog.show();
                            }
                        } else {
                            log("不大于");
                        }


                    }
                });
    }

}
