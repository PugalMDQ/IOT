package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mdq.ViewModel.GetUserViewModel;
import com.mdq.ViewModel.UpdateProfileViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.GetUserResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.UpdateProfileResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityProfileBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GetUserResponseModel;
import com.mdq.pojo.jsonresponse.UpdateProfileResponseModel;
import com.mdq.utils.PreferenceManager;

public class profileActivity extends AppCompatActivity implements GetUserResponseInterface, UpdateProfileResponseInterface {

    ActivityProfileBinding activityProfileBindingl;
    GetUserViewModel getUserViewModel;
    PreferenceManager preferenceManager;
    UpdateProfileViewModel updateProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBindingl=ActivityProfileBinding.inflate(getLayoutInflater());

        updateProfileViewModel=new UpdateProfileViewModel(this,this);
        getUserViewModel=new GetUserViewModel(this,this);
        getUserViewModel.setMobile(getPreferenceManager().getPrefMobile());
        getUserViewModel.setAuth("Bearer "+getPreferenceManager().getPrefToken());
        getUserViewModel.GetUserRequestCall();

        setContentView(activityProfileBindingl.getRoot());

        //onClick
        setClick();


    }

    private void setClick() {

        activityProfileBindingl.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activityProfileBindingl.SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfileViewModel.setMobile(getPreferenceManager().getPrefMobile());
                updateProfileViewModel.setAuth("Bearer "+getPreferenceManager().getPrefToken());
                updateProfileViewModel.setEmergency_mobile(activityProfileBindingl.EMobileNo.getText().toString().trim());
                updateProfileViewModel.setPwd(activityProfileBindingl.REMpin.getText().toString().trim());
                updateProfileViewModel.setUsername(activityProfileBindingl.username.getText().toString().trim());
                updateProfileViewModel.UpdateUserRequest();

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
    public void generateGetUserCall(GetUserResponseModel getUserResponseModel) {

        if (getUserResponseModel.getMessage().trim().equals("Data fetched successfully")) {
            activityProfileBindingl.username.setText(getUserResponseModel.getData().get(0).getUsername());
            activityProfileBindingl.Mpin.setText(getUserResponseModel.getData().get(0).getPwd());
            activityProfileBindingl.REMpin.setText(getUserResponseModel.getData().get(0).getPwd());
            activityProfileBindingl.email.setText(getUserResponseModel.getData().get(0).getEmail());
            activityProfileBindingl.EMobileNo.setText(getUserResponseModel.getData().get(0).getEmergency_mobile());
        }

    }

    @Override
    public void generateUserProfileUpdateCall(UpdateProfileResponseModel updateProfileResponseModel) {

        if(updateProfileResponseModel.getMessage().trim().equals("Profile updated successfully")){

            Toast.makeText(this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
            getUserViewModel.setMobile(getPreferenceManager().getPrefMobile());
            getUserViewModel.setAuth("Bearer "+getPreferenceManager().getPrefToken());
            getUserViewModel.GetUserRequestCall();

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
}