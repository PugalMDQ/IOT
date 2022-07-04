package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.GetWifiDataManager;
import com.mdq.DataManager.InsertEmergencyMobileNoDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.GetWifiRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.InsertEmergencyMobileNORequestInterface;
import com.mdq.interfaces.ViewResponseInterface.GetWifiResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.InsertEmergencyMobileNoResponseInterface;
import com.mdq.pojo.jsonrequest.GetWifiRequestModel;
import com.mdq.pojo.jsonrequest.InsertEmergencyMobileNoRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GetWifiResponseModel;
import com.mdq.pojo.jsonresponse.InsertEmergencyMobileNoResponseModel;
import com.mdq.utils.ApiClass;

public class GetWifiViewModel  extends GetWifiBaseViewModel implements GetWifiRequestInterface {

    private GetWifiDataManager getWifiDataManager;
    private GetWifiResponseInterface getWifiResponseInterface;
    private Context mContext;

    public GetWifiViewModel(Context mContext, GetWifiResponseInterface getWifiResponseInterface) {
        this.getWifiResponseInterface = getWifiResponseInterface;
        this.getWifiDataManager = new GetWifiDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateLogin() {
        GetWifiRequestModel getWifiRequestModel=new GetWifiRequestModel();
        getWifiRequestModel.mobile=getMobile();
        getWifiDataManager.callEnqueue(ApiClass.EMERGENCYNUMBER,getAuth(), getWifiRequestModel, new ResponseHandler<GetWifiResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GetWifiResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    getWifiResponseInterface.generateGetWifiCall(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    getWifiResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }

    @Override
    public void GetWifiRequestCall() {
        goGenerateLogin();
    }
}

