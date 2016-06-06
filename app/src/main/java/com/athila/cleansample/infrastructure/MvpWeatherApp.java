package com.athila.cleansample.infrastructure;

import android.app.Application;

import com.athila.cleansample.di.component.ApplicationComponent;
import com.athila.cleansample.di.component.DaggerApplicationComponent;
import com.athila.cleansample.di.module.ApplicationModule;
import com.athila.cleansample.di.module.DatabaseModule;
import com.athila.cleansample.di.module.NetworkModule;

/**
 * Created by athila on 30/05/16.
 */

public class MvpWeatherApp extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule())
                .networkModule(new NetworkModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
