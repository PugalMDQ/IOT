package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateSetMac_IDResponseModel;

public interface SetMac_IDResponseInterface extends StateViewInterface {

    void generateSetMAc_IDProcess(GenerateSetMac_IDResponseModel generateSetMac_idResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);
}
