package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.LoginDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.LoginRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.LoginResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateLoginRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.utils.ApiClass;


public class LoginRequestViewModel extends LoginRequestBaseViewModel implements LoginRequestInterface {

    private LoginDataManager loginDataManager;
    private LoginResponseInterface loginResponseInterface;
    private Context mContext;

    public LoginRequestViewModel(Context mContext, LoginResponseInterface loginResponseInterface) {
        this.loginResponseInterface = loginResponseInterface;
        this.loginDataManager = new LoginDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateLogin() {
        GenerateLoginRequestModel generateLoginRequestModel=new GenerateLoginRequestModel();
        generateLoginRequestModel.email=getEmail();
        generateLoginRequestModel.pwd=getPwd();
        loginDataManager.callEnqueue(ApiClass.LOGIN, generateLoginRequestModel, new ResponseHandler<GenerateLoginResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(GenerateLoginResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    loginResponseInterface.generateLoginProcessed(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    loginResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }


    @Override
    public void generateLoginRequest() {
        goGenerateLogin();
    }
}

