package com.mdq.activities;

import static android.view.View.VISIBLE;

import static com.mdq.utils.Helper.showToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mdq.ViewModel.GetWifiViewModel;
import com.mdq.ViewModel.MacIDStatusViewModel;
import com.mdq.ViewModel.UpdateWifiViewModel;
import com.mdq.ViewModel.WifiConfigurationViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewRequestInterface.MacIDStatusRequestInterface;
import com.mdq.interfaces.ViewResponseInterface.GetWifiResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.UpdateWifiResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.WifiConfigurationResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.activities.BleActivity;
import com.mdq.marinetechapp.databinding.ActivityWifiConfigurationBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GetWifiResponseModel;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;
import com.mdq.pojo.jsonresponse.UpdateWifiResponseModel;
import com.mdq.pojo.jsonresponse.WifiConfigurationResponseModel;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class Wifi_configuration extends AppCompatActivity implements MacIDStatusResponseInterface, WifiConfigurationResponseInterface, GetWifiResponseInterface, UpdateWifiResponseInterface {

    ActivityWifiConfigurationBinding activityWifiConfigurationBinding;
    private BleUtil bleUtil;
    Dialog spinner_WIFI_Configure;
    String from;
    PreferenceManager preferenceManager;
    MacIDStatusViewModel macIDStatusViewModel;
    private ListView wifiList;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;
    private List<ScanResult> wifiListt;
    ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    WifiConfigurationViewModel wifiConfigurationViewModel;
    boolean UINStatus = true;
    GetWifiViewModel wifiViewModel;
    UpdateWifiViewModel updateWifiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWifiConfigurationBinding = ActivityWifiConfigurationBinding.inflate(getLayoutInflater());
        setContentView(activityWifiConfigurationBinding.getRoot());
        bleUtil = new BleUtil(getApplicationContext(), "summa");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("ble_data"));
        spinner_WIFI_Configure = new Dialog(Wifi_configuration.this, R.style.dialog_center);
        macIDStatusViewModel = new MacIDStatusViewModel(this, this);
        wifiConfigurationViewModel = new WifiConfigurationViewModel(this, this);

        try {
            Intent intent = getIntent();
            String from = intent.getStringExtra("from");

            if (from.equals("deviceManager")) {
                activityWifiConfigurationBinding.wifiDropDown.setVisibility(View.INVISIBLE);
                activityWifiConfigurationBinding.SETWIFI.setText("Update");
                updateWifiViewModel=new UpdateWifiViewModel(this,this);

                wifiViewModel = new GetWifiViewModel(this, this);
                wifiViewModel.setAuth("Bearer " + getPreferenceManager().getPrefToken());
                wifiViewModel.setMobile(getPreferenceManager().getPrefMobile());
                wifiViewModel.GetWifiRequestCall();
            }
        } catch (Exception e) {

        }


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();

        if (ActivityCompat.checkSelfPermission(Wifi_configuration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    Wifi_configuration.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Please turn ON wifi", Toast.LENGTH_LONG).show();
            }
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Please turn ON wifi", Toast.LENGTH_LONG).show();
            } else if (wifiManager.isWifiEnabled()) {
                registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifiManager.startScan();
                Toast.makeText(getApplicationContext().getApplicationContext(), "Scanning. Please wait...!", Toast.LENGTH_LONG).show();
            }
        }

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        arrayList.clear();

        //onclick
        setOnClick();
        try {
            Intent intent = getIntent();
            from = intent.getStringExtra("from");
        } catch (Exception e) {

        }

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        activityWifiConfigurationBinding.wifiDropDown.setAdapter(adapter);

        activityWifiConfigurationBinding.wifiDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wifiManager.isWifiEnabled()) {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Please turn ON wifi", Toast.LENGTH_LONG).show();
                }
                if (!wifiManager.isWifiEnabled()) {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Please turn ON wifi", Toast.LENGTH_LONG).show();
                } else if (wifiManager.isWifiEnabled()) {
                    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    wifiManager.startScan();
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Scanning. Please wait...!", Toast.LENGTH_LONG).show();
                }
                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                arrayList.clear();

                activityWifiConfigurationBinding.wifiDropDown.showDropDown();
            }
        });
    }

    private void setOnClick() {
        activityWifiConfigurationBinding.SETWIFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityWifiConfigurationBinding.WIFINAME.getText().length() > 1 && activityWifiConfigurationBinding.password.getText().length() > 1) {
                    if (from.equals("deviceManager")) {
                        bleUtil.wifiSetupUpdate(activityWifiConfigurationBinding.WIFINAME.getText().toString(), activityWifiConfigurationBinding.password.getText().toString());
                    }else{
                        bleUtil.wifiSetup(activityWifiConfigurationBinding.WIFINAME.getText().toString(), activityWifiConfigurationBinding.password.getText().toString(),"cec");
                    }

                    spinner_WIFI_Configure.setContentView(R.layout.spinner_wifi_configure);
                    spinner_WIFI_Configure.setCancelable(false);
                    spinner_WIFI_Configure.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (UINStatus) {
                                spinner_WIFI_Configure.dismiss();
                                Toast.makeText(Wifi_configuration.this, "Retry", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 6000);
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
                        UINStatus = false;
                        spinner_WIFI_Configure.dismiss();

                        //macIDStatus insert into DB
                        macIDStatusViewModel.setMacid_status("4");
                        macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                        macIDStatusViewModel.generateMacIDStatusCall();

                        //wifi insert into DB
                        if(from.equals("deviceManager")){
                            updateWifiViewModel.setWifi_ssd(activityWifiConfigurationBinding.WIFINAME.getText().toString().trim());
                            updateWifiViewModel.setWifi_pin(activityWifiConfigurationBinding.password.getText().toString().trim());
                            updateWifiViewModel.setAuth(getPreferenceManager().getPrefToken());
                            updateWifiViewModel.setMobile(getPreferenceManager().getPrefMobile());
                            updateWifiViewModel.UpdateWifiRequest();
                        }else {
                            wifiConfigurationViewModel.wifi_ssd = activityWifiConfigurationBinding.wifiDropDown.getText().toString().trim();
                            wifiConfigurationViewModel.wifi_pin = activityWifiConfigurationBinding.password.getText().toString().trim();
                            wifiConfigurationViewModel.WifiConfigurationRequest();
                        }

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
                        UINStatus = false;

                        spinner_WIFI_Configure.dismiss();

                        macIDStatusViewModel.setMacid_status("4");
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
    protected void onPostResume() {
        super.onPostResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
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
    public void generateVarification(WifiConfigurationResponseModel wifiConfigurationResponseModel) {

    }

    @Override
    public void generateGetWifiCall(GetWifiResponseModel getWifiResponseModel) {
        if (getWifiResponseModel.getMessage().trim().equals("Data fetched successfully")) {
            activityWifiConfigurationBinding.WIFINAME.setText(getWifiResponseModel.getData().get(0).wifi_ssd);
            activityWifiConfigurationBinding.password.setText(getWifiResponseModel.getData().get(0).wifi_pin);
        }
    }

    @Override
    public void generateUpdateWifiCall(UpdateWifiResponseModel updateWifiResponseModel) {
        if(updateWifiResponseModel.getMessage().trim().equals("Wifi details updated successfully")) {
            wifiViewModel = new GetWifiViewModel(this, this);
            wifiViewModel.setAuth("Bearer " + getPreferenceManager().getPrefToken());
            wifiViewModel.setMobile(getPreferenceManager().getPrefMobile());
            wifiViewModel.GetWifiRequestCall();
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

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                wifiListt = wifiManager.getScanResults();
                Toast.makeText(getApplicationContext(), "" + wifiListt.get(0).SSID, Toast.LENGTH_SHORT).show();
                if (!wifiListt.isEmpty()) {
                    int totalWifiDevices = wifiListt.size();
                    Log.w("WiFi Exception size: ", "" + totalWifiDevices);
                    try {
                        while (totalWifiDevices >= 0) {
                            totalWifiDevices--;
                            arrayList.add(wifiListt.get(totalWifiDevices).SSID);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.w("WiFi Exception: ", e);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Wifi found.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Wifi_configuration.this, "permission granted", Toast.LENGTH_SHORT).show();
                    wifiManager.startScan();
                } else {
                    Toast.makeText(Wifi_configuration.this, "permission not granted", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }
}