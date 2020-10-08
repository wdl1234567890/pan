package com.example.demo.domain;

import java.io.Serializable;

public class MyFriendInfo implements Serializable {
    private String mFIId;

    private Integer myId;

    private Integer friendId;

    private String info;

    private static final long serialVersionUID = 1L;

    public String getmFIId() {
        return mFIId;
    }

    public void setmFIId(String mFIId) {
        this.mFIId = mFIId == null ? null : mFIId.trim();
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }
}