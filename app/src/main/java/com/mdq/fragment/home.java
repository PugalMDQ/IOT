package com.mdq.fragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.mdq.activities.Notificationactivity;
import com.mdq.activities.safetySelectionActivity;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.FragmentHomeBinding;
import com.mdq.utils.BleUtil;
import com.mdq.utils.PreferenceManager;
import com.mdq.utils.RequestPermission;

public class home extends Fragment {

    FragmentHomeBinding fragmentHomeBinding;
    boolean Door_status = true;
    MediaPlayer mediaPlayer;
    BleUtil bleUtil;
    PreferenceManager preferenceManager;
    boolean openClose = false;
    Dialog locationDialog;
    Dialog dialogBluetooth;
    RequestPermission requestPermission;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, new IntentFilter("ble_data"));

        locationDialog = new Dialog(requireContext(), R.style.dialog_center);
        dialogBluetooth = new Dialog(requireContext(), R.style.dialog_center);

        requestPermission = new RequestPermission(requireActivity());
        bleUtil = new BleUtil(requireContext());
        bleUtil.pingCmd();

        checkLocationPermission();
        BluetoothCheck();

        fragmentHomeBinding.drack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(requireActivity(), R.style.dialog_center);
                dialog.setContentView(R.layout.dialog_for_pin);
                dialog.setCanceledOnTouchOutside(false);
                TextView textView = dialog.findViewById(R.id.textView24);
                TextView pin = dialog.findViewById(R.id.pin);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pin.getText().length() == 4) {
                            if (!openClose) {
                                if (getPreferenceManager().getPrefLockerPin().equals(pin.getText().toString().trim()) || preferenceManager.getPrefSosNum().equals(pin.getText().toString().trim())) {
                                    dialog.dismiss();
                                    bleUtil.Open_Locker(getPreferenceManager().getPrefMobile());
                                } else {
                                    Toast.makeText(requireContext(), "Entered PIN is wrong ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Door already open ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Enter correct PIN", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        fragmentHomeBinding.Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), Notificationactivity.class));
            }
        });

        return fragmentHomeBinding.getRoot();

    }

    private void firstVisible() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentHomeBinding.medium.setVisibility(View.VISIBLE);
                secondVisible();
            }
        }, 500);
    }

    private void secondVisible() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                fragmentHomeBinding.lite.setVisibility(View.VISIBLE);
                thirdVisible();

            }
        }, 300);
    }

    private void thirdVisible() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AudioManager mgr = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
                int valuess = 15;//range(0-15)
                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, valuess, 0);
                mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.door_open);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playMedia();
                    }
                }, 8800);

                fragmentHomeBinding.statusImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlock_padlock_svgrepo_com));
                fragmentHomeBinding.lite.setVisibility(View.INVISIBLE);
                fragmentHomeBinding.medium.setVisibility(View.INVISIBLE);
                fragmentHomeBinding.noteText.setVisibility(View.VISIBLE);
                fragmentHomeBinding.noteText.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flash));

            }
        }, 200);
    }

    private void posting() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
    }

    private void playMedia() {
        mediaPlayer.stop();
    }


    /**
     * @return
     * @brief initializing the preferenceManager from shared preference for local use in this activity
     */
    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager.getInstance();
            preferenceManager.initialize(requireContext());
        }
        return preferenceManager;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = null;
            String selfteststatus = null;
            String receivedData = null;
            String status = null;

            if (intent != null) {
                data = (String) intent.getExtras().get("val");
                selfteststatus = (String) intent.getExtras().get("decryptValue");
                receivedData = (String) intent.getExtras().get("receivedData");
                status = (String) intent.getExtras().get("status");
                Log.i("testdataonthetop", data + "");
            }


            if (data.equals("door_closed")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("65")) {
                        openClose = false;
                    }
                }
            }

            if (data.equals("door_opened")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("64")) {
                        Toast.makeText(context, "Opened", Toast.LENGTH_SHORT).show();
                        fragmentHomeBinding.drack.setBackgroundColor(getResources().getColor(R.color.colorRed));
                        fragmentHomeBinding.statusImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlock_padlock_svgrepo_com));
                        openClose = true;
                    }
                }
            }

            if (data.equals("locker_opened")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("109")) {
                        firstVisible();
                    }
                }
            }


            if (data.equals("ERROR")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("C8")) {
                        Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver);
    }
    /**
     * @brief - A method to check Location permission is On/Off
     */
    private void checkLocationPermission() {
        if (requestPermission.checkLocationPermission()) {
            checkGPSStatus();
        } else {
            requestPermission.requestPermissionForLocation();
        }
    }

    private void BluetoothCheck() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth

        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                dialog_bluetooth();
            }else{
                dialogBluetooth.dismiss();
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothCheck();
            }
        },4000);
    }

    private void dialog_bluetooth() {

        dialogBluetooth.setContentView(R.layout.dialog_for_location_enable);
        dialogBluetooth.setCancelable(false);
        TextView textView = dialogBluetooth.findViewById(R.id.textView24);
        TextView subText = dialogBluetooth.findViewById(R.id.subText);
        subText.setText("Bluetooth settings must be enabled from the settings to use the application.");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);
            }
        });

        dialogBluetooth.show();

    }

    private void checkGPSStatus() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationDialog.dismiss();
        } else {
            locationDialog.setContentView(R.layout.dialog_for_location_enable);
            locationDialog.setCancelable(false);
            TextView textView = locationDialog.findViewById(R.id.textView24);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            locationDialog.show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkGPSStatus();
            }
        },4000);
    }

}