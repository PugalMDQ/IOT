package com.mdq.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mdq.ViewModel.SetMac_IDViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.SetMac_IDResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityMobileNumBinding;
import com.mdq.marinetechapp.databinding.ActivityMobileRegisterBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateSetMac_IDResponseModel;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;

public class MobileRegistration extends AppCompatActivity implements SetMac_IDResponseInterface {
    TextView con;
    TextView cancel;
    private BleUtil bleUtil;
    ActivityMobileRegisterBinding activityMobileRegisterBinding;
    PreferenceManager preferenceManager;
    Dialog dialog_Spinner;
    SetMac_IDViewModel setMac_idViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMobileRegisterBinding = ActivityMobileRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityMobileRegisterBinding.getRoot());

        setMac_idViewModel=new SetMac_IDViewModel(this,this);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver,new IntentFilter("ble_data"));

        bleUtil = new BleUtil(getApplicationContext());
        con = findViewById(R.id.con);
        cancel = findViewById(R.id.cancel);

        setonClick();

    }

    private void setonClick() {
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityMobileRegisterBinding.UINKey.getText().toString().isEmpty()) {
                    Toast.makeText(MobileRegistration.this, "Enter the UIN number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activityMobileRegisterBinding.UINKey.getText().toString().length()!=10) {
                    Toast.makeText(MobileRegistration.this, "Enter the correct UIN number", Toast.LENGTH_SHORT).show();
                    return;
                }
                bleUtil.ValidateDevice(activityMobileRegisterBinding.UINKey.getText().toString());
                getPreferenceManager().setPrefUinNum(activityMobileRegisterBinding.UINKey.getText().toString());

                 dialog_Spinner = new Dialog(MobileRegistration.this, R.style.dialog_center);
                dialog_Spinner.setContentView(R.layout.dialog_spinner);
                dialog_Spinner.setCanceledOnTouchOutside(false);
                dialog_Spinner.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog_Spinner.dismiss();
                        Toast.makeText(MobileRegistration.this, "Failed to validate UIN.", Toast.LENGTH_SHORT).show();
                    }
                },3000);
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

            if (data.equals("UIN_SET")) {
                if(!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("66")) {
                        dialog_Spinner.dismiss();
                        Dialog dialog = new Dialog(MobileRegistration.this, R.style.dialog_center);
                        dialog.setContentView(R.layout.dialog_toast);
                        dialog.setCanceledOnTouchOutside(false);

                        dialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                setMac_idViewModel.setMobile(getPreferenceManager().getPrefMobile());
                                setMac_idViewModel.setMac_id(activityMobileRegisterBinding.UINKey.getText().toString().trim());
                                setMac_idViewModel.setMacid_status("1");
                                setMac_idViewModel.SetMAc_IDRequest();
                                Intent intent = new Intent(MobileRegistration.this, fingerprintenable.class);
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


    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, String errorMessage) {

    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, ViewType viewType, String errorMessage) {

    }

    @Override
    public void generateSetMAc_IDProcess(GenerateSetMac_IDResponseModel generateSetMac_idResponseModel) {

    }

    @Override
    public void onFailure(ErrorBody errorBody, int statusCode) {

    }
}