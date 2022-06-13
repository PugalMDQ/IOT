package com.mdq.v_safe.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdq.v_safe.databinding.ActivityMain2Binding;

import java.nio.charset.StandardCharsets;

public class  MainActivity2 extends AppCompatActivity {

    // Used to load the 'v_safe_c_java' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ActivityMain2Binding binding;

    char ttf,ttf1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;

//        tv.setText(enc(hexStringToByteArray("1212"),hexStringToByteArray("123456789012345"),4));

    }

    public  byte[] hexStringToByteArray(String s) {
        int len = s.trim().length();
        byte[] byteArrray = s.getBytes(StandardCharsets.UTF_8);

        return byteArrray;

    }

    /**
     * A native method that is implemented by the 'v_safe_c_java' native library,
     * which is packaged with this application.
     */

//    public native String enc(byte[] getdata,byte[] getdata1,int len);

}