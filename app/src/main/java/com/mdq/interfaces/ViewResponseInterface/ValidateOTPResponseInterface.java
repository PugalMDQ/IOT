package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateRegisterResponseModel;
import com.mdq.pojo.jsonresponse.GenerateValidateOTPResponseModel;

public interface ValidateOTPResponseInterface extends StateViewInterface {

    void generateValidateOTPProcessed(GenerateValidateOTPResponseModel generateValidateOTPResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}
