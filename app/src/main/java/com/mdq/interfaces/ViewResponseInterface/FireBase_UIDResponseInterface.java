package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.FireBase_UIDResponseModel;

public interface FireBase_UIDResponseInterface extends StateViewInterface {

    void generateFireBase_UIDCallProcessed(FireBase_UIDResponseModel fireBase_uidResponseModel);

    void onFailure(ErrorBody errorBody, int statusCode);

}