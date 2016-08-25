package com.athila.cleansample.presentation.citieslist;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.interactor.usecase.city.AddCities;
import com.athila.cleansample.interactor.usecase.city.DeleteCities;
import com.athila.cleansample.interactor.usecase.city.GetCities;
import com.athila.cleansample.interactor.usecase.city.UpdateCity;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by athila on 18/03/16.
 */
@SuppressWarnings("unchecked")
@RunWith(PowerMockRunner.class)
@PrepareForTest({PutResults.class, DeleteResults.class, PutResult.class, GetCities.class})
public class CitiesListPresenterTest {

    private static final int SETUP_SUCCESS = 1;
    private static final int SETUP_ERROR = 2;
    private static final int SETUP_EMPTY = 3;

    @Mock
    private GetCities mGetCities;
    @Mock
    private AddCities mAddCities;
    @Mock
    private DeleteCities mDeleteCities;
    @Mock
    private UpdateCity mUpdateCity;
    @Mock
    private CitiesListContract.View mView;

    private CitiesListPresenter mPresenter;

    @Before
    public void setupCitiesListPresenter() {
        MockitoAnnotations.initMocks(this);

        // Since the 'execute' methods from UseCases is responsible for subscribing and execute the operation itself,
        // we cannot let them mocked. So, tell mockito to use the real versions of these methods when they are called
        doCallRealMethod().when(mGetCities).execute(any(Subscriber.class));
        doCallRealMethod().when(mGetCities).execute(any(Subscriber.class), any(Observable.Transformer.class));

        doCallRealMethod().when(mAddCities).execute(any(Subscriber.class));
        doCallRealMethod().when(mAddCities).execute(any(Subscriber.class), any(Observable.Transformer.class));

        doCallRealMethod().when(mUpdateCity).execute(any(Subscriber.class));
        doCallRealMethod().when(mUpdateCity).execute(any(Subscriber.class), any(Observable.Transformer.class));

        doCallRealMethod().when(mDeleteCities).execute(any(Subscriber.class));
        doCallRealMethod().when(mDeleteCities).execute(any(Subscriber.class), any(Observable.Transformer.class));

        // Necessary spy in order to test superclass method invocation (like handleBasicError)
        mPresenter = Mockito.spy(new CitiesListPresenter(mView, mGetCities, mUpdateCity, mDeleteCities, mAddCities));
    }

    @Test
    public void unsubscribeOnDetach() {
        mPresenter.stop();
        verify(mGetCities).unsubscribe();
        verify(mUpdateCity).unsubscribe();
        verify(mDeleteCities).unsubscribe();
        verify(mAddCities).unsubscribe();
    }

    @Test
    public void getCitiesSuccess() {
        setupGetCities(SETUP_SUCCESS);
        mPresenter.getCities();

        verify(mGetCities).execute(any(Subscriber.class));
        verify(mView).showCitiesList(anyListOf(City.class));
    }

    @Test
    public void getCitiesError() {
        setupGetCities(SETUP_ERROR);
        mPresenter.getCities();

        verify(mGetCities).execute(any(Subscriber.class));
        verify(mPresenter).handleBasicError(any(CitiesListContract.View.class), any(Exception.class));
    }

    @Test
    public void getCitiesEmpty() {
        setupGetCities(SETUP_EMPTY);
        mPresenter.getCities();

        verify(mGetCities).execute(any(Subscriber.class));
        verify(mView).showEmptyView();
    }

    @Test
    public void addCitySuccess() {
        setupAddCities(SETUP_SUCCESS);
        City mockCity = Mockito.mock(City.class);
        mPresenter.addCity(mockCity);
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(mAddCities).setCitiesToBeAdded(captor.capture());
        assertTrue(captor.getValue().contains(mockCity));

        verify(mAddCities).execute(any(Subscriber.class));
        verify(mView).onCityAdded(mockCity);
    }

    @Test
    public void addCityError() {
        setupAddCities(SETUP_ERROR);
        mPresenter.addCity(Mockito.mock(City.class));
        verify(mAddCities).setCitiesToBeAdded(anyListOf(City.class));
        verify(mAddCities).execute(any(Subscriber.class));
        verify(mPresenter).handleBasicError(any(CitiesListContract.View.class), any(Exception.class));
    }

    @Test
    public void updateCitySuccess() {
        setupUpdateCity(SETUP_SUCCESS);
        City mockCity = Mockito.mock(City.class);
        mPresenter.updateCity(mockCity);
        verify(mUpdateCity).setUpdatedCity(mockCity);
        verify(mUpdateCity).execute(any(Subscriber.class));
        verify(mView).onCityEditionFinished(mockCity);
    }

    @Test
    public void updateCityError() {
        setupUpdateCity(SETUP_ERROR);
        City mockCity = Mockito.mock(City.class);
        mPresenter.updateCity(mockCity);
        verify(mUpdateCity).setUpdatedCity(mockCity);
        verify(mUpdateCity).execute(any(Subscriber.class));
        verify(mPresenter).handleBasicError(any(CitiesListContract.View.class), any(Exception.class));
    }

    @Test
    public void deleteCitySuccess() {
        setupDeleteCities(SETUP_SUCCESS);
        mPresenter.deleteCities(mock(List.class));
        verify(mDeleteCities).setCitiesToBeDeleted(anyListOf(City.class));
        verify(mDeleteCities).execute(any(Subscriber.class));
        verify(mView).onCitiesDeleted(anyInt());
    }

    @Test
    public void deleteCityError() {
        setupDeleteCities(SETUP_ERROR);
        mPresenter.deleteCities(mock(List.class));
        verify(mDeleteCities).setCitiesToBeDeleted(anyListOf(City.class));
        verify(mDeleteCities).execute(any(Subscriber.class));
        verify(mPresenter).handleBasicError(any(CitiesListContract.View.class), any(Exception.class));
    }

    @After
    public void validate() {
        validateMockitoUsage();
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

    private void setupAddCities(int setup) {
        switch (setup) {
            case SETUP_SUCCESS:
                PutResults<City> insertResultMock = PowerMockito.mock(PutResults.class);
                when(mAddCities.buildUseCaseObservable()).thenReturn(Observable.just(insertResultMock));
                break;

            case SETUP_ERROR:
                Observable<PutResults<City>> insertCitiesError = Observable.error(Mockito.mock(Throwable.class));
                when(mAddCities.buildUseCaseObservable()).thenReturn(insertCitiesError);
                break;
        }
    }

    private void setupDeleteCities(int setup) {
        switch (setup) {
            case SETUP_SUCCESS:
                DeleteResults<City> deleteResultMock = PowerMockito.mock(DeleteResults.class);
                when(mDeleteCities.buildUseCaseObservable()).thenReturn(Observable.just(deleteResultMock));
                break;
            case SETUP_ERROR:
                Observable<DeleteResults<City>> deleteCitiesError = Observable.error(Mockito.mock(Throwable.class));
                when(mDeleteCities.buildUseCaseObservable()).thenReturn(deleteCitiesError);
                break;
        }
    }

    private void setupUpdateCity(int setup) {
        switch (setup) {
            case SETUP_SUCCESS:
                PutResult updateResultMock = PowerMockito.mock(PutResult.class);
                when(updateResultMock.wasUpdated()).thenReturn(true);
                when(mUpdateCity.buildUseCaseObservable()).thenReturn(Observable.just(updateResultMock));
                break;
            case SETUP_ERROR:
                Observable<PutResult> updateCitiesError = Observable.error(Mockito.mock(Throwable.class));
                when(mUpdateCity.buildUseCaseObservable()).thenReturn(updateCitiesError);
                break;
        }
    }
}
