package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityDeviceMangerBinding;
import com.mdq.utils.PreferenceManager;

public class DeviceManger_activity extends AppCompatActivity {

    ActivityDeviceMangerBinding activityDeviceMangerBinding;
    PreferenceManager preferenceManager;
    boolean SOS=false;
    boolean MECH=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDeviceMangerBinding = ActivityDeviceMangerBinding.inflate(getLayoutInflater());
        setContentView(activityDeviceMangerBinding.getRoot());

        //onclick
        setOnClick();

    }

    private void setOnClick() {
        activityDeviceMangerBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DeviceManger_activity.this, Wifi_configuration.class)
                        .putExtra("from","deviceManager"));

            }
        });

        activityDeviceMangerBinding.security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DeviceManger_activity.this, security.class));

            }
        });

        activityDeviceMangerBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activityDeviceMangerBinding.mechpasschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog("MECH");
            }
        });
        activityDeviceMangerBinding.DeviceManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog("SOS");

            }
        });
        activityDeviceMangerBinding.digitalKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog("LOCKER");
            }
        });
    }

    private void dialog(String name) {

        Dialog dialog = new Dialog(DeviceManger_activity.this, R.style.dialog_center);
        dialog.setContentView(R.layout.dialog_for_pin);
        dialog.setCanceledOnTouchOutside(false);

        TextView textView = dialog.findViewById(R.id.textView24);
        TextView subtext = dialog.findViewById(R.id.subText);
        if(name.equals("MECH")) {
            subtext.setText("Enter the PIN to see you Mechanical key.");
        }else if(name.equals("SOS")){
            subtext.setText("Enter the PIN to configure your SOS PIN.");
        }else{
            subtext.setText("Enter the PIN to configure your Locker PIN.");
        }
        TextView pin = dialog.findViewById(R.id.pin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin.getText().toString().trim().length() == 4) {
                    if (getPreferenceManager().getPrefLockerPin().trim().equals(pin.getText().toString().trim())) {
                        if (name.equals("MECH")) {
                            startActivity(new Intent(DeviceManger_activity.this, Mechanical_key.class));
                        } else if (name.equals("SOS")) {
                            startActivity(new Intent(DeviceManger_activity.this, SOS_pin_change.class));
                        } else {
                            startActivity(new Intent(DeviceManger_activity.this, LockerPinChange.class));
                        }
                        dialog.dismiss();
                    }else{
                        Toast.makeText(DeviceManger_activity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DeviceManger_activity.this, "Enter correct PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
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