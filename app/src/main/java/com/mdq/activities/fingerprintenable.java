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

import com.mdq.ViewModel.MacIDStatusViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityFingerprintenableBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;
import com.mdq.utils.PreferenceManager;

public class fingerprintenable extends AppCompatActivity implements MacIDStatusResponseInterface {

    ActivityFingerprintenableBinding activityFingerprintenableBinding;
    MacIDStatusViewModel macIDStatusViewModel;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFingerprintenableBinding=ActivityFingerprintenableBinding.inflate(getLayoutInflater());
        setContentView(activityFingerprintenableBinding.getRoot());

        macIDStatusViewModel=new MacIDStatusViewModel(this,this);

        activityFingerprintenableBinding.con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication

                    Toast.makeText(fingerprintenable.this, "Your device does not have fingerprint reader", Toast.LENGTH_SHORT).show();
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    Dialog dialog=new Dialog(fingerprintenable.this, R.style.dialog_center);
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
                    Intent intent=new Intent(fingerprintenable.this,activity_fingerprint_authentication.class);
                    startActivity(intent);
                }
            }
        });

        activityFingerprintenableBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                macIDStatusViewModel.setMacid_status("2");
                macIDStatusViewModel.generateMacIDStatusCall();
                startActivity(new Intent(fingerprintenable.this,activity_profile_setup.class));
            }
        });
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