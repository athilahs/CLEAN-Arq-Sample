package com.athila.cleansample.di.component;

import com.athila.cleansample.di.ActivityScoped;
import com.athila.cleansample.di.module.presentation.ForecastPresenterModule;
import com.athila.cleansample.presentation.forecast.ForecastActivity;

import dagger.Component;

/**
 * Created by athila on 30/05/16.
 */

@ActivityScoped
@Component(dependencies = {ApplicationComponent.class}, modules = {ForecastPresenterModule.class})
public interface ForecastComponent {

    void inject(ForecastActivity forecastActivity);
}
