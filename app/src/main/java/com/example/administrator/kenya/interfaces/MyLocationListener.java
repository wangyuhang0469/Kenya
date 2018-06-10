package com.example.administrator.kenya.interfaces;

/**
 * Created by Administrator on 2018/4/19.
 */

public interface MyLocationListener {
    void success(String province, String city);

    void failed(String message);
}
