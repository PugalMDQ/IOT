package com.mdq.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivitySplashScreenBinding;
import com.mdq.utils.PreferenceManager;

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {

    PreferenceManager preferenceManager;
    ActivitySplashScreenBinding activitySplashScreenBinding;
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_grey));

        super.onCreate(savedInstanceState);

        activitySplashScreenBinding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(activitySplashScreenBinding.getRoot());
        gifImageView=findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getPreferenceManager().getPrefSignUp()!=null) {
                    if (getPreferenceManager().getPrefSignUp().equals("sanjai")) {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, EnterScreen.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(SplashScreen.this, EnterScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        },5900);

    }

    /**
     * @return
     * @brief initializing the preferenceManager from shared preference for local use in this activity
     */
    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager.getInstance();
            preferenceManager.initialize(getApplicationContext());
        }
        return preferenceManager;
    }
}