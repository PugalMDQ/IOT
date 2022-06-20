package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.ValidateOTPDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.ValidateOTPRequestInterface;
import com.mdq.interfaces.ViewRequestInterface.VerificationKeyRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.ValidateOTPResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.VerificationKeyResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateOTPRequestModel;
import com.mdq.pojo.jsonrequest.GenerateValidateOTPRequestModel;
import com.mdq.pojo.jsonrequest.GenerateVerificationKeyRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateValidateOTPResponseModel;
import com.mdq.pojo.jsonresponse.GenerateVerificationKeyResponseModel;
import com.mdq.utils.ApiClass;

public class ValidateOTPResponseViewModel extends ValidateOTPRequestBaseViewModel implements ValidateOTPRequestInterface {
    private ValidateOTPDataManager validateOTPDataManager;
    private ValidateOTPResponseInterface validateOTPResponseInterface;
    private Context mContext;

    public ValidateOTPResponseViewModel(Context mContext, ValidateOTPResponseInterface validateOTPResponseInterface) {
        this.validateOTPResponseInterface = validateOTPResponseInterface;
        this.validateOTPDataManager = new ValidateOTPDataManager(mContext);
        this.mContext = mContext;
    }

    private void goVerification() {
        GenerateValidateOTPRequestModel generateValidateOTPRequestModel=new GenerateValidateOTPRequestModel();
        generateValidateOTPRequestModel.mobile=getMobile();
        generateValidateOTPRequestModel.otp_number=getOtp_number();
        validateOTPDataManager.callEnqueue(ApiClass.VALIDATEOTP, generateValidateOTPRequestModel, new ResponseHandler<GenerateValidateOTPResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GenerateValidateOTPResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    validateOTPResponseInterface.generateValidateOTPProcessed(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    validateOTPResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }

    @Override
    public void generateValidationOTPRequest() {
        goVerification();
    }
}