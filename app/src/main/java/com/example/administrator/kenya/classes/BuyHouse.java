package com.example.administrator.kenya.classes;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/6/7.
 */

public class BuyHouse implements Serializable {

    private String houseid;
    private String houseimgs;
    private String housename;
    private String houseprice;
    private String housephone;
    private String housesquare;
    private String houseaddress;
    private String househome;
    private String houseimg1;
    private String houseimg2;
    private String houseimg3;
    private String houseimg4;
    private String userid;
    private String houseuser;
    private String housedate;
    private String housecity;
    private String housetype;
    private String housedesc;
    private Issuer user;
    private CityProvince cityProvince;

    public CityProvince getCityProvince() {
        return cityProvince;
    }

    public void setCityProvince(CityProvince cityProvince) {
        this.cityProvince = cityProvince;
    }

    public Issuer getUser() {
        return user;
    }

    public void setUser(Issuer user) {
        this.user = user;
    }


    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getHouseimgs() {
        return houseimgs;
    }

    public void setHouseimgs(String houseimgs) {
        this.houseimgs = houseimgs;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public String getHouseprice() {
        return houseprice;
    }

    public void setHouseprice(String houseprice) {
        this.houseprice = houseprice;
    }

    public String getHousephone() {
        return housephone;
    }

    public void setHousephone(String housephone) {
        this.housephone = housephone;
    }

    public String getHousesquare() {
        return housesquare;
    }

    public void setHousesquare(String housesquare) {
        this.housesquare = housesquare;
    }

    public String getHouseaddress() {
        return houseaddress;
    }

    public void setHouseaddress(String houseaddress) {
        this.houseaddress = houseaddress;
    }

    public String getHousehome() {
        return househome;
    }

    public void setHousehome(String househome) {
        this.househome = househome;
    }

    public String getHouseimg1() {
        return houseimg1;
    }

    public void setHouseimg1(String houseimg1) {
        this.houseimg1 = houseimg1;
    }

    public String getHouseimg2() {
        return houseimg2;
    }

    public void setHouseimg2(String houseimg2) {
        this.houseimg2 = houseimg2;
    }

    public String getHouseimg3() {
        return houseimg3;
    }

    public void setHouseimg3(String houseimg3) {
        this.houseimg3 = houseimg3;
    }

    public String getHouseimg4() {
        return houseimg4;
    }

    public void setHouseimg4(String houseimg4) {
        this.houseimg4 = houseimg4;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHouseuser() {
        return houseuser;
    }

    public void setHouseuser(String houseuser) {
        this.houseuser = houseuser;
    }

    public String getHousedate() {
        return housedate;
    }

    public void setHousedate(String housedate) {
        this.housedate = housedate;
    }

    public String getHousecity() {
        return housecity;
    }

    public void setHousecity(String housecity) {
        this.housecity = housecity;
    }

    public String getHousetype() {
        return housetype;
    }

    public void setHousetype(String housetype) {
        this.housetype = housetype;
    }

    public String getHousedesc() {
        return housedesc;
    }

    public void setHousedesc(String housedesc) {
        this.housedesc = housedesc;
    }

    public List<String> getBuyHouseImageUrlList() {
        List<String> houseimageUrlList = new ArrayList<>();
        if (!(houseimgs == null || houseimgs.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + houseimgs);
        if (!(houseimg1 == null || houseimg1.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + houseimg1);
        if (!(houseimg2 == null || houseimg2.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + houseimg2);
        if (!(houseimg3 == null || houseimg3.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + houseimg3);
        if (!(houseimg4 == null || houseimg4.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + houseimg4);
        return houseimageUrlList;
    }


}
