package com.athila.cleansample.interactor.usecase.forecast;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.repository.forecast.ForecastRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("unchecked")
public class GetForecastUseCaseTest {

  private GetForecast mGetForecast;

  @Mock
  private ForecastRepository mForecastRepository;

  @Before
  public void setupGetForecastUseCase() {
    MockitoAnnotations.initMocks(this);
    mGetForecast = new GetForecast(mForecastRepository);
  }

  @Test
  public void testBuildUseCaseObservableCallRepository() {
    double testLatitude = -23456.567;
    double testLongitude = 876543.56;
    mGetForecast.buildUseCaseObservable(GetForecast.GetForecastParams.forCity(new City("MockCity", testLatitude, testLongitude)));

    verify(mForecastRepository).getForecast(testLatitude, testLongitude);
    verifyNoMoreInteractions(mForecastRepository);
  }

  // TODO: if there is any business rules, test them here (they will be executed on excute(...) method)
}
