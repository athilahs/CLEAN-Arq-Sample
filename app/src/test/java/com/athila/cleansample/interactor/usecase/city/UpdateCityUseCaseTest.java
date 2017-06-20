package com.athila.cleansample.interactor.usecase.city;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.repository.city.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("unchecked")
public class UpdateCityUseCaseTest {

  private UpdateCity mUpdateCity;

  @Mock
  private CityRepository mCityRepository;

  @Before
  public void setupUpdateCitiesUseCase() {
    MockitoAnnotations.initMocks(this);
    mUpdateCity = new UpdateCity(mCityRepository);
  }

  @Test
  public void testBuildUseCaseObservableCallRepository() {
    City testCity = new City("Mocked", -1234, 1234);

    mUpdateCity.buildUseCaseObservable(UpdateCity.UpdateCityParams.forCity(testCity));

    verify(mCityRepository).updateCity(testCity);
    verifyNoMoreInteractions(mCityRepository);
  }

  // TODO: if there is any business rules, test them here (they will be executed on excute(...) method)
}
