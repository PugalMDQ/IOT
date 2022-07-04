package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityLokerPinChangeBinding;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

public class LockerPinChange extends AppCompatActivity {

    ActivityLokerPinChangeBinding activityLokerPinChangeBinding;
    BleUtil bleUtil;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLokerPinChangeBinding=ActivityLokerPinChangeBinding.inflate(getLayoutInflater());
        setContentView(activityLokerPinChangeBinding.getRoot());

        bleUtil=new BleUtil(getApplicationContext(),"summa");

        setonclick();

    }

    private void setonclick() {

        activityLokerPinChangeBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activityLokerPinChangeBinding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleUtil.LockerPinChange(preferenceManager.getPrefMobile(),preferenceManager.getPrefLockerPin(),activityLokerPinChangeBinding.email.getText().toString());
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