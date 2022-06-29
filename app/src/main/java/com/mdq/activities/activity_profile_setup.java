package com.mdq.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityProfileSetupBinding;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

import org.w3c.dom.Text;

public class activity_profile_setup extends AppCompatActivity {

    TextView con;
    ActivityProfileSetupBinding activityProfileSetupBinding;
    PreferenceManager preferenceManager;
    BleUtil bleUtil;
    Dialog dialog_Spinner;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileSetupBinding = ActivityProfileSetupBinding.inflate(getLayoutInflater());
         id  = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver,new IntentFilter("ble_data"));

        setContentView(activityProfileSetupBinding.getRoot());
        con = findViewById(R.id.con);

        bleUtil=new BleUtil(getApplicationContext());
        setomClick();

    }

    private void setomClick() {
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                bleUtil.MobileKeyRegister(id,getPreferenceManager().getPrefMobile(),activityProfileSetupBinding.lockerpin.getText().toString(),activityProfileSetupBinding.SosPin.getText().toString());

                dialog_Spinner = new Dialog(activity_profile_setup.this, R.style.dialog_center);
                dialog_Spinner.setContentView(R.layout.dialog_spinner);
                TextView textView=dialog_Spinner.findViewById(R.id.subText);
                dialog_Spinner.setCanceledOnTouchOutside(false);
                textView.setText("Setup PIN and SOS PIN...");
                dialog_Spinner.show();

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
                if(!TextUtils.isEmpty(receivedData)) {
                    dialog_Spinner.dismiss();
                    if (receivedData.substring(0, 2).equals("C8")) {
                        Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            if (data.equals("PIN_SET")) {
                if(!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("67")) {
                        dialog_Spinner.dismiss();
                        Dialog dialog = new Dialog(activity_profile_setup.this, R.style.dialog_center);
                        dialog.setContentView(R.layout.dialog_toast);
                        TextView textView=dialog.findViewById(R.id.subText);
                        textView.setText("PIN and SOS PIN successfully inserted");
                        dialog.setCanceledOnTouchOutside(false);

                        dialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(activity_profile_setup.this, Wifi_configuration.class);
                                intent.putExtra("from","signUPFlow");
                                startActivity(intent);
                                dialog.dismiss();

                            }
                        },3000);
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