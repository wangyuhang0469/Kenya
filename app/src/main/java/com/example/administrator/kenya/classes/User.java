package com.example.administrator.kenya.classes;

import java.io.Serializable;


/**
 * Created by wang on 2017/7/31.
 */
public class User implements Serializable {


    private String userName = "";
    private Boolean status = false;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    private User() {
    }

    private static class UserHolder {
        static final User INSTANCE = new User();
    }

    public static User getInstance() {
        return UserHolder.INSTANCE;
    }


}
