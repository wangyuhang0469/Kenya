package com.example.administrator.kenya.classes;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class Husbandry implements Serializable {

    String framid;
    String framname;
    String framimgs;
    String framimg1;
    String framimg2;
    String framimg3;
    String framimg4;
    String framtype;
    String framuser;
    String framphone;
    String userid;
    String framdesc;
    Issuer user;

    public String getFramid() {
        return framid;
    }

    public void setFramid(String framid) {
        this.framid = framid;
    }

    public String getFramname() {
        return framname;
    }

    public void setFramname(String framname) {
        this.framname = framname;
    }

    public String getFramimgs() {
        return framimgs;
    }

    public void setFramimgs(String framimgs) {
        this.framimgs = framimgs;
    }

    public String getFramimg1() {
        return framimg1;
    }

    public void setFramimg1(String framimg1) {
        this.framimg1 = framimg1;
    }

    public String getFramimg2() {
        return framimg2;
    }

    public void setFramimg2(String framimg2) {
        this.framimg2 = framimg2;
    }

    public String getFramimg3() {
        return framimg3;
    }

    public void setFramimg3(String framimg3) {
        this.framimg3 = framimg3;
    }

    public String getFramimg4() {
        return framimg4;
    }

    public void setFramimg4(String framimg4) {
        this.framimg4 = framimg4;
    }

    public String getFramtype() {
        return framtype;
    }

    public void setFramtype(String framtype) {
        this.framtype = framtype;
    }

    public String getFramuser() {
        return framuser;
    }

    public void setFramuser(String framuser) {
        this.framuser = framuser;
    }

    public String getFramphone() {
        return framphone;
    }

    public void setFramphone(String framphone) {
        this.framphone = framphone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFramdesc() {
        return framdesc;
    }

    public void setFramdesc(String framdesc) {
        this.framdesc = framdesc;
    }

    public Issuer getUser() {
        return user;
    }

    public void setUser(Issuer user) {
        this.user = user;
    }

    public List<String> getImageUrlList() {
        List<String> imageUrlList = new ArrayList<>();
        if (!(framimgs == null || framimgs.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + framimgs);
        if (!(framimg1 == null || framimg1.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + framimg1);
        if (!(framimg2 == null || framimg2.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + framimg2);
        if (!(framimg3 == null || framimg3.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + framimg3);
        if (!(framimg4 == null || framimg4.equals("")))
            imageUrlList.add(AppConstants.BASE_URL + framimg4);

        return imageUrlList;
    }
}
