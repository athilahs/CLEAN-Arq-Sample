package com.athila.mvpweather.presentation.forecast;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.model.Forecast;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.athila.mvpweather.data.repository.forecast.ForecastRepository;
import com.athila.mvpweather.infrastructure.JSONMocks;
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
import rx.Subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by athila on 18/03/16.
 */
public class ForecastPresenterTest {

    private static final int CITY_REPO_SETUP_FILLED = 1;
    private static final int CITY_REPO_SETUP_ERROR = 2;
    private static final int CITY_REPO_SETUP_EMPTY = 3;

    @Mock
    private ForecastRepository mForecastRepository;

    @Mock
    private CityRepository mCityRepository;

    @Mock
    private ForecastContract.View mView;

    private ForecastPresenter mPresenter;

    private City mSuccessCity;
    private City mErrorCity;

    @Before
    public void setupForecastPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new ForecastPresenter(mView, mForecastRepository, mCityRepository);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getForecastSuccess() {
        setupForecastRepository();
        mPresenter.getForecast(mSuccessCity);

        verify(mView).showProgress();
        verify(mForecastRepository).getForecast(mSuccessCity.getLatitude(), mSuccessCity.getLongitude());
        verify(mView).setForecast(any(Forecast.class));
        verify(mView).hideProgress();
    }

    @Test
    public void getForecastError() {
        setupForecastRepository();
        mPresenter.getForecast(mErrorCity);

        verify(mView).showProgress();
        verify(mForecastRepository).getForecast(mErrorCity.getLatitude(), mErrorCity.getLongitude());
        verify(mView).handleGenericErrors(any(Exception.class));
        verify(mView).hideProgress();
    }

    @Test
    public void unsubscribeOnDetach() {
        Subscription mockForecastSubscription = Mockito.mock(Subscription.class);
        Subscription mockCitySubscription = Mockito.mock(Subscription.class);
        mPresenter.setForecastSubscription(mockForecastSubscription);
        mPresenter.setCitySubscription(mockCitySubscription);

        mPresenter.stop();
        verify(mockForecastSubscription).unsubscribe();
        verify(mockCitySubscription).unsubscribe();
    }

    @Test
    public void getCitiesSuccess() {
        setupCityRepository(CITY_REPO_SETUP_FILLED);
        mPresenter.getCities();

        verify(mCityRepository).getCities();
        verify(mView).onCitiesLoaded(anyListOf(City.class));
    }

    @Test
    public void getCitiesError() {
        setupCityRepository(CITY_REPO_SETUP_ERROR);
        mPresenter.getCities();

        verify(mCityRepository).getCities();
        verify(mView).handleGetCitiesError();
    }

    @Test
    public void getCitiesEmpty() {
        setupCityRepository(CITY_REPO_SETUP_EMPTY);
        mPresenter.getCities();

        verify(mCityRepository).getCities();
        // Empty cities is seen as error, since database is pre-populated with some cities
        verify(mView).showEmptyView();
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    private void setupForecastRepository() {
        mSuccessCity = new City("Success", 1, 1);
        mErrorCity = new City("Error", -1, -1);

        when(mForecastRepository.getForecast(mSuccessCity.getLatitude(), mSuccessCity.getLongitude()))
                // Attention: JSONMocks is available only on mock flavor. Do not try to execute unit tests on other flavors
                .thenReturn(Observable.just(new Gson().fromJson(JSONMocks.FORECAST_SUCCESS, Forecast.class)));

        // sending a generic error since HttpException from Retrofit is final and cannot be mocked
        Observable<Forecast> error = Observable.error(Mockito.mock(Exception.class));
        when(mForecastRepository.getForecast(mErrorCity.getLatitude(), mErrorCity.getLongitude())).thenReturn(error);
    }

    private void setupCityRepository(int setup) {
        switch (setup) {
            case CITY_REPO_SETUP_FILLED:
                List<City> mockedCities = new ArrayList<>();
                mockedCities.add(new City("MockCity 1", -123, 123));
                mockedCities.add(new City("MockCity 2", -124, 123));
                mockedCities.add(new City("MockCity 3", -123, 124));
                mockedCities.add(new City("MockCity 4", -125, 123));
                mockedCities.add(new City("MockCity 5", -123, 125));

                when(mCityRepository.getCities()).thenReturn(Observable.just(mockedCities));
                break;

            case CITY_REPO_SETUP_EMPTY:
                List<City> empty = new ArrayList<>();
                when(mCityRepository.getCities()).thenReturn(Observable.just(empty));
                break;

            case CITY_REPO_SETUP_ERROR:
            default:
                Observable<List<City>> error = Observable.error(Mockito.mock(Throwable.class));
                when(mCityRepository.getCities()).thenReturn(error);
        }

    }
}
