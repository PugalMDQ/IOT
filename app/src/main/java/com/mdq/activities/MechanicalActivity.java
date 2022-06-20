package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.VideoView;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityMechanicalBinding;

public class MechanicalActivity extends AppCompatActivity {

    ActivityMechanicalBinding activityMechanicalBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMechanicalBinding=ActivityMechanicalBinding.inflate(getLayoutInflater());
        setContentView(activityMechanicalBinding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        VideoView mVideoView = (VideoView)findViewById(R.id.videoview);
        String uriPath = "android.resource://"+getPackageName()+"/"+R.raw.mkey;
        Uri uri = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityMechanicalBinding.next.setVisibility(View.VISIBLE);
            }
        },21000);

        activityMechanicalBinding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MechanicalActivity.this,safetySelectionActivity.class));
            }
        });
        activityMechanicalBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MechanicalActivity.this,safetySelectionActivity.class));
            }
        });
    }
}