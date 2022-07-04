package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdq.ViewModel.InsertEmergencyMobileNoViewModel;
import com.mdq.ViewModel.MacIDStatusViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.InsertEmergencyMobileNoResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.InsertEmergencyMobileNoResponseModel;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;
import com.mdq.utils.PreferenceManager;

public class EmergencyNumberActivity extends AppCompatActivity implements MacIDStatusResponseInterface , InsertEmergencyMobileNoResponseInterface {

    TextView Confirm;
    EditText number;
    ImageView back;
    PreferenceManager preferenceManager;
    MacIDStatusViewModel macIDStatusViewModel;
    InsertEmergencyMobileNoViewModel insertEmergencyMobileNoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_number);

        macIDStatusViewModel=new MacIDStatusViewModel(this,this);
        insertEmergencyMobileNoViewModel=new InsertEmergencyMobileNoViewModel(this,this);

        Confirm=findViewById(R.id.Confirm);
        number=findViewById(R.id.number);
        back=findViewById(R.id.back);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString().length()==10){
                    macIDStatusViewModel.setMacid_status("5");
                    macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                    macIDStatusViewModel.generateMacIDStatusCall();

                    insertEmergencyMobileNoViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                    insertEmergencyMobileNoViewModel.setEmergencyNumber(number.getText().toString().trim());
                    insertEmergencyMobileNoViewModel.getEmergencyNumber();

                    startActivity(new Intent(EmergencyNumberActivity.this,HomeActivity.class)
                            .putExtra("from","signup"));

                }else{
                    Toast.makeText(EmergencyNumberActivity.this, "Enter correct number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    public void generateInsertENOCall(InsertEmergencyMobileNoResponseModel insertEmergencyMobileNoResponseModel) {

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