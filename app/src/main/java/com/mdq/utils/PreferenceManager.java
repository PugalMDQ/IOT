package com.mdq.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

//For local Storage
public class PreferenceManager {
    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private int mPrivateMode = 0;

    //For storing token
    private static final String PREF_TOKEN = "PREF_TOKEN";

    //For Storing account login or not
    private static final String PREF_IS_LOGGED_IN = "PREF_IS_LOGGED_IN";

    //For Storing user name
    private static final String PREF_NAME = "PREF_MYFINALYST";

    private static final String PREF_VERIFICATION = "PREF_VERIFICATION";

    private static final String PREF_MOBILE = "PREF_MOBILE";

    private static final String PREF_LOCKER_PIN = "PREF_LOCKER_PIN";

    private static final String PREF_ID = "PREF_ID";

    private static final String PREF_BLE_POSITION = "PREF_BLE_POSITION";

    private static final String PREF_UIN_NUM = "PREF_UIN_NUM";

    private static final String PREF_SOS_NUM = "PREF_SOS_NUM";

    private static final String PREF_BLE_CONNECTED = "PREF_BLE_CONNECTED";

    private static final String PREF_USERNAME = "PREF_USERNAME";

    private static final String PREF_BLE_DEVICE = "PREF_BLE_DEVICE";

    private static final String PREF_SIGN_UP = "PREF_SIGN_UP";

    private static final String PREF_LOGIN_BIOMETRIC = "PREF_LOGIN_BIOMETRIC";

    private static final String PREF_MACID_STATUS = "PREF_MACID_STATUS";

    private static PreferenceManager mInstance;

    public void initialize(Context context) {
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, mPrivateMode);
        editor = sharedPreferences.edit();
    }

    public static PreferenceManager getInstance() {
        if (mInstance == null)
            mInstance = new PreferenceManager();

        return mInstance;
    }

    private PreferenceManager() {
    }

    //    public PreferenceManager(Context context) {
//        this.context = context;
//        pref = context.getSharedPreferences(PREF_NAME, mPrivateMode);
//        editor = pref.edit();
//    }

    public void clearPreference() {
        //editor.clear();
        editor.remove(PREF_IS_LOGGED_IN);
        editor.remove(PREF_TOKEN);
        editor.apply();
    }

    public void setPrefToken(String Token) {
        editor.putString(PREF_TOKEN, Token);
        editor.commit();
    }

    public String getPrefToken() {
        return sharedPreferences.getString(PREF_TOKEN, null);
    }

    public void setPrefVerification(String Token) {
        editor.putString(PREF_VERIFICATION, Token);
        editor.commit();
    }

    public String getPrefVerification() {
        return sharedPreferences.getString(PREF_VERIFICATION, null);
    }

    public void setPrefMobile(String Token) {
        editor.putString(PREF_MOBILE, Token);
        editor.commit();
    }

    public String getPrefMobile() {
        return sharedPreferences.getString(PREF_MOBILE, null);
    }

    public void setPrefUsername(String Token) {
        editor.putString(PREF_USERNAME, Token);
        editor.commit();
    }

    public String getPrefUsername() {
        return sharedPreferences.getString(PREF_USERNAME, null);
    }

    public void setPrefUinNum(String Token) {
        editor.putString(PREF_UIN_NUM, Token);
        editor.commit();
    }

    public String getPrefUinNum() {
        return sharedPreferences.getString(PREF_UIN_NUM, null);
    }

    public void setPrefBleDevice(BluetoothDevice mmDevice) {
        Gson gson = new Gson();
        String json = gson.toJson(mmDevice);
        editor.putString(PREF_BLE_DEVICE, json);
        editor.commit();
    }

    public String getPrefBleDevice() {
        return sharedPreferences.getString(PREF_BLE_DEVICE, null);
    }

    public void setPrefSosNum(String Token) {
        editor.putString(PREF_SOS_NUM, Token);
        editor.commit();
    }

    public String getPrefSosNum() {
        return sharedPreferences.getString(PREF_SOS_NUM, null);
    }

    public void setPrefLoginBiometric(String Token) {
        editor.putString(PREF_LOGIN_BIOMETRIC, Token);
        editor.commit();
    }

    public String getPrefLoginBiometric() {
        return sharedPreferences.getString(PREF_LOGIN_BIOMETRIC, "no");
    }

    public void setPrefMacidStatus(String Token) {
        editor.putString(PREF_MACID_STATUS, Token);
        editor.commit();
    }

    public String getPrefMacidStatus() {
        return sharedPreferences.getString(PREF_MACID_STATUS, "0");
    }

    public void setPrefBleConnected(String Token) {
        editor.putString(PREF_BLE_CONNECTED, Token);
        editor.commit();
    }

    public String getPrefBleConnected() {
        return sharedPreferences.getString(PREF_BLE_CONNECTED, null);
    }

    public void setPrefLockerPin(String Token) {
        editor.putString(PREF_LOCKER_PIN, Token);
        editor.commit();
    }

    public String getPrefLockerPin() {
        return sharedPreferences.getString(PREF_LOCKER_PIN, null);
    }

    public void setPrefSignUp(String Token) {
        editor.putString(PREF_SIGN_UP, Token);
        editor.commit();
    }

    public String getPrefSignUp() {
        return sharedPreferences.getString(PREF_SIGN_UP, null);
    }

    public void setPrefId(String Token) {
        editor.putString(PREF_MOBILE, Token);
        editor.commit();
    }

    public String getBLEPosition() {
        return sharedPreferences.getString(PREF_BLE_POSITION, "");
    }

    public String getPrefId() {
        return sharedPreferences.getString(PREF_MOBILE, null);
    }

    public void setBLEPosition(String blePosition) {
        editor.putString(PREF_BLE_POSITION, blePosition);
        editor.commit();
    }
}