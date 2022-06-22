package com.mdq.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdq.ViewModel.RegisterRequestViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.RegisterResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityRegisterBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateRegisterResponseModel;
import com.mdq.utils.PreferenceManager;


public class RegisterActivity extends AppCompatActivity implements RegisterResponseInterface {

    ActivityRegisterBinding activityRegisterBinding;
    EditText password,confirmpassword;
    boolean passwordVisibility;
    boolean passwordVisibility1;
    String pass,cPass,name,email;
    ConnectivityManager connectivityManager ;
    RegisterRequestViewModel registerRequestViewModel;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());
        password=findViewById(R.id.Mpin);
        confirmpassword=findViewById(R.id.REMpin);
        registerRequestViewModel=new RegisterRequestViewModel(this,this);
        //set click
        click();

        connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        //password
        password();

    }

    private void password() {
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
        confirmpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= confirmpassword.getRight() - confirmpassword.getCompoundDrawables()[Right].getBounds().width()) {

                        int selection = confirmpassword.getSelectionEnd();
                        if (passwordVisibility1) {
                            confirmpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                            confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisibility1 = false;
                        } else {
                            confirmpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0);
                            confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisibility1 = true;
                        }
                        confirmpassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void click() {

        pass=activityRegisterBinding.Mpin.getText().toString();
        cPass=activityRegisterBinding.REMpin.getText().toString();
        name=activityRegisterBinding.username.getText().toString();
        email=activityRegisterBinding.email.getText().toString();

        activityRegisterBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        activityRegisterBinding.SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(activityRegisterBinding.Mpin.getText().toString()==null){
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                 if(activityRegisterBinding.REMpin.getText().toString()==null){
                    Toast.makeText(getApplicationContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                 if(activityRegisterBinding.email.getText().toString()==null){
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                 if(activityRegisterBinding.username.getText().toString()==null){
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                 if(activityRegisterBinding.Mpin.getText().toString().equals(activityRegisterBinding.REMpin.getText().toString())){

                 }else{
                     Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                     return;
                 }

                if ((connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                        || (connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .getState() == NetworkInfo.State.CONNECTED)) {

                }else{
                    Toast.makeText(getApplicationContext(), "Application Required Internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Patterns.EMAIL_ADDRESS.matcher(activityRegisterBinding.email.getText().toString()).matches()) {

                }else{
                    Toast.makeText(getApplicationContext(), "Enter proper Email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(getPreferenceManager().getPrefMobile()==null){
                    Toast.makeText(getApplicationContext(), "Required mobile num", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerRequestViewModel.email=activityRegisterBinding.email.getText().toString();
                registerRequestViewModel.pwd=activityRegisterBinding.Mpin.getText().toString();
                registerRequestViewModel.username=activityRegisterBinding.username.getText().toString();
                registerRequestViewModel.mobile=getPreferenceManager().getPrefMobile();
                registerRequestViewModel.generateRegisterRequest();

                }
        });
    }

    @Override
    public void generateRegisterProcessed(GenerateRegisterResponseModel generateRegisterResponseModel) {
        Toast.makeText(getApplicationContext(), ""+generateRegisterResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
        if(generateRegisterResponseModel.getMessage().equals("Registered successfully")){
            getPreferenceManager().setPrefUsername(activityRegisterBinding.username.getText().toString().trim());
            startActivity(new Intent(getApplicationContext(),MechanicalActivity.class));
            finishAffinity();
        }
    }

    @Override
    public void onFailure(ErrorBody errorBody, int statusCode) {

    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, String errorMessage) {

    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, ViewType viewType, String errorMessage) {

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
}