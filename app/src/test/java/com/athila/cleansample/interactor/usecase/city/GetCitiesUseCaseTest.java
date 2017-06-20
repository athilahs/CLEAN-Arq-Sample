package com.athila.cleansample.interactor.usecase.city;

import com.athila.cleansample.data.repository.city.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("unchecked")
public class GetCitiesUseCaseTest {

  private GetCities mGetCities;

  @Mock
  private CityRepository mCityRepository;

  @Before
  public void setupGetCitiesUseCase() {
    MockitoAnnotations.initMocks(this);
    mGetCities = new GetCities(mCityRepository);
  }

  @Test
  public void testBuildUseCaseObservableCallRepository() {
    mGetCities.buildUseCaseObservable(null);

    verify(mCityRepository).getCities();
    verifyNoMoreInteractions(mCityRepository);
  }

  // TODO: if there is any business rules, test them here (they will be executed on excute(...) method)
}
