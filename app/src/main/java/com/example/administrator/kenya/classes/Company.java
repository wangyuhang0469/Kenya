package com.example.administrator.kenya.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/4/13.
 */

public class Company implements Serializable {
    /**
     * companyid : 67
     * companyname : 公司名字
     * companystation : 公司招聘岗位
     * companystationsalary : 3.33333333E8
     * companyphone : 公司手机号
     * userid : 4
     * companystationdesc : null
     * companyaddress : null
     * companydesc : null
     */

    private String companyid;
    private String companyname;
    private String companystation;
    private String companystationsalary;
    private String companyphone;
    private String userid;
    private String companystationdesc;
    private String companyaddress;
    private String companydesc;
    private String companyimg0;
    private String companyimg1;
    private String companyimg2;
    private String companyimg3;
    private String companyimg4;
    private String companyimg5;

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanystation() {
        return companystation;
    }

    public void setCompanystation(String companystation) {
        this.companystation = companystation;
    }

    public String getCompanystationsalary() {
        return companystationsalary;
    }

    public void setCompanystationsalary(String companystationsalary) {
        this.companystationsalary = companystationsalary;
    }

    public String getCompanyphone() {
        return companyphone;
    }

    public void setCompanyphone(String companyphone) {
        this.companyphone = companyphone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCompanystationdesc() {
        return companystationdesc;
    }

    public void setCompanystationdesc(String companystationdesc) {
        this.companystationdesc = companystationdesc;
    }

    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getCompanydesc() {
        return companydesc;
    }

    public void setCompanydesc(String companydesc) {
        this.companydesc = companydesc;
    }

    public String getCompanyimg0() {
        return companyimg0;
    }

    public void setCompanyimg0(String companyimg0) {
        this.companyimg0 = companyimg0;
    }

    public String getCompanyimg1() {
        return companyimg1;
    }

    public void setCompanyimg1(String companyimg1) {
        this.companyimg1 = companyimg1;
    }

    public String getCompanyimg2() {
        return companyimg2;
    }

    public void setCompanyimg2(String companyimg2) {
        this.companyimg2 = companyimg2;
    }

    public String getCompanyimg3() {
        return companyimg3;
    }

    public void setCompanyimg3(String companyimg3) {
        this.companyimg3 = companyimg3;
    }

    public String getCompanyimg4() {
        return companyimg4;
    }

    public void setCompanyimg4(String companyimg4) {
        this.companyimg4 = companyimg4;
    }

    public String getCompanyimg5() {
        return companyimg5;
    }

    public void setCompanyimg5(String companyimg5) {
        this.companyimg5 = companyimg5;
    }

    public List<String> getCompanyImageUrlList() {
        List<String> companyimageUrlList = new ArrayList<>();

        companyimageUrlList.add(companyimg0);
        companyimageUrlList.add(companyimg1);
        companyimageUrlList.add(companyimg2);
        companyimageUrlList.add(companyimg3);
        companyimageUrlList.add(companyimg4);
        return companyimageUrlList;
    }

}
