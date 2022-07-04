package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GetLockerStatusResponseModel;
import com.mdq.pojo.jsonresponse.InsertEmergencyMobileNoResponseModel;

public interface GetLockerStatusResponseInterface extends StateViewInterface {

    void generateInsertENOCall(GetLockerStatusResponseModel getLockerStatusResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);
}
