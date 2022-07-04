package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserResponseModel {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public List<ResponseForGetUser> data;

    @SerializedName("error")
    @Expose
    public boolean error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public List<ResponseForGetUser> getData() {
        return data;
    }

    public void setData(List<ResponseForGetUser> data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
