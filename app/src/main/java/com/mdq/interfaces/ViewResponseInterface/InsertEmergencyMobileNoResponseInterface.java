package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateOTPResponseModel;
import com.mdq.pojo.jsonresponse.InsertEmergencyMobileNoResponseModel;

public interface InsertEmergencyMobileNoResponseInterface  extends StateViewInterface {

    void generateInsertENOCall(InsertEmergencyMobileNoResponseModel insertEmergencyMobileNoResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);
}
