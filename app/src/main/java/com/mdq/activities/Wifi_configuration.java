package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityWifiConfigurationBinding;
import com.mdq.utils.BleUtil;

public class Wifi_configuration extends AppCompatActivity {

    ActivityWifiConfigurationBinding activityWifiConfigurationBinding;

    private BleUtil bleUtil;

    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWifiConfigurationBinding = ActivityWifiConfigurationBinding.inflate(getLayoutInflater());
        setContentView(activityWifiConfigurationBinding.getRoot());
        bleUtil = new BleUtil(getApplicationContext());

        //onclick
        setOnClick();
        try {

            Intent intent = getIntent();
            from =intent.getStringExtra("from");
        }catch (Exception e){

        }

    }

    private void setOnClick() {
        activityWifiConfigurationBinding.SETWIFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(activityWifiConfigurationBinding.WIFINAME.getText()!=null && activityWifiConfigurationBinding.password.getText()!=null) {
                    bleUtil.wifiSetup(activityWifiConfigurationBinding.WIFINAME.getText().toString(), activityWifiConfigurationBinding.password.getText().toString());

                    if(from.equals("signUPFlow")) {
                        startActivity(new Intent(Wifi_configuration.this, EmergencyNumberActivity.class));
                    }
                }else{
                    Toast.makeText(Wifi_configuration.this, "Enter the fields", Toast.LENGTH_SHORT).show();
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