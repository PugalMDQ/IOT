package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenerateLoginResponseModel {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("error")
    @Expose
    public String error;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ResponseForLogin> getId() {
        return id;
    }

    public void setId(List<ResponseForLogin> id) {
        this.id = id;
    }

    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("id")
    @Expose
    public List<ResponseForLogin> id;
}
