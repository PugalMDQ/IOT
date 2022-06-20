package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenerateVerificationKeyResponseModel {


    @SerializedName("message")
    @Expose
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<com.mdq.pojo.jsonresponse.ResponseForVerification> getData() {
        return data;
    }

    public void setData(List<com.mdq.pojo.jsonresponse.ResponseForVerification> data) {
        this.data = data;
    }

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public List<com.mdq.pojo.jsonresponse.ResponseForVerification> data;

}

