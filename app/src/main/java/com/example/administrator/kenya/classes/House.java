package com.example.administrator.kenya.classes;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/24.
 */

public class House implements Serializable {

    private String leaseid;
    private String leaseimgs;
    private String leasename;
    private String leaseprice;
    private String leasephone;
    private String leasesquare;
    private String leaseaddress;
    private String leasehome;
    private String leaseimg1;
    private String leaseimg2;
    private String leaseimg3;
    private String leaseimg4;
    private String userid;
    private String leasedesc;
    private String leaseIms;
    private String leaseIm1;
    private String leaseIm2;
    private String leaseIm3;
    private String leaseIm4;
    private String user_phoneNumber;

    public String getLeaseid() {
        return leaseid;
    }

    public void setLeaseid(String leaseid) {
        this.leaseid = leaseid;
    }

    public String getLeaseimgs() {
        return leaseimgs;
    }

    public void setLeaseimgs(String leaseimgs) {
        this.leaseimgs = leaseimgs;
    }

    public String getLeaseprice() {
        return leaseprice;
    }

    public void setLeaseprice(String leaseprice) {
        this.leaseprice = leaseprice;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLeasename() {
        return leasename;
    }

    public void setLeasename(String leasename) {
        this.leasename = leasename;
    }


    public String getLeasephone() {
        return leasephone;
    }

    public void setLeasephone(String leasephone) {
        this.leasephone = leasephone;
    }

    public String getLeasesquare() {
        return leasesquare;
    }

    public void setLeasesquare(String leasesquare) {
        this.leasesquare = leasesquare;
    }

    public String getLeaseaddress() {
        return leaseaddress;
    }

    public void setLeaseaddress(String leaseaddress) {
        this.leaseaddress = leaseaddress;
    }

    public String getLeasehome() {
        return leasehome;
    }

    public void setLeasehome(String leasehome) {
        this.leasehome = leasehome;
    }

    public String getLeaseimg1() {
        return leaseimg1;
    }

    public void setLeaseimg1(String leaseimg1) {
        this.leaseimg1 = leaseimg1;
    }

    public String getLeaseimg2() {
        return leaseimg2;
    }

    public void setLeaseimg2(String leaseimg2) {
        this.leaseimg2 = leaseimg2;
    }

    public String getLeaseimg3() {
        return leaseimg3;
    }

    public void setLeaseimg3(String leaseimg3) {
        this.leaseimg3 = leaseimg3;
    }

    public String getLeaseimg4() {
        return leaseimg4;
    }

    public void setLeaseimg4(String leaseimg4) {
        this.leaseimg4 = leaseimg4;
    }


    public String getLeasedesc() {
        return leasedesc;
    }

    public void setLeasedesc(String leasedesc) {
        this.leasedesc = leasedesc;
    }

    public String getLeaseIms() {
        return leaseIms;
    }

    public void setLeaseIms(String leaseIms) {
        this.leaseIms = leaseIms;
    }

    public String getLeaseIm1() {
        return leaseIm1;
    }

    public void setLeaseIm1(String leaseIm1) {
        this.leaseIm1 = leaseIm1;
    }

    public String getLeaseIm2() {
        return leaseIm2;
    }

    public void setLeaseIm2(String leaseIm2) {
        this.leaseIm2 = leaseIm2;
    }

    public String getLeaseIm3() {
        return leaseIm3;
    }

    public void setLeaseIm3(String leaseIm3) {
        this.leaseIm3 = leaseIm3;
    }

    public String getLeaseIm4() {
        return leaseIm4;
    }

    public void setLeaseIm4(String leaseIm4) {
        this.leaseIm4 = leaseIm4;
    }

    public String getUser_phoneNumber() {
        return user_phoneNumber;
    }

    public void setUser_phoneNumber(String user_phoneNumber) {
        this.user_phoneNumber = user_phoneNumber;
    }

    public List<String> getHouseImageUrlList() {
        List<String> houseimageUrlList = new ArrayList<>();
        if (!(leaseimgs == null || leaseimgs.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + leaseimgs);
        if (!(leaseimg1 == null || leaseimg1.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + leaseimg1);
        if (!(leaseimg2 == null || leaseimg2.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + leaseimg2);
        if (!(leaseimg3 == null || leaseimg3.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + leaseimg3);
        if (!(leaseimg4 == null || leaseimg4.equals("")))
            houseimageUrlList.add(AppConstants.BASE_URL + leaseimg4);
        return houseimageUrlList;
    }
}
