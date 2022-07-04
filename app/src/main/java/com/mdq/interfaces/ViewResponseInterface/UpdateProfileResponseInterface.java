package com.mdq.interfaces.ViewResponseInterface;

import com.mdq.interfaces.StateViewInterface;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.UpdateProfileResponseModel;
import com.mdq.pojo.jsonresponse.WifiConfigurationResponseModel;

public interface UpdateProfileResponseInterface extends StateViewInterface {

    void generateUserProfileUpdateCall(UpdateProfileResponseModel updateProfileResponseModel);
    void onFailure(ErrorBody errorBody, int statusCode);

}
