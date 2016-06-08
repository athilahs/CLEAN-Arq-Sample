package com.athila.cleansample.data.datasource.database;

import com.athila.cleansample.data.datasource.database.table.CitiesTable;
import com.athila.cleansample.data.model.City;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by athila on 08/06/16.
 */

public class CityDatabaseTest extends DatabaseTest {
    // setup is done by superclass

    @Test
    public void testInsertCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(new City("Mock 1", -123.45, 123.45));
        testCities.add(new City("Mock 2", -567.78, 567.78));
        testCities.add(new City("Mock 3", -890.12, 890.12));

        Observable<PutResults<City>> operationResultsObservable = mStorIOSQLite.put()
                .objects(testCities)
                .prepare()
                .asRxObservable();

        TestSubscriber<PutResults<City>> testSubscriber = new TestSubscriber<>();
        operationResultsObservable
                .toBlocking() // since we will query the database later for testing
                .subscribe(testSubscriber);

        List<PutResults<City>> operationResultsEvents = testSubscriber.getOnNextEvents();
        assertEquals(operationResultsEvents.size(), 1); // only one onNextEvent

        PutResults<City> operationResults = operationResultsEvents.get(0);
        assertEquals(operationResults.numberOfInserts(), testCities.size());

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();

        Map<City, PutResult> resultsMap = operationResults.results();

        for (City testCity : testCities) {
            boolean foundInResults = false;
            for (Map.Entry<City, PutResult> resultEntry : resultsMap.entrySet()) {
                City cityResult = resultEntry.getKey();
                PutResult putResult = resultEntry.getValue();

                if (cityResult.equals(testCity)) {
                    foundInResults = true;
                    if (!putResult.wasInserted()) {
                        fail(cityResult + " was not inserted.");
                    }
                }
            }
            if (!foundInResults) {
                fail(testCity + " was not found on insert results.");
            }
        }

        List<City> citiesFromDb = queryCitiesTableAsBlocking();

        int originalSize = testCities.size();
        testCities.retainAll(citiesFromDb);
        assertEquals(testCities.size(), originalSize);
    }

    @Test
    public void testUpdateCity() {
        String originalName = "Original City";
        City testCity = new City(originalName, -123, 1234.34);

        // insert a test city
        mStorIOSQLite.put()
                .object(testCity)
                .prepare()
                .executeAsBlocking();

        String updatedName = "Updated City";
        testCity.setName(updatedName);
        Observable<PutResult> updateObservable = mStorIOSQLite.put()
                .object(testCity)
                .prepare()
                .asRxObservable();

        TestSubscriber<PutResult> testUpdateSubscriber = new TestSubscriber<>();
        updateObservable
                .toBlocking() // block operation since we will query the database again to test the contents
                .subscribe(testUpdateSubscriber);

        List<PutResult> updateResults = testUpdateSubscriber.getOnNextEvents();
        assertEquals(updateResults.size(), 1);
        assertTrue(updateResults.get(0).wasUpdated());

        testUpdateSubscriber.unsubscribe();

        // check the "old" city does not exists anymore
        City old = mStorIOSQLite.get()
                .object(City.class)
                .withQuery(Query.builder()
                        .table(CitiesTable.TABLE_NAME)
                        .where(CitiesTable.NAME + "=?")
                        .whereArgs(originalName)
                        .build())
                .prepare()
                .executeAsBlocking();
        assertNull(old);

        // Check if the city was updated
        City cityFromDB = mStorIOSQLite.get()
                .object(City.class)
                .withQuery(Query.builder()
                        .table(CitiesTable.TABLE_NAME)
                        .where(CitiesTable.NAME + "=?")
                        .whereArgs(updatedName)
                        .build())
                .prepare()
                .executeAsBlocking();

        assertNotNull(cityFromDB);
        assertEquals(cityFromDB, testCity);
    }

    @Test
    public void testDeleteSingleCity() {
        // first insert some test cities
        List<City> testCities = new ArrayList<>();
        testCities.add(new City("Mock 1", -123.45, 123.45));
        testCities.add(new City("Mock 2", -567.78, 567.78));
        testCities.add(new City("Mock 3", -890.12, 890.12));
        testCities.add(new City("Mock 4", -1234, 863457645));
        testCities.add(new City("Mock 5", -1234, 342));
        mStorIOSQLite.put()
                .objects(testCities)
                .prepare()
                .executeAsBlocking();

        // then, query them and make sure they're at database
        List<City> citiesFromDb = queryCitiesTableAsBlocking();

        assertEquals(testCities.size(), citiesFromDb.size());
        int originalSize = testCities.size();
        // make sure all cities where inserted with success
        testCities.retainAll(citiesFromDb);
        assertEquals(testCities.size(), originalSize);

        // now, lets delete a single city
        City singleCity = testCities.get(0);
        Observable<DeleteResult> deleteResultObservable = mStorIOSQLite
                .delete()
                .object(singleCity)
                .prepare()
                .asRxObservable();
        TestSubscriber<DeleteResult> testSubscriber = new TestSubscriber<>();
        deleteResultObservable.toBlocking() // since we will query the db for testing
                .subscribe(testSubscriber);

        List<DeleteResult> deleteResultsEvents = testSubscriber.getOnNextEvents();
        assertEquals(deleteResultsEvents.size(), 1);
        DeleteResult deleteResult = deleteResultsEvents.get(0);
        assertEquals(deleteResult.numberOfRowsDeleted(), 1);

        testSubscriber.unsubscribe();

        // query db and make sure the entry was deleted
        citiesFromDb = queryCitiesTableAsBlocking();
        assertFalse(citiesFromDb.contains(singleCity));
    }

    // Get Cities was indirectly tested by the other tests

    private List<City> queryCitiesTableAsBlocking() {
        return mStorIOSQLite.get()
                .listOfObjects(City.class)
                .withQuery(Query.builder()
                        .table(CitiesTable.TABLE_NAME)
                        .build())
                .prepare()
                .executeAsBlocking();
    }
}
