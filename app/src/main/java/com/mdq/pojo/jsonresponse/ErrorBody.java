package com.mdq.pojo.jsonresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorBody {
    public String Code;
    public String Message;
    public String error_description;
    public String ErrorMessage;
}
