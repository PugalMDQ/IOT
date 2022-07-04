package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.VerificationKeyDataManager;
import com.mdq.DataManager.WifiConfigurationDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.VerificationKeyRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.WifiConfigurationRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.VerificationKeyResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.WifiConfigurationResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateVerificationKeyRequestModel;
import com.mdq.pojo.jsonrequest.WifiConfigurationRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateVerificationKeyResponseModel;
import com.mdq.pojo.jsonresponse.WifiConfigurationResponseModel;
import com.mdq.utils.ApiClass;

public class WifiConfigurationViewModel extends WifiConfigurationBaseViewModel implements WifiConfigurationRequestInterface {
    private WifiConfigurationDataManager wifiConfigurationDataManager;
    private WifiConfigurationResponseInterface wifiConfigurationResponseInterface;
    private Context mContext;

    public WifiConfigurationViewModel(Context mContext, WifiConfigurationResponseInterface wifiConfigurationResponseInterface) {
        this.wifiConfigurationResponseInterface = wifiConfigurationResponseInterface;
        this.wifiConfigurationDataManager = new WifiConfigurationDataManager(mContext);
        this.mContext = mContext;
    }

    private void goVerification() {
        WifiConfigurationRequestModel wifiConfigurationRequestModel=new WifiConfigurationRequestModel();
        wifiConfigurationRequestModel.mobile=getMobile();
        wifiConfigurationRequestModel.wifi_pin=getWifi_pin();
        wifiConfigurationRequestModel.wifi_ssd=getWifi_ssd();

        wifiConfigurationDataManager.callEnqueue(ApiClass.INSERTWIFI, wifiConfigurationRequestModel, new ResponseHandler<WifiConfigurationResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(WifiConfigurationResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    wifiConfigurationResponseInterface.generateVarification(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    wifiConfigurationResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }
    @Override
    public void WifiConfigurationRequest() {
        goVerification();
    }
}


