package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.RegisterDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.RegisterRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.RegisterResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateRegisterRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateRegisterResponseModel;
import com.mdq.utils.ApiClass;


public class RegisterRequestViewModel extends RegisterRequestBaseViewModel implements RegisterRequestInterface {
    private RegisterDataManager registerDataManager;
    private RegisterResponseInterface registerResponseInterface;
    private Context mContext;

    public RegisterRequestViewModel(Context mContext, RegisterResponseInterface registerResponseInterface) {
        this.registerResponseInterface = registerResponseInterface;
        this.registerDataManager = new RegisterDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateLogin() {
        GenerateRegisterRequestModel generateRegisterRequestModel=new GenerateRegisterRequestModel();
        generateRegisterRequestModel.email=getEmail();
        generateRegisterRequestModel.pwd=getPwd();
        generateRegisterRequestModel.username=getUsername();
        generateRegisterRequestModel.mobile=getMobile();
        registerDataManager.callEnqueue(ApiClass.REGISTER, generateRegisterRequestModel, new ResponseHandler<GenerateRegisterResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GenerateRegisterResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    registerResponseInterface.generateRegisterProcessed(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    registerResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }



    @Override
    public void generateRegisterRequest() {
        goGenerateLogin();
    }
}



