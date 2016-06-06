package com.athila.cleansample.di.component;

import com.athila.cleansample.di.ActivityScoped;
import com.athila.cleansample.di.module.presentation.CitiesListPresenterModule;
import com.athila.cleansample.presentation.citieslist.CitiesListActivity;

import dagger.Component;

/**
 * Created by athila on 30/05/16.
 */

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = {CitiesListPresenterModule.class})
public interface CitiesListComponent {

    void inject(CitiesListActivity citiesListActivity);
}
