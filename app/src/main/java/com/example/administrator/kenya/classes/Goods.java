package com.example.administrator.kenya.classes;

import android.util.Log;
import android.widget.Toast;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/24.
 */

public class Goods implements Serializable {

    String goodsid;
    String goodsname;
    String goodsimgs;
    String goodsimg1;
    String goodsimg2;
    String goodsimg3;
    String goodsimg4;
    String goodsprice;
    String goodsusername;
    String goodsphone;
    String userid;
    String goodsdesc;


    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsimgs() {
        return goodsimgs;
    }

    public void setGoodsimgs(String goodsimgs) {
        this.goodsimgs = goodsimgs;
    }

    public String getGoodsimg1() {
        return goodsimg1;
    }

    public void setGoodsimg1(String goodsimg1) {
        this.goodsimg1 = goodsimg1;
    }

    public String getGoodsimg2() {
        return goodsimg2;
    }

    public void setGoodsimg2(String goodsimg2) {
        this.goodsimg2 = goodsimg2;
    }

    public String getGoodsimg3() {
        return goodsimg3;
    }

    public void setGoodsimg3(String goodsimg3) {
        this.goodsimg3 = goodsimg3;
    }

    public String getGoodsimg4() {
        return goodsimg4;
    }

    public void setGoodsimg4(String goodsimg4) {
        this.goodsimg4 = goodsimg4;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getGoodsusername() {
        return goodsusername;
    }

    public void setGoodsusername(String goodsusername) {
        this.goodsusername = goodsusername;
    }

    public String getGoodsphone() {
        return goodsphone;
    }

    public void setGoodsphone(String goodsphone) {
        this.goodsphone = goodsphone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoodsdesc() {
        return goodsdesc;
    }

    public void setGoodsdesc(String goodsdesc) {
        this.goodsdesc = goodsdesc;
    }


    public List<String> getImageUrlList() {
        List<String> imageUrlList = new ArrayList<>();
        if (!goodsimgs.equals(""))
            imageUrlList.add(AppConstants.BASE_URL + goodsimgs);
        if (!goodsimg1.equals(""))
            imageUrlList.add(AppConstants.BASE_URL + goodsimg1);
        if (!goodsimg2.equals(""))
            imageUrlList.add(AppConstants.BASE_URL + goodsimg2);
        if (!goodsimg3.equals(""))
            imageUrlList.add(AppConstants.BASE_URL + goodsimg3);
        if (!goodsimg4.equals(""))
            imageUrlList.add(AppConstants.BASE_URL + goodsimg4);
        return imageUrlList;
    }
}
