package com.example.administrator.kenya.MyApplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/3/29.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private List<Activity> activityList;
    public static Context mContext;
    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void setdata(String message) {
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("data", message);
        editor.commit();
    }

    public String getdata() {
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        String message = preferences.getString("data", "");
        return message;
    }

    /*添加activity*/
    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<Activity>();
        }
        activityList.add(activity);
    }

    public int getListSize() {
        if (activityList != null) {
            return activityList.size();
        }
        return 0;
    }

    public void removeActivity(Activity activity) {
        if (activityList != null) {

            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }

    }

    /*清空activity，取消引用*/
    public void clearActivity() {
        activityList.clear();
    }

    /*app退出*/
    public void exit() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing() && activity != null) {
                activity.finish();
            }
        }
        clearActivity();
        System.exit(0);
    }

    /*结束activity*/
    public void finishActivity(Activity activity) {

        if (activity != null) {
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**/
    public void finishActivity(Class<?> cls) {
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i).getClass().equals(cls)) {
                finishActivity(activityList.get(i));
            }
        }
    }
}
