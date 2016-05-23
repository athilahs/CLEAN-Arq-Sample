package com.athila.mvpweather;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.athila.mvpweather.data.datasource.database.Database;
import com.athila.mvpweather.data.repository.CityRepositoryMock;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.athila.mvpweather.data.repository.forecast.ForecastRepository;
import com.athila.mvpweather.data.repository.forecast.ForecastRepositoryImpl;

/**
 * Created by athila on 15/03/16.
 */
public class Injection {

    public static ForecastRepository provideForecastRepository() {
        // TODO: mock it for UI Tests
        return new ForecastRepositoryImpl();
    }

    public static CityRepository provideCityRepository(Context context) {
        // TODO: mock it for UI Tests
        return new CityRepositoryMock(null);
    }

    public static SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
        // TODO: wanna mock database creation / structure / pre-fill data for UI Test? Do it here
        return new Database(context);
    }
}
