package com.athila.cleansample.data.repository.city;

import com.athila.cleansample.data.datasource.database.table.CitiesTable;
import com.athila.cleansample.data.model.City;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by athila on 19/03/16.
 */
public class CityRepositoryImpl implements CityRepository {

    private StorIOSQLite mStorIOSQLite;

    @Inject
    public CityRepositoryImpl(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public Observable<List<City>> getCities() {
        return mStorIOSQLite
                .get()
                .listOfObjects(City.class)
                .withQuery(Query.builder()
                        .table(CitiesTable.TABLE_NAME)
                        .orderBy(CitiesTable.NAME + " ASC")
                        .build())
                .prepare()
                .asRxObservable(); // so it will subscribe to future changes
    }

    @Override
    public Observable<PutResults<City>> insertCities(List<City> cities) {
        return mStorIOSQLite
                .put()
                .objects(cities)
                .prepare()
                .asRxObservable();
    }

    @Override
    public Observable<PutResult> updateCity(City city) {
        return mStorIOSQLite
                .put()
                .object(city)
                .prepare()
                .asRxObservable();
    }

    @Override
    public Observable<DeleteResults<City>> deleteCities(List<City> cities) {
        return mStorIOSQLite
                .delete()
                .objects(cities)
                .prepare()
                .asRxObservable();
    }
}
