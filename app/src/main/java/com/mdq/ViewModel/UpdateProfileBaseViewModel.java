package com.mdq.ViewModel;

public class UpdateProfileBaseViewModel {

    public String username;
    public String emergency_mobile;
    public String pwd;
    public String mobile;

    public String getAuth() {
        return Auth;
    }

    public void setAuth(String auth) {
        Auth = auth;
    }

    public String Auth;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
