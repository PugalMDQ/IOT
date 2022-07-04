package com.mdq.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mdq.ViewModel.MacIDStatusViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityProfileSetupBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

import org.w3c.dom.Text;

public class activity_profile_setup extends AppCompatActivity implements MacIDStatusResponseInterface {

    TextView con;
    ActivityProfileSetupBinding activityProfileSetupBinding;
    PreferenceManager preferenceManager;
    BleUtil bleUtil;
    Dialog dialog_Spinner;
    String id;
    MacIDStatusViewModel macIDStatusViewModel;

    boolean UINStatus = true;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileSetupBinding = ActivityProfileSetupBinding.inflate(getLayoutInflater());
        macIDStatusViewModel = new MacIDStatusViewModel(this, this);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("ble_data"));

        setContentView(activityProfileSetupBinding.getRoot());
        con = findViewById(R.id.con);

        bleUtil = new BleUtil(getApplicationContext(), "summa");
        setomClick();

    }

    private void setomClick() {
        con.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint({"MissingPermission", "HardwareIds"})
            @Override
            public void onClick(View v) {

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(activity_profile_setup.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity_profile_setup.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //getDeviceId() is Deprecated so for android O we can use getImei() method
                    id= telephonyManager.getImei(1);
                } else {
                    id= telephonyManager.getDeviceId();
                }

                if (activityProfileSetupBinding.lockerpin.getText().toString().isEmpty()) {
                    Toast.makeText(activity_profile_setup.this, "Enter Locker PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activityProfileSetupBinding.CLockerPin.getText().toString().isEmpty()) {
                    Toast.makeText(activity_profile_setup.this, "Enter Confirm Locker PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activityProfileSetupBinding.SosPin.getText().toString().isEmpty()) {
                    Toast.makeText(activity_profile_setup.this, "Enter SOS PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activityProfileSetupBinding.CSosPin.getText().toString().isEmpty()) {
                    Toast.makeText(activity_profile_setup.this, "Enter Confirm SOS PIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!activityProfileSetupBinding.lockerpin.getText().toString().equals(activityProfileSetupBinding.CLockerPin.getText().toString())) {
                    Toast.makeText(activity_profile_setup.this, "Locker PIN mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!activityProfileSetupBinding.SosPin.getText().toString().equals(activityProfileSetupBinding.CSosPin.getText().toString())) {
                    Toast.makeText(activity_profile_setup.this, "SOS PIN mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (activityProfileSetupBinding.SosPin.getText().toString().equals(activityProfileSetupBinding.CLockerPin.getText().toString())) {
                    Toast.makeText(activity_profile_setup.this, "Enter different locker PIN and SOS PIN", Toast.LENGTH_SHORT).show();
                    return;
                }

                getPreferenceManager().setPrefLockerPin(activityProfileSetupBinding.lockerpin.getText().toString());
                getPreferenceManager().setPrefSosNum(activityProfileSetupBinding.SosPin.getText().toString());

                bleUtil.MobileKeyRegister(id, getPreferenceManager().getPrefMobile(), activityProfileSetupBinding.lockerpin.getText().toString(), activityProfileSetupBinding.SosPin.getText().toString());

                dialog_Spinner = new Dialog(activity_profile_setup.this, R.style.dialog_center);
                dialog_Spinner.setContentView(R.layout.dialog_spinner);
                TextView textView = dialog_Spinner.findViewById(R.id.subText);
                dialog_Spinner.setCanceledOnTouchOutside(false);
                textView.setText("Setup PIN and SOS PIN...");
                dialog_Spinner.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (UINStatus) {
                            dialog_Spinner.dismiss();
                            Toast.makeText(activity_profile_setup.this, "Retry", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 6000);

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

            if (data.equals("ERROR")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    dialog_Spinner.dismiss();
                    if (receivedData.substring(0, 2).equals("C8")) {
                        UINStatus = false;
                        Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            if (data.equals("PIN_SET")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("67")) {
                        //macIDStatus insert into DB
                        macIDStatusViewModel.setMacid_status("3");
                        macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                        macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                        macIDStatusViewModel.generateMacIDStatusCall();

                        dialog_Spinner.dismiss();
                        Dialog dialog = new Dialog(activity_profile_setup.this, R.style.dialog_center);
                        dialog.setContentView(R.layout.dialog_toast);
                        TextView textView = dialog.findViewById(R.id.subText);
                        textView.setText("PIN and SOS PIN successfully inserted");
                        dialog.setCanceledOnTouchOutside(false);
                        UINStatus = false;
                        dialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(activity_profile_setup.this, Wifi_configuration.class);
                                intent.putExtra("from", "signUPFlow");
                                startActivity(intent);
                                dialog.dismiss();

                            }
                        }, 3000);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
}