package com.mdq.marinetechapp.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mdq.marinetechapp.R;
import com.mdq.utils.GPSBroadcastReceiver;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    List<Address> addresses;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private GPSBroadcastReceiver gpsBroadcastReceiver = new GPSBroadcastReceiver();
    public static final int REQUEST_LOCATION = 199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        registerReceiver(gpsBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        LocalBroadcastManager.getInstance(SplashActivity.this).registerReceiver(receiver,
                new IntentFilter("location_data"));
        checkLocationPermission();
        //setDefaults();
    }

    private void setDefaults() {
        int splashTiming = 3000;
        new Handler().postDelayed(() -> {
            Intent splashIntent = new Intent(getApplicationContext(), LoginActivityMarine.class);
            startActivity(splashIntent);
        }, splashTiming);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = (String) intent.getExtras().get("val");


            if (data.equals("location_off")) {
                Log.i("Location","off");
            }
            if (data.equals("location_on")) {
                setDefaults();
            }
        }
    };

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            setDefaults();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (!isstatusCheck()) {
                    displayLocationSettingsRequest(SplashActivity.this);
                    Log.i("Locaton", "OFF");
                } else {
                    setDefaults();
                    getLocation();
                }
            }
        }
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e("SecurityException", "", e);
        }
    }

    public boolean isstatusCheck() {
        final LocationManager manager = (LocationManager) SplashActivity.this.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(SplashActivity.this, REQUEST_LOCATION);

                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;

                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.e("Enter", "onProviderEnabled");

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.e("Enter", "onProviderDisabled");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    Toast.makeText(SplashActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                    break;

                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(SplashActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                    if (!isstatusCheck()) {
                        displayLocationSettingsRequest(SplashActivity.this);
                        Log.i("Locaton", "OFF");
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(SplashActivity.this).unregisterReceiver(receiver);
        unregisterReceiver(gpsBroadcastReceiver);
    }
}