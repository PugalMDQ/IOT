package com.mdq.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerMarine {
    private Context context;
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static final String PREF_NAME = "PREF_MARINE_TECH_APP";
    private int mPrivateMode = 0;

    private static final String PREF_BLE_POSITION = "PREF_BLE_POSITION";

    public PreferenceManagerMarine(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, mPrivateMode);
        editor = pref.edit();
    }


    public String getBLEPosition() {
        return pref.getString(PREF_BLE_POSITION, "");
    }

    public void setBLEPosition(String blePosition) {
        editor.putString(PREF_BLE_POSITION, blePosition);
        editor.commit();
    }
}
