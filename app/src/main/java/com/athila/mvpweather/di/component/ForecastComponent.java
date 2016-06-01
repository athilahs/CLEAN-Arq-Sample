package com.athila.mvpweather.di.component;

import com.athila.mvpweather.di.FragmentScoped;
import com.athila.mvpweather.di.component.ApplicationComponent;
import com.athila.mvpweather.di.module.presentation.ForecastPresenterModule;
import com.athila.mvpweather.presentation.forecast.ForecastFragment;

import dagger.Component;

/**
 * Created by athila on 30/05/16.
 */

@FragmentScoped
@Component(dependencies = {ApplicationComponent.class}, modules = {ForecastPresenterModule.class})
public interface ForecastComponent /*extends ForecastRepositoryComponent, CityRepositoryComponent*/ {

    void inject(ForecastFragment forecastFragment);
}
