package com.athila.mvpweather.data.datasource.api.interceptors;

import com.athila.mvpweather.data.datasource.api.ApiConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by athila on 14/03/16.
 */
public class BasicHeadersInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Content-Type", ApiConstants.SUPPORTED_CONTENT_TYPE)
                // TODO: whatever you want to send in all requests
                .build();

        return chain.proceed(request);
    }
}
