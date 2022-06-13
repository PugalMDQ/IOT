
//
// Created by praja on 12/28/2018.
//

#include "native-lib.h"
#include<jni.h>
#include<string.h>
#include <stdio.h>
#include <malloc.h>
#include <android/log.h>
#include <syslog.h>
#include <stdlib.h>
#include "android/log.h"
#include "aes.h"
#include "TI_aes_128.h"

typedef unsigned char uint8_t;

uint8_t string_output[40];
unsigned char* enckey;
uint8_t enclen = 0;
int len=0;

uint8_t ascii_conversion(uint8_t hex_input)
{
    uint8_t ascii_char = 0;

    if(hex_input < 10)
    {
        hex_input += 0x30;
        ascii_char = hex_input;
    }
    else if((hex_input > 9) && (hex_input < 16))
    {
        switch(hex_input)
        {
            case 10:
                ascii_char = 'A';
                break;
            case 11:
                ascii_char = 'B';
                break;
            case 12:
                ascii_char = 'C';
                break;
            case 13:
                ascii_char = 'D';
                break;
            case 14:
                ascii_char = 'E';
                break;
            case 15:
                ascii_char = 'F';
                break;
        }
    }
    return ascii_char;
}

void hex2ascii(uint8_t hex_value[],int enclen)
{
    uint8_t temp = 0;
    uint8_t temp1 = 0;

    for (int i = 0; i <enclen; i++) {
        /*Convert MSB to ascii*/
        temp = hex_value[i];
        temp &= 0xF0;
        temp >>= 4;
        string_output[temp1++] = ascii_conversion(temp);
        /*Convert LSB to ascii*/
        temp = hex_value[i];
        temp &= 0x0F;
        string_output[temp1++] = ascii_conversion(temp);
    }
    __android_log_print(ANDROID_LOG_ERROR, "string_output12134", "%s", string_output);

}


//void hex2String(uint8_t hex_value[])
//{
//    uint8_t temp = 0;
//    uint8_t temp1 = 0;
//
//    for (int i = 0; i < 16; i++) {
//        temp = hex_value[i];
//        temp &= 0xF0;
//        temp >>= 4;
//        string_output[i] = ascii_conversion(temp);
//
//    }
//    __android_log_print(ANDROID_LOG_ERROR, "string_output12134", "%s", string_output);
//
//}
char dataenc[128];
JNIEXPORT
jstring Java_com_mdq_v_1safe_1c_1java_MainActivity2_enc(JNIEnv* env, jobject jobject1, jbyteArray getdata, jbyteArray getdata12, jint lens){

    unsigned char* plaintext = NULL;
    plaintext = (*env)->GetByteArrayElements(env, getdata, NULL);
    unsigned char* plaintext1 = (*env)->GetByteArrayElements(env, getdata12, NULL);

    len=lens;
    uint8_t i, r;

    typedef unsigned char uint8_t;
    __android_log_print(ANDROID_LOG_ERROR, "encprtyyy", "%s", plaintext);

    // hex2ascii(plaintext);
    // encryption

    int enclen = aes_encrypt(plaintext, len, plaintext1);

    // hex2ascii(plaintext);

    __android_log_print(ANDROID_LOG_ERROR, "encprtyyy", "%s", plaintext);

        hex2ascii(plaintext,enclen);

    __android_log_print(ANDROID_LOG_ERROR, "encprtyyy", "%s", string_output);

    //    hex2ascii(plaintext1);

    aes_decrypt(plaintext,enclen,plaintext1);

    memset(dataenc, 0, sizeof(dataenc));
    strncpy(dataenc, plaintext, enclen);

//    hex2ascii(plaintext);

    __android_log_print(ANDROID_LOG_ERROR, "string_output5678", "%s", dataenc);


    return (*env)->NewStringUTF(env,dataenc);

}

JNIEXPORT
jstring Java_com_mdq_v_1safe_1c_1java_MainActivity2_dec(JNIEnv* env, jobject jobject1, jbyteArray getdata, jbyteArray getdata12, jint lens){

    unsigned char* plaintext = NULL;
    plaintext = (*env)->GetByteArrayElements(env, getdata, NULL);
    unsigned char* plaintext1 = (*env)->GetByteArrayElements(env, getdata12, NULL);

    len=lens;
    uint8_t i, r;

    typedef unsigned char uint8_t;
    __android_log_print(ANDROID_LOG_ERROR, "encprtyyy", "%s", plaintext);

    // hex2ascii(plaintext);
    // encryption

    // hex2ascii(plaintext);

    __android_log_print(ANDROID_LOG_ERROR, "encprtyyy", "%s", plaintext);

        hex2ascii(plaintext,enclen);

    __android_log_print(ANDROID_LOG_ERROR, "encprtyyy", "%s", string_output);

    //    hex2ascii(plaintext1);

    aes_decrypt(plaintext,enclen,plaintext1);

    memset(dataenc, 0, sizeof(dataenc));
    strncpy(dataenc, plaintext, enclen);

//    hex2ascii(plaintext);

    __android_log_print(ANDROID_LOG_ERROR, "string_output5678", "%s", dataenc);


    return (*env)->NewStringUTF(env,dataenc);

}
