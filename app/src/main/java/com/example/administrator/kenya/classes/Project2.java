package com.example.administrator.kenya.classes;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/4/12.
 */

public class Project2 implements Serializable {

    private int projectid;
    private String projectname;
    private String projectprice;
    private String projectdesc;
    private String projectphone;
    private String projectimgs;
    private String projectimg1;
    private String projectimg2;
    private String projectimg3;
    private String projectimg4;
    private int adminid;
    private String projectuser;
    private String projectaddress;

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectprice() {
        return projectprice;
    }

    public void setProjectprice(String projectprice) {
        this.projectprice = projectprice;
    }

    public String getProjectdesc() {
        return projectdesc;
    }

    public void setProjectdesc(String projectdesc) {
        this.projectdesc = projectdesc;
    }

    public String getProjectphone() {
        return projectphone;
    }

    public void setProjectphone(String projectphone) {
        this.projectphone = projectphone;
    }

    public String getProjectimgs() {
        return projectimgs;
    }

    public void setProjectimgs(String projectimgs) {
        this.projectimgs = projectimgs;
    }

    public Object getProjectimg1() {
        return projectimg1;
    }

    public void setProjectimg1(String projectimg1) {
        this.projectimg1 = projectimg1;
    }

    public Object getProjectimg2() {
        return projectimg2;
    }

    public void setProjectimg2(String projectimg2) {
        this.projectimg2 = projectimg2;
    }

    public Object getProjectimg3() {
        return projectimg3;
    }

    public void setProjectimg3(String projectimg3) {
        this.projectimg3 = projectimg3;
    }

    public Object getProjectimg4() {
        return projectimg4;
    }

    public void setProjectimg4(String projectimg4) {
        this.projectimg4 = projectimg4;
    }

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public String getProjectuser() {
        return projectuser;
    }

    public void setProjectuser(String projectuser) {
        this.projectuser = projectuser;
    }

    public String getProjectaddress() {
        return projectaddress;
    }

    public void setProjectaddress(String projectaddress) {
        this.projectaddress = projectaddress;
    }

    public List<String> getProjectImageUrlList() {
        List<String> projectimageUrlList = new ArrayList<>();
        if (!(projectimgs == null || projectimgs.equals("")))
            projectimageUrlList.add(AppConstants.BASE_URL + projectimgs);
        if (!(projectimg1 == null || projectimg1.equals("")))
            projectimageUrlList.add(AppConstants.BASE_URL + projectimg1);
        if (!(projectimg2 == null || projectimg2.equals("")))
            projectimageUrlList.add(AppConstants.BASE_URL + projectimg2);
        if (!(projectimg3 == null || projectimg3.equals("")))
            projectimageUrlList.add(AppConstants.BASE_URL + projectimg3);
        if (!(projectimg4 == null || projectimg4.equals("")))
            projectimageUrlList.add(AppConstants.BASE_URL + projectimg4);
        return projectimageUrlList;
    }
}
