package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetWifiResponseModel {



    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public List<ResponseForGetWifi> data;

    @SerializedName("error")
    @Expose
    public boolean error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResponseForGetWifi> getData() {
        return data;
    }

    public void setData(List<ResponseForGetWifi> data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
