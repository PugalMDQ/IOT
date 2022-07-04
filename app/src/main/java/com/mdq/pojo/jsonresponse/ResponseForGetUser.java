package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseForGetUser {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("emergency_mobile")
    @Expose
    public String emergency_mobile;
    @SerializedName("pwd")
    @Expose
    public String pwd;
    @SerializedName("email")
    @Expose
    public String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmergency_mobile() {
        return emergency_mobile;
    }

    public void setEmergency_mobile(String emergency_mobile) {
        this.emergency_mobile = emergency_mobile;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
