package com.mdq.utils;

import static android.content.Context.LOCATION_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class GPSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("action", intent.getAction() + "");
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.i("Location", "ON");
                Intent intentVal = new Intent("location_data"); //FILTER is a stringto to identify this intent
                intentVal.putExtra("val", "location_on");
                intentVal.putExtra("extra", "true");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);
            } else {
                Log.i("Location", "OFF");
                Intent intentVal = new Intent("location_data"); //FILTER is a stringto to identify this intent
                intentVal.putExtra("val", "location_off");
                intentVal.putExtra("extra", "true");
            }
        } catch (Exception ex) {
            Log.i("GBSException",ex+"");
        }
    }
}
