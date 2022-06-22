package com.mdq.activities;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mdq.ViewModel.LoginRequestViewModel;
import com.mdq.ViewModel.VerificationKeyViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.LoginResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.VerificationKeyResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityLoginBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateLoginResponseModel;
import com.mdq.pojo.jsonresponse.GenerateVerificationKeyResponseModel;
import com.mdq.utils.PreferenceManager;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements VerificationKeyResponseInterface, LoginResponseInterface {

    ActivityLoginBinding getActivityLoginBinding;
    boolean passwordVisibility;
    EditText password;
    PreferenceManager preferenceManager;
    String pass;
    LoginRequestViewModel loginRequestViewModel;
    VerificationKeyViewModel verificationKeyViewModel;
    ConnectivityManager connectivityManager;
    String SIMmobileNum1;
    String SIMmobileNum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(getActivityLoginBinding.getRoot());
        password = findViewById(R.id.password);
        loginRequestViewModel = new LoginRequestViewModel(this, this);
        verificationKeyViewModel = new VerificationKeyViewModel(this, this);

        connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //password visibility
        password();
        //set click
        click();
        //verification api call
        verification();
        GetNumber();
        secondNumber();

    }

    private void verification() {

        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            verificationKeyViewModel.app_key = "76736166656d6f62696c656b657932303232";
            verificationKeyViewModel.generateVerificationRequest();
        } else {
            Toast.makeText(getApplicationContext(), "Application required Internet ", Toast.LENGTH_SHORT).show();
        }
    }

    private void click() {
        getActivityLoginBinding.SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MobileNumActivity.class);
                startActivity(intent);
            }
        });

        getActivityLoginBinding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivityLoginBinding.email.getText().toString().length() == 10) {
                    if (password.getText().toString().length() >= 1) {
                        if (getPreferenceManager().getPrefVerification().equals("Mobile")) {
                            if ((connectivityManager
                                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                                    || (connectivityManager
                                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                                    .getState() == NetworkInfo.State.CONNECTED)) {
                                if (getActivityLoginBinding.email.getText().toString().equals(SIMmobileNum1) || getActivityLoginBinding.email.getText().toString().equals(SIMmobileNum2)) {
                                    loginRequestViewModel.email = getActivityLoginBinding.email.getText().toString();
                                    loginRequestViewModel.pwd = getActivityLoginBinding.password.getText().toString();
                                    loginRequestViewModel.generateLoginRequest();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Use the mobile number available in this handset.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Application required Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not verified", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void password() {

        getActivityLoginBinding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    pass = s.toString();
                    getActivityLoginBinding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                } else {
                    pass = s.toString();
                    getActivityLoginBinding.email.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= password.getRight() - password.getCompoundDrawables()[Right].getBounds().width()) {

                        int selection = password.getSelectionEnd();
                        if (passwordVisibility) {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisibility = false;
                        } else {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisibility = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, String errorMessage) {

    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, ViewType viewType, String errorMessage) {

    }

    @Override
    public void generateVarification(GenerateVerificationKeyResponseModel generateVerificationKeyResponseModel) {

        if (generateVerificationKeyResponseModel.data.get(0).applications != null) {
            getPreferenceManager().setPrefVerification(generateVerificationKeyResponseModel.data.get(0).applications);
        }

    }

    @Override
    public void generateLoginProcessed(GenerateLoginResponseModel generateLoginResponseModel) {

        Toast.makeText(getApplicationContext(), "" + generateLoginResponseModel.message, Toast.LENGTH_SHORT).show();

        if (generateLoginResponseModel.getMessage().equals("Logged in successfully")) {

            if (generateLoginResponseModel.getId().get(0).getId() != null && generateLoginResponseModel.getToken() != null) {
                getPreferenceManager().setPrefId(generateLoginResponseModel.getId().get(0).getId());
                getPreferenceManager().setPrefToken(generateLoginResponseModel.getToken());
            }

            getPreferenceManager().setPrefMobile(getActivityLoginBinding.email.getText().toString().trim());
            getPreferenceManager().setPrefUsername(generateLoginResponseModel.getId().get(0).getUsername().trim());

            if (generateLoginResponseModel.getId().get(0).getMacid_status().trim().equals("1")) {
                getPreferenceManager().setPrefUinNum(generateLoginResponseModel.getId().get(0).getMac_id());
                startActivity(new Intent(getApplicationContext(), safetySelectionActivity.class)
                        .putExtra("from","login"));

                finish();
            }else{
                startActivity(new Intent(getApplicationContext(), safetySelectionActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onFailure(ErrorBody errorBody, int statusCode) {

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

    private void secondNumber() {

        if (Build.VERSION.SDK_INT > 22) {
            //for dual sim mobile
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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

                Log.i("sim1", SIMmobileNum1);
                Log.i("sim2", SIMmobileNum2);
            }
        }
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

            return;
        } else {
            // Ask for permission
            requestPermission();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
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
                String phoneNumber = telephonyManager.getLine1Number();

                SIMmobileNum1 = phoneNumber.trim().substring(2);
                secondNumber();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

}
