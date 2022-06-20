package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.LoginDataManager;
import com.mdq.DataManager.numberValidationDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.numberValidationRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.LoginResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.numberValidationResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateLoginRequestModel;
import com.mdq.pojo.jsonrequest.GenerateNumberValidationRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.pojo.jsonresponse.GenerateNumberValidationResponseModel;
import com.mdq.utils.ApiClass;

public class numberValidationViewModel extends numberValidationBaseViewModel implements numberValidationRequestInterface {

    private numberValidationDataManager numberValidationDataManager;
    private numberValidationResponseInterface numberValidationResponseInterface;
    private Context mContext;

    public numberValidationViewModel(Context mContext, numberValidationResponseInterface numberValidationResponseInterface) {
        this.numberValidationResponseInterface = numberValidationResponseInterface;
        this.numberValidationDataManager = new numberValidationDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateLogin() {
        GenerateNumberValidationRequestModel generateNumberValidationRequestModel=new GenerateNumberValidationRequestModel();
        generateNumberValidationRequestModel.mobile=getMobile();

        numberValidationDataManager.callEnqueue(ApiClass.existmobile, generateNumberValidationRequestModel, new ResponseHandler<GenerateNumberValidationResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GenerateNumberValidationResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    numberValidationResponseInterface.generateLoginProcessed(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    numberValidationResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }
    @Override
    public void generateNumberValidationRequest() {
        goGenerateLogin();
    }
}

