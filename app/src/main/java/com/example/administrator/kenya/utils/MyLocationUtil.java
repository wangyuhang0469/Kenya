package com.example.administrator.kenya.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;

import com.example.administrator.kenya.interfaces.MyLocationListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 */

public class MyLocationUtil {

    private String city;
    private String province;

    // 此对象能通过经纬度来获取相应的城市等信息
    private Geocoder geocoder;
    private LocationManager locationManager;
    private String provider;
    private Context context;
    private MyLocationListener myLocationListener;
    final Handler handler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (province != null && province.length() != 0 && city != null && city.length() != 0) {
                        myLocationListener.success(province, city);
                    } else {
                        myLocationListener.failed("error");
                    }
                    break;
            }
        }

        ;
    };

    public MyLocationUtil(Context context) {
        this.context = context;
    }

    public static MyLocationUtil getInstance(Context context) {
        return new MyLocationUtil(context);
    }

    private void getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);  //低精度
        criteria.setAltitudeRequired(false);  //不查询海拔
        criteria.setBearingRequired(false);  //不查询方位
        criteria.setCostAllowed(true);  //不允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW);  //低耗
        // 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前  provider
        provider = locationManager.getBestProvider(criteria, true);
    }
    private void getInformationByLocation(Location location) {
        if (location == null)
            return;
        String mCity = "";
        String mProvince = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            System.out.println("error");
        }
        try {
            addList = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mCity += add.getLocality();
                mProvince += add.getAdminArea();
            }
        }

        city = mCity;
        province = mProvince;

    }

    private final LocationListener locationListener = new LocationListener() {


        public void onLocationChanged(Location location) {
            getInformationByLocation(location);

        }

        public void onProviderDisabled(String provider) {
            getInformationByLocation(null);

        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    private Location getLocation() {
        Location location = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA}, 1);
        } else {
            geocoder = new Geocoder(context);
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            getProvider();
            // 通过最后一次的地理位置来获得Location对象
            location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000000, 1, locationListener);
            }
            return location;
        }
        return location;
    }

    public void getLocationInformation(MyLocationListener myLocationListener) {
        this.myLocationListener = myLocationListener;

        new Thread() {
            @Override
            public void run() {
                getInformationByLocation(getLocation());
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                super.run();
            }
        }.start();
    }


}
