package com.mdq.http;

import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.pojo.jsonrequest.BioMetricRequestModel;
import com.mdq.pojo.jsonrequest.DeleteRequestModel;
import com.mdq.pojo.jsonrequest.FireBase_UIDRequestModel;
import com.mdq.pojo.jsonrequest.GenerateLoginRequestModel;
import com.mdq.pojo.jsonrequest.GenerateNumberValidationRequestModel;
import com.mdq.pojo.jsonrequest.GenerateOTPRequestModel;
import com.mdq.pojo.jsonrequest.GenerateRegisterRequestModel;
import com.mdq.pojo.jsonrequest.GenerateSetMAc_IDRequestModel;
import com.mdq.pojo.jsonrequest.GenerateValidateOTPRequestModel;
import com.mdq.pojo.jsonrequest.GenerateVerificationKeyRequestModel;
import com.mdq.pojo.jsonrequest.MacIDStatusRequestModel;
import com.mdq.pojo.jsonresponse.BioMetricResponseModel;
import com.mdq.pojo.jsonresponse.DeleteResponseModel;
import com.mdq.pojo.jsonresponse.FireBase_UIDResponseModel;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.pojo.jsonresponse.GenerateNumberValidationResponseModel;
import com.mdq.pojo.jsonresponse.GenerateOTPResponseModel;
import com.mdq.pojo.jsonresponse.GenerateRegisterResponseModel;
import com.mdq.pojo.jsonresponse.GenerateSetMac_IDResponseModel;
import com.mdq.pojo.jsonresponse.GenerateValidateOTPResponseModel;
import com.mdq.pojo.jsonresponse.GenerateVerificationKeyResponseModel;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST
    Call<GenerateVerificationKeyResponseModel> generateVerificationCall(@Url String url, @Body GenerateVerificationKeyRequestModel generateVerificationKeyRequestModel);

    @POST
    Call<GenerateLoginResponseModel> generatePostLoginCall(@Url String url, @Body GenerateLoginRequestModel generateLoginRequestModel);

    @POST
    Call<GenerateRegisterResponseModel> generatePostRegisterCall(@Url String url, @Body GenerateRegisterRequestModel generateRegisterRequestModel);

    @POST
    Call<GenerateNumberValidationResponseModel> generatePostNumValidationCall(@Url String url, @Body GenerateNumberValidationRequestModel generateNumberValidationRequestModel);

    @POST
    Call<GenerateOTPResponseModel> generateOTPall(@Url String url, @Body GenerateOTPRequestModel generateOTPRequestModel);

    @POST
    Call<GenerateValidateOTPResponseModel> validateOTPall(@Url String url, @Body GenerateValidateOTPRequestModel generateValidateOTPRequestModel);

    @POST
    Call<GenerateSetMac_IDResponseModel> SetMAc_IDCall(@Url String url, @Body GenerateSetMAc_IDRequestModel generateValidateOTPRequestModel);

    @POST
    Call<DeleteResponseModel> DELETE(@Url String url, @Body DeleteRequestModel deleteRequestModele);

    @POST
    Call<FireBase_UIDResponseModel> FireBase_UID(@Url String url, @Body FireBase_UIDRequestModel fireBase_uidRequestModel);

    @POST
    Call<MacIDStatusResponseModel> MacIDStatusCall(@Url String url, @Body MacIDStatusRequestModel macIDStatusRequestModel);

    @POST
    Call<BioMetricResponseModel> BioMetricEnableCall(@Url String url, @Body BioMetricRequestModel bioMetricRequestModel);

}
