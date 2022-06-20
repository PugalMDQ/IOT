package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateNumberValidationResponseModel;

public interface numberValidationResponseInterface extends StateViewInterface {

    void generateLoginProcessed(GenerateNumberValidationResponseModel generateNumberValidationResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}