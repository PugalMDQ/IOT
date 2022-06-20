package com.mdq.base;

import android.app.Application;
import android.content.Context;

import com.mdq.http.ApiInterface;
import com.mdq.http.VSafeApiClient;

//For setting the retrofit for api call to communicate with the backend server
public class VSafeApp extends Application {

        public static Context mContext;
        public static VSafeApp mInstance;
        public static VSafeApiClient vSafeApiClient ;

        @Override
        public void onCreate() {
            super.onCreate();
            mInstance = this;
            mContext = this;
            vSafeApiClient = new VSafeApiClient();
        }

        public static synchronized Context getContext() {
            return mContext;
        }

        public static VSafeApp getApp() {
            if (mInstance != null && mInstance instanceof VSafeApp) {
                return mInstance;
            } else {
                mInstance = new VSafeApp();
                mInstance.onCreate();
                return mInstance;
            }
        }

        public ApiInterface getRetrofitInterface() {
            return VSafeApiClient.apiinterface();
        }

        protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);
        }

    }


