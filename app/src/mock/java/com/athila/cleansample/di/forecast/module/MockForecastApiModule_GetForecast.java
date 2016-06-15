package com.athila.cleansample.di.forecast.module;

import android.content.Context;

import com.athila.cleansample.data.datasource.api.ForecastApi;
import com.athila.cleansample.data.datasources.api.forecast.MockGetForecast_NoInternetError;
import com.athila.cleansample.data.datasources.api.forecast.MockGetForecast_Success;
import com.athila.cleansample.di.MockApiNoConnectionError;
import com.athila.cleansample.di.forecast.MockForecastApiSuccess;

import dagger.Module;
import dagger.Provides;

/**
 * Created by athila on 31/05/16.
 */

@Module
public class MockForecastApiModule_GetForecast {

    private Context mContext;
    public MockForecastApiModule_GetForecast(Context context) {
        mContext = context;
    }

    @Provides
    @MockForecastApiSuccess
    ForecastApi provideMockForecastApi_Success() {
        return new MockGetForecast_Success(mContext);
    }

    @Provides
    @MockApiNoConnectionError
    ForecastApi provideMockForecastApi_NoConnectionError() {
        return new MockGetForecast_NoInternetError(mContext);
    }
}
