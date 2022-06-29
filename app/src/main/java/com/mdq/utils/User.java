package com.mdq.utils;

public class User {
    String uid;
    String name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String profileImageUrl;
    String mobileno;
    String userID;
     public User(
             String uid,
             String name,
             String profileImageUrl,
             String mobileno,
             String userID
     )  {

         this.uid=uid;
         this.name=name;
         this.profileImageUrl=profileImageUrl;
         this.mobileno=mobileno;
         this.userID=userID;

    }
}
