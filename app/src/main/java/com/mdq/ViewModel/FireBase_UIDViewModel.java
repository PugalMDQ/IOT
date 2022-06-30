package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mdq.DataManager.FireBaseUIDDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.FireBase_UIDRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.FireBase_UIDResponseInterface;
import com.mdq.pojo.jsonrequest.FireBase_UIDRequestModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.FireBase_UIDResponseModel;
import com.mdq.utils.ApiClass;

public class FireBase_UIDViewModel extends FireBase_UIDBaseViewModel implements FireBase_UIDRequestInterface {

    private FireBaseUIDDataManager fireBaseUIDDataManager;
    private FireBase_UIDResponseInterface fireBase_uidResponseInterface;
    private Context mContext;

    public FireBase_UIDViewModel(Context mContext, FireBase_UIDResponseInterface fireBase_uidResponseInterface) {
        this.fireBase_uidResponseInterface = fireBase_uidResponseInterface;
        this.fireBaseUIDDataManager = new FireBaseUIDDataManager(mContext);
        this.mContext = mContext;
    }

    private void goGenerateFireBase() {
        FireBase_UIDRequestModel fireBase_uidRequestModel = new FireBase_UIDRequestModel();
        fireBase_uidRequestModel.mobile = getMobile();
        fireBase_uidRequestModel.firebase_uid = getFirebase_uid();
        fireBaseUIDDataManager.callEnqueue(ApiClass.FIREBASE_UID, fireBase_uidRequestModel, new ResponseHandler<FireBase_UIDResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(FireBase_UIDResponseModel item, String message) {
                Log.i("otpR", "rr");
                if (item.getMessage() != null) {
                    Log.i("otpRecevied", item.getMessage());
                    fireBase_uidResponseInterface.generateFireBase_UIDCallProcessed(item);
                }
            }

            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if (statusCode >= 400 && statusCode < 500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    fireBase_uidResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }

    @Override
    public void FireBase_UIDcall() {
        goGenerateFireBase();
    }
}