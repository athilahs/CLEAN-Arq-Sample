package com.athila.mvpweather.presentation.forecast;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.model.Forecast;
import com.athila.mvpweather.presentation.BasePresenter;
import com.athila.mvpweather.presentation.BaseView;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by athila on 05/03/16.
 */
public interface ForecastContract {
    interface View extends BaseView<ForecastContract.Presenter>, OnProgressRequestListener, OnCitiesLoadedListener {
        void handleForecastError(HttpException error);
        void handleGetCitiesError();
        void setForecast(Forecast forecast);
        void showEmptyView();
        void handleGenericErrors(Throwable error);
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

    interface Presenter extends BasePresenter {
        void getForecast(City city);
        void getCities();
    }
}
