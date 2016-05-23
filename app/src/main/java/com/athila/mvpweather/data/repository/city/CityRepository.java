package com.athila.mvpweather.data.repository.city;

import com.athila.mvpweather.data.model.City;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import java.util.List;

import rx.Observable;

/**
 * Created by athila on 19/03/16.
 */
public interface CityRepository {
    Observable<List<City>> getCities();
    Observable<PutResults<City>> insertCities(List<City> cities);
    Observable<PutResult> updateCity(City city);
    Observable<DeleteResults<City>> deleteCities(List<City> cities);
}
