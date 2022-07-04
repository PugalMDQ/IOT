package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.UpdateProfileResponseModel;
import com.mdq.pojo.jsonresponse.UpdateWifiResponseModel;

public interface UpdateWifiResponseInterface extends StateViewInterface {

    void generateUpdateWifiCall(UpdateWifiResponseModel updateWifiResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}