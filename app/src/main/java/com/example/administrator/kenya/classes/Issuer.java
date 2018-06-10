package com.example.administrator.kenya.classes;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/13.
 */

public class Issuer implements Serializable {
    String userId;
    String userName;
    String userPsw;
    String userSex;
    String userPhonenumber;
    String userHavecar;
    String userBirthday;
    String userPortrait;
    String userProhibit;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPsw() {
        return userPsw;
    }

    public void setUserPsw(String userPsw) {
        this.userPsw = userPsw;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPhonenumber() {
        return userPhonenumber;
    }

    public void setUserPhonenumber(String userPhonenumber) {
        this.userPhonenumber = userPhonenumber;
    }

    public String getUserHavecar() {
        return userHavecar;
    }

    public void setUserHavecar(String userHavecar) {
        this.userHavecar = userHavecar;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserProhibit() {
        return userProhibit;
    }

    public void setUserProhibit(String userProhibit) {
        this.userProhibit = userProhibit;
    }
}
