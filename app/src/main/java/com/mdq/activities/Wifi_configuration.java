package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityWifiConfigurationBinding;
import com.mdq.utils.BleUtil;

public class Wifi_configuration extends AppCompatActivity {

    ActivityWifiConfigurationBinding activityWifiConfigurationBinding;

    private BleUtil bleUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWifiConfigurationBinding = ActivityWifiConfigurationBinding.inflate(getLayoutInflater());
        setContentView(activityWifiConfigurationBinding.getRoot());
        bleUtil = new BleUtil(getApplicationContext());

        //onclick
        setOnClick();

    }

    private void setOnClick() {
        activityWifiConfigurationBinding.SETWIFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(activityWifiConfigurationBinding.WIFINAME.getText()!=null && activityWifiConfigurationBinding.password.getText()!=null) {
                    bleUtil.wifiSetup(activityWifiConfigurationBinding.WIFINAME.getText().toString(), activityWifiConfigurationBinding.password.getText().toString());
                }

            }
        });

        activityWifiConfigurationBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

    }
}