package com.mdq.pojo.jsonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateOTPResponseModel {


    @SerializedName("message")
    @Expose
    public String message;
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

    public void setError(boolean error) {
        this.error = error;
    }

}
