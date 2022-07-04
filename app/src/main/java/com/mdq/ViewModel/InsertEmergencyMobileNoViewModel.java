package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.mdq.DataManager.InsertEmergencyMobileNoDataManager;
import com.mdq.DataManager.LoginDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.InsertEmergencyMobileNORequestInterface;
import com.mdq.interfaces.ViewResponseInterface.InsertEmergencyMobileNoResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.LoginResponseInterface;
import com.mdq.pojo.jsonrequest.GenerateLoginRequestModel;
import com.mdq.pojo.jsonrequest.InsertEmergencyMobileNoRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.pojo.jsonresponse.InsertEmergencyMobileNoResponseModel;
import com.mdq.utils.ApiClass;

public class InsertEmergencyMobileNoViewModel extends InsertEmergencyMobileNoBaseViewModel implements InsertEmergencyMobileNORequestInterface {

    private InsertEmergencyMobileNoDataManager insertEmergencyMobileNoDataManager;
    private InsertEmergencyMobileNoResponseInterface insertEmergencyMobileNoResponseInterface;
    private Context mContext;

    public InsertEmergencyMobileNoViewModel(Context mContext, InsertEmergencyMobileNoResponseInterface insertEmergencyMobileNoResponseInterface) {
        this.insertEmergencyMobileNoResponseInterface = insertEmergencyMobileNoResponseInterface;
        this.insertEmergencyMobileNoDataManager = new InsertEmergencyMobileNoDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateLogin() {
        InsertEmergencyMobileNoRequestModel insertEmergencyMobileNoRequestModel=new InsertEmergencyMobileNoRequestModel();
        insertEmergencyMobileNoRequestModel.mobile=getMobile();
        insertEmergencyMobileNoRequestModel.emergencyNumber=getEmergencyNumber();
        insertEmergencyMobileNoDataManager.callEnqueue(ApiClass.EMERGENCYNUMBER, insertEmergencyMobileNoRequestModel, new ResponseHandler<InsertEmergencyMobileNoResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(InsertEmergencyMobileNoResponseModel item, String message) {
                Log.i("otpR","rr");
                if(item.getMessage()!=null) {
                    Log.i("otpRecevied", item.getMessage());
                    insertEmergencyMobileNoResponseInterface.generateInsertENOCall(item);
                }

            }
            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if(statusCode>=400 && statusCode<500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    insertEmergencyMobileNoResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }
    @Override
    public void InsertENORequest() {
        goGenerateLogin();
    }
}

