package com.athila.mvpweather.di.component;

import com.athila.mvpweather.di.ActivityScoped;
import com.athila.mvpweather.di.module.presentation.ForecastPresenterModule;
import com.athila.mvpweather.presentation.forecast.ForecastActivity;

import dagger.Component;

/**
 * Created by athila on 30/05/16.
 */

@ActivityScoped
@Component(dependencies = {ApplicationComponent.class}, modules = {ForecastPresenterModule.class})
public interface ForecastComponent {

    void inject(ForecastActivity forecastActivity);
}
