package com.mdq.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.mdq.DataManager.BioMetricDataManager;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.interfaces.ViewRequestInterface.BioMetricRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.BioMetricResponseInterface;
import com.mdq.pojo.jsonrequest.BioMetricRequestModel;
import com.mdq.pojo.jsonresponse.BioMetricResponseModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.utils.ApiClass;

public class BioMetricViewModel extends BioMetricBaseViewModel implements BioMetricRequestInterface {

    private BioMetricDataManager bioMetricDataManager;
    private BioMetricResponseInterface bioMetricResponseInterface;
    private Context mContext;

    public BioMetricViewModel(Context mContext, BioMetricResponseInterface bioMetricResponseInterface) {
        this.bioMetricResponseInterface = bioMetricResponseInterface;
        this.bioMetricDataManager = new BioMetricDataManager(mContext);
        this.mContext = mContext;
    }

    private void setBioMetricCall() {
        BioMetricRequestModel bioMetricRequestModel = new BioMetricRequestModel();
        bioMetricRequestModel.mobile = getMobile();
        bioMetricRequestModel.biometric = getBiometric();
        bioMetricDataManager.callEnqueue(ApiClass.BIOMETRIC, bioMetricRequestModel, new ResponseHandler<BioMetricResponseModel>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onSuccess(BioMetricResponseModel item, String message) {
                Log.i("otpR", "rr");
                if (item.getMessage() != null) {
                    Log.i("otpRecevied", item.getMessage());
                    bioMetricResponseInterface.BioMetricCall(item);
                }
            }

            @Override
            public void onFailure(ErrorBody errorBody, int statusCode) {
                if (statusCode >= 400 && statusCode < 500) {
                    Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    bioMetricResponseInterface.onFailure(errorBody, statusCode);
                }
            }
        });
    }

    @Override
    public void generateBioMetricRequestCall() {
        setBioMetricCall();
    }

}