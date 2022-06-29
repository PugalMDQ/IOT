package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseForLogin {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("firebase_uid")
    @Expose
    public String firebase_uid;

    @SerializedName("biometric")
    @Expose
    public String biometric;

    public String getFirebase_uid() {
        return firebase_uid;
    }

    public void setFirebase_uid(String firebase_uid) {
        this.firebase_uid = firebase_uid;
    }

    public String getBiometric() {
        return biometric;
    }

    public void setBiometric(String biometric) {
        this.biometric = biometric;
    }

    @SerializedName("mac_id")
    @Expose
    public String mac_id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("username")
    @Expose
    public String username;

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
        return firebase_uid;
    }

    public void setDevice_token(String device_token) {
        this.firebase_uid = device_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
