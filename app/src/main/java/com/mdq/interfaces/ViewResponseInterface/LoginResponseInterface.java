package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;


public interface LoginResponseInterface extends StateViewInterface {

    void generateLoginProcessed(GenerateLoginResponseModel generateLoginResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);
}
