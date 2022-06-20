package com.mdq.marinetechapp.activities;

import static com.mdq.utils.Helper.EMOJI_FILTER;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.activities.BleActivity;
import com.mdq.utils.Helper;

public class LoginActivityMarine extends AppCompatActivity {
    private EditText etEmailAddress;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_marine);
        initialisation();
        helper = new Helper(this);
        setDefaults();
        setListeners();
    }

    private void initialisation() {
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setDefaults() {
        etEmailAddress.setFilters(new InputFilter[]{EMOJI_FILTER});
        etPassword.setFilters(new InputFilter[]{EMOJI_FILTER, new InputFilter.LengthFilter(15)});
        etPassword.setTransformationMethod(new Helper.AsteriskPasswordTransformationMethod());
    }

    private void setListeners() {
        btnLogin.setOnClickListener(view -> {
            Intent loginIntent = new Intent(this, BleActivity.class);
            startActivity(loginIntent);
//            if (etEmailAddress.getText().toString().isEmpty()) {
//                Toast.makeText(this, "Please enter email address.", Toast.LENGTH_SHORT).show();
//            } else if (etPassword.getText().toString().isEmpty()) {
//                Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
//            } else if (etEmailAddress.getText().toString().equals("praja@hubbell.com") && etPassword.getText().toString().equals("Hub@123")) {
//                Intent loginIntent = new Intent(this, BleActivity.class);
//                startActivity(loginIntent);
//            } else {
//                Toast.makeText(this, "Email Id/Password is incorrect.", Toast.LENGTH_SHORT).show();
//
//            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}