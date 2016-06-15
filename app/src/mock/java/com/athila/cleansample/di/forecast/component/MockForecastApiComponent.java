package com.athila.cleansample.di.forecast.component;

/**
 * Created by athila on 15/06/16.
 */

import com.athila.cleansample.data.datasource.api.ForecastApi;
import com.athila.cleansample.di.MockApiNoConnectionError;
import com.athila.cleansample.di.forecast.MockForecastApiSuccess;
import com.athila.cleansample.di.forecast.module.MockForecastApiModule_GetForecast;

import dagger.Component;

/**
 * This module contains all mocks for 'getForecast' api call
 */
@Component(modules = {
        MockForecastApiModule_GetForecast.class
        // Add other modules that provides Mocks for other Forecast API methods
})
public interface MockForecastApiComponent {

    @MockForecastApiSuccess
    ForecastApi getForecast_Success();

    @MockApiNoConnectionError
    ForecastApi getForecast_NoConnectionError();
}
