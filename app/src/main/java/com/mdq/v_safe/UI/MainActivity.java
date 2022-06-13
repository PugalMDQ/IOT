package com.mdq.v_safe.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.mdq.v_safe.Utils.BleUtil12;
import com.mdq.v_safe.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mDeviceList = new ArrayList<String>();

    private BroadcastReceiver mReceiver;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
    private boolean scanning;
    private Handler handler = new Handler();

    // Stops scanning after 500 seconds.
    private static final long SCAN_PERIOD = 500000;

    private LeDeviceListAdapter leDeviceListAdapter;

    ActivityMainBinding activityMainBinding;

    private BleUtil12 bleUtil;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater()) ;
        setContentView(activityMainBinding.getRoot());

//        scanLeDevice();
        bleUtil = new BleUtil12(MainActivity.this);
        bleUtil.startScanning();

    }

    private void scanLeDevice() {
        if (!scanning) {
            Toast.makeText(this, "h", Toast.LENGTH_SHORT).show();
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }

    }

    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "hello"+result.getDevice().getName(), Toast.LENGTH_SHORT).show();
                    leDeviceListAdapter = new LeDeviceListAdapter(result.getDevice().getName());
                    activityMainBinding.listView.setAdapter(leDeviceListAdapter);
                }
            };
}