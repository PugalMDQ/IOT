package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GetUserResponseModel;
import com.mdq.pojo.jsonresponse.GetWifiResponseModel;

public interface GetWifiResponseInterface  extends StateViewInterface {

    void generateGetWifiCall(GetWifiResponseModel getWifiResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}

