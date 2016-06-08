package com.athila.cleansample.interactor.usecase.city;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.repository.city.CityRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("unchecked")
public class AddCitiesUseCaseTest {

    private AddCities mAddCities;

    @Mock
    private CityRepository mCityRepository;

    @Before
    public void setupGetCitiesUseCase() {
        MockitoAnnotations.initMocks(this);
        mAddCities = new AddCities(mCityRepository);
    }

    @Test
    public void testBuildUseCaseObservableCallRepository() {
        List<City> testCities = new ArrayList<>();
        testCities.add(mock(City.class));
        testCities.add(mock(City.class));
        testCities.add(mock(City.class));
        testCities.add(mock(City.class));

        mAddCities.setCitiesToBeAdded(testCities);
        mAddCities.buildUseCaseObservable();

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(mCityRepository).insertCities(captor.capture());
        assertEquals(captor.getValue().size(), testCities.size());
        verifyNoMoreInteractions(mCityRepository);
    }

    // TODO: if there is any business rules, test them here (they will be executed on excute(...) method)
}
