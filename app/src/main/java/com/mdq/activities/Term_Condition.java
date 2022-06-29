
package com.mdq.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityTermConditionBinding;

public class Term_Condition extends AppCompatActivity {

    ActivityTermConditionBinding  activityTermConditionBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTermConditionBinding=ActivityTermConditionBinding.inflate(getLayoutInflater());
        setContentView(activityTermConditionBinding.getRoot());

        activityTermConditionBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}