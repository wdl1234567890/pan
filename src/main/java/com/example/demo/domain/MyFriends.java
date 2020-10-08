package com.example.demo.domain;

import java.io.Serializable;

public class MyFriends implements Serializable {
    private String mFId;

    private Integer myId;

    private Integer friendId;

    private static final long serialVersionUID = 1L;

    public String getmFId() {
        return mFId;
    }

    public void setmFId(String mFId) {
        this.mFId = mFId == null ? null : mFId.trim();
    }

    public Integer getMyId() {
        return myId;
    }

    public void setMyId(Integer myId) {
        this.myId = myId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }
}