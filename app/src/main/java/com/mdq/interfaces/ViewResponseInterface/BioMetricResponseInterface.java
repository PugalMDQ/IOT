package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.BioMetricResponseModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;

public interface BioMetricResponseInterface extends StateViewInterface {

    void BioMetricCall(BioMetricResponseModel bioMetricResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}
