package com.example.administrator.kenya.classes;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/18.
 */

public class Transition implements Serializable {

    private String id;
    private String name;
    private String img0;
    private String diedReport;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    public String getDiedReport() {
        return diedReport;
    }

    public void setDiedReport(String diedReport) {
        this.diedReport = diedReport;
    }
}
