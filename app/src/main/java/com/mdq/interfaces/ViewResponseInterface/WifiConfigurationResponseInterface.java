package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.WifiConfigurationResponseModel;

public interface WifiConfigurationResponseInterface extends StateViewInterface {
    void generateVarification(WifiConfigurationResponseModel wifiConfigurationResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}
