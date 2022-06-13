package com.mdq.v_safe.UI;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.mdq.v_safe.Utils.Helper.EMOJI_FILTER;
import static com.mdq.v_safe.Utils.Helper.HexToString;
import static com.mdq.v_safe.Utils.Helper.showToast;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mdq.v_safe.Adapter.UnConfigDeviceListAdapter;
import com.mdq.v_safe.Interface.AppConstants;
import com.mdq.v_safe.Interface.ConnectPosition;
import com.mdq.v_safe.Interface.DeviceListInterface;
import com.mdq.v_safe.R;
import com.mdq.v_safe.Utils.BleUtil;
import com.mdq.v_safe.Utils.Helper;
import com.mdq.v_safe.Utils.PreferenceManager;
import com.mdq.v_safe.Utils.RequestPermission;
import com.mdq.v_safe.databinding.ActivityBleBinding;
import com.mdq.v_safe.pojo.DeviceListObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BleActivity extends AppCompatActivity implements DeviceListInterface, ConnectPosition, QrCodeScannerActivity.OnQrScannerInterface {

    public static RecyclerView rcvSearchMarineBleDevice;
    public static List<DeviceListObject> deviceListObjects;
    public static ArrayList<String> deviceListDub;
    public static UnConfigDeviceListAdapter unConfigDeviceListAdapter;
    private static Context context;
    private static DeviceListInterface deviceListInterface;
    private static ConnectPosition connectPosition;
    RequestPermission requestPermission;
    private BleUtil bleUtil;
    public static Set<String> set_deviceList;
    public static ArrayList<String> deviceArrayList;
    public static List<BluetoothDevice> bluetoothDeviceList;
    private String blePosition;
    private PreferenceManager preferenceManager;
    public BluetoothDevice bluetoothDevice;
    public static BluetoothGatt gatt;
    private boolean onClick = true;
    private TextView txtProgressStatus;
    private Helper helper;
    public static boolean isBTconnected = false;
    private Button btnRefresh;
    private ListView listView;
    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;
    private List<ScanResult> wifiListt;
    ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String wifiSsid;
    private String wifiSsidPassword;
    private ProgressBar pbProgressCircular;
    private boolean wifissidCmdValue = false;
    private boolean wifissidPasswordCmdValue = false;
    private int wifissidTestTimer;
    private int wifiSsidCmdCount;
    private int outOfRangeCount = 0;
    private int wifissidPasswordTestTimer;
    private int wifiSsidPasswordCmdCount;
    private CountDownTimer setWifiSsidPasswordCountDownTimer;
    private boolean wifiConfigCmdValue = false;
    private int wifiTestTimer;
    private int wifiCmdCount = 0;
    private CountDownTimer setWifiConfigCountDownTimer;
    private String sideA = "";
    private String sideB = "";
    private String serialNumber;
    private CountDownTimer getSerialIdCountDownTimer;
    private boolean getSerialIdTickValue = false;
    private int getSerialIdWaitingTimer;
    private CountDownTimer setWifiSsidCountDownTimer;
    private int serialNumberLength = 0;
    private int serialNumberLengthCalculation = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        initialisation();
        accessOtherClass();
        setListeners();
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                wifiListt = wifiManager.getScanResults();
                if (!wifiListt.isEmpty()) {
                    int totalWifiDevices = wifiListt.size();
                    try {
                        while (totalWifiDevices >= 0) {
                            totalWifiDevices--;
                            arrayList.add(wifiListt.get(totalWifiDevices).SSID);
                            listView.setVisibility(VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.w("WiFi Exception: ", e);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Wifi found.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListeners() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refresh() {
        BleUtil.disconnect_tool();
        bleUtil.stopScanning();
        bluetoothDeviceList.clear();
        set_deviceList.clear();
        deviceListDub.clear();
        try {
            unConfigDeviceListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("refreshException", e + "");
        }
        bleUtil.startScanning();
        onClick = true;
        isBTconnected = false;
        disAbleViews();
    }

    private void initialisation() {
        rcvSearchMarineBleDevice = findViewById(R.id.rcvSearchMarineBleDevice);
        txtProgressStatus = findViewById(R.id.txtProgressStatus);

        btnRefresh = findViewById(R.id.btnRefresh);

        pbProgressCircular = findViewById(R.id.pbProgressCircular);

    }

    public static void setAdapter(ArrayList<String> deviceList, int position) {
        deviceListObjects.clear();
        deviceListDub = deviceList;
        unConfigDeviceListAdapter = new UnConfigDeviceListAdapter(context, deviceListDub, deviceListInterface, connectPosition, position);
        LinearLayoutManager llmtool = new LinearLayoutManager(context);
        rcvSearchMarineBleDevice.setLayoutManager(llmtool);
        unConfigDeviceListAdapter.notifyDataSetChanged();
        rcvSearchMarineBleDevice.setAdapter(unConfigDeviceListAdapter);
    }

    private void accessOtherClass() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext().getApplicationContext(), "Please turn ON wifi", Toast.LENGTH_LONG).show();
        }

        deviceListObjects = new ArrayList<>();
        deviceListDub = new ArrayList<>();
        context = this;
        helper = new Helper(context);
        deviceListInterface = this;
        connectPosition = (ConnectPosition) this;
        requestPermission = new RequestPermission(this);
        bleUtil = new BleUtil(context);
        set_deviceList = new HashSet<String>();
        deviceArrayList = new ArrayList<>();
        bluetoothDeviceList = new ArrayList<BluetoothDevice>();
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,
                new IntentFilter("ble_data"));
        LocalBroadcastManager.getInstance(context).registerReceiver(qrReceiver,
                new IntentFilter("from_scan"));

        checkLocationPermission();

    }

    /**
     * @brief - A method to check Location permission is On/Off
     */
    private void checkLocationPermission() {
        if (requestPermission.checkLocationPermission()) {
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("NewApi")
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

    @SuppressLint("MissingPermission")
    @Override
    public void goToConfigure(String position) {
        bleUtil.stopScanning();

        try {
            blePosition = position;
            Log.i("blePosition", getPreferenceManager().getBLEPosition());

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
                        showProgressBar();
                        // progressUtil.ShowConnectingProgressDialog();
                        //helper.toastSpeed("Connecting... Please Wait");
//                        pbProgressCircular.setVisibility(View.VISIBLE);
//                        txtProgressStatus.setVisibility(View.GONE);
                        txtProgressStatus.setText("Connecting... Please Wait");
                        Toast.makeText(getApplicationContext(), "Connecting... Please Wait", Toast.LENGTH_SHORT).show();
                        //recyclerviewKiosks.setBackgroundColor(Color.parseColor("#ffffff"));
                        rcvSearchMarineBleDevice.setPadding(2, 2, 2, 2);
                        bleUtil.stopScanning();
                        bleUtil.connect_to_tool(bluetoothDevice);
                        onClick = false;
                        // isRetestDevice = false;
                    }

//                    if (isRetestDevice) {
//                        showReconnectionAlert();
//                    }
                }
            } else {
                Log.i("Blesizetoast", "Size is 0");
            }
        } catch (Exception e) {
            Log.e("Exception", "", e);
        }


    }

    @Override
    public void getPosition(String position) {

    }

    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) preferenceManager = new PreferenceManager(this);
        return preferenceManager;
    }

    private BroadcastReceiver qrReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String extraAction = "";
            String side = "";
            if (action != null) {
                if (action.equals("from_scan")) {
                    extraAction = intent.getStringExtra("broadcastdata");
                    side = intent.getStringExtra("broadcastSide");
                    Log.i("extraaction", extraAction);
                    if (side.equals("sideA")) {
                        sideA = extraAction;
                        Log.i("sideA", sideA);

                        showSideB();
                    } else if (side.equals("sideB")) {
                        sideB = extraAction;
                        Log.i("sideB", sideB);

                        showSetSerialNumberButton();
                    }
                }
            }


        }
    };
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
                hideProgressBar();
                showToast("connected");
                isBTconnected = true;
                final String finalReceivedData = receivedData;
                Log.i("receivedblename", finalReceivedData);

                connectPosition.getPosition(blePosition);
                setAdapter(deviceListDub, Integer.parseInt(blePosition));


//                btnScanAGrey.setVisibility(View.GONE);
//                btnScanSideA.setVisibility(View.VISIBLE);
//
//                btnScanBGrey.setVisibility(View.GONE);
//                btnScanSideB.setVisibility(View.VISIBLE);

            }

            if (data.equals("ble_device_disconnected")) {
                hideProgressBar();
                onClick = true;
                isBTconnected = false;
                setAdapter(deviceListDub, -1);
            }

            if (data.equals("ping")) {
                if (!TextUtils.isEmpty(receivedData)) {
                    // String getSerialnumber = HexToString(receivedData.substring(4, 32));
                    //Log.i("getserialnumber", receivedData + ".........." + getSerialnumber);
                    // getPreferenceManager().setPrefGetBleSerialNumber(getSerialnumber);
                    Log.i("ble_status", "ping_success" + "......" + receivedData);
                    hideProgressBar();
//                    pingCmdValue = false;
//                    setversionui();
                    if (isBTconnected) {
                        wifiSsidPasswordDialog();
                    }
                }

            }

            if (data.equals("wifi_ssid")) {
                wifissidCmdValue = false;
                if (setWifiSsidCountDownTimer != null) {
                    setWifiSsidCountDownTimer.cancel();
                }
                hideProgressBar();
                if (isBTconnected && !TextUtils.isEmpty(selfteststatus)) {
                    String wifiSsidcode = selfteststatus.substring(0, 2);
                    String wifiSsidstatus = selfteststatus.substring(2, 4);
                    Log.i("wifiSsidcode", wifiSsidcode);
                    Log.i("wifiSsidstatus", wifiSsidstatus);
                    if (wifiSsidcode.equals("12") && wifiSsidstatus.equals("00")) {
                        showProgressBar();
                        wifissidPasswordCmdValue = true;
                        bleUtil.setWifiSsidPasswordConfig(wifiSsidPassword);
                        setWifiSsidPasswordCmdTimer();
                        setWifiSsidPasswordCountDownTimer.start();
                        showToast("Wifi ssid has been set successfuly");
                    } else {

                        showToast("Something went wrong");
                        showScanA();
                    }
                }
                //showScanA();
            }

            if (data.equals("wifi_ssid_password")) {
                wifissidPasswordCmdValue = false;
                if (setWifiSsidPasswordCountDownTimer != null) {
                    setWifiSsidPasswordCountDownTimer.cancel();
                }
                hideProgressBar();
                if (isBTconnected && !TextUtils.isEmpty(selfteststatus)) {
                    String wifiSsidPasswordCode = selfteststatus.substring(0, 2);
                    String wifiSsidPasswordStatus = selfteststatus.substring(2, 4);
                    Log.i("wifiSsidPasswordcode", wifiSsidPasswordCode);
                    Log.i("wifiSsidPasswordstatus", wifiSsidPasswordStatus);
                    if (wifiSsidPasswordCode.equals("13") && wifiSsidPasswordStatus.equals("00")) {
                        showProgressBar();
                        wifiConfigCmdValue = true;
                        bleUtil.setWifiSsid();
                        setWifiCmdTimer();
                        setWifiConfigCountDownTimer.start();
                        showToast("Wifi Ssid password has been set successfully");
                    } else {

                        showToast("Something went wrong");
                        showScanA();
                    }
                }
                //showScanA();

            }

            if (data.equals("wifi_config")) {
                wifiConfigCmdValue = false;
                String wifiIpAddress = "";
                String secondWifiIpAddress = "";
                if (setWifiConfigCountDownTimer != null) {
                    setWifiConfigCountDownTimer.cancel();
                }
                hideProgressBar();
                if (isBTconnected && !TextUtils.isEmpty(receivedData)) {

                    Log.i("wifidata", receivedData);
                    String wificode = receivedData.substring(0, 2);
                    String wifistatus = receivedData.substring(2, 4);
                    if (wificode.equals("14") && wifistatus.equals("00")) {
                        try {
                            wifiIpAddress = HexToString(receivedData.substring(4, 32));

                            if (receivedData.length() > 32 && receivedData.length() <= 64) {
                                secondWifiIpAddress = HexToString(receivedData.substring(32, 64));
                                wifiIpAddress = wifiIpAddress + secondWifiIpAddress;
                            }
                            Log.i("wifilength", HexToString(receivedData));
                            String[] seperated = wifiIpAddress.split("\\.");
                            Log.i("seperated[0]", seperated[0]);
                            Log.i("seperated[1]", seperated[1]);
                            Log.i("seperated[2]", seperated[2]);
                            Log.i("seperated[3]", seperated[3]);
                            Log.i("wifiIpAddress", wifiIpAddress);

                            String finalIp = seperated[0] + "." + seperated[1] + "." + seperated[2] + "." + seperated[3];
                            showIpAddressAlert(finalIp);
                        } catch (Exception e) {
                            Log.i("IpException", e + "");
                        }
                        showToast("Wifi configuration has been done successfully");

                    } else {

                        showIpAddressErrorAlert("Unable to set wifi configuration. Please check your wifi name and password and try again.");
                    }

                }
                showScanA();
            }

            if (data.equals("setserialnumber")) {
                hideProgressBar();
                if (isBTconnected && !TextUtils.isEmpty(selfteststatus)) {
                    String serialIdCode = selfteststatus.substring(0, 2);
                    String serialIdStatus = selfteststatus.substring(2, 4);
                    Log.i("serialIdCode", serialIdCode);
                    Log.i("serialidstatus", serialIdStatus);
                    if (serialIdCode.equals("02") && serialIdStatus.equals("00")) {
                        showProgressBar();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            bleUtil.getSerialNumber();
                            getSerialIdCountDownTimer.start();
                        }, 500);
                        getSerialIdTickValue = true;
                        getSerialIdTimer();
                    } else {

                        showIpAddressErrorAlert("Unable to set Serial Number. Please try again.");
                    }
                }
            }

            if (data.equals("getserialnumber")) {
                hideProgressBar();
                if (getSerialIdCountDownTimer != null) {
                    getSerialIdCountDownTimer.cancel();
                }
                getSerialIdTickValue = false;
                if (isBTconnected && !TextUtils.isEmpty(receivedData)) {
                    String getSerialnumber = HexToString(receivedData.substring(4, serialNumberLengthCalculation));
                    String getSerialNumberCode = receivedData.substring(0, 2);
                    String getSerialNumberStatusCode = receivedData.substring(2, 4);
                    Log.i("getserialnumber", getSerialnumber);
                    showToast(getSerialnumber);
                    if (getSerialNumberCode.equals("01") && getSerialNumberStatusCode.equals("00") && getSerialnumber.equals(serialNumber)) {
                        showIpAddressErrorAlert("Serial Number has been set successfully. Please restart the dock station.");
                        getPreferenceManager().setBLEPosition("");
                        //BleUtil.testDisconnect();

                        // refresh();
                    } else {

                        showIpAddressErrorAlert("Unable to set Serial Number. Please try again.");
                    }
                }

            }
        }

    };

    private void showScanA() {


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleUtil.isScanEnable = false;
        bleUtil.stopScanning();
        try {
            unregisterReceiver(wifiReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(qrReceiver);
    }

    @Override
    public void onSuccessfulQrCodeScan(String result) {
        Log.e("result", result);

    }

    private void wifiSsidPasswordDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(BleActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_wifi_ssid, null);
        final EditText etWifiSsid = (EditText) mView.findViewById(R.id.etWifiSsid);
        final EditText etWifiPassword = (EditText) mView.findViewById(R.id.etWifiPassword);
        etWifiSsid.setFilters(new InputFilter[]{EMOJI_FILTER, new InputFilter.LengthFilter(32)});
        etWifiPassword.setFilters(new InputFilter[]{EMOJI_FILTER, new InputFilter.LengthFilter(32)});
        listView = (ListView) mView.findViewById(R.id.listView);
        Button buttonCancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button buttonOkay = (Button) mView.findViewById(R.id.btn_okay);
        Button btnScan = (Button) mView.findViewById(R.id.btnScan);
        alert.setView(mView);
        final AlertDialog scanAlertDialog = alert.create();
        scanAlertDialog.setCanceledOnTouchOutside(false);
        /*Scanning the nearby available wifi scans*/
        btnScan.setOnClickListener(view -> {
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Please turn ON wifi", Toast.LENGTH_LONG).show();
            } else if (wifiManager.isWifiEnabled()) {
                registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifiManager.startScan();
                showToast("Scanning. Please wait...!");
            }
            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
            arrayList.clear();
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            etWifiSsid.setText(listView.getItemAtPosition(i).toString());
            listView.setVisibility(GONE);
        });
        buttonCancel.setOnClickListener(v -> scanAlertDialog.dismiss());
        buttonOkay.setOnClickListener(v -> {
            wifiSsid = etWifiSsid.getText().toString();
            wifiSsidPassword = etWifiPassword.getText().toString();
            /*Check whether the password and ssid not empty*/
            if (!wifiSsid.isEmpty() && !wifiSsidPassword.isEmpty()) {
                showProgressBar();
                wifissidCmdValue = true;
                bleUtil.setWifiSsidConfig(wifiSsid);
                setWifiSsidCmdTimer();
                setWifiSsidCountDownTimer.start();
                scanAlertDialog.dismiss();
            } else {
                if (wifiSsid.isEmpty()) {
                    showToast("Please enter the wifi ssid.");
                } else {
                    showToast("Please enter the wifi ssid password.");
                }
            }

        });
        scanAlertDialog.show();
    }

    private void showProgressBar() {
        pbProgressCircular.setVisibility(VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressBar() {
        pbProgressCircular.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void setWifiSsidCmdTimer() {
        Log.i("Call", "wifissidTimer");
        wifissidTestTimer = 0;
        setWifiSsidCountDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                if (wifissidCmdValue) {
                    ++wifissidTestTimer;
                    Log.i("wifissidTestTimer", wifissidTestTimer + "");
                }
            }

            @Override
            public void onFinish() {
                Log.i("wifissidCALL", AppConstants.ON_FINISH);
                if (wifissidCmdValue) {
                    if (wifissidTestTimer >= 8) {
                        ++wifiSsidCmdCount;
                        wifissidTestTimer = 0;
                        Log.e(AppConstants.RETRY, String.valueOf(wifissidCmdValue));
                        if (wifiSsidCmdCount < 3) {
                            bleUtil.setWifiSsidConfig(wifiSsid);
                            wifissidTestTimer = 0;
                            wifissidCmdValue = true;
                            setWifiSsidCountDownTimer.start();
                            Log.e(AppConstants.RETRYOUTOFRANGE, String.valueOf(outOfRangeCount));
                        } else {
                            wifiSsidCmdCount = 0;
                            wifissidCmdValue = false;
                            setWifiSsidCountDownTimer.cancel();
                            hideProgressBar();
                            showScanA();
                            showToast("Unable to set wifi ssid. Please try again.");

                        }

                    } else {
                        wifissidTestTimer = 0;
                        wifissidCmdValue = false;
                        setWifiSsidCountDownTimer.cancel();
                        hideProgressBar();
                        showScanA();
                    }

                }
            }
        };
    }

    private void setWifiSsidPasswordCmdTimer() {
        Log.i("Call", "wifissidPwdTimer");
        wifissidPasswordTestTimer = 0;
        setWifiSsidPasswordCountDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                if (wifissidPasswordCmdValue) {
                    ++wifissidPasswordTestTimer;
                    Log.i("wifissidpwdTestTimer", wifissidPasswordTestTimer + "");
                }
            }

            @Override
            public void onFinish() {
                Log.i("wifissidpwdCALL", AppConstants.ON_FINISH);
                if (wifissidPasswordCmdValue) {
                    if (wifissidPasswordTestTimer >= 8) {
                        ++wifiSsidPasswordCmdCount;
                        wifissidPasswordTestTimer = 0;
                        Log.e(AppConstants.RETRY, String.valueOf(wifissidPasswordCmdValue));
                        if (wifiSsidPasswordCmdCount < 3) {
                            bleUtil.setWifiSsidPasswordConfig(wifiSsidPassword);
                            wifissidPasswordTestTimer = 0;
                            wifissidPasswordCmdValue = true;
                            setWifiSsidPasswordCountDownTimer.start();
                            Log.e(AppConstants.RETRYOUTOFRANGE, String.valueOf(outOfRangeCount));
                        } else {
                            wifiSsidPasswordCmdCount = 0;
                            wifissidPasswordCmdValue = false;
                            setWifiSsidPasswordCountDownTimer.cancel();
                            hideProgressBar();
                            showScanA();
                            showToast("Unable to set wifi ssid password. Please try again.");

                        }

                    } else {
                        wifissidPasswordTestTimer = 0;
                        wifissidPasswordCmdValue = false;
                        setWifiSsidPasswordCountDownTimer.cancel();
                        hideProgressBar();
                        showScanA();
                    }

                }
            }
        };
    }

    private void setWifiCmdTimer() {
        Log.i("Call", "setwifiTimer");
        wifiTestTimer = 0;
        setWifiConfigCountDownTimer = new CountDownTimer(25000, 1000) {
            @Override
            public void onTick(long l) {
                if (wifiConfigCmdValue) {
                    ++wifiTestTimer;
                    Log.i("wifiTestTimer", wifiTestTimer + "");
                }
            }

            @Override
            public void onFinish() {
                Log.i("wifiCALL", AppConstants.ON_FINISH);
                if (wifiConfigCmdValue) {
                    if (wifiTestTimer >= 22) {
                        ++wifiCmdCount;
                        wifiTestTimer = 0;
                        Log.e(AppConstants.RETRY, String.valueOf(wifiConfigCmdValue));
                        if (wifiCmdCount < 3) {
                            bleUtil.setWifiSsid();
                            wifiTestTimer = 0;
                            wifiConfigCmdValue = true;
                            setWifiConfigCountDownTimer.start();
                            Log.e(AppConstants.RETRYOUTOFRANGE, String.valueOf(outOfRangeCount));
                        } else {
                            wifiCmdCount = 0;
                            wifiConfigCmdValue = false;
                            setWifiConfigCountDownTimer.cancel();
                            hideProgressBar();
                            showScanA();
                            showIpAddressErrorAlert("Unable to set wifi configuration. Please try again.");

                        }

                    } else {
                        wifiTestTimer = 0;
                        wifiConfigCmdValue = false;
                        setWifiConfigCountDownTimer.cancel();
                        hideProgressBar();
                        showScanA();
                    }

                }
            }
        };
    }

    private void showIpAddressErrorAlert(String s) {
        AlertDialog.Builder ipAlert = new AlertDialog.Builder(BleActivity.this);

        ipAlert.setTitle(AppConstants.ALERT);

        //Setting message manually and performing action on button click
        ipAlert.setMessage(s)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
        //Creating dialog box
        AlertDialog alert = ipAlert.create();
        //Setting the title manually
        alert.show();
    }

    private void showIpAddressAlert(String s) {
        AlertDialog.Builder ipAlert = new AlertDialog.Builder(BleActivity.this);

        ipAlert.setTitle(AppConstants.ALERT);

        //Setting message manually and performing action on button click
        ipAlert.setMessage("IP address : " + s)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
        //Creating dialog box
        AlertDialog alert = ipAlert.create();
        //Setting the title manually
        alert.show();
    }

    private void showSideB() {


    }

    private void disAbleViews() {


        disAbleWifiButton();
        disAbleSideAButton();
        disAbleSideBButton();
        disAbleSetSerialNumberButton();
    }

    private void disAbleWifiButton() {

    }

    private void disAbleSideAButton() {


    }

    private void disAbleSideBButton() {


    }

    private void disAbleSetSerialNumberButton() {


    }

    private void showSetSerialNumberButton() {


    }

    private void setSerialNumberDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(BleActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_set_serial_number, null);
        final EditText etEnterSerialNumber = (EditText) mView.findViewById(R.id.etEnterSerialNumber);
        etEnterSerialNumber.setFilters(new InputFilter[]{EMOJI_FILTER, new InputFilter.LengthFilter(14)});
        Button buttonSerialCancel = (Button) mView.findViewById(R.id.btn_serial_cancel);
        Button buttonSerialOkay = (Button) mView.findViewById(R.id.btn_serial_okay);
        alert.setView(mView);
        final AlertDialog setSerialAlertDialog = alert.create();
        setSerialAlertDialog.setCanceledOnTouchOutside(false);
//        etEnterSerialNumber.setText(Appconstants.JCCS);
//        Selection.setSelection(etEnterSerialNumber.getText(), etEnterSerialNumber.getText().length());

//        etEnterSerialNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //Do Nothing
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //Do Nothing
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                /*appending with JCCS as default for the users*/
//                if (!editable.toString().startsWith("JCCS-")) {
//                    etEnterSerialNumber.setText("JCCS-");
//                    Selection.setSelection(etEnterSerialNumber.getText(), etEnterSerialNumber.getText().length());
//
//                }
//            }
//        });

        buttonSerialCancel.setOnClickListener(v -> setSerialAlertDialog.dismiss());
        buttonSerialOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serialNumber = etEnterSerialNumber.getText().toString();

                /*Check the length of the serial number. If equal to 14, check serial number with cloud*/
                if (serialNumber.length() >= 9 && serialNumber.length() <= 14) {
                    showProgressBar();
                    serialNumberLength = serialNumber.length();
                    serialNumberLengthCalculation = (serialNumberLength * 2) + 4;
                    Log.e("serialnumbercheck", serialNumber);
                    Log.e("serialnumberCalculation", serialNumberLengthCalculation + "");
//                    checkSerialNumberViewModel.setSerialnumber(serialNumber);
//                    checkSerialNumberViewModel.checkSerialNumber();
                    bleUtil.setSerialNumber(serialNumber);
                    setSerialAlertDialog.dismiss();
                } else {
                    showToast("Please enter the Serial Number correctly");
                }

            }
        });
        setSerialAlertDialog.show();
    }

    private void getSerialIdTimer() {
        getSerialIdWaitingTimer = 0;
        getSerialIdCountDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                if (getSerialIdTickValue) {
                    ++getSerialIdWaitingTimer;
                    Log.i("getserialIdTimer", getSerialIdWaitingTimer + "");
                }
            }

            @Override
            public void onFinish() {
                Log.i("CALL", AppConstants.ON_FINISH);
                hideProgressBar();
                showIpAddressErrorAlert("Unable to set serial number. Please try again.");
                getSerialIdTickValue = false;

            }
        };
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}