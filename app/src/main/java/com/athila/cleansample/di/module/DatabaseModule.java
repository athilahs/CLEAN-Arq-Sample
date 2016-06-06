package com.athila.cleansample.di.module;

import android.database.sqlite.SQLiteOpenHelper;

import com.athila.cleansample.data.datasource.database.Database;
import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.model.CityStorIOSQLiteDeleteResolver;
import com.athila.cleansample.data.model.CityStorIOSQLiteGetResolver;
import com.athila.cleansample.data.model.CityStorIOSQLitePutResolver;
import com.athila.cleansample.infrastructure.MvpWeatherApp;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by athila on 31/05/16.
 */

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    SQLiteOpenHelper provideSQLiteOpenHelper(MvpWeatherApp context) {
        return new Database(context);
    }

    @Provides
    @Singleton
    StorIOSQLite provideStorio(SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(City.class, SQLiteTypeMapping.<City>builder()
                        .putResolver(new CityStorIOSQLitePutResolver()) // object that knows how to perform Put Operation (insert or update)
                        .getResolver(new CityStorIOSQLiteGetResolver()) // object that knows how to perform Get Operation
                        .deleteResolver(new CityStorIOSQLiteDeleteResolver())  // object that knows how to perform Delete Operation
                        .build()) // This instance of StorIOSQLite will know how to work with City objects
                .build();
    }
}
