package com.mdq.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mdq.marinetechapp.R;

import java.util.concurrent.Executor;

public class BioMetrix extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    boolean FromLogin = false;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_metrix);

        try {
            Intent intent = getIntent();
            from = intent.getStringExtra("from");
            if (from.trim().equals("login")) {
                FromLogin = true;
            }
        } catch (Exception e) {

        }

        biometricAuthentication();
    }

    private void biometricAuthentication() {
        executor = ContextCompat.getMainExecutor(this);
        BiometricManager biometricManager=BiometricManager.from(this);
        biometricPrompt = new BiometricPrompt(BioMetrix.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show();

                biometricAuthentication();
                startActivity(new Intent(BioMetrix.this,LoginActivity.class));
                finish();

            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                if (FromLogin) {
                    startActivity(new Intent(getApplicationContext(), safetySelectionActivity.class)
                            .putExtra("from", "login"));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), safetySelectionActivity.class));
                    finish();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText(" ")

                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}