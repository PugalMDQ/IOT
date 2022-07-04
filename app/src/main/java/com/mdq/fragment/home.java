package com.mdq.fragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.mdq.activities.Notificationactivity;
import com.mdq.activities.Wifi_configuration;
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
    boolean openClose = true;
    boolean bleCheck = true;
    Dialog locationDialog;
    Dialog dialogBluetooth;
    RequestPermission requestPermission;
    boolean dialogs = true;
    Dialog dialog;
    Dialog dialog_Spinner;
    Dialog dialog_Spinner_Locker_status;
    String from;
    boolean UINStatus=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, new IntentFilter("ble_data"));

        try {
            Bundle bundle = this.getArguments();
            from = bundle.getString("message");
//            Toast.makeText(requireContext(), "" + from, Toast.LENGTH_SHORT).show();
            if (from.trim().equals("login")) {
                gatheringLockerStatusDialog();
            } else if (from.trim().equals("signup")) {
                gatheringLockerStatusDialog();
            }
        } catch (Exception e) {

        }

        locationDialog = new Dialog(requireContext(), R.style.dialog_center);
        dialogBluetooth = new Dialog(requireContext(), R.style.dialog_center);
        dialog_Spinner = new Dialog(requireContext(), R.style.dialog_center);
        dialog_Spinner_Locker_status = new Dialog(requireContext(), R.style.dialog_center);

        gatheringLockerStatusDialog();

        requestPermission = new RequestPermission(requireActivity());
        bleUtil = new BleUtil(requireContext(),"summa");
        bleCall();
        checkLocationPermission();
        BluetoothCheck();
        dialog = new Dialog(requireActivity(), R.style.dialog_center);

        dialogCheck();
        SpannableString ss = new SpannableString("Hello "+getPreferenceManager().getPrefUsername().trim());
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 7, 6+getPreferenceManager().getPrefUsername().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        fragmentHomeBinding.name.setText(ss);

        fragmentHomeBinding.drack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!openClose) {
                    dialog.setContentView(R.layout.dialog_for_pin);
                    dialog.setCanceledOnTouchOutside(false);
                    TextView textView = dialog.findViewById(R.id.textView24);
                    TextView pin = dialog.findViewById(R.id.pin);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pin.getText().length() == 4) {
                                if (!openClose) {
                                    dialog.dismiss();
                                    bleUtil.Open_Locker(pin.getText().toString().trim(), getPreferenceManager().getPrefMobile());
                                    spinner_dialog();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(UINStatus) {
                                                dialog_Spinner.dismiss();
                                                Toast.makeText(getActivity(), "Retry", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },6000);

                                } else {
                                    Toast.makeText(requireContext(), "Door already open ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Enter correct PIN", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                    dialogCheck();
                } else {
                    Toast.makeText(requireContext(), "Door already open ", Toast.LENGTH_SHORT).show();
                }
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

    private void dialogCheck() {
        if (dialog.isShowing()) {
            dialogs = false;
        } else {
            dialogs = true;
        }
    }

    private void gatheringLockerStatusDialog() {
        dialog_Spinner_Locker_status.setContentView(R.layout.dialog_gathering_locker_status);
        dialog_Spinner_Locker_status.setCanceledOnTouchOutside(false);
        dialog_Spinner_Locker_status.show();
    }

    private void bleCall() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bleCheck) {
                    if (dialogs) {
                        bleUtil.pingCmd();
                        bleCall();
                    }
                }
            }
        }, 5000);
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

                dialogCheck();
                bleCall();
                fragmentHomeBinding.drack.setBackground(getResources().getDrawable(R.drawable.round_bg));
                fragmentHomeBinding.statusImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlock_padlock_svgrepo_com));
                fragmentHomeBinding.lite.setVisibility(View.INVISIBLE);
                fragmentHomeBinding.medium.setVisibility(View.INVISIBLE);
                fragmentHomeBinding.noteText.setVisibility(View.VISIBLE);
                fragmentHomeBinding.noteText.setText("Locker is OPEN...");
                fragmentHomeBinding.noteText.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flash));
            }
        }, 200);
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
                        dialogCheck();
                        bleCall();
                        dialog_Spinner_Locker_status.dismiss();
                        fragmentHomeBinding.drack.setBackground(getResources().getDrawable(R.drawable.green_round_bg));
                        fragmentHomeBinding.statusImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_svgrepo_com));
                        openClose = false;
                        fragmentHomeBinding.noteText.setVisibility(View.VISIBLE);
                        fragmentHomeBinding.noteText.setText("Locker is CLOSE...");
                        fragmentHomeBinding.noteText.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flash));
                    }
                }
            }

            if (data.equals("door_opened")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("64")) {
                        dialogCheck();
                        bleCall();
                        dialog_Spinner_Locker_status.dismiss();
                        fragmentHomeBinding.drack.setBackground(getResources().getDrawable(R.drawable.round_bg));
                        fragmentHomeBinding.statusImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlock_padlock_svgrepo_com));
                        openClose = true;
                        fragmentHomeBinding.noteText.setVisibility(View.VISIBLE);
                        fragmentHomeBinding.noteText.setText("Locker is OPEN...");
                        fragmentHomeBinding.noteText.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flash));

                    }
                }
            }

            if (data.equals("locker_opened")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    UINStatus=false;
                    if (receivedData.substring(0, 2).equals("6E")) {
                        dialog_Spinner.dismiss();
                        firstVisible();
                    }
                }
            }

            if (data.equals("ERROR")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("C8")) {
                        UINStatus=false;
                        dialog_Spinner.dismiss();
                        Toast.makeText(context, "Invalid Request", Toast.LENGTH_LONG).show();
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
        if (bleCheck) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth

            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    // Bluetooth is not enable :)
                    dialog_bluetooth();
                } else {
                    dialogBluetooth.dismiss();
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BluetoothCheck();
                }
            }, 4000);
        }
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

        if (bleCheck) {
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
            }, 4000);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bleCheck = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        bleCheck = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        bleCheck = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        bleCheck = false;
    }

    private void spinner_dialog() {
        dialog_Spinner.setContentView(R.layout.dialog_spinner);
        dialog_Spinner.setCanceledOnTouchOutside(false);
        ProgressBar progressBar = dialog_Spinner.findViewById(R.id.progress);
        TextView textView = dialog_Spinner.findViewById(R.id.subText);
        textView.setText("Validating...");
        dialog_Spinner.show();
    }

}