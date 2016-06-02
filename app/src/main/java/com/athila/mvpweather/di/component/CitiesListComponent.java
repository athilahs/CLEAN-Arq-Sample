package com.athila.mvpweather.di.component;

import com.athila.mvpweather.di.ActivityScoped;
import com.athila.mvpweather.di.module.presentation.CitiesListPresenterModule;
import com.athila.mvpweather.presentation.citieslist.CitiesListActivity;

import dagger.Component;

/**
 * Created by athila on 30/05/16.
 */

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = {CitiesListPresenterModule.class})
public interface CitiesListComponent {

    void inject(CitiesListActivity citiesListActivity);
}
