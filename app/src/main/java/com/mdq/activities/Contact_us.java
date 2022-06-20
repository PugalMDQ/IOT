package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityContactUsBinding;

public class Contact_us extends AppCompatActivity {

    ActivityContactUsBinding activityContactUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContactUsBinding=ActivityContactUsBinding.inflate(getLayoutInflater());

        setContentView(activityContactUsBinding.getRoot());

        //onclick
        setClick();
    }

    private void setClick() {

        activityContactUsBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}