package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityEnterScreenBinding;

public class EnterScreen extends AppCompatActivity {

    ActivityEnterScreenBinding activityEnterScreenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEnterScreenBinding=ActivityEnterScreenBinding.inflate(getLayoutInflater());
        setContentView(activityEnterScreenBinding.getRoot());

        activityEnterScreenBinding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterScreen.this,LoginActivity.class));
                finish();
            }
        });
         activityEnterScreenBinding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterScreen.this,MobileNumActivity.class));
                finish();
            }
        });

    }
}