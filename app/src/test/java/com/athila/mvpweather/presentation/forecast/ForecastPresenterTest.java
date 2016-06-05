package com.athila.mvpweather.presentation.forecast;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.model.Forecast;
import com.athila.mvpweather.infrastructure.JSONMocks;
import com.athila.mvpweather.interactor.usecase.city.GetCities;
import com.athila.mvpweather.interactor.usecase.forecast.GetForecast;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;

/**
 * Created by athila on 18/03/16.
 */
@SuppressWarnings("unchecked")
public class ForecastPresenterTest {

    private static final int SETUP_SUCCESS = 1;
    private static final int SETUP_ERROR = 2;
    private static final int SETUP_EMPTY = 3;


    @Mock
    private ForecastContract.View mView;

    private ForecastPresenter mPresenter;

    @Mock
    private GetCities mGetCities;

    @Mock
    private GetForecast mGetForecast;

    @Before
    public void setupForecastPresenter() {
        MockitoAnnotations.initMocks(this);

        // Since the 'execute' methods from UseCases is responsible for subscribing and execute the operation itself,
        // we cannot let them mocked. So, tell mockito to use the real versions of these methods when they are called
        doCallRealMethod().when(mGetCities).execute(any(Subscriber.class));
        doCallRealMethod().when(mGetCities).execute(any(Subscriber.class), any(Observable.Transformer.class));

        doCallRealMethod().when(mGetForecast).execute(any(Subscriber.class));
        doCallRealMethod().when(mGetForecast).execute(any(Subscriber.class), any(Observable.Transformer.class));

        mPresenter = new ForecastPresenter(mView, mGetForecast, mGetCities);
    }

    @Test
    public void unsubscribeOnDetach() {
        mPresenter.stop();
        verify(mGetCities).unsubscribe();
        verify(mGetForecast).unsubscribe();
    }

    @Test
    public void getForecastSuccess() {
        setupGetForecast(SETUP_SUCCESS);
        City testCity = new City("Success", 1, 1);
        mPresenter.getForecast(testCity);

        verify(mView).showProgress();
        verify(mGetForecast).setCity(testCity);
        verify(mGetForecast).execute(any(Subscriber.class));
        verify(mView).setForecast(any(Forecast.class));
        verify(mView).hideProgress();
    }

    @Test
    public void getForecastError() {
        setupGetForecast(SETUP_ERROR);
        City testCity = new City("Error", -1, -1);
        mPresenter.getForecast(testCity);

        verify(mView).showProgress();
        verify(mGetForecast).setCity(testCity);
        verify(mGetForecast).execute(any(Subscriber.class));
        verify(mView).handleGenericErrors(any(Exception.class));
        verify(mView).hideProgress();
    }

    @Test
    public void getCitiesSuccess() {
        setupGetCities(SETUP_SUCCESS);
        mPresenter.getCities();

        verify(mGetCities).execute(any(Subscriber.class));
        verify(mView).onCitiesLoaded(anyListOf(City.class));
    }

    @Test
    public void getCitiesError() {
        setupGetCities(SETUP_ERROR);
        mPresenter.getCities();

        verify(mGetCities).execute(any(Subscriber.class));
        verify(mView).handleGetCitiesError();
    }

    @Test
    public void getCitiesEmpty() {
        setupGetCities(SETUP_EMPTY);
        mPresenter.getCities();

        verify(mGetCities).execute(any(Subscriber.class));
        verify(mView).showEmptyView();
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    private void setupGetForecast(int setup) {
        switch(setup) {
            case SETUP_SUCCESS:
                doReturn(Observable.just(new Gson().fromJson(JSONMocks.FORECAST_SUCCESS, Forecast.class)))
                        .when(mGetForecast)
                        .buildUseCaseObservable();
                break;
            case SETUP_ERROR:
                Observable<Forecast> error = Observable.error(Mockito.mock(Exception.class));
                doReturn(error).when(mGetForecast).buildUseCaseObservable();
                break;
        }
    }

    private void setupGetCities(int setup) {
        switch (setup) {
            case SETUP_SUCCESS:
                List<City> mockedCities = new ArrayList<>();
                mockedCities.add(new City("MockCity 1", -123, 123));
                mockedCities.add(new City("MockCity 2", -124, 123));
                mockedCities.add(new City("MockCity 3", -123, 124));
                mockedCities.add(new City("MockCity 4", -125, 123));
                mockedCities.add(new City("MockCity 5", -123, 125));

                // Use this syntax to avoid the real buildUseCaseObservable being called once resulting in NPE
                // (since it will call the real implementation, which could try to access a null member variable)
                doReturn(Observable.just(mockedCities)).when(mGetCities).buildUseCaseObservable();
                break;

            case SETUP_EMPTY:
                List<City> empty = new ArrayList<>();
                // Use this syntax to avoid the real buildUseCaseObservable being called once resulting in NPE
                // (since it will call the real implementation, which could try to access a null member variable)
                doReturn(Observable.just(empty)).when(mGetCities).buildUseCaseObservable();
                break;

            case SETUP_ERROR:
                Observable<List<City>> getCitiesError = Observable.error(Mockito.mock(Throwable.class));
                // Use this syntax to avoid the real buildUseCaseObservable being called once resulting in NPE
                // (since it will call the real implementation, which could try to access a null member variable)
                doReturn(getCitiesError).when(mGetCities).buildUseCaseObservable();
                break;
        }
    }
}
