package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.GenerateOTPDataManager;
import com.mdq.DataManager.LoginDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.GenerateOTPRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.LoginRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.GenerateOTPResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.LoginResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateLoginRequestModel;
import com.mdq.pojo.jsonrequest.GenerateOTPRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.pojo.jsonresponse.GenerateOTPResponseModel;
import com.mdq.utils.ApiClass;

public class GenerateOTPRequestViewModel extends GenerateOTPRequestBaseViewModel implements GenerateOTPRequestInterface {

    private GenerateOTPDataManager generateOTPDataManager;
    private GenerateOTPResponseInterface generateLoginProcessed;
    private Context mContext;

    public GenerateOTPRequestViewModel(Context mContext, GenerateOTPResponseInterface loginResponseInterface) {
        this.generateLoginProcessed = loginResponseInterface;
        this.generateOTPDataManager = new GenerateOTPDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateOTP() {
        GenerateOTPRequestModel generateOTPRequestModel=new GenerateOTPRequestModel();
        generateOTPRequestModel.mobile=getMobile();
        generateOTPDataManager.callEnqueue(ApiClass.GENERATEOTP, generateOTPRequestModel, new ResponseHandler<GenerateOTPResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GenerateOTPResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    generateLoginProcessed.generateOTPProcessed(item);
                }
            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    generateLoginProcessed.onFailure(errorBody, statusCode);
                }
            }
        });
    }

    @Override
    public void generateOTPRequest() {
        goGenerateOTP();
    }

}

