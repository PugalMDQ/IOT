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

import com.mdq.ViewModel.MacIDStatusViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewRequestInterface.MacIDStatusRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityWifiConfigurationBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

public class Wifi_configuration extends AppCompatActivity implements MacIDStatusResponseInterface {

    ActivityWifiConfigurationBinding activityWifiConfigurationBinding;
    private BleUtil bleUtil;
    Dialog spinner_WIFI_Configure;
    String from;
    PreferenceManager preferenceManager;
    MacIDStatusViewModel macIDStatusViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWifiConfigurationBinding = ActivityWifiConfigurationBinding.inflate(getLayoutInflater());
        setContentView(activityWifiConfigurationBinding.getRoot());
        bleUtil = new BleUtil(getApplicationContext());
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("ble_data"));
        spinner_WIFI_Configure = new Dialog(Wifi_configuration.this, R.style.dialog_center);
        macIDStatusViewModel=new MacIDStatusViewModel(this,this);

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

                        macIDStatusViewModel.setMacid_status("3");
                        macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                        macIDStatusViewModel.generateMacIDStatusCall();
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

                        macIDStatusViewModel.setMacid_status("3");
                        macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                        macIDStatusViewModel.generateMacIDStatusCall();
                        Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Wifi_configuration.this, EmergencyNumberActivity.class));

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

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, String errorMessage) {

    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, ViewType viewType, String errorMessage) {

    }

    @Override
    public void generateMacIDStatus(MacIDStatusResponseModel macIDStatusResponseModel) {

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