package com.athila.cleansample.data.repository.city;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.repository.RepositoryTest;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by athila on 07/06/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({PutResults.class, DeleteResults.class, PutResult.class})
public class CityRepositoryTest extends RepositoryTest {

    private CityRepository mCityRepository;

    @Before
    public void setupCityRepository() {
        setupStorIO();

        mCityRepository = new CityRepositoryImpl(mStorIOSQLite);
    }

    @Test
    public void testInsertCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(new City("City 1", -1,-2));
        testCities.add(new City("City 2", -12,-23));
        testCities.add(new City("City 3", -14,-24));
        testCities.add(new City("City 4", -156,-2456));

        mCityRepository.insertCities(testCities);

        verify(mStorIOSQLite).put();
        verify(mPreparedPutBuilder).objects(testCities);

        // TODO: if repository integrates more than one datasource, test this behavior here
    }

    @Test
    public void testDeleteCities() {
        List<City> testCities = new ArrayList<>();
        testCities.add(new City("City 1", -1,-2));
        testCities.add(new City("City 2", -12,-23));
        testCities.add(new City("City 3", -14,-24));
        testCities.add(new City("City 4", -156,-2456));

        mCityRepository.deleteCities(testCities);

        verify(mStorIOSQLite).delete();
        verify(mPreparedDeleteBuilder).objects(testCities);

        // TODO: if repository integrates more than one datasource, test this behavior here
    }

    @Test
    public void testUpateCity() {
        City testCity = new City("City 1", -1,-2);

        mCityRepository.updateCity(testCity);

        verify(mStorIOSQLite).put();
        verify(mPreparedPutBuilder).object(testCity);

        // TODO: if repository integrates more than one datasource, test this behavior here
    }

    @Test
    public void testGetCities() {
        mCityRepository.getCities();

        verify(mStorIOSQLite).get();
        verify(mPreparedGetBuilder).listOfObjects(City.class);

        // TODO: if repository integrates more than one datasource, test this behavior here
    }
}
