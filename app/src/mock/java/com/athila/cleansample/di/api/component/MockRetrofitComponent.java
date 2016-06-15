package com.athila.cleansample.di.api.component;

/**
 * Created by athila on 15/06/16.
 */

import com.athila.cleansample.di.api.module.MockRetrofitModule;
import com.athila.cleansample.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.mock.MockRetrofit;

/**
 * This module contains all mocks for 'getForecast' api call
 */
@Singleton
@Component(modules = {
        NetworkModule.class, // for Retrofit instances required by MockRetrofit
        MockRetrofitModule.class,
})
public interface MockRetrofitComponent {
    MockRetrofit mockRetrofit();
}
