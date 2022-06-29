package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityWifiConfigurationBinding;
import com.mdq.utils.BleUtil;

public class Wifi_configuration extends AppCompatActivity {

    ActivityWifiConfigurationBinding activityWifiConfigurationBinding;

    private BleUtil bleUtil;

    Dialog spinner_WIFI_Configure;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWifiConfigurationBinding = ActivityWifiConfigurationBinding.inflate(getLayoutInflater());
        setContentView(activityWifiConfigurationBinding.getRoot());
        bleUtil = new BleUtil(getApplicationContext());
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("ble_data"));
        spinner_WIFI_Configure = new Dialog(Wifi_configuration.this, R.style.dialog_center);

        //onclick
        setOnClick();
        try {
            Intent intent = getIntent();
            from = intent.getStringExtra("from");
        } catch (Exception e) {

        }

    }

    private void setOnClick() {
        activityWifiConfigurationBinding.SETWIFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activityWifiConfigurationBinding.WIFINAME.getText().length() > 1 && activityWifiConfigurationBinding.password.getText().length() > 1) {
                    bleUtil.wifiSetup(activityWifiConfigurationBinding.WIFINAME.getText().toString(), activityWifiConfigurationBinding.password.getText().toString());

                    spinner_WIFI_Configure.setContentView(R.layout.spinner_wifi_configure);
                    spinner_WIFI_Configure.setCancelable(false);
                    spinner_WIFI_Configure.show();

                } else {
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = null;
            String selfteststatus = null;
            String receivedData = null;
            String status = null;

            if (intent != null) {
                data = (String) intent.getExtras().get("val");
                selfteststatus = (String) intent.getExtras().get("decryptValue");
                receivedData = (String) intent.getExtras().get("receivedData");
                status = (String) intent.getExtras().get("status");
                Log.i("testdataonthetop", data + "");
            }

            if (data.equals("wifi_configured")) {

                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("106")) {
                        spinner_WIFI_Configure.dismiss();

                        Toast.makeText(context, "WI-FI credentials are stored in the device.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Wifi_configuration.this, EmergencyNumberActivity.class));

                        if (from.equals("signUPFlow")) {
                        }
                    }
                }
            }

            if (data.equals("ERROR")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("C8")) {
                        spinner_WIFI_Configure.dismiss();
                        Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Wifi_configuration.this, EmergencyNumberActivity.class));

                        if (from.equals("signUPFlow")) {
                        }
                    }
                }
            }

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }
}