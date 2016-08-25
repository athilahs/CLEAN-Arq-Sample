package com.athila.cleansample.presentation.forecast;

import android.support.annotation.NonNull;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.model.Forecast;
import com.athila.cleansample.infrastructure.CleanSampleLog;
import com.athila.cleansample.interactor.rx.DefaultSubscriber;
import com.athila.cleansample.interactor.usecase.city.GetCities;
import com.athila.cleansample.interactor.usecase.forecast.GetForecast;
import com.athila.cleansample.presentation.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by athila on 14/03/16.
 */
public class ForecastPresenter extends BasePresenter implements ForecastContract.Presenter {

    private ForecastContract.View mView;
    private GetForecast mGetForecast;
    private GetCities mGetCities;

    @Inject
    ForecastPresenter(@NonNull ForecastContract.View forecastView, @NonNull GetForecast getForecast, @NonNull GetCities getCities) {
        mView = forecastView;
        mGetForecast = getForecast;
        mGetCities = getCities;
    }

    @Override
    @Inject
    public void attachView() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // do nothing
    }

    @Override
    public void stop() {
        mView = null;
        if (mGetForecast != null) {
            mGetForecast.unsubscribe();
            mGetForecast = null;
        }

        if (mGetCities != null) {
            mGetCities.unsubscribe();
            mGetCities = null;
        }
    }

    @Override
    public void getForecast(City city) {
        if (mView == null) {
            return;
        }

        if (mGetForecast == null) {
            handleBasicError(mView, new Exception("GetForecast usecase is null. Cannot execute requested operation."));
            return;
        }
        mGetForecast.unsubscribe();

        mView.showProgress();
        mGetForecast.setCity(city);
        mGetForecast.execute(new GetForecastSubscriber());
    }

    public void getCities() {
        if (mView == null) {
            return;
        }

        if (mGetCities == null) {
            handleBasicError(mView, new Exception("GetCities usecase is null. Cannot execute requested operation."));
            return;
        }

        mGetCities.unsubscribe();
        mGetCities.execute(new GetCitiesSubscriber());
    }

    private class GetForecastSubscriber extends DefaultSubscriber<Forecast> {
        @Override
        public void onCompleted() {
            if (mView != null) {
                mView.hideProgress();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (mView != null) {
                mView.hideProgress();

                if (e instanceof HttpException) {
                    // TODO: translate the Exception to a more view-friendly errorCode?
                    mView.handleForecastError((HttpException) e);
                } else {
                    handleBasicError(mView, e);
                }
            }
        }

        @Override
        public void onNext(Forecast forecast) {
            if (mView != null) {
                mView.setForecast(forecast);
            }
        }
    }

    private class GetCitiesSubscriber extends DefaultSubscriber<List<City>> {
        @Override
        public void onError(Throwable e) {
            CleanSampleLog.error("Error on ForecastPresenter: ", e);
            mView.handleGetCitiesError();
        }

        @Override
        public void onNext(List<City> cities) {
            CleanSampleLog.debug("onNext called in ForecastPresenter with " + cities.size() + " cities");
            if (cities.isEmpty()) {
                mView.showEmptyView();
            } else {
                mView.onCitiesLoaded(cities);
            }
        }
    }
}
