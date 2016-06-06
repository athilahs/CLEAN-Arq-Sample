package com.athila.cleansample.presentation.forecast;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.model.Forecast;
import com.athila.cleansample.presentation.BasePresenter;
import com.athila.cleansample.presentation.BaseView;

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
