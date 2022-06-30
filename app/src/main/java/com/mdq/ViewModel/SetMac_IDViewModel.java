package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.SetMac_IDDataManager;
import com.mdq.DataManager.ValidateOTPDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.SetMac_IDRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.ValidateOTPRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.SetMac_IDResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.ValidateOTPResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateSetMAc_IDRequestModel;
import com.mdq.pojo.jsonrequest.GenerateValidateOTPRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateSetMac_IDResponseModel;
import com.mdq.pojo.jsonresponse.GenerateValidateOTPResponseModel;
import com.mdq.utils.ApiClass;

public class SetMac_IDViewModel extends SetMac_IDBaseViewModel implements SetMac_IDRequestInterface {
    private SetMac_IDDataManager setMac_idDataManager;
    private SetMac_IDResponseInterface setMac_idResponseInterface;
    private Context mContext;

    public SetMac_IDViewModel(Context mContext, SetMac_IDResponseInterface setMac_idResponseInterface) {
        this.setMac_idResponseInterface = setMac_idResponseInterface;
        this.setMac_idDataManager = new SetMac_IDDataManager(mContext);
        this.mContext = mContext;
    }

    private void goVerification() {
        GenerateSetMAc_IDRequestModel generateSetMAc_idRequestModel=new GenerateSetMAc_IDRequestModel();
        generateSetMAc_idRequestModel.mobile=getMobile();
        generateSetMAc_idRequestModel.mac_id=getMac_id();

        setMac_idDataManager.callEnqueue(ApiClass.MAC_ID, generateSetMAc_idRequestModel, new ResponseHandler<GenerateSetMac_IDResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GenerateSetMac_IDResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    setMac_idResponseInterface.generateSetMAc_IDProcess(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    setMac_idResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }

   @Override
    public void SetMAc_IDRequest() {
        goVerification();
    }
}