package com.example.administrator.kenya.classes;

import com.example.administrator.kenya.constants.AppConstants;

import java.io.Serializable;


/**
 * Created by wang on 2017/7/31.
 */
public class User implements Serializable {


    private String userId;
    private String userName = "Anna DLeiy";
    private String userPsw;
    private String userSex;
    private String userPhonenumber ="0714625689";
    private String userHavecar;
    private String userBirthday;
    private String userPortrait;
    private Boolean status = false;




    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    private User() {
    }

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
        if (userPortrait == null||userPortrait.equals("null")|| userPortrait.equals(""))
            return null;
        return AppConstants.BASE_URL +userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }



    private static class UserHolder {
        static final User INSTANCE = new User();
    }

    public static User getInstance() {
        return UserHolder.INSTANCE;
    }


}
