package com.athila.mvpweather.data.datasource.api.retrorfit;

import com.athila.mvpweather.data.datasource.api.ApiConstants;
import com.athila.mvpweather.data.datasource.api.interceptors.BasicHeadersInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class provides a default instance os Retrofit with Logging interceptor and BasicHeadersInterceptors
 * <p/>
 * Created by athila on 17/05/16.
 */
public class DefaultRetrofit {
    private static Retrofit sInstance;

    public static Retrofit get() {
        if (sInstance == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                    .addInterceptor(new BasicHeadersInterceptor())
                    .addInterceptor(loggingInterceptor);

            // Make singleton
            sInstance = new Retrofit.Builder()
                    .baseUrl(ApiConstants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(builder.build())
                    .build();
        }
        return sInstance;
    }
}
