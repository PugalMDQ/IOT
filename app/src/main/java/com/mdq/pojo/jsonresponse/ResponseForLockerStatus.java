package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseForLockerStatus {

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("locker_status")
    @Expose
    public String locker_status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocker_status() {
        return locker_status;
    }

    public void setLocker_status(String locker_status) {
        this.locker_status = locker_status;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    @SerializedName("created_time")
    @Expose
    public String created_time;

}
