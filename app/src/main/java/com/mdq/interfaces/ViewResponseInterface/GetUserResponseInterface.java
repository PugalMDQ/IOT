package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GetUserResponseModel;
import com.mdq.pojo.jsonresponse.InsertEmergencyMobileNoResponseModel;

public interface GetUserResponseInterface  extends StateViewInterface {

    void generateGetUserCall(GetUserResponseModel getUserResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}

