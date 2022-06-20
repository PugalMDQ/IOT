package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateRegisterResponseModel;


public interface RegisterResponseInterface extends StateViewInterface {

    void generateRegisterProcessed(GenerateRegisterResponseModel generateRegisterResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);
}
