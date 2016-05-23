package com.athila.mvpweather.presentation.forecast;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.model.Forecast;
import com.athila.mvpweather.presentation.GenericPresenter;
import com.athila.mvpweather.presentation.GenericView;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by athila on 05/03/16.
 */
public interface ForecastContract {
    interface View extends GenericView, OnProgressRequestListener, OnCitiesLoadedListener {
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

    interface Presenter extends GenericPresenter<ForecastContract.View> {
        void getForecast(City city);
        void getCities();
    }
}
