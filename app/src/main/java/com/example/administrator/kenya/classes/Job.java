package com.example.administrator.kenya.classes;

import java.io.Serializable;

/**
 * Created by 123 on 2018/4/12.
 */

public class Job implements Serializable {
    /**
     * jobid : 77
     * userid : 80
     * name : 1
     * headimg : /kenya/surveyLogos/50762066295831.jpg
     * sex : 1
     * jobwant : 伯伯
     * phone : 523523523
     * birthday : 1361548800000
     * jointime : null
     * persondesc : null
     */

    private int jobid;
    private int userid;
    private String name;
    private String headimg;
    private String sex;
    private String jobwant;
    private String phone;
    private String birthday;
    private String jointime;
    private String persondesc;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String age;

    public String getHopesalary() {
        return hopesalary;
    }

    public void setHopesalary(String hopesalary) {
        this.hopesalary = hopesalary;
    }

    private String hopesalary;

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJobwant() {
        return jobwant;
    }

    public void setJobwant(String jobwant) {
        this.jobwant = jobwant;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJointime() {
        return jointime;
    }

    public void setJointime(String jointime) {
        this.jointime = jointime;
    }

    public String getPersondesc() {
        return persondesc;
    }

    public void setPersondesc(String persondesc) {
        this.persondesc = persondesc;
    }
}
