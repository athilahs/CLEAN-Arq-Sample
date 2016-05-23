package com.athila.mvpweather.presentation.forecast;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.model.Forecast;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.athila.mvpweather.data.repository.forecast.ForecastRepository;
import com.athila.mvpweather.infrastructure.MvpWeatherLog;
import com.athila.mvpweather.infrastructure.rx.DefaultSubscriber;
import com.athila.mvpweather.infrastructure.rx.WWSchedulers;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;

/**
 * Created by athila on 14/03/16.
 */
public class ForecastPresenter implements ForecastContract.Presenter {

    private Subscription mForecastSubscription;
    private Subscription mCitySubscription;
    private ForecastContract.View mView;
    private ForecastRepository mForecastRepository;
    private CityRepository mCityRepository;

    public ForecastPresenter(@NonNull ForecastRepository forecastRepository, @NonNull CityRepository cityRepository) {
        mForecastRepository = forecastRepository;
        mCityRepository = cityRepository;
    }

    @Override
    public void attachView(ForecastContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mForecastSubscription != null) {
            mForecastSubscription.unsubscribe();
        }

        if (mCitySubscription != null) {
            mCitySubscription.unsubscribe();
        }
    }

    @Override
    public void getForecast(City city) {
        if (mView == null) {
            return;
        }

        mView.showProgress();

        if (mForecastSubscription != null && !mForecastSubscription.isUnsubscribed()) {
            mForecastSubscription.unsubscribe();
        }

        mForecastSubscription = mForecastRepository.getForecast(city.getLatitude(), city.getLongitude())
                .observeOn(WWSchedulers.getMainScheduler())
                .subscribeOn(WWSchedulers.getIoScheduler())
                .subscribe(new DefaultSubscriber<Forecast>() {
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
                                mView.handleGenericErrors(e);
                            }
                        }
                    }

                    @Override
                    public void onNext(Forecast forecast) {
                        if (mView != null) {
                            mView.setForecast(forecast);
                        }
                    }
                });
    }

    public void  getCities() {
        if (mView == null) {
            return;
        }

        // Need loading? I dont think so...

        if (mCitySubscription != null && mCitySubscription.isUnsubscribed()) {
            mCitySubscription.unsubscribe();
        }

        mCitySubscription = mCityRepository.getCities()
                .observeOn(WWSchedulers.getMainScheduler())
                .subscribeOn(WWSchedulers.getIoScheduler())
                .subscribe(new DefaultSubscriber<List<City>>() {

                    @Override
                    public void onError(Throwable e) {
                        MvpWeatherLog.error("Error on ForecastPresenter: ", e);
                        mView.handleGetCitiesError();
                    }

                    @Override
                    public void onNext(List<City> cities) {
                        MvpWeatherLog.debug("onNext called in ForecastPresenter with "+cities.size()+" cities");
                        if (cities.isEmpty()) {
                            mView.showEmptyView();
                        } else {
                            mView.onCitiesLoaded(cities);
                        }
                    }
                });
    }

    @VisibleForTesting
    public void setForecastSubscription(Subscription mockSubscription) {
        mForecastSubscription = mockSubscription;
    }

    @VisibleForTesting
    public void setCitySubscription(Subscription mockSubscription) {
        mCitySubscription = mockSubscription;
    }
}
