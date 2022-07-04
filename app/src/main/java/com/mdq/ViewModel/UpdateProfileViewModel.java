package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.UpdateProfileDataManager;
import com.mdq.DataManager.WifiConfigurationDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.UpdateProfileRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.WifiConfigurationRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.UpdateProfileResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.WifiConfigurationResponseInterface;
import com.mdq.pojo.jsonrequest.UpdateProfileRequestModel;
import com.mdq.pojo.jsonrequest.WifiConfigurationRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.UpdateProfileResponseModel;
import com.mdq.pojo.jsonresponse.WifiConfigurationResponseModel;
import com.mdq.utils.ApiClass;

public class UpdateProfileViewModel extends UpdateProfileBaseViewModel implements UpdateProfileRequestInterface {
    private UpdateProfileDataManager updateProfileDataManager;
    private UpdateProfileResponseInterface updateProfileResponseInterface;
    private Context mContext;

    public UpdateProfileViewModel(Context mContext, UpdateProfileResponseInterface updateProfileResponseInterface) {
        this.updateProfileResponseInterface = updateProfileResponseInterface;
        this.updateProfileDataManager = new UpdateProfileDataManager(mContext);
        this.mContext = mContext;
    }

    private void goVerification() {
        UpdateProfileRequestModel updateProfileRequestModel=new UpdateProfileRequestModel();
        updateProfileRequestModel.mobile=getMobile();
        updateProfileRequestModel.emergency_mobile=getEmergency_mobile();
        updateProfileRequestModel.username=getUsername();
        updateProfileRequestModel.pwd =getPwd();

        updateProfileDataManager.callEnqueue(ApiClass.UPDATEUSERPROFILE,getAuth(), updateProfileRequestModel, new ResponseHandler<UpdateProfileResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(UpdateProfileResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    updateProfileResponseInterface.generateUserProfileUpdateCall(item);
                }
            }

            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    updateProfileResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }

    @Override
    public void UpdateUserRequest() {
        goVerification();
    }
}


