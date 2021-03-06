package com.mdq.activities;

import static android.view.View.VISIBLE;
import static com.mdq.utils.Helper.HexToString;
import static com.mdq.utils.Helper.hexStringToByteArray;
import static com.mdq.utils.Helper.showToast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdq.adapters.UnConfigDeviceListAdapter;
import com.mdq.interfaces.ConnectPosition;
import com.mdq.interfaces.DeviceListInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.activities.BleActivity;
import com.mdq.marinetechapp.databinding.ActivitySafetySelectionBinding;
import com.mdq.pojo.DeviceListObject;
import com.mdq.utils.BleUtil;
import com.mdq.utils.Helper;
import com.mdq.utils.PreferenceManager;
import com.mdq.utils.RequestPermission;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class safetySelectionActivity extends AppCompatActivity implements DeviceListInterface, ConnectPosition {

    public static ActivitySafetySelectionBinding activitySafetySelectionBinding;
    public static Set<String> set_deviceList;
    public static ArrayList<String> deviceArrayList;
    public static List<BluetoothDevice> bluetoothDeviceList;
    public static List<DeviceListObject> deviceListObjects;
    public static ArrayList<String> deviceListDub;
    public static ArrayList<String> deviceAddress = new ArrayList<>();
    public static UnConfigDeviceListAdapter unConfigDeviceListAdapter;
    private static Context context;
    private static DeviceListInterface deviceListInterface;
    private static ConnectPosition connectPosition;
    public static RecyclerView rcvSearchMarineBleDevice;
    private BleUtil bleUtil;
    private WifiManager wifiManager;
    PreferenceManager preferenceManager;
    RequestPermission requestPermission;
    private String blePosition;
    public BluetoothDevice bluetoothDevice;
    public static BluetoothGatt gatt;
    private boolean onClick = true;
    boolean Connected = false;
    boolean openClose = false;
    boolean FromLogin = false;
    String from;
    public static SwipeRefreshLayout swipeRefreshLayout;
    Dialog dialog_Spinner;
    ProgressBar progressBar;
    TextView textView;
    Dialog locationDialog;
    Dialog dialogBluetooth;
    boolean bleCheck = true;
    Dialog spinner_Connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySafetySelectionBinding = ActivitySafetySelectionBinding.inflate(getLayoutInflater());
        setContentView(activitySafetySelectionBinding.getRoot());

        try {
            Intent intent = getIntent();
            from = intent.getStringExtra("from");
            if (from.equals("login")) {
                FromLogin = true;
                openClose = true;
            }
        } catch (Exception e) {

        }

        clicks();
        swipeRefreshLayout = findViewById(R.id.refresh);
        locationDialog = new Dialog(safetySelectionActivity.this, R.style.dialog_center);
        dialogBluetooth = new Dialog(safetySelectionActivity.this, R.style.dialog_center);
        spinner_Connection = new Dialog(safetySelectionActivity.this, R.style.dialog_center);

        BluetoothCheck();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("ble_data"));
        bleUtil = new BleUtil(getApplicationContext(),"safe");
        activitySafetySelectionBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connected) {
                    if (activitySafetySelectionBinding.check.isChecked()) {
                        if (openClose) {
                            if(getPreferenceManager().getPrefMacidStatus().equals("5")){
                                startActivity(new Intent(safetySelectionActivity.this, HomeActivity.class)
                                        .putExtra("from","login"));
                            } else if(getPreferenceManager().getPrefMacidStatus().equals("4")){
                                startActivity(new Intent(safetySelectionActivity.this, EmergencyNumberActivity.class));
                            } else if(getPreferenceManager().getPrefMacidStatus().equals("3")){
                                startActivity(new Intent(safetySelectionActivity.this, Wifi_configuration.class));
                            } else if(getPreferenceManager().getPrefMacidStatus().equals("2")){
                                startActivity(new Intent(safetySelectionActivity.this, activity_profile_setup.class));
                            } else if(getPreferenceManager().getPrefMacidStatus().equals("1")){
                                startActivity(new Intent(safetySelectionActivity.this, fingerprintenable.class));
                            } else if(getPreferenceManager().getPrefMacidStatus().equals("0")){
                                startActivity(new Intent(safetySelectionActivity.this, MobileRegistration.class));
                            }

                        } else {
                            if (bleCheck) {
                                bleUtil.pingCmd();
                            }
                        }
                    } else {
                        Toast.makeText(safetySelectionActivity.this, "Read the Term and Condition", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(safetySelectionActivity.this, "Device is not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activitySafetySelectionBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });



        initialize();
        accessOtherClass();

    }

    private void clicks() {
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.check));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                        startActivity(new Intent(safetySelectionActivity.this, Term_Condition.class));
            }
        };
        spannableString.setSpan(clickableSpan1, 63, 80, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        activitySafetySelectionBinding.TermsCondition.setText(spannableString);
        activitySafetySelectionBinding.TermsCondition.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void BluetoothCheck() {
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
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

    private void spinner_dialog() {
        dialog_Spinner = new Dialog(safetySelectionActivity.this, R.style.dialog_center);
        dialog_Spinner.setContentView(R.layout.dialog_spinner);
        dialog_Spinner.setCanceledOnTouchOutside(false);
        progressBar = dialog_Spinner.findViewById(R.id.progress);
        textView = dialog_Spinner.findViewById(R.id.subText);
        textView.setText("Please Open the V SAFE Locker door.");
        dialog_Spinner.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog_Spinner.dismiss();
            }
        }, 3000);

    }

    private void initialize() {
        deviceListInterface = this;
        context = getApplicationContext();
        rcvSearchMarineBleDevice = findViewById(R.id.rcvSearchBleDevice);
        deviceListDub = new ArrayList<>();
        set_deviceList = new HashSet<String>();
        deviceArrayList = new ArrayList<>();
        bluetoothDeviceList = new ArrayList<BluetoothDevice>();
        deviceListObjects = new ArrayList<>();
    }

    public static void setAdapter(ArrayList<String> deviceList, int position) {

        swipeRefreshLayout.setRefreshing(false);
        deviceListObjects.clear();
        deviceListDub = deviceList;
        if (deviceListDub.isEmpty()) {
            activitySafetySelectionBinding.text.setText("Swipe down to scan your V SAFE smart locker!");
        } else {
            activitySafetySelectionBinding.text.setVisibility(View.GONE);
        }
        unConfigDeviceListAdapter = new UnConfigDeviceListAdapter(context, deviceListDub, deviceListInterface, connectPosition, position);
        LinearLayoutManager llmtool = new LinearLayoutManager(context);
        rcvSearchMarineBleDevice.setLayoutManager(llmtool);
        unConfigDeviceListAdapter.notifyDataSetChanged();
        rcvSearchMarineBleDevice.setAdapter(unConfigDeviceListAdapter);
    }

    private void refresh() {
        activitySafetySelectionBinding.text.setText("Searching V SAFE smart lockers...");
        activitySafetySelectionBinding.text.setVisibility(VISIBLE);
        BleUtil.disconnect_tool();
        bleUtil.stopScanning();
        bluetoothDeviceList.clear();
        deviceListDub.clear();
        set_deviceList.clear();
        try {
            unConfigDeviceListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("refreshException", e + "");
        }
        bleUtil.startScanning();
        onClick = true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void goToConfigure(String position) {
        bleUtil.stopScanning();

        try {
            blePosition = position;
            Log.i("blePosition", " getPreferenceManager().getBLEPosition()");

            int pos = 0;
            if (bluetoothDeviceList.size() > 0) {
                for (int j = 0; j < bluetoothDeviceList.size(); j++) {
                    if (bluetoothDeviceList.get(j).getName().equalsIgnoreCase(String.valueOf(deviceArrayList.get(Integer.parseInt(position))))) {
                        pos = j;
                    }
                }
                bluetoothDevice = bluetoothDeviceList.get(pos);
                gatt = null;
                if (gatt == null) {
                    if (onClick) {
                        bleUtil.stopScanning();
                        bleUtil.connect_to_tool(bluetoothDevice);

                        Connection_Dialog();
                        Log.e("check_to_connect_device", bluetoothDevice.getName());

                        getPreferenceManager().setPrefBleDevice(bluetoothDevice);
                        getPreferenceManager().setPrefBleAddress(bluetoothDevice.getAddress());

//                        Gson gson = new Gson();
//                        String json = getPreferenceManager().getPrefBleDevice();
//                        Type type = new TypeToken<BluetoothDevice>() {
//                        }.getType();
//                        BluetoothDevice bluetoothDevices = gson.fromJson(json, BluetoothDevice.class);
//                        Log.e("check_to_connect_device123", bluetoothDevices.getName());
                        onClick = false;
                    }
                }
            } else {
                Log.i("Blesizetoast", "Size is 0");
            }
        } catch (Exception e) {
            Log.e("Exception", "", e);
        }
    }

    private void Connection_Dialog() {
        spinner_Connection.setContentView(R.layout.spinner_connection);
        spinner_Connection.setCancelable(false);
        spinner_Connection.show();
    }

    private void accessOtherClass() {

        connectPosition = (ConnectPosition) this;
        requestPermission = new RequestPermission(this);
        bleUtil = new BleUtil(context,"safe");
        set_deviceList = new HashSet<String>();
        deviceArrayList = new ArrayList<>();
        bluetoothDeviceList = new ArrayList<BluetoothDevice>();
        checkLocationPermission();

    }

    /**
     * @brief - A method to check Location permission is On/Off
     */
    private void checkLocationPermission() {
        if (requestPermission.checkLocationPermission()) {
            checkGPSStatus();

            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    bleUtil.isScanEnable = true;
                    bleUtil.startScanning();
                }
            }, 0);
        } else {
            requestPermission.requestPermissionForLocation();
        }
    }

    @Override
    public void getPosition(String position) {

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
            if (data.equals("ble_device_connected")) {
                Connected = true;
                if (bleCheck) {
                    if (from == null) {
                        bleUtil.pingCmd();
                    } else {
                        if (!from.equals("login")) {
                            bleUtil.pingCmd();
                        }else{
                            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                            spinner_Connection.dismiss();
                        }
                    }
                }
            }

            if (data.equals("ble_device_disconnected")) {
                onClick = true;
                setAdapter(deviceListDub, -1);
            }

            if (data.equals("door_closed")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("65")) {
                        openClose = false;
                        if(spinner_Connection.isShowing()){
                            Toast.makeText(context, "Connected.", Toast.LENGTH_SHORT).show();
                        }
                        spinner_Connection.dismiss();

                        if (bleCheck) {
                            bleUtil.pingCmd();
                            spinner_dialog();
                        }
                    }
                }
            }

            if (data.equals("door_opened")) {

                if (!TextUtils.isEmpty(receivedData)) {
                    if (receivedData.substring(0, 2).equals("64")) {
                        if(spinner_Connection.isShowing()){
                            Toast.makeText(context, "Connected.", Toast.LENGTH_SHORT).show();
                        }
                        spinner_Connection.dismiss();
                        openClose = true;
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
    protected void onDestroy() {
        super.onDestroy();
        bleCheck = false;
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bleCheck = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        bleCheck = true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        bleCheck = false;

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
