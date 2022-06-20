package com.mdq.http;

import com.mdq.utils.ApiClass;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

//For making the connection with the backend server
public class VSafeApiClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;
    private static ApiClass apiClass;

    public VSafeApiClient() {}

    public static ApiInterface apiinterface() {
        apiClass = new ApiClass();
        if (retrofit == null) {
            OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(apiClass.BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(defaultHttpClient)
                    .build();

            return retrofit.create(ApiInterface.class);

        }

        if (retrofit1 == null) {
            OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            retrofit1 = new Retrofit.Builder()
                    .baseUrl(apiClass.BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(defaultHttpClient)
                    .build();

        }
        return retrofit1.create(ApiInterface.class);

    }
}


