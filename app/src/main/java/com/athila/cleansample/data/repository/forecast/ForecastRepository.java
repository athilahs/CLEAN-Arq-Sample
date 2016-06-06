package com.athila.cleansample.data.repository.forecast;

import com.athila.cleansample.data.model.Forecast;

import rx.Observable;


/**
 * Created by athila on 14/03/16.
 */
public interface ForecastRepository {
     Observable<Forecast> getForecast(double latitude, double longitude);
}
