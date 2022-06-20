package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityOurProductsBinding;

public class our_products extends AppCompatActivity {

    ActivityOurProductsBinding activityOurProductsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOurProductsBinding=ActivityOurProductsBinding.inflate(getLayoutInflater());
        setContentView(activityOurProductsBinding.getRoot());

        setonclick();

    }

    private void setonclick() {
        activityOurProductsBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });
    }
}