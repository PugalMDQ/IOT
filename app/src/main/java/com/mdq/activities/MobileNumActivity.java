package com.mdq.activities;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mdq.ViewModel.GenerateOTPRequestViewModel;
import com.mdq.ViewModel.numberValidationViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.GenerateOTPResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.numberValidationResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityMobileNumBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateNumberValidationResponseModel;
import com.mdq.pojo.jsonresponse.GenerateOTPResponseModel;
import com.mdq.utils.PreferenceManager;

import java.util.List;


public class MobileNumActivity extends AppCompatActivity implements numberValidationResponseInterface, GenerateOTPResponseInterface {

    PreferenceManager preferenceManager;
    ActivityMobileNumBinding activityMobileNumBinding;
    numberValidationViewModel numberValidationViewModel;
    GenerateOTPRequestViewModel generateOTPRequestViewModel;
    Dialog dialog_Spinner;
    TextView textView;
    ProgressBar progressBar;
    String mobile;
    String SIMmobileNum1;
    String SIMmobileNum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMobileNumBinding = ActivityMobileNumBinding.inflate(getLayoutInflater());
        setContentView(activityMobileNumBinding.getRoot());

        numberValidationViewModel = new numberValidationViewModel(this, this);
        generateOTPRequestViewModel = new GenerateOTPRequestViewModel(this, this);

        //set click
        click();
        rPermission();
    }

    private void rPermission() {

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            secondNumber();


        }else{

            requestPermission();
        }
    }

    private void permisionCheck() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_PHONE_STATE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        permisionCheck();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void secondNumber() {
        if (Build.VERSION.SDK_INT > 22) {
            //for dual sim mobile
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                //if there are two sims in dual sim mobile
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(0);
                SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(1);

                final String sim1 = simInfo.getNumber().toString();
                final String sim2 = simInfo1.getNumber().toString();

                SIMmobileNum1 = sim1.trim().substring(2);
                SIMmobileNum2 = sim2.trim();

                dialog();
                Log.i("sim1", SIMmobileNum1);
                Log.i("sim2", SIMmobileNum2);
            }else{
                GetNumber();
            }
        }
    }

    private void dialog() {

        Dialog dialog = new Dialog(this, R.style.dialog_center);
        dialog.setContentView(R.layout.dialog_for_number);
        dialog.setCanceledOnTouchOutside(false);
        TextView sim1 = dialog.findViewById(R.id.Sim1);
        TextView sim2 = dialog.findViewById(R.id.Sim2);

        sim1.setText(" +91  "+SIMmobileNum1);
        if(SIMmobileNum2!=null) {
            sim2.setText("+91  " + SIMmobileNum2);
        }else{

        }
        sim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMobileNumBinding.mobile.setText(SIMmobileNum1);
                dialog.dismiss();

            }
        });
        sim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMobileNumBinding.mobile.setText(SIMmobileNum2);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void click() {
        activityMobileNumBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        activityMobileNumBinding.GenerateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                if (activityMobileNumBinding.mobile.getText().toString() != null) {
                    if (activityMobileNumBinding.mobile.getText().toString().length() >= 10) {

                        if (activityMobileNumBinding.mobile.getText().toString().equals(SIMmobileNum1) ||
                                activityMobileNumBinding.mobile.getText().toString().equals(SIMmobileNum2)) {
                            mobile = activityMobileNumBinding.mobile.getText().toString().trim();
                            dialog_Spinner = new Dialog(MobileNumActivity.this, R.style.dialog_center);
                            dialog_Spinner.setContentView(R.layout.dialog_spinner);
                            dialog_Spinner.setCanceledOnTouchOutside(false);
                            progressBar = dialog_Spinner.findViewById(R.id.progress);
                            textView = dialog_Spinner.findViewById(R.id.subText);
                            textView.setText("Validating the mobile number.");
                            dialog_Spinner.show();

                            numberValidationViewModel.setMobile(activityMobileNumBinding.mobile.getText().toString());
                            numberValidationViewModel.generateNumberValidationRequest();

                        } else {
                            Toast.makeText(MobileNumActivity.this, "Use the mobile number available in this handset.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter correct mobile number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @return
     * @brief initializing the preferenceManager from shared preference for local use in this activity
     */
    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager.getInstance();
            preferenceManager.initialize(getApplicationContext());
        }
        return preferenceManager;
    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, String errorMessage) {

    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, ViewType viewType, String errorMessage) {

    }

    @Override
    public void generateLoginProcessed(GenerateNumberValidationResponseModel generateNumberValidationResponseModel) {

        if (generateNumberValidationResponseModel.getMessage().equals("Mobile number verified successfully")) {
            generateOTPRequestViewModel.setMobile(activityMobileNumBinding.mobile.getText().toString().trim());
            generateOTPRequestViewModel.generateOTPRequest();
            getPreferenceManager().setPrefMobile(activityMobileNumBinding.mobile.getText().toString().trim());
            startActivity(new Intent(MobileNumActivity.this, OTPVerification.class));

        } else {
            progressBar.setVisibility(View.GONE);
            textView.setText("Mobile number already exist!");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog_Spinner.dismiss();
                }
            }, 2000);

        }
    }

    @Override
    public void generateOTPProcessed(GenerateOTPResponseModel generateOTPResponseModel) {

    }

    @Override
    public void onFailure(ErrorBody errorBody, int statusCode) {

    }


    public void GetNumber() {

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = telephonyManager.getLine1Number();
            SIMmobileNum1 = phoneNumber.trim().substring(2);
            dialog();
        }

    }

    private void requestPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
            }
        }catch (Exception e){
            Log.i("Exception",""+e);
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                TelephonyManager telephonyManager1 = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                String phoneNumber = telephonyManager1.getLine1Number();
                SIMmobileNum1 = phoneNumber.trim().substring(2);
                dialog();
                secondNumber();

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}