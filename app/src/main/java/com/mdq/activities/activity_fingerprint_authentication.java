package com.mdq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityFingerprintAuthenticationBinding;

import java.util.Timer;
import java.util.TimerTask;

public class activity_fingerprint_authentication extends AppCompatActivity {

    TextView con;
    TextView fin;
    TextView print;
    TextView cancel;
    CircularProgressIndicator cpi;
    TextView percentage;
    TextView scan;
    final Handler mHandler=new Handler();
    ImageView right;
    ActivityFingerprintAuthenticationBinding activityFingerprintAuthenticationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFingerprintAuthenticationBinding=ActivityFingerprintAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(activityFingerprintAuthenticationBinding.getRoot());

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
                Intent intent=new Intent(activity_fingerprint_authentication.this, activity_profile_setup.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}