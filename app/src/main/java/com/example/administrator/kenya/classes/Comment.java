package com.example.administrator.kenya.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/16.
 */

public class Comment implements Serializable {
    private String id;
    private String userId;
    private String commentText;
    private String thingsId;
    private String createTime;
    private String thingsType;
    private String pageSize;
    private Issuer issuer;
    private ArrayList<ChildComment> childs;


    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getThingsType() {
        return thingsType;
    }

    public void setThingsType(String thingsType) {
        this.thingsType = thingsType;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public ArrayList<ChildComment> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ChildComment> childs) {
        this.childs = childs;
    }
}
