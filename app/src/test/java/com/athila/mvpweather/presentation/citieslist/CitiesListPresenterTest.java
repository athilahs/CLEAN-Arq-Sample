package com.athila.mvpweather.presentation.citieslist;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by athila on 18/03/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PutResults.class, DeleteResults.class, PutResult.class})
public class CitiesListPresenterTest {

    private static final int CITY_REPO_SETUP_SUCCESS = 1;
    private static final int CITY_REPO_SETUP_ERROR = 2;
    private static final int CITY_REPO_SETUP_EMPTY = 3;

    @Mock
    private CityRepository mCityRepository;

    @Mock
    private CitiesListContract.View mView;

    private CitiesListPresenter mPresenter;

    @Before
    public void setupCitiesListPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new CitiesListPresenter(mView, mCityRepository);
    }

    @Test
    public void unsubscribeOnDetach() {
        Subscription mockCityGeneralSubscription = Mockito.mock(Subscription.class);
        Subscription mockCityGetListSubscription = Mockito.mock(Subscription.class);
        mPresenter.setGeneralSubscription(mockCityGeneralSubscription);
        mPresenter.setGetListSubscription(mockCityGetListSubscription);

        mPresenter.stop();
        verify(mockCityGetListSubscription).unsubscribe();
        verify(mockCityGetListSubscription).unsubscribe();
    }

    @Test
    public void getCitiesSuccess() {
        setupCityRepository(CITY_REPO_SETUP_SUCCESS);
        mPresenter.getCities();

        verify(mCityRepository).getCities();
        verify(mView).showCitiesList(anyListOf(City.class));
    }

    @Test
    public void getCitiesError() {
        setupCityRepository(CITY_REPO_SETUP_ERROR);
        mPresenter.getCities();

        verify(mCityRepository).getCities();
        verify(mView).handleGenericErrors(any(Throwable.class));
    }

    @Test
    public void getCitiesEmpty() {
        setupCityRepository(CITY_REPO_SETUP_EMPTY);
        mPresenter.getCities();

        verify(mCityRepository).getCities();
        verify(mView).showEmptyView();
    }

    @Test
    public void addCitySuccess() {
        setupCityRepository(CITY_REPO_SETUP_SUCCESS);
        City mockCity = Mockito.mock(City.class);
        mPresenter.addCity(mockCity);
        verify(mCityRepository).insertCities(anyListOf(City.class));
        verify(mView).onCityAdded(mockCity);
    }

    @Test
    public void addCityError() {
        setupCityRepository(CITY_REPO_SETUP_ERROR);
        mPresenter.addCity(Mockito.mock(City.class));
        verify(mCityRepository).insertCities(anyListOf(City.class));
        verify(mView).handleGenericErrors(any(Throwable.class));
    }

    @Test
    public void updateCitySuccess() {
        setupCityRepository(CITY_REPO_SETUP_SUCCESS);
        City mockCity = Mockito.mock(City.class);
        mPresenter.updateCity(mockCity);
        verify(mCityRepository).updateCity(mockCity);
        verify(mView).onCityEditionFinished(mockCity);
    }

    @Test
    public void updateCityError() {
        setupCityRepository(CITY_REPO_SETUP_ERROR);
        City mockCity = Mockito.mock(City.class);
        mPresenter.updateCity(mockCity);
        verify(mCityRepository).updateCity(mockCity);
        verify(mView).handleGenericErrors(any(Throwable.class));
    }

    @Test
    public void deleteCitySuccess() {
        setupCityRepository(CITY_REPO_SETUP_SUCCESS);
        mPresenter.deleteCities(anyListOf(City.class));
        verify(mCityRepository).deleteCities(anyListOf(City.class));
        verify(mView).onCitiesDeleted(anyInt());
    }

    @Test
    public void deleteCityError() {
        setupCityRepository(CITY_REPO_SETUP_ERROR);
        mPresenter.deleteCities(anyListOf(City.class));
        verify(mCityRepository).deleteCities(anyListOf(City.class));
        verify(mView).handleGenericErrors(any(Throwable.class));
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    private void setupCityRepository(int setup) {
        PutResults<City> insertResultMock = PowerMockito.mock(PutResults.class);
        DeleteResults<City> deleteResultMock = PowerMockito.mock(DeleteResults.class);
        switch (setup) {
            case CITY_REPO_SETUP_SUCCESS:
                List<City> mockedCities = new ArrayList<>();
                mockedCities.add(new City("MockCity 1", -123, 123));
                mockedCities.add(new City("MockCity 2", -124, 123));
                mockedCities.add(new City("MockCity 3", -123, 124));
                mockedCities.add(new City("MockCity 4", -125, 123));
                mockedCities.add(new City("MockCity 5", -123, 125));

                when(mCityRepository.getCities()).thenReturn(Observable.just(mockedCities));
                when(mCityRepository.insertCities(anyListOf(City.class))).thenReturn(Observable.just(insertResultMock));
                when(mCityRepository.deleteCities(anyListOf(City.class))).thenReturn(Observable.just(deleteResultMock));

                PutResult updateResultMock = PowerMockito.mock(PutResult.class);
                when(updateResultMock.wasUpdated()).thenReturn(true);
                when(mCityRepository.updateCity(any(City.class))).thenReturn(Observable.just(updateResultMock));
                break;

            case CITY_REPO_SETUP_EMPTY:
                List<City> empty = new ArrayList<>();
                when(mCityRepository.getCities()).thenReturn(Observable.just(empty));
                // insert, update and delete for empty repo will behaves as success
                when(mCityRepository.insertCities(anyListOf(City.class))).thenReturn(Observable.just(insertResultMock));
                when(mCityRepository.updateCity(any(City.class))).thenReturn(Observable.just(Mockito.mock(PutResult.class)));
                when(mCityRepository.deleteCities(anyListOf(City.class))).thenReturn(Observable.just(deleteResultMock));
                break;

            case CITY_REPO_SETUP_ERROR:
            default:
                Observable<List<City>> getCitiesError = Observable.error(Mockito.mock(Throwable.class));
                Observable<PutResults<City>> insertCitiesError = Observable.error(Mockito.mock(Throwable.class));
                Observable<PutResult> updateCitiesError = Observable.error(Mockito.mock(Throwable.class));
                Observable<DeleteResults<City>> deleteCitiesError = Observable.error(Mockito.mock(Throwable.class));
                when(mCityRepository.getCities()).thenReturn(getCitiesError);
                when(mCityRepository.insertCities(anyListOf(City.class))).thenReturn(insertCitiesError);
                when(mCityRepository.updateCity(any(City.class))).thenReturn(updateCitiesError);
                when(mCityRepository.deleteCities(anyListOf(City.class))).thenReturn(deleteCitiesError);
        }

    }
}
