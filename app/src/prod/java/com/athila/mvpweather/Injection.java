package com.athila.mvpweather;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.athila.mvpweather.data.datasource.database.Database;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.athila.mvpweather.data.repository.city.CityRepositoryImpl;
import com.athila.mvpweather.data.repository.forecast.ForecastRepository;
import com.athila.mvpweather.data.repository.forecast.ForecastRepositoryImpl;

/**
 * Created by athila on 15/03/16.
 */
public class Injection {
    public static ForecastRepository provideForecastRepository() {
        return new ForecastRepositoryImpl();
    }

    public static CityRepository provideCityRepository(Context context) {
        return new CityRepositoryImpl(context.getApplicationContext());
    }

    public static SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
        return new Database(context);
    }
}
