package com.example.administrator.kenya.classes;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/4/12.
 */

public class Funds implements Serializable {

    private int fundsid;
    private String fundsname;
    private String fundsprice;
    private String fundsdesc;
    private String fundsphone;
    private String fundsimgs;
    private String fundsimg1;
    private String fundsimg2;
    private String fundsimg3;
    private String fundsimg4;

    public String getFundsuser() {
        return fundsuser;
    }

    public void setFundsuser(String fundsuser) {
        this.fundsuser = fundsuser;
    }

    private String fundsuser;
    private int adminid;

    public int getFundsid() {
        return fundsid;
    }

    public void setFundsid(int fundsid) {
        this.fundsid = fundsid;
    }

    public String getFundsname() {
        return fundsname;
    }

    public void setFundsname(String fundsname) {
        this.fundsname = fundsname;
    }

    public String getFundsprice() {
        return fundsprice;
    }

    public void setFundsprice(String fundsprice) {
        this.fundsprice = fundsprice;
    }

    public String getFundsdesc() {
        return fundsdesc;
    }

    public void setFundsdesc(String fundsdesc) {
        this.fundsdesc = fundsdesc;
    }

    public String getFundsphone() {
        return fundsphone;
    }

    public void setFundsphone(String fundsphone) {
        this.fundsphone = fundsphone;
    }

    public String getFundsimgs() {
        return fundsimgs;
    }

    public void setFundsimgs(String fundsimgs) {
        this.fundsimgs = fundsimgs;
    }

    public Object getFundsimg1() {
        return fundsimg1;
    }

    public void setFundsimg1(String fundsimg1) {
        this.fundsimg1 = fundsimg1;
    }

    public Object getFundsimg2() {
        return fundsimg2;
    }

    public void setFundsimg2(String fundsimg2) {
        this.fundsimg2 = fundsimg2;
    }

    public Object getFundsimg3() {
        return fundsimg3;
    }

    public void setFundsimg3(String fundsimg3) {
        this.fundsimg3 = fundsimg3;
    }

    public Object getFundsimg4() {
        return fundsimg4;
    }

    public void setFundsimg4(String fundsimg4) {
        this.fundsimg4 = fundsimg4;
    }

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }


    public List<String> getFundsImageUrlList() {
        List<String> fundsimageUrlList = new ArrayList<>();
        if (!(fundsimgs == null || fundsimgs.equals("")))
            fundsimageUrlList.add(AppConstants.BASE_URL + fundsimgs);
        if (!(fundsimg1 == null || fundsimg1.equals("")))
            fundsimageUrlList.add(AppConstants.BASE_URL + fundsimg1);
        if (!(fundsimg2 == null || fundsimg2.equals("")))
            fundsimageUrlList.add(AppConstants.BASE_URL + fundsimg2);
        if (!(fundsimg3 == null || fundsimg3.equals("")))
            fundsimageUrlList.add(AppConstants.BASE_URL + fundsimg3);
        if (!(fundsimg4 == null || fundsimg4.equals("")))
            fundsimageUrlList.add(AppConstants.BASE_URL + fundsimg4);
        return fundsimageUrlList;
    }

}
