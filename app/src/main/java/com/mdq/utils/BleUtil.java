package com.mdq.utils;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mdq.interfaces.AppConstants;
import com.mdq.activities.safetySelectionActivity;
import com.mdq.marinetechapp.databinding.ActivitySafetySelectionBinding;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class BleUtil {
    public String TAG = BleUtil.class.getSimpleName();
    private Context context;
    private static BluetoothGatt mBluetoothGatt;

    private static final String SERVICE_ID = "00001f00-0000-1000-8000-00805f9b34fb"; //BLE Service Id
    private static UUID CHARACTERISTIC_WRITE_UUID = UUID.fromString("00001f01-0000-1000-8000-00805f9b34fb");
    private static UUID CHARACTERISTIC_READ_UUID = UUID.fromString("00001F04-0000-1000-8000-00805F9B34FB");

    private static String CHARACTERISTIC_WIFI_SETUP = "06ff";
    private static String DEVICE_VALIDATION = "02ff";
    private static String LOCKER_PIN = "07ff";
    private static String SETUP_LOCKER = "03ff";
    private static String SOS_NUM_CHANGE = "08ff";
    private static String NORMAL_SECURE_MODE = "09ff";
    private static String OPEN_CLOSE_LOCKER_STATUS = "09ff";
    private static String VALIDATE_MOBILE_KEY = "0Bff";
    private static String OPEN_CLOSE_LOCKER = "0AFF";

    private PreferenceManagerMarine preferenceManager;
    private PreferenceManager preferenceManagerVSafe;
    private String commandToPair;
    private BluetoothDevice bluetoothName;
    private boolean isWrite = false;
    private boolean isRead = false;
    private boolean isNotify = false;
    private boolean isScanning = false;
    public static boolean isScanEnable = false;

    // Used to load the 'marinetechapp' library on application startup.
    static {
        System.loadLibrary("marinetechapp");
    }

    public native String enc(byte[] getdata);
    public native String dec(byte[] decryptData);

    Handler handler = new Handler();
    int positionWrite;


    public BleUtil(Context context) {
        this.context = context;
        preferenceManager = new PreferenceManagerMarine(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startScanning() {
        try {

            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            ScanSettings settings = new ScanSettings.Builder()
                    .setLegacy(false)
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setReportDelay(0)
                    .build();

            scanner.startScan(leScanCallback);

            Log.i("SCAN", "START");
            isScanning = true;
            handler.postDelayed(() -> {
                BluetoothLeScannerCompat scanner1 = BluetoothLeScannerCompat.getScanner();
                try {
                    if (isScanning) {
                        scanner1.stopScan(leScanCallback);
                        Log.i("SCAN", "STOP");
                        isScanning = false;
                    }
                } catch (IllegalStateException e) {
                    Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
                    intentVal.putExtra("val", "ble_disconnected");
                    intentVal.putExtra("extra", "true");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);
                }
                if (!isScanning && isScanEnable) {
                    startScanning();
                }

            }, 15000);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception e", "", e);
        }
    }

    private final ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i("onScanFailed", errorCode + "");
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            Log.e("call", "onScanResult");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.e("DeviceNameAdd1", result.getDevice().getName() + "");

                if (result.getDevice().getName() != null) {
                    Log.e("DeviceNameAdd2", result.getDevice().getName() + "");

                    if (safetySelectionActivity.set_deviceList != null) {
                        Log.e("DeviceNameAdd3", result.getDevice().getName() + "");

                        if (!safetySelectionActivity.set_deviceList.contains(result.getDevice().getName())) {
                            Log.e("DeviceNameAdd", result.getDevice().getName() + "");
                            safetySelectionActivity.set_deviceList.add(result.getDevice().getName() + "");
                            safetySelectionActivity.deviceAddress.add(result.getDevice().getAddress().trim());
                            safetySelectionActivity.deviceArrayList.add(result.getDevice().getName() + "");
                            BluetoothDevice bluetoothDevice = result.getDevice();
                            safetySelectionActivity.bluetoothDeviceList.add(bluetoothDevice);
                            safetySelectionActivity.setAdapter(safetySelectionActivity.deviceArrayList, -1);

//                            BLEFragment.pbProgressCircular.setVisibility(View.VISIBLE);
//                            BLEFragment.txtProgressStatus.setVisibility(View.GONE);

                            int deviceArrayListsize = safetySelectionActivity.deviceArrayList.size();
                            Log.e("deviceArrayListsize", String.valueOf(deviceArrayListsize));

                            int bluetoothDeviceListsize = safetySelectionActivity.bluetoothDeviceList.size();
                            Log.e("bluetoothDeviceListsize", String.valueOf(bluetoothDeviceListsize));

                            /*final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.recyclerview_border).mutate();
                            BLEFragment.recyclerviewKiosks.setBackground(drawable);*/

                        }
                        //}
                    }
                }
            }
        }
    };

    //stop ble scan
    public void stopScanning() {
        Log.e("StopScanning", "StopScanning");
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        try {
            if (scanner != null) {
                scanner.stopScan(leScanCallback);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("IllegalStateException", "", e);
        }
    }

    //Connect BLE Tool
    @SuppressLint("MissingPermission")
    public void connect_to_tool(BluetoothDevice bluetoothDevice) {
        Log.e("check_to_connect_device", bluetoothDevice.getName());

        try {
            connectBle(bluetoothDevice);
            Log.e("Connected", "");
            Toast.makeText(context, "Connected...", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //set Delay to connect BLE
    private void connectBle(final BluetoothDevice bluetoothDevice) {
        try {
            Log.e("connectBle", "In the connectBle function.");
            bluetoothName = bluetoothDevice;
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    mBluetoothGatt = bluetoothDevice.connectGatt(context, false, gattCallback);
                }
            }, 60);
        } catch (Exception e) {
            Log.e("Exception", "", e);
        }
    }


    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            int state = newState;
            Log.e("onConnectionStatus", status + "....." + newState);
            if (newState == STATE_CONNECTED) {
                Log.e("Device State", "Device Connected");

                gatt.discoverServices();

                Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
                intentVal.putExtra("val", "ble_device_connected");
                intentVal.putExtra("receivedData", bluetoothName.getName());
                intentVal.putExtra("status", String.valueOf(status));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);

//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//                safetySelectionActivity.registerReceiver(mReceiver, filter);

            } else if (newState == STATE_DISCONNECTED) {
                Log.e("Device State", "Device Disconnected");
                try {
                    Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
                    intentVal.putExtra("val", "ble_device_disconnected");
                    intentVal.putExtra("status", String.valueOf(status));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);
                    disconnect_tool();
                    if (mBluetoothGatt != null) {
                        refreshDeviceCache(mBluetoothGatt);
                        mBluetoothGatt.close();
                        mBluetoothGatt = null;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("NullPointerException", "", e);
                }
                if (gatt != null) {
                    gatt.close();
                    gatt = null;
                }
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            Log.d("onServicesDiscovered", "Service uuid ");
            List<BluetoothGattService> gattServices = gatt.getServices();
            Log.d("onServicesDiscovered", "Services count: " + gattServices.size());

            for (BluetoothGattService gattService : gattServices) {
                Log.d("gattService", gattService.getUuid().toString());
            }

            BluetoothGattCharacteristic characteristic = gatt.getService(UUID.fromString(SERVICE_ID)).getCharacteristic(CHARACTERISTIC_READ_UUID);

            Log.e("charcheck", characteristic.getUuid().toString());
            gatt.setCharacteristicNotification(characteristic, true);

            List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = mBluetoothGatt.getService(UUID.fromString(SERVICE_ID)).getCharacteristics();
            Log.e("BluetoothGattCharacter", bluetoothGattCharacteristics.toString());

            for (int i = 0; i < bluetoothGattCharacteristics.size(); i++) {
                BluetoothGattCharacteristic bluetoothGattCharacteristics1 = bluetoothGattCharacteristics.get(i);
                String string = String.valueOf(bluetoothGattCharacteristics1);
                if (bluetoothGattCharacteristics1.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE || characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) {
                    isWrite = true;
                }
                if (bluetoothGattCharacteristics1.getProperties() == BluetoothGattCharacteristic.PROPERTY_READ) {
                    isRead = true;
                }
                if (bluetoothGattCharacteristics1.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                    isNotify = true;
                }
                Log.e("BluetoothGattCharacter1", "" + bluetoothGattCharacteristics1.getUuid() + bluetoothGattCharacteristics1.getProperties() + isWrite + isRead + isNotify);
            }

            for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                Log.e("BluetoothGattDescriptor", "BluetoothGattDescriptor: " + descriptor.getUuid().toString());
            }
            final BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002901-0000-1000-8000-00805f9b34fb"));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            } else {
                Log.e("null val", "null val");
            }

            boolean subscribed = gatt.setCharacteristicNotification(characteristic, true); //returns true
            if (subscribed) {
                Log.e(TAG, "Subscription to notifications successful");
            } else {
                Log.e(TAG, "failed to subscribe to MESH_PROVISIONING_OUT notifications");
            }
            gatt.readDescriptor(descriptor);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            String decryptedValue = "";
            Log.e("read_Enter", "read_Enter");

            if (characteristic.getValue() != null) {
                byte[] charValue = characteristic.getValue();
                final String str_result = Helper.bytesToHex(charValue);
                Log.e("onCharacteristicRead", str_result);
                checkDecryptedValue(str_result, decryptedValue);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
//            String encryptSecondData = "";
//            String decryptSecondDataValue = "";
            byte[] charValue = characteristic.getValue();
            final String str_result = Helper.bytesToHex(charValue);
            Log.e("datacheck", str_result);

//            String firstFour = str_result.substring(0, 4);
//            String last_four = str_result.substring(36, 40);
//            String encryptValue = str_result.substring(4, 36);
//            String bleLength = str_result.substring(2, 4);
//
//            Log.e("SubstringValue", firstFour + "," + last_four + "," + encryptValue);
//
//            String decryptedValue = dec(Helper.hexStringToByteArray(encryptValue));
            String decryptedValue = "";
//            Log.e("decryptedValue", decryptedValue + "");
//            if (bleLength.equals("20")) {
//                encryptSecondData = str_result.substring(36, 68);
//                Log.i("encryptSecondData", encryptSecondData);
//                decryptSecondDataValue = dec(Helper.hexStringToByteArray(encryptSecondData));
//                decryptedValue = decryptedValue + decryptSecondDataValue;
//            }
//

            checkDecryptedValue(str_result, decryptedValue);
        }
    };

    private void checkDecryptedValue(String receivedData, String decryptedValue) {
        Log.e("checkdecryptedValue", String.valueOf(decryptedValue.length()));
        Log.e("checkreceiveddata", String.valueOf(receivedData.length()));

        String firstTwo = receivedData.substring(0, 2);
        String success_value = receivedData.substring(2, 4);
//        String wifiStatus = decryptedValue.substring(4, 6);
//        String ethernetStatus = decryptedValue.substring(6, 8);
//        String nfcStatus = decryptedValue.substring(8, 10);
//        String bleStatus = decryptedValue.substring(10, 12);
//        String get_response_value = decryptedValue.substring(4, 32);
//
//        String getSerialNumber = decryptedValue.substring(4, 19);

//        Log.e("first_two_string", firstTwo);
//        Log.e("SelfTestStatus", wifiStatus + " ...! " + ethernetStatus + " ...! " + nfcStatus);

        if (firstTwo.equals("65")) {
            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "door_closed");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);
        } else if (firstTwo.equals("64")) {
            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "door_opened");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);
        } else if (firstTwo.equals("66")) {

            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "UIN_SET");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);
        } else if (firstTwo.equals("67")) {

            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "PIN_SET");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);

        } else if (firstTwo.equals("C8")) {

            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "ERROR");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);

        } else if (firstTwo.equals("111")) {

            //Validate mobile number in home screen
            //existing mobile num and existing device id
            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "ERROR");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);

        } else if (firstTwo.equals("112")) {

            //Validate mobile number in home screen
            //- Existing mobile
            //number and new device
            //ID.
            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "ERROR");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);

        } else if (firstTwo.equals("113")) {

            //Validate mobile number in home screen
            //New mobile number
            //and Existing device ID
            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "ERROR");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);

        } else if (firstTwo.equals("109")) {

            //Validated the user
            Intent intentVal = new Intent(AppConstants.BLE_DATA); //FILTER is a stringto to identify this intent
            intentVal.putExtra("val", "locker_opened");
            intentVal.putExtra("receivedData", receivedData);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intentVal);

        }
    }


    //Disconnect BLE Tool
    @SuppressLint("MissingPermission")
    public static void disconnect_tool() {
        if (mBluetoothGatt != null) {
            if (mBluetoothGatt.getDevice() != null) {
                mBluetoothGatt.disconnect();
            }
        }
    }

    public boolean refreshDeviceCache(BluetoothGatt gatt) {
        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
            if (localMethod != null) {
                boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
                return bool;
            }
        } catch (Exception localException) {
            Log.e(TAG, "An exception occurred while refreshing device");
        }
        return false;
    }

    public void showToast(final String toast) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pingCmd() {
        try {
            Log.e(AppConstants.ENTER, "pingCmd");

            String pair = "01ff"; //Test LED CMD
            Log.e("pingCmd", pair);

            commandToPair = pair;
            Log.e("commandToPing", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("pingCMDException", "", e);
        }
    }

    public void wifiSetup(String WIFI_name, String PASSWORD) {
        try {
            Log.e(AppConstants.ENTER, "wifiSetUP");

            String hex_WIFI_NAME = Helper.String_to_hex(WIFI_name);
            Log.e("hex_WIFI_NAME", hex_WIFI_NAME);

            String hex_WIFI_PASSWORD = Helper.String_to_hex(PASSWORD);
            Log.e("hex_WIFI_PASSWORD", hex_WIFI_PASSWORD);

            String len = String.valueOf(hex_WIFI_NAME.length() / 2);
            Log.e("LEN/", "" + hex_WIFI_NAME.length());
            Log.e("len", len);

            String Len_WIFI_NAME = Helper.decToHex(hex_WIFI_NAME.length() / 2);
            Log.e("Len_WIFI_NAME", Len_WIFI_NAME);

            Len_WIFI_NAME = Len_WIFI_NAME.substring(6);

            String Len_WIFI_Password = Helper.decToHex(hex_WIFI_PASSWORD.length() / 2);
            Len_WIFI_Password = Len_WIFI_Password.substring(6);

            Log.e("Len_WIFI_Password", Len_WIFI_Password);

            String pair = CHARACTERISTIC_WIFI_SETUP + Len_WIFI_NAME + hex_WIFI_NAME + Len_WIFI_Password + hex_WIFI_PASSWORD;

            Log.e("wifiSetUP", pair);

            commandToPair = pair;
            Log.e("commandToPing", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("pingCMDException", "", e);
        }
    }

    public void DeviceNameChange() {
        try {
            Log.e(AppConstants.ENTER, "DeviceNameChange");

            String pair = "05ff4d4451"; //Test LED CMD
            Log.e("wifiSetUP", pair);

            commandToPair = pair;
            Log.e("DeviceNameChange", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("pingCMDException", "", e);
        }
    }

    public void LockerPinChange(String num, String OLD_Pin, String NEW_Pin) {
        try {
            Log.e(AppConstants.ENTER, "LockerPinChange");

            String pair = LOCKER_PIN + Helper.String_to_hex(num) + Helper.String_to_hex(OLD_Pin) + Helper.String_to_hex(NEW_Pin);

            Log.e("wifiSetUP", pair);

            commandToPair = pair;
            Log.e("LockerPinChange", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("pingCMDException", "", e);
        }
    }

    public void NormalSecureMode(String num, String mode) {
        try {
            Log.e(AppConstants.ENTER, "NormalSecureMode");

            String pair = NORMAL_SECURE_MODE + Helper.String_to_hex(num) + mode;//Test LED CMD
            Log.e("wifiSetUP", pair);

            commandToPair = pair;
            Log.e("NormalSecureMode", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("pingCMDException", "", e);
        }
    }

    public void EmergencyPinAndMobile(String oldNum, String oldSosPin, String NewSosPin, String newnum) {
        try {
            Log.e(AppConstants.ENTER, "NormalSecureMode");

            String pair = SOS_NUM_CHANGE + Helper.String_to_hex(oldNum) + Helper.String_to_hex(oldSosPin) + Helper.String_to_hex(NewSosPin) + Helper.String_to_hex(newnum);
            Log.e("wifiSetUP", pair);

            commandToPair = pair;
            Log.e("NormalSecureMode", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("pingCMDException", "", e);
        }
    }

    public void ValidateDevice(String key) {
        try {
            Log.e(AppConstants.ENTER, "ValidateDevice");

//            String pair = "02ff31323334353637383930"; //Test LED CMD
            String pair = DEVICE_VALIDATION + Helper.String_to_hex(key); //Test LED CMD

            Log.e("wifiSetUP", pair);

            commandToPair = pair;
            Log.e("ValidateDevice", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("pingCMDException", "", e);
        }
    }

    public void OpenCloseLockerStatus(String num, String PIN) {
        try {
            Log.e(AppConstants.ENTER, "OpenCloseLockerStatus");

            String pair = OPEN_CLOSE_LOCKER_STATUS + Helper.String_to_hex(num) + Helper.String_to_hex(PIN); //Test LED CMD
            Log.e("OpenCloseLockerStatus", pair);

            commandToPair = pair;
            Log.e("OpenCloseLockerStatus", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("OpenCloseLockerStatus", "", e);
        }
    }

    public void ValidateMobileKey(String num) {
        try {
            Log.e(AppConstants.ENTER, "ValidateMobileKey");

            String pair = VALIDATE_MOBILE_KEY + Helper.String_to_hex(num) + Helper.String_to_hex(num); //Test LED CMD
            Log.e("ValidateMobileKey", pair);

            commandToPair = pair;
            Log.e("ValidateMobileKey", commandToPair);
            handler.postDelayed(() -> {
                Log.e("ValidateMobileKey", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("ValidateMobileKey", "", e);
        }
    }

    public void MobileKeyRegister(String num, String L_PIN, String SOS_PIN) {
        try {
            Log.e(AppConstants.ENTER, "MobileKeyRegister");
            String pair = SETUP_LOCKER + Helper.String_to_hex(L_PIN) + Helper.String_to_hex(SOS_PIN) + Helper.String_to_hex(num);

            Log.e("MobileKeyRegister", pair);
            commandToPair = pair;
            Log.e("MobileKeyRegister", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("MobileKeyRegister", "", e);
        }
    }

    public void Open_Locker(String num) {
        try {
            Log.e(AppConstants.ENTER, "MobileKeyRegister");
            String pair = OPEN_CLOSE_LOCKER + Helper.String_to_hex(num);

            Log.e("MobileKeyRegister", pair);
            commandToPair = pair;
            Log.e("MobileKeyRegister", commandToPair);
            handler.postDelayed(() -> {
                Log.e("testPing", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);
        } catch (Exception e) {
            Log.e("MobileKeyRegister", "", e);
        }
    }

    public String setWifiSsidConfig(String wifiSsidName) {
        try {
            Log.e(AppConstants.ENTER, "setWifiSsidConfig");
            String pair = "";
            String checksum = "";
            String strChecksumFinal = "";
            if (wifiSsidName.length() <= 15) {
                pair = "3A10120D";
            } else if (wifiSsidName.length() > 15 && wifiSsidName.length() <= 31) {
                pair = "3A20120D";
            } else if (wifiSsidName.length() == 32) {
                pair = "3A30120D";
            }

            Log.e("setWifiConfig", pair);

            String lastTwo = pair.substring(6);
            String firstFour = pair.substring(0, 4);
            String funCode = pair.substring(4, 6);
            String wifiSSID = wifiSsidName;

            String encWifiSsid = Helper.String_to_hex(wifiSSID);
            Log.e("enc_wifi_ssid", encWifiSsid);
            Log.e("enc_wifi_ssid_len", encWifiSsid.length() + "");

            //wifiSSID 32byte
            while (encWifiSsid.length() != 64) {
                encWifiSsid = encWifiSsid + "0";
            }

            Log.e("enc_wifi_ssid", encWifiSsid);

            String toBeEncrypted = enc(Helper.hexStringToByteArray(funCode + encWifiSsid));
            String toBeEncryptedremainingsecond = enc(Helper.hexStringToByteArray(encWifiSsid.substring(30, 62)));
            String tobeEncryptedremainingthird = enc(Helper.hexStringToByteArray(encWifiSsid.substring(62, 64) + "000000000000000000000000000000"));
            Log.i("substring", encWifiSsid.substring(62, 64));
            Log.i("ssidtobenecrypted", toBeEncrypted);
            Log.i("toBeEncryptedremaining", toBeEncryptedremainingsecond);
            Log.i("tobeEncryptedremthird", tobeEncryptedremainingthird);
            if (wifiSsidName.length() <= 15) {
                checksum = Helper.get_xorVal(firstFour + toBeEncrypted);
                strChecksumFinal = Integer.toHexString(Integer.parseInt(checksum));
                if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
                Log.e(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
                commandToPair = firstFour + toBeEncrypted + strChecksumFinal + lastTwo;
            } else if (wifiSsidName.length() > 15 && wifiSsidName.length() <= 31) {
                checksum = Helper.get_xorVal(firstFour + toBeEncrypted + toBeEncryptedremainingsecond);
                strChecksumFinal = Integer.toHexString(Integer.parseInt(checksum));
                if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
                Log.e(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
                commandToPair = firstFour + toBeEncrypted + toBeEncryptedremainingsecond + strChecksumFinal + lastTwo;
            } else if (wifiSsidName.length() == 32) {
                checksum = Helper.get_xorVal(firstFour + toBeEncrypted + toBeEncryptedremainingsecond + tobeEncryptedremainingthird);
                strChecksumFinal = Integer.toHexString(Integer.parseInt(checksum));
                if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
                Log.e(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
                commandToPair = firstFour + toBeEncrypted + toBeEncryptedremainingsecond + tobeEncryptedremainingthird + strChecksumFinal + lastTwo;
            }
            characteristicWrite();

            Log.e("cmdToSetWifiSsidConfig", commandToPair);

            String cmdLength = String.valueOf(commandToPair.length());
            Log.e(AppConstants.COMMANDLENGTH, cmdLength);

        } catch (Exception e) {
            Log.e("setwifiidconfexception", "", e);
        }

        return commandToPair;

    }

    public String setWifiSsidPasswordConfig(String wifiSsidPassword) {
        try {
            Log.e(AppConstants.ENTER, "setWifipwdConfig");
            String pair = "";
            String checksum = "";
            String strChecksumFinal = "";
            if (wifiSsidPassword.length() <= 15) {
                pair = "3A10130D";
            } else if (wifiSsidPassword.length() > 15 && wifiSsidPassword.length() <= 31) {
                pair = "3A20130D";
            } else if (wifiSsidPassword.length() == 32) {
                pair = "3A30130D";
            }
            Log.e("setWifipwdConfig", pair);

            String lastTwo = pair.substring(6);
            String firstFour = pair.substring(0, 4);
            String funCode = pair.substring(4, 6);
            String wifiPwd = wifiSsidPassword;
            String encWifiPwd = Helper.String_to_hex(wifiPwd);
            Log.e("encWifiPwd", encWifiPwd);

            // wifiPwd 32byte
            while (encWifiPwd.length() != 64) {
                encWifiPwd = encWifiPwd + "0";
            }
            String toBeEncrypted = enc(Helper.hexStringToByteArray(funCode + encWifiPwd));
            String toBeEncryptedRemainingPasswordsecond = enc(Helper.hexStringToByteArray(encWifiPwd.substring(30, 62)));
            String tobeEncryptedRemainingPasswordthird = enc(Helper.hexStringToByteArray(encWifiPwd.substring(62, 64) + "000000000000000000000000000000"));
            if (wifiSsidPassword.length() <= 15) {
                checksum = Helper.get_xorVal(firstFour + toBeEncrypted);
                strChecksumFinal = Integer.toHexString(Integer.parseInt(checksum));

                if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
                Log.e(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
                commandToPair = firstFour + toBeEncrypted + strChecksumFinal + lastTwo;
            } else if (wifiSsidPassword.length() > 15 && wifiSsidPassword.length() <= 31) {
                checksum = Helper.get_xorVal(firstFour + toBeEncrypted + toBeEncryptedRemainingPasswordsecond);
                strChecksumFinal = Integer.toHexString(Integer.parseInt(checksum));

                if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
                Log.e(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
                commandToPair = firstFour + toBeEncrypted + toBeEncryptedRemainingPasswordsecond + strChecksumFinal + lastTwo;
            } else if (wifiSsidPassword.length() == 32) {
                checksum = Helper.get_xorVal(firstFour + toBeEncrypted + toBeEncryptedRemainingPasswordsecond + tobeEncryptedRemainingPasswordthird);
                strChecksumFinal = Integer.toHexString(Integer.parseInt(checksum));

                if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
                Log.e(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
                commandToPair = firstFour + toBeEncrypted + toBeEncryptedRemainingPasswordsecond + tobeEncryptedRemainingPasswordthird + strChecksumFinal + lastTwo;
            }

            characteristicWrite();

            Log.e("cmdToSetWifipwdConfig", commandToPair);

            String cmdLength = String.valueOf(commandToPair.length());
            Log.e(AppConstants.COMMANDLENGTH, cmdLength);

        } catch (Exception e) {
            Log.e("setwifipwdconfexception", "", e);
        }
        return commandToPair;
    }

    public String setWifiSsid() {
        try {
            Log.e(AppConstants.ENTER, "setwifi");
            String pair = "3A10140D";
            String lastTwo = pair.substring(6);
            String firstFour = pair.substring(0, 4);
            String toBeEncrypted = enc(Helper.getdata((byte) 0x14, (byte) 0x00));
            String strChecksum = Helper.get_xorVal(firstFour + toBeEncrypted);
            String strChecksumFinal = Integer.toHexString(Integer.valueOf(strChecksum));
            if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
            Log.i(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
            commandToPair = firstFour + toBeEncrypted + strChecksumFinal + lastTwo;
            Log.i("commandTosetwifi", commandToPair);
            characteristicWrite();
        } catch (Exception e) {
            Log.e("wifiexception", "", e);
        }
        return commandToPair;
    }

    @SuppressLint("MissingPermission")
    private void characteristicWrite() {
        try {
            Log.e(AppConstants.CHARACTERISTICWRITE, AppConstants.CHARACTERISTICWRITE);
            BluetoothGatt localblutoothgatt = mBluetoothGatt;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (localblutoothgatt != null) {

                        BluetoothGattService sevice = localblutoothgatt.getService(UUID.fromString(SERVICE_ID));

                        final BluetoothGattCharacteristic characteristic =
                                localblutoothgatt.getService(UUID.fromString(SERVICE_ID)).getCharacteristic(CHARACTERISTIC_WRITE_UUID);

                        Log.e("Char to write", characteristic.getUuid().toString());

                        positionWrite = 1;
                        byte[] byt_arr;
                        byt_arr = Helper.hexStringToByteArray(commandToPair);
                        characteristic.setValue(byt_arr);
                        Log.e(AppConstants.ENTER, "writeCharacteristic");
                        if (characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE || characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)
                            mBluetoothGatt.writeCharacteristic(characteristic);
                        else {
                            Log.e(AppConstants.CHARACTERISTICWRITE, "No Permission to Write");
                            final BluetoothGattCharacteristic characteristic1 =
                                    localblutoothgatt.getService(UUID.fromString(SERVICE_ID)).getCharacteristics().get(0);
                            Log.e("Char to write", characteristic1.getUuid().toString());
                            positionWrite = 0;
                            byte[] byt_arr1;
                            byt_arr1 = Helper.hexStringToByteArray(commandToPair);
                            stopScanning();
                            characteristic1.setValue(byt_arr1);
                            mBluetoothGatt.writeCharacteristic(characteristic1);
                        }
                        try {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final BluetoothGattCharacteristic characteristic1 =
                                            localblutoothgatt.getService(UUID.fromString(SERVICE_ID)).getCharacteristic(CHARACTERISTIC_READ_UUID);
                                    mBluetoothGatt.readCharacteristic(characteristic1);
                                    Log.e("READ_CALL", "READ_CALL");
                                }
                            }, 2000);
                        } catch (Exception e) {
                            Log.e("Exception", "" + e);
                        }
                    }
                }
            }, 1000);
        } catch (Exception e) {
            Log.e("characteristicWriteExc", "" + e);
        }
    }

    public String setSerialNumber(String serialNumber) {
        try {
            Log.e(AppConstants.ENTER, "setSerialNumber");

            String pair = "3A10020D";
            Log.e("CheckoutCmd", pair);

            String lastTwo = pair.substring(6);
            String firstFour = pair.substring(0, 4);
            String funCode = pair.substring(4, 6);
            Log.e("fun_Serial_Number", funCode + serialNumber);

            String enc_Serial_number = Helper.String_to_hex(serialNumber);
            Log.e("enc_Serial_number", enc_Serial_number);

            String toBeEncrypted = enc(Helper.hexStringToByteArray(funCode + enc_Serial_number));
            String strChecksum = Helper.get_xorVal(firstFour + toBeEncrypted);
            String strChecksumFinal = Integer.toHexString(Integer.parseInt(strChecksum));
            if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
            Log.e(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
            commandToPair = firstFour + toBeEncrypted + strChecksumFinal + lastTwo;
            Log.e("cmdToSetSerialNumber", commandToPair);

            handler.postDelayed(() -> {
                Log.e("setSerial", AppConstants.CHARACTERISTICWRITE);
                characteristicWrite();
            }, 0);

        } catch (Exception e) {
            Log.e("setSerialNumException", "", e);
        }
        return commandToPair;
    }

    public String getSerialNumber() {

        try {

            Log.e(AppConstants.ENTER, "getSerialNumber");
            String pair = "3A10010D"; //Get Serial Number
            String lastTwo = pair.substring(6);
            String firstFour = pair.substring(0, 4);
            String toBeEncrypted = enc(Helper.getdata((byte) 0x01, (byte) 0x00));
            String strChecksum = Helper.get_xorVal(firstFour + toBeEncrypted);
            String strChecksumFinal = Integer.toHexString(Integer.valueOf(strChecksum));
            if (strChecksumFinal.length() != 2) strChecksumFinal = "0" + strChecksumFinal;
            Log.i(AppConstants.STRCHECKSUMFINAL, strChecksumFinal);
            commandToPair = firstFour + toBeEncrypted + strChecksumFinal + lastTwo;
            Log.i("commandToGetSerialNum", commandToPair);
            characteristicWrite();

        } catch (Exception e) {
            Log.e("getSerialException", "", e);
        }
        return commandToPair;
    }
    /**
     * @return
     * @brief initializing the preferenceManager from shared preference for local use in this activity
     */
    public PreferenceManager getPreferenceManager() {
        if (preferenceManagerVSafe == null) {
            preferenceManagerVSafe = PreferenceManager.getInstance();
            preferenceManagerVSafe.initialize(context);
        }
        return preferenceManagerVSafe;
    }
}
