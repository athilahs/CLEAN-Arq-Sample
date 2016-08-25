package com.athila.cleansample.presentation.forecast;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.model.Forecast;
import com.athila.cleansample.presentation.BasePresenterContract;
import com.athila.cleansample.presentation.BaseViewContract;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by athila on 05/03/16.
 */
public interface ForecastContract {
    interface View extends BaseViewContract<ForecastContract.Presenter>, OnProgressRequestListener, OnCitiesLoadedListener {
        void handleForecastError(HttpException error);
        void handleGetCitiesError();
        void setForecast(Forecast forecast);
        void showEmptyView();
    }

    interface OnCitiesLoadedListener {
        void onCitiesLoaded(List<City> cities);
    }

    interface OnCitySelectedListener {
        void onCitySelected(City selectedCity);
    }

    interface OnProgressRequestListener {
        void showProgress();
        void hideProgress();
    }

    interface Presenter extends BasePresenterContract {
        void getForecast(City city);
        void getCities();
    }
}
