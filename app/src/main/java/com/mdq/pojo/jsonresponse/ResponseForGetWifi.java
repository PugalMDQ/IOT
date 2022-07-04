package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseForGetWifi {


    @SerializedName("wifi_ssd")
    @Expose
    public String wifi_ssd;


    public String getWifi_ssd() {
        return wifi_ssd;
    }

    public void setWifi_ssd(String wifi_ssd) {
        this.wifi_ssd = wifi_ssd;
    }

    public String getWifi_pin() {
        return wifi_pin;
    }

    public void setWifi_pin(String wifi_pin) {
        this.wifi_pin = wifi_pin;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @SerializedName("wifi_pin")
    @Expose
    public String wifi_pin;


    @SerializedName("mobile")
    @Expose
    public String mobile;

}
