package com.mdq.DataManager;

import static com.mdq.base.VSafeApp.getApp;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mdq.http.ApiInterface;
import com.mdq.interfaces.ResponseHandler;
import com.mdq.pojo.jsonrequest.BioMetricRequestModel;
import com.mdq.pojo.jsonrequest.GenerateLoginRequestModel;
import com.mdq.pojo.jsonresponse.BioMetricResponseModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BioMetricDataManager {

    private final String TAG = BioMetricDataManager.class.getSimpleName();
    private ApiInterface apiInterface;
    Context context;

    public BioMetricDataManager(Context context) {
        this.context = context;
        this.apiInterface = getApp().getRetrofitInterface();
    }

    public void callEnqueue(String url, BioMetricRequestModel bioMetricRequestModel, final ResponseHandler<BioMetricResponseModel> dataresponse) {

        //calling the generatePostLoginCall methode from call apiInterface
        Call<BioMetricResponseModel> userMpinCall = apiInterface.BioMetricEnableCall(url, bioMetricRequestModel);
        userMpinCall.enqueue(new Callback<BioMetricResponseModel>() {

            /**
             * @param call
             * @param response
             * @breif getting response from api
             */
            @Override
            public void onResponse(Call<BioMetricResponseModel> call, Response<BioMetricResponseModel> response) {
                /**
                 * Invoked for a received HTTP response.
                 * <p>
                 * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                 * Call {@link Response#isSuccessful()} to determine if the response indicates success.
                 *
                 * @param call
                 * @param response
                 */
                Log.i("responce", "response get");
                int statusCode = response.code();

                //if response is successful set the body of response to onSuccess methode in GenerateRegisterResponseModel else get the error body and set on onFailure in generateRegisterResponseModel
                if (response.isSuccessful()) {
                    dataresponse.onSuccess(response.body(), "SuccessModel");
                } else {
                    String serviceResponse = null;
                    try {
                        serviceResponse = response.errorBody().string();
                        ErrorBody errorBody = new Gson().fromJson(serviceResponse, ErrorBody.class);

                        dataresponse.onFailure(errorBody, statusCode);

                    } catch (JsonSyntaxException e) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            /**
             * @param call
             * @param t
             * @breif api call failure
             */
            @Override
            public void onFailure(Call<BioMetricResponseModel> call, Throwable t) {
                Log.d(TAG, "onTokenExpired: " + t.getMessage());
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
