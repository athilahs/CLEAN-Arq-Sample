package com.athila.mvpweather.data.datasource.api;

import com.athila.mvpweather.data.model.Forecast;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by athila on 14/03/16.
 */
public interface ForecastApi {
    @GET("forecast/" + ApiConstants.REST_API_KEY + "/{latitude},{longitude}")
    Observable<Forecast> getForecast(@Path("latitude") double latitude,
                                     @Path("longitude") double longitude);
}
