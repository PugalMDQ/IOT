package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivitySosPinChangeBinding;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

public class SOS_pin_change extends AppCompatActivity {

    ActivitySosPinChangeBinding activitySosPinChangeBinding;
    BleUtil bleUtil;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySosPinChangeBinding=ActivitySosPinChangeBinding.inflate(getLayoutInflater());
        setContentView(activitySosPinChangeBinding.getRoot());

        bleUtil=new BleUtil(getApplicationContext());
        setonClick();


    }

    private void setonClick() {

        activitySosPinChangeBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activitySosPinChangeBinding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activitySosPinChangeBinding.email.getText().toString().length()!=4){
                    Toast.makeText(SOS_pin_change.this, "Enter correct PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(activitySosPinChangeBinding.password.getText().toString().length()!=10){
                    Toast.makeText(SOS_pin_change.this, "Enter correct mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                bleUtil.EmergencyPinAndMobile(getPreferenceManager().getPrefMobile(),getPreferenceManager().getPrefSosNum(),activitySosPinChangeBinding.email.getText().toString(),activitySosPinChangeBinding.password.getText().toString());
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
}