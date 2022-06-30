package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.mdq.DataManager.LoginDataManager;
import com.mdq.DataManager.MacIDStatusUpdateDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.MacIDStatusRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.LoginResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateLoginRequestModel;
import com.mdq.pojo.jsonrequest.MacIDStatusRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;
import com.mdq.utils.ApiClass;

public class MacIDStatusViewModel extends MacIDStatusBaseViewModel implements MacIDStatusRequestInterface {

    private MacIDStatusUpdateDataManager macIDStatusUpdateDataManager;
    private MacIDStatusResponseInterface macIDStatusResponseInterface;
    private Context mContext;

    public MacIDStatusViewModel(Context mContext, MacIDStatusResponseInterface macIDStatusResponseInterface) {
        this.macIDStatusResponseInterface = macIDStatusResponseInterface;
        this.macIDStatusUpdateDataManager = new MacIDStatusUpdateDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateStatusCall() {
        MacIDStatusRequestModel macIDStatusRequestModel=new MacIDStatusRequestModel();
        macIDStatusRequestModel.mobile=getMobile();
        macIDStatusRequestModel.macid_status=getMacid_status();
        macIDStatusUpdateDataManager.callEnqueue(ApiClass.MACID_STATUS, macIDStatusRequestModel, new ResponseHandler<MacIDStatusResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(MacIDStatusResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    macIDStatusResponseInterface.generateMacIDStatus(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    macIDStatusResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }


    @Override
    public void generateMacIDStatusCall() {
        goGenerateStatusCall();
    }
}

