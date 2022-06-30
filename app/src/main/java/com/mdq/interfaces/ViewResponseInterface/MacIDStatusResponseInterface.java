package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;

public interface MacIDStatusResponseInterface  extends StateViewInterface {

    void generateMacIDStatus(MacIDStatusResponseModel macIDStatusResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}
