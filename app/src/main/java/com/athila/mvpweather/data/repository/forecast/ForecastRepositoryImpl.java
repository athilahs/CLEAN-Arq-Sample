package com.athila.mvpweather.data.repository.forecast;

import com.athila.mvpweather.data.datasource.api.ForecastApi;
import com.athila.mvpweather.data.model.Forecast;

import rx.Observable;

/**
 * Created by athila on 17/03/16.
 */
public class ForecastRepositoryImpl implements ForecastRepository {

    @Override
    public Observable<Forecast> getForecast(double latitude, double longitude) {
        ForecastApi forecastApi = ForecastApi.Factory.create();

        // TODO: always get from API? Would be better if the data came first from database, so the UI can be updated immediately
        return forecastApi.getForecast(latitude, longitude);
    }
}
