package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityProfileBinding;

public class profileActivity extends AppCompatActivity {

    ActivityProfileBinding activityProfileBindingl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBindingl=ActivityProfileBinding.inflate(getLayoutInflater());

        setContentView(activityProfileBindingl.getRoot());

        //onClick
        setClick();

    }

    private void setClick() {

        activityProfileBindingl.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}