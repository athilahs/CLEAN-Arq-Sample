package com.athila.cleansample.data.repository.forecast;

import com.athila.cleansample.data.datasource.api.ForecastApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by athila on 07/06/16.
 */

public class ForecastRepositoryTest {

    @Mock
    private ForecastApi mForecastApi;

    private ForecastRepository mForecastRepository;

    @Before
    public void setupCityRepository() {
        MockitoAnnotations.initMocks(this);

        mForecastRepository = new ForecastRepositoryImpl(mForecastApi);
    }

    @Test
    public void testGetForecast() {
        double testLatitude = -12345.67;
        double testLongitude = 12345.67;
        mForecastRepository.getForecast(testLatitude, testLongitude);

        verify(mForecastApi).getForecast(testLatitude, testLongitude);

        // TODO: if repository integrates more than one datasource, test this behavior here
    }
}
