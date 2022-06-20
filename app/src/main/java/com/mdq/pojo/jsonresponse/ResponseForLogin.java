package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseForLogin {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("device_token")
    @Expose
    public String device_token;

    @SerializedName("mac_id")
    @Expose
    public String mac_id;

    @SerializedName("macid_status")
    @Expose
    public String macid_status;

    public String getMac_id() {
        return mac_id;
    }

    public void setMac_id(String mac_id) {
        this.mac_id = mac_id;
    }

    public String getMacid_status() {
        return macid_status;
    }

    public void setMacid_status(String macid_status) {
        this.macid_status = macid_status;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
