package com.athila.mvpweather.di.component;

import com.athila.mvpweather.data.repository.city.CityRepository;
import com.athila.mvpweather.data.repository.forecast.ForecastRepository;
import com.athila.mvpweather.di.module.ApplicationModule;
import com.athila.mvpweather.di.module.DatabaseModule;
import com.athila.mvpweather.di.module.NetworkModule;
import com.athila.mvpweather.di.module.repository.CityRepositoryModule;
import com.athila.mvpweather.di.module.repository.ForecastRepositoryModule;
import com.athila.mvpweather.infrastructure.MvpWeatherApp;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by athila on 30/05/16.
 */

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        DatabaseModule.class,
        CityRepositoryModule.class,
        ForecastRepositoryModule.class
})
public interface ApplicationComponent {
    // expose to sub graphs

    MvpWeatherApp application();

    // Repositories
    CityRepository cityRepository();
    ForecastRepository forecastRepository();
}
