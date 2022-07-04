package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.GetUserDataManager;
import com.mdq.DataManager.InsertEmergencyMobileNoDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.GetUserRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.InsertEmergencyMobileNORequestInterface;
import com.mdq.interfaces.ViewResponseInterface.GetUserResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.InsertEmergencyMobileNoResponseInterface;
import com.mdq.pojo.jsonrequest.GetUserRequestModel;
import com.mdq.pojo.jsonrequest.InsertEmergencyMobileNoRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GetUserResponseModel;
import com.mdq.pojo.jsonresponse.InsertEmergencyMobileNoResponseModel;
import com.mdq.utils.ApiClass;

public class GetUserViewModel extends GetUserBaseViewModel implements GetUserRequestInterface {

    private GetUserDataManager getUserDataManager;
    private GetUserResponseInterface getUserResponseInterface;
    private Context mContext;

    public GetUserViewModel(Context mContext, GetUserResponseInterface getUserResponseInterface) {
        this.getUserResponseInterface = getUserResponseInterface;
        this.getUserDataManager = new GetUserDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateLogin() {
        GetUserRequestModel getUserRequestModel=new GetUserRequestModel();
        getUserRequestModel.mobile=getMobile();
        getUserDataManager.callEnqueue(ApiClass.GETUSER, getAuth(),getUserRequestModel, new ResponseHandler<GetUserResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GetUserResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    getUserResponseInterface.generateGetUserCall(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    getUserResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }
    @Override
    public void GetUserRequestCall() {
        goGenerateLogin();
    }
}

