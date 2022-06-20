package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityWarrentyBinding;

public class Warrenty_activity extends AppCompatActivity {

    ActivityWarrentyBinding activityWarrentyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWarrentyBinding=ActivityWarrentyBinding.inflate(getLayoutInflater());
        setContentView(activityWarrentyBinding.getRoot());

        //onClick
        setClick();

    }

    private void setClick() {
        activityWarrentyBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}