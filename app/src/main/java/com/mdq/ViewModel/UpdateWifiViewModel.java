package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.UpdateWifiDataManager;
import com.mdq.DataManager.WifiConfigurationDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.UpdateWifiRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.WifiConfigurationRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.UpdateWifiResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.WifiConfigurationResponseInterface;
import com.mdq.pojo.jsonrequest.UpdateWifiRequestModel;
import com.mdq.pojo.jsonrequest.WifiConfigurationRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.UpdateWifiResponseModel;
import com.mdq.pojo.jsonresponse.WifiConfigurationResponseModel;
import com.mdq.utils.ApiClass;

public class UpdateWifiViewModel extends UpdateWifiBaseViewModel implements UpdateWifiRequestInterface {
    private UpdateWifiDataManager updateWifiDataManager;
    private UpdateWifiResponseInterface updateWifiResponseInterface;
    private Context mContext;

    public UpdateWifiViewModel(Context mContext, UpdateWifiResponseInterface updateWifiResponseInterface) {
        this.updateWifiResponseInterface = updateWifiResponseInterface;
        this.updateWifiDataManager = new UpdateWifiDataManager(mContext);
        this.mContext = mContext;
    }

    private void goVerification() {
        UpdateWifiRequestModel updateWifiRequestModel=new UpdateWifiRequestModel();
        updateWifiRequestModel.mobile=getMobile();
        updateWifiRequestModel.wifi_pin=getWifi_pin();
        updateWifiRequestModel.wifi_ssd=getWifi_ssd();

        updateWifiDataManager.callEnqueue(ApiClass.UPDATEWIFI, getAuth(),updateWifiRequestModel, new ResponseHandler<UpdateWifiResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(UpdateWifiResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    updateWifiResponseInterface.generateUpdateWifiCall(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    updateWifiResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }

    @Override
    public void UpdateWifiRequest() {
        goVerification();
    }
}


