package com.example.lob.DTO;

import java.io.Serializable;

public class UserDto implements Serializable {
    private String userMail;
    private String userUid;
    private String userImg;

    public UserDto(String userMail, String userUid , String userImg) {
        this.userMail = userMail;
        this.userUid = userUid;
        this.userImg=  userImg;
    }

    public UserDto(String userMail, String userUid) {
        this.userMail = userMail;
        this.userUid = userUid;
    }
    public UserDto(){

    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
