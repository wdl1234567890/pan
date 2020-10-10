package com.example.demo.domain;

import java.io.Serializable;

public class Message implements Serializable {
    private String mId;

    private Integer sendId;

    private Integer recId;

    private Boolean mType;

    private Integer departmentId;

    private String cTime;

    private String messageCon;

    private static final long serialVersionUID = 1L;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId == null ? null : mId.trim();
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public Boolean getmType() {
        return mType;
    }

    public void setmType(Boolean mType) {
        this.mType = mType;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime == null ? null : cTime.trim();
    }

    public String getMessageCon() {
        return messageCon;
    }

    public void setMessageCon(String messageCon) {
        this.messageCon = messageCon == null ? null : messageCon.trim();
    }
}