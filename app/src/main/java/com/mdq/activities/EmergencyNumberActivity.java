package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdq.marinetechapp.R;

public class EmergencyNumberActivity extends AppCompatActivity {

    TextView Confirm;
    EditText number;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_number);

        Confirm=findViewById(R.id.Confirm);
        number=findViewById(R.id.number);
        back=findViewById(R.id.back);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString().length()==10){
                    startActivity(new Intent(EmergencyNumberActivity.this,HomeActivity.class)
                            .putExtra("from","signup"));
                }else{
                    Toast.makeText(EmergencyNumberActivity.this, "Enter correct number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}