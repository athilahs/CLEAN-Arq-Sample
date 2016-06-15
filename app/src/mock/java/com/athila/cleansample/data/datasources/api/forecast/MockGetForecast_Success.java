package com.athila.cleansample.data.datasources.api.forecast;

import android.content.Context;

import com.athila.cleansample.data.datasources.api.util.JsonUtils;
import com.athila.cleansample.data.model.Forecast;
import com.google.gson.Gson;

import retrofit2.Response;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by athila on 15/06/16.
 */

public class MockGetForecast_Success extends MockForecastAPI {

    public MockGetForecast_Success(Context context) {
        super(context);
    }

    @Override
    public Observable<Forecast> getForecast(@Path("latitude") double latitude, @Path("longitude") double longitude) {
        Forecast mockForecast = new Gson().fromJson(JsonUtils.getFromFile(mContext, "json-mocks/forecast/get_forecast_200_ok.json"), Forecast.class);
        return mDelegate.returningResponse(Response.success(mockForecast)).getForecast(latitude, longitude);
    }
}
