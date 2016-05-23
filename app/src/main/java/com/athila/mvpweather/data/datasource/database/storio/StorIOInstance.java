package com.athila.mvpweather.data.datasource.database.storio;

import android.database.sqlite.SQLiteOpenHelper;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.model.CityStorIOSQLiteDeleteResolver;
import com.athila.mvpweather.data.model.CityStorIOSQLiteGetResolver;
import com.athila.mvpweather.data.model.CityStorIOSQLitePutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

/**
 * Created by athila on 17/03/16.
 */
public class StorIOInstance {

    private static StorIOSQLite sStorIOSQlite;

    public static StorIOSQLite get(SQLiteOpenHelper sqLiteOpenHelper) {
        if (sStorIOSQlite == null) {
            sStorIOSQlite = DefaultStorIOSQLite.builder()
                    .sqliteOpenHelper(sqLiteOpenHelper)
                    .addTypeMapping(City.class, SQLiteTypeMapping.<City>builder()
                            .putResolver(new CityStorIOSQLitePutResolver()) // object that knows how to perform Put Operation (insert or update)
                            .getResolver(new CityStorIOSQLiteGetResolver()) // object that knows how to perform Get Operation
                            .deleteResolver(new CityStorIOSQLiteDeleteResolver())  // object that knows how to perform Delete Operation
                            .build()) // This instance of StorIOSQLite will know how to work with City objects
                    .build();
        }

        return sStorIOSQlite;
    }
}
