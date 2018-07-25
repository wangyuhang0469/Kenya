package com.example.administrator.kenya.classes;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/16.
 */

public class ChildComment implements Serializable{
    private String id;
    private String createTime;
    private String toUsername;
    private String childCommentText;
    private String toUserId;
    private Issuer issuer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getChildCommentText() {
        return childCommentText;
    }

    public void setChildCommentText(String childCommentText) {
        this.childCommentText = childCommentText;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }
}
