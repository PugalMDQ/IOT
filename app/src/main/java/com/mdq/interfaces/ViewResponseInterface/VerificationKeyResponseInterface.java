package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateVerificationKeyResponseModel;


public interface VerificationKeyResponseInterface  extends StateViewInterface {

    void generateVarification(GenerateVerificationKeyResponseModel generateVerificationKeyResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}
