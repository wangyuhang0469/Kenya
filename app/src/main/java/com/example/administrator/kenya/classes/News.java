package com.example.administrator.kenya.classes;

import java.io.Serializable;

/**
 * Created by 123 on 2018/4/17.
 */

public class News implements Serializable {

    private int newsid;
    private String newstitle;
    private String newsimg0;
    private String newsimg1;
    private String newsauthor;
    private String newscreatetime;
    private String newstext;

    public int getNewsid() {
        return newsid;
    }

    public void setNewsid(int newsid) {
        this.newsid = newsid;
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public String getNewsimg0() {
        return newsimg0;
    }

    public void setNewsimg0(String newsimg0) {
        this.newsimg0 = newsimg0;
    }

    public String getNewsimg1() {
        return newsimg1;
    }

    public void setNewsimg1(String newsimg1) {
        this.newsimg1 = newsimg1;
    }

    public String getNewsauthor() {
        return newsauthor;
    }

    public void setNewsauthor(String newsauthor) {
        this.newsauthor = newsauthor;
    }

    public String getNewscreatetime() {
        return newscreatetime;
    }

    public void setNewscreatetime(String newscreatetime) {
        this.newscreatetime = newscreatetime;
    }

    public String getNewstext() {
        return newstext;
    }

    public void setNewstext(String newstext) {
        this.newstext = newstext;
    }
}
