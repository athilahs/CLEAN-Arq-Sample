package com.athila.cleansample.data.datasources.api.forecast;

import android.content.Context;

import com.athila.cleansample.data.model.Forecast;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by athila on 15/06/16.
 */

public class MockGetForecast_NoInternetError extends MockForecastAPI {

    public MockGetForecast_NoInternetError(Context context) {
        super(context);
    }

    @Override
    public Observable<Forecast> getForecast(@Path("latitude") double latitude, @Path("longitude") double longitude) {
        return mDelegate.returningResponse(Response.error(401, ResponseBody.create(MediaType.parse("application/json"), "")))
                .getForecast(latitude, longitude);
    }
}
