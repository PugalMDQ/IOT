package com.mdq.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Helper {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    static Context context;

    public Helper(Context context) {
        this.context = context;
    }

    //Emoji Validation
    /**
     * @brief - This emoji filter method used to filter the unwanted emoji in edittext field
     */
    public static InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    };

    /**
     * @brief - This Asterisk method used to show asterisk in password field
     */
    public static class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void showToast(final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public static String get_xorVal(String val) {
        byte[] byte_data = hexStringToByteArray(val);
        int i = 2;
        long xor = 0;
        long res = 0;

        ArrayList<Long> datalist = new ArrayList<Long>();

        for (int j = 0; j <= val.length() - 2; j = j + 2) {
            Long a = Long.parseLong(val.substring(j, j + 2), 16);
            datalist.add(a);
        }
        long i1 = byte_data[0];
        long i2 = byte_data[1];
        res = datalist.get(0) ^ datalist.get(1);
        while (i != datalist.size()) {
            long data = datalist.get(i);
            res = res ^ (data);
            i = i + 1;
        }
        String s3 = String.valueOf(res);
        return String.valueOf(s3);
    }

    public static byte[] getdata(byte a, byte b) {
        byte[] sendCommandByte = {a, b, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        return sendCommandByte;
    }

    public static String String_to_hex(String data) {
        byte[] buf = data.getBytes();
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = hexArray[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = hexArray[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static String HexToString(String hex) {
        StringBuilder stringbuilder = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            stringbuilder.append((char) decimal);
        }
        return stringbuilder.toString();
    }

    public static String decToHex(int dec) {
        int sizeOfIntInHalfBytes = 8;
        int numberOfBitsInAHalfByte = 4;
        int halfByte = 0x0F;
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
        {

            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= numberOfBitsInAHalfByte;

        }
        return hexBuilder.toString();
    }
}
