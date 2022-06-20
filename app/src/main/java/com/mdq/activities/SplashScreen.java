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

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {

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
                Intent intent=new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },5900);

    }
}