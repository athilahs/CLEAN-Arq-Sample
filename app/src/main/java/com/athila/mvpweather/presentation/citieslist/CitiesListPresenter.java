package com.athila.mvpweather.presentation.citieslist;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.athila.mvpweather.infrastructure.MvpWeatherLog;
import com.athila.mvpweather.infrastructure.rx.DefaultSubscriber;
import com.athila.mvpweather.infrastructure.rx.WWSchedulers;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by athila on 14/03/16.
 */
public class CitiesListPresenter implements CitiesListContract.Presenter {

    // Use the same subscription for all operations, since they are not supposed to be executed at same time
    // (only one operation - delete, update or query should be ongoing at a time)
    private Subscription mGeneralSubscription;

    // Keep a separate subscription to getList operation, so it can receive changes notification
    private Subscription mGetListSubscription;

    private CitiesListContract.View mView;
    private CityRepository mCityRepository;

    public CitiesListPresenter(@NonNull CityRepository cityRepository) {
        mCityRepository = cityRepository;
    }

    @Override
    public void attachView(CitiesListContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        MvpWeatherLog.debug("CitiesListPresenter::detachView");
        mView = null;
        if (mGeneralSubscription != null) {
            mGeneralSubscription.unsubscribe();
        }

        if (mGetListSubscription != null) {
            mGetListSubscription.unsubscribe();
        }
    }

    @Override
    public void getCities() {
        if (mView == null) {
            return;
        }

        // Need loading? I dont think so...

        if (mGetListSubscription != null) {
            mGetListSubscription.unsubscribe();
        }

        mGetListSubscription = mCityRepository.getCities()
                .observeOn(WWSchedulers.getMainScheduler())
                .subscribeOn(WWSchedulers.getIoScheduler())
                .subscribe(new DefaultSubscriber<List<City>>() {

                    @Override
                    public void onError(Throwable e) {
                        MvpWeatherLog.error("Error on CitiesListPresenter::getCities: ", e);
                        if (mView != null) {
                            mView.handleGenericErrors(e);
                        }
                    }

                    @Override
                    public void onNext(List<City> cities) {
                        MvpWeatherLog.debug("onNext called in CitiesListPresenter with "+cities.size()+" cities");
                        if (mView != null) {
                            if (cities.isEmpty()) {
                                mView.showEmptyView();
                            } else {
                                mView.showCitiesList(cities);
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }
                });
    }

    @Override
    public void updateCity(final City updatedCity) {
        if (mView == null) {
            return;
        }

        // Need loading? I dont think so...

        if (mGeneralSubscription != null) {
            mGeneralSubscription.unsubscribe();
        }

        mGeneralSubscription = mCityRepository.updateCity(updatedCity)
                .observeOn(WWSchedulers.getMainScheduler())
                .subscribeOn(WWSchedulers.getIoScheduler())
                .subscribe(new DefaultSubscriber<PutResult>() {

                    @Override
                    public void onError(Throwable e) {
                        MvpWeatherLog.error("Error on CitiesListPresenter::updateCity: ", e);
                        if (mView != null) {
                            mView.handleGenericErrors(e);
                        }
                    }

                    @Override
                    public void onNext(PutResult result) {
                        MvpWeatherLog.debug("onNext called on CitiesListPresenter::updateCity. city: "+updatedCity.getName());
                        if (mView != null && result.wasUpdated()) {
                            mView.onCityEditionFinished(updatedCity);
                        }
                    }
                });
    }

    @Override
    public void deleteCities(List<City> citiesToDelete) {
        if (mView == null) {
            return;
        }

        // Need loading? I dont think so...

        if (mGeneralSubscription != null) {
            mGeneralSubscription.unsubscribe();
        }

        mGeneralSubscription = mCityRepository.deleteCities(citiesToDelete)
                .observeOn(WWSchedulers.getMainScheduler())
                .subscribeOn(WWSchedulers.getIoScheduler())
                .subscribe(new DefaultSubscriber<DeleteResults<City>>() {

                    @Override
                    public void onError(Throwable e) {
                        MvpWeatherLog.error("Error on CitiesListPresenter::deleteCities: ", e);
                        if (mView != null) {
                            mView.handleGenericErrors(e);
                        }
                    }

                    @Override
                    public void onNext(DeleteResults<City> result) {
                        MvpWeatherLog.debug("onNext called on CitiesListPresenter::deleteCities.");
                        if (mView != null) {
                            mView.onCitiesDeleted(result.results().size());
                        }
                    }
                });
    }

    @Override
    public void addCity(final City cityToAdd) {
        if (mView == null) {
            return;
        }

        // Need loading? I dont think so...

        if (mGeneralSubscription != null && mGeneralSubscription.isUnsubscribed()) {
            mGeneralSubscription.unsubscribe();
        }

        List<City> city = new ArrayList<>();
        city.add(cityToAdd);
        mGeneralSubscription = mCityRepository.insertCities(city)
                .observeOn(WWSchedulers.getMainScheduler())
                .subscribeOn(WWSchedulers.getIoScheduler())
                .subscribe(new DefaultSubscriber<PutResults<City>>() {

                    @Override
                    public void onError(Throwable e) {
                        MvpWeatherLog.error("Error on CitiesListPresenter::addCity: ", e);
                        if (mView != null) {
                            mView.handleGenericErrors(e);
                        }
                    }

                    @Override
                    public void onNext(PutResults<City> result) {
                        MvpWeatherLog.debug("onNext called on CitiesListPresenter::addCity. city: "+cityToAdd.getName());
                        if (mView != null) {
                            mView.onCityAdded(cityToAdd);
                        }
                    }
                });
    }

    @VisibleForTesting
    public void setGeneralSubscription(Subscription mockSubscription) {
        mGeneralSubscription = mockSubscription;
    }

    @VisibleForTesting
    public void setGetListSubscription(Subscription mockSubscription) {
        mGetListSubscription = mockSubscription;
    }
}
