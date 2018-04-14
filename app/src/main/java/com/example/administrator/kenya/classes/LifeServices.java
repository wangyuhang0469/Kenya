package com.example.administrator.kenya.classes;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */

public class LifeServices implements Serializable {

    String liveid;
    String livephone;
    String livename;
    String livetype;
    String liveimgs="";
    String liveimg1="";
    String liveimg2="";
    String liveimg3="";
    String liveimg4="";
    String userid;
    String liveuser;
    String livedesc;
    String user_phoneNumber;
    Issuer user;


    public String getLiveid() {
        return liveid;
    }

    public void setLiveid(String liveid) {
        this.liveid = liveid;
    }

    public String getLivephone() {
        return livephone;
    }

    public void setLivephone(String livephone) {
        this.livephone = livephone;
    }

    public String getLivename() {
        return livename;
    }

    public void setLivename(String livename) {
        this.livename = livename;
    }

    public String getLivetype() {
        return livetype;
    }

    public void setLivetype(String livetype) {
        this.livetype = livetype;
    }

    public String getLiveimgs() {
        return liveimgs;
    }

    public void setLiveimgs(String liveimgs) {
        this.liveimgs = liveimgs;
    }

    public String getLiveimg1() {
        return liveimg1;
    }

    public void setLiveimg1(String liveimg1) {
        this.liveimg1 = liveimg1;
    }

    public String getLiveimg2() {
        return liveimg2;
    }

    public void setLiveimg2(String liveimg2) {
        this.liveimg2 = liveimg2;
    }

    public String getLiveimg3() {
        return liveimg3;
    }

    public void setLiveimg3(String liveimg3) {
        this.liveimg3 = liveimg3;
    }

    public String getLiveimg4() {
        return liveimg4;
    }

    public void setLiveimg4(String liveimg4) {
        this.liveimg4 = liveimg4;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLivedesc() {
        return livedesc;
    }

    public void setLivedesc(String livedesc) {
        this.livedesc = livedesc;
    }

    public String getUser_phoneNumber() {
        return user_phoneNumber;
    }

    public void setUser_phoneNumber(String user_phoneNumber) {
        this.user_phoneNumber = user_phoneNumber;
    }

    public String getLiveuser() {
        return liveuser;
    }

    public void setLiveuser(String liveuser) {
        this.liveuser = liveuser;
    }

    public Issuer getUser() {
        return user;
    }

    public void setUser(Issuer user) {
        this.user = user;
    }


    public List<String> getImageUrlList() {
        List<String> imageUrlList = new ArrayList<>();
        if (!(liveimgs == null || liveimgs.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + liveimgs);
        if (!(liveimg1 == null || liveimg1.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + liveimg1);
        if (!(liveimg2 == null || liveimg2.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + liveimg2);
        if (!(liveimg3 == null || liveimg3.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + liveimg3);
        if (!(liveimg4 == null || liveimg4.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + liveimg4);

        return imageUrlList;
    }



}
