package com.prajwal.covid_prajwal.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static Retrofit retrofit = null;
    static OkHttpClient.Builder builder = new OkHttpClient.Builder();

    public static Retrofit getRetrofit()
    {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(httpLoggingInterceptor);

        if(retrofit == null)
        {

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.covid19india.org/")
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
