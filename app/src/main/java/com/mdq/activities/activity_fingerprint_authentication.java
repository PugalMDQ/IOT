package com.mdq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mdq.ViewModel.BioMetricViewModel;
import com.mdq.ViewModel.MacIDStatusViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.BioMetricResponseInterface;
import com.mdq.interfaces.ViewResponseInterface.MacIDStatusResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityFingerprintAuthenticationBinding;
import com.mdq.pojo.jsonresponse.BioMetricResponseModel;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.MacIDStatusResponseModel;
import com.mdq.utils.PreferenceManager;

import java.util.Timer;
import java.util.TimerTask;

public class activity_fingerprint_authentication extends AppCompatActivity implements MacIDStatusResponseInterface , BioMetricResponseInterface {

    TextView con;
    TextView fin;
    TextView print;
    TextView cancel;
    CircularProgressIndicator cpi;
    TextView percentage;
    TextView scan;
    final Handler mHandler=new Handler();
    ImageView right;
    boolean finish=false;
    ActivityFingerprintAuthenticationBinding activityFingerprintAuthenticationBinding;
    PreferenceManager preferenceManager;
    MacIDStatusViewModel macIDStatusViewModel;
    BioMetricViewModel bioMetricViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFingerprintAuthenticationBinding=ActivityFingerprintAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(activityFingerprintAuthenticationBinding.getRoot());

        macIDStatusViewModel=new MacIDStatusViewModel(this,this);
        bioMetricViewModel=new BioMetricViewModel(this,this);

        con=findViewById(R.id.con);
        print=findViewById(R.id.print);
        fin=findViewById(R.id.fin);
        cancel=findViewById(R.id.cancel);
        cpi=findViewById(R.id.cpi);
        percentage=findViewById(R.id.percentage);
        scan=findViewById(R.id.scan);
        right=findViewById(R.id.right);
        cpi.setIndeterminate(false);
        setProgress();

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finish) {

                    //set bioMetric enable to DB
                    bioMetricViewModel.setBiometric("1");
                    bioMetricViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                    bioMetricViewModel.generateBioMetricRequestCall();

                    //set MacID status enable to DB
                    macIDStatusViewModel.setMacid_status("2");
                    macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                    macIDStatusViewModel.generateMacIDStatusCall();

                    Intent intent = new Intent(activity_fingerprint_authentication.this, activity_profile_setup.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(activity_fingerprint_authentication.this, "Biometric setup is not completed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                macIDStatusViewModel.setMacid_status("2");
                macIDStatusViewModel.setMobile(getPreferenceManager().getPrefMobile().trim());
                macIDStatusViewModel.generateMacIDStatusCall();
                Intent intent=new Intent(activity_fingerprint_authentication.this, activity_profile_setup.class);
                startActivity(intent);
            }
        });

    }

    private void setProgress(){

        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            int progress;
            @Override
            public void run() {
                progress=progress+10;
                cpi.setProgressCompat(progress,true);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        percentage.setText(+progress+"%");
                        fin.setText(R.string.please);
                    }
                });

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        scan.setText("scanning...");
                        if(progress==100){
                            getPreferenceManager().setPrefLoginBiometric("yes");
                            finish=true;
                            scan.setText("Hurray Completed");
                            right.setVisibility(View.VISIBLE);
                            fin.setVisibility(View.GONE);
                            print.setVisibility(View.GONE);
                        }
                    }
                });

                if(progress==100){
                    timer.cancel();
                }
            }
        };
        timer.schedule(task,1000,1000);
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
    public void BioMetricCall(BioMetricResponseModel bioMetricResponseModel) {

    }

    @Override
    public void onFailure(ErrorBody errorBody, int statusCode) {

    }
}