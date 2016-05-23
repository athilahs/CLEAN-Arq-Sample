package com.athila.mvpweather.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.athila.mvpweather.Injection;
import com.athila.mvpweather.data.datasource.database.storio.StorIOInstance;
import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import java.util.List;

import rx.Observable;

/**
 * Created by athila on 19/03/16.
 */
public class CityRepositoryMock implements CityRepository {

    private Context mContext;

    public CityRepositoryMock(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public Observable<List<City>> getCities() {
        // return your mock
        return null;
    }

    @Override
    public Observable<PutResults<City>> insertCities(List<City> cities) {
        return Observable.just(StorIOInstance.get(Injection.provideSQLiteOpenHelper(mContext))
                .put()
                .objects(cities)
                .prepare()
                .executeAsBlocking()); // this observable does not need to know about updates
    }

    @Override
    public Observable<PutResult> updateCity(City city) {
        return Observable.just(StorIOInstance.get(Injection.provideSQLiteOpenHelper(mContext))
                .put()
                .object(city)
                .prepare()
                .executeAsBlocking());
    }

    @Override
    public Observable<DeleteResults<City>> deleteCities(List<City> cities) {
        return Observable.just(StorIOInstance.get(Injection.provideSQLiteOpenHelper(mContext))
                .delete()
                .objects(cities)
                .prepare()
                .executeAsBlocking());
    }
}
