package com.athila.cleansample.data.datasources.api.forecast;

import android.content.Context;

import com.athila.cleansample.data.datasource.api.ForecastApi;
import com.athila.cleansample.data.datasources.api.MockAPI;
import com.athila.cleansample.data.model.Forecast;
import com.athila.cleansample.di.api.component.DaggerMockRetrofitComponent;
import com.athila.cleansample.di.api.module.MockRetrofitModule;
import com.athila.cleansample.di.module.NetworkModule;

import retrofit2.http.Path;
import retrofit2.mock.MockRetrofit;
import rx.Observable;

/**
 * Created by athila on 15/06/16.
 */

public abstract class MockForecastAPI extends MockAPI<ForecastApi> implements ForecastApi {

    // Need context so we can get json mocks from assets folder
    protected Context mContext;

    protected MockForecastAPI(Context context) {
        mContext = context;

        MockRetrofit mockRetrofit = DaggerMockRetrofitComponent.builder()
                .networkModule(new NetworkModule())
                .mockRetrofitModule(new MockRetrofitModule())
                .build()
                .mockRetrofit();

        mDelegate = mockRetrofit.create(ForecastApi.class);
    }

    // By default, methods will throw exception. Subclasses should override the desired behavior
    @Override
    public Observable<Forecast> getForecast(@Path("latitude") double latitude, @Path("longitude") double longitude) {
        throw new RuntimeException("not implemented");
    }
}
