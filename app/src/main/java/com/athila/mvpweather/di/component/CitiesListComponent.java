package com.athila.mvpweather.di.component;

import com.athila.mvpweather.di.FragmentScoped;
import com.athila.mvpweather.di.component.ApplicationComponent;
import com.athila.mvpweather.di.module.presentation.CitiesListPresenterModule;
import com.athila.mvpweather.presentation.citieslist.CitiesListFragment;

import dagger.Component;

/**
 * Created by athila on 30/05/16.
 */

@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = {CitiesListPresenterModule.class})
public interface CitiesListComponent /*extends CityRepositoryComponent*/ {

    void inject(CitiesListFragment citiesListFragment);
}
