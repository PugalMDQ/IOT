package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateOTPResponseModel;

public interface GenerateOTPResponseInterface extends StateViewInterface {

    void generateOTPProcessed(GenerateOTPResponseModel generateOTPResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);
}
