package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.VerificationKeyDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.VerificationKeyRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.VerificationKeyResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateVerificationKeyRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateVerificationKeyResponseModel;
import com.mdq.utils.ApiClass;


public class VerificationKeyViewModel extends VerificationKeyBaseViewModel implements VerificationKeyRequestInterface {
    private VerificationKeyDataManager verificationKeyDataManager;
    private VerificationKeyResponseInterface verificationKeyResponseInterface;
    private Context mContext;

    public VerificationKeyViewModel(Context mContext, VerificationKeyResponseInterface verificationKeyResponseInterface) {
        this.verificationKeyResponseInterface = verificationKeyResponseInterface;
        this.verificationKeyDataManager = new VerificationKeyDataManager(mContext);
        this.mContext = mContext;
    }

    private void goVerification() {
        GenerateVerificationKeyRequestModel generateVerificationKeyRequestModel=new GenerateVerificationKeyRequestModel();
        generateVerificationKeyRequestModel.app_key=getApp_key();
        verificationKeyDataManager.callEnqueue(ApiClass.VERIFICATION, generateVerificationKeyRequestModel, new ResponseHandler<GenerateVerificationKeyResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GenerateVerificationKeyResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    verificationKeyResponseInterface.generateVarification(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    verificationKeyResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }


    @Override
    public void generateVerificationRequest() {
        goVerification();
    }
}


