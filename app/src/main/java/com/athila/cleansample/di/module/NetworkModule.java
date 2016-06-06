package com.athila.cleansample.di.module;

import com.athila.cleansample.data.datasource.api.ApiConstants;
import com.athila.cleansample.data.datasource.api.ForecastApi;
import com.athila.cleansample.data.datasource.api.interceptors.BasicHeadersInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by athila on 31/05/16.
 */

@Module
public class NetworkModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .addInterceptor(new BasicHeadersInterceptor())
                .addInterceptor(loggingInterceptor);

        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideDefaultRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

//    @Provides
//    @Singleton
//    @AnotherHostBaseUrl
//    Retrofit provideAnotherHostRetrofit(OkHttpClient okHttpClient) {
//        return new Retrofit.Builder()
//                .baseUrl(ApiConstants.API_ANOTHER_HOST_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(okHttpClient)
//                .build();
//    }

    @Provides
    @Singleton
    ForecastApi provideForecastApi(Retrofit retrofit) {
        return retrofit.create(ForecastApi.class);
    }
}
