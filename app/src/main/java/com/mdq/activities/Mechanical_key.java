package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityMechanicalKeyBinding;
import com.mdq.utils.BleUtil;

public class Mechanical_key extends AppCompatActivity {

    ActivityMechanicalKeyBinding activityMechanicalKeyBinding;
    BleUtil bleUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMechanicalKeyBinding=ActivityMechanicalKeyBinding.inflate(getLayoutInflater());
        setContentView(activityMechanicalKeyBinding.getRoot());

        bleUtil=new BleUtil(getApplicationContext(),"summa");
        setonclick();

    }

    private void setonclick() {
        activityMechanicalKeyBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}