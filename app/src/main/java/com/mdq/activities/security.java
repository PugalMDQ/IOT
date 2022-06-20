package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivitySecurityBinding;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

public class security extends AppCompatActivity {

    boolean secure=true;
    ActivitySecurityBinding activitySecurityBinding;
    BleUtil bleUtil;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySecurityBinding=ActivitySecurityBinding.inflate(getLayoutInflater());
        setContentView(activitySecurityBinding.getRoot());
        bleUtil=new BleUtil(getApplicationContext());

        //onclick
        setOnClick();

    }

    private void setOnClick() {

        activitySecurityBinding.switchBioSecure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secure) {
                    secure=false;
                    activitySecurityBinding.switchBioSecure.setChecked(false);
                    dialog();
                }else{
                    secure=true;
                    bleUtil.NormalSecureMode(getPreferenceManager().getPrefMobile(),"02");

                }
            }
        });
        activitySecurityBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }

    private void dialog() {

        Dialog dialog=new Dialog(security.this, R.style.dialog_center);
        dialog.setContentView(R.layout.dialog_for_secure_mode);
        dialog.setCanceledOnTouchOutside(false);

        TextView textView=dialog.findViewById(R.id.textView24);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    Toast.makeText(security.this, "Your device does not have fingerprint reader", Toast.LENGTH_SHORT).show();
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    Dialog dialog=new Dialog(security.this, R.style.dialog_center);
                    dialog.setContentView(R.layout.dialog_biometrix_check);
                    dialog.setCanceledOnTouchOutside(false);
                    TextView textView=dialog.findViewById(R.id.textView24);
                    textView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }

                    });
                    dialog.show();
                } else {
                    // Everything is ready for fingerprint authentication
                    activitySecurityBinding.switchBioSecure.setChecked(true);
                    bleUtil.NormalSecureMode(getPreferenceManager().getPrefMobile(),"01");
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