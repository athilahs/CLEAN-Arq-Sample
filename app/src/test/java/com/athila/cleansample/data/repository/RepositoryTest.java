package com.athila.cleansample.data.repository;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.delete.PreparedDelete;
import com.pushtorefresh.storio.sqlite.operations.delete.PreparedDeleteCollectionOfObjects;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGet;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetListOfObjects;
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPut;
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPutCollectionOfObjects;
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPutObject;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.when;

/**
 * Created by athila on 07/06/16.
 */
@SuppressWarnings("unchecked")
public class RepositoryTest {

    @Mock
    protected StorIOSQLite mStorIOSQLite;

    // Put builder steps
    @Mock
    protected PreparedPut.Builder mPreparedPutBuilder;
    @Mock
    protected PreparedPut mPreparedPut;
    @Mock
    protected PreparedPutCollectionOfObjects.Builder mPreparedPutCollectionOfObjectsBuilder;
    @Mock
    protected PreparedPutCollectionOfObjects mPreparedPutCollectionOfObjects;
    @Mock
    protected PreparedPutObject.Builder mPreparedPutObjectBuilder;
    @Mock
    protected PreparedPutObject mPreparedPutObject;

    // Delete builder steps
    @Mock
    protected PreparedDelete.Builder mPreparedDeleteBuilder;
    @Mock
    protected PreparedDelete mPreparedDelete;
    @Mock
    protected PreparedDeleteCollectionOfObjects.Builder mPreparedDeleteCollectionOfObjectsBuilder;
    @Mock
    protected PreparedDeleteCollectionOfObjects mPreparedDeleteCollectionOfObjects;

    // Query builder steps
    @Mock
    protected PreparedGet.Builder mPreparedGetBuilder;
    @Mock
    protected PreparedGetListOfObjects.Builder mPreparedGetListOfObjectsBuilder;
    @Mock
    protected PreparedGetListOfObjects.CompleteBuilder mPreparedGetListOfObjectsCompleteBuilder;
    @Mock
    protected PreparedGetListOfObjects mPreparedGetListOfObjects;

    protected void setupStorIO() {
        MockitoAnnotations.initMocks(this);

        // Delete Operation config
        when(mStorIOSQLite.delete()).thenReturn(mPreparedDeleteBuilder);
        when(mPreparedDeleteBuilder.objects(anyCollection())).thenReturn(mPreparedDeleteCollectionOfObjectsBuilder);
        when(mPreparedDeleteCollectionOfObjectsBuilder.prepare()).thenReturn(mPreparedDeleteCollectionOfObjects);
        // Default mock. Subclasses should override this instruction on each test
        when(mPreparedDeleteCollectionOfObjects.executeAsBlocking()).thenReturn(PowerMockito.mock(DeleteResults.class));

        // Insert / Update Operations config
        when(mStorIOSQLite.put()).thenReturn(mPreparedPutBuilder);
        when(mPreparedPutBuilder.objects(anyCollection())).thenReturn(mPreparedPutCollectionOfObjectsBuilder);
        when(mPreparedPutBuilder.object(Matchers.any())).thenReturn(mPreparedPutObjectBuilder);
        when(mPreparedPutCollectionOfObjectsBuilder.prepare()).thenReturn(mPreparedPutCollectionOfObjects);
        when(mPreparedPutObjectBuilder.prepare()).thenReturn(mPreparedPutObject);
        // Default mocks. Subclasses should override these instructions on each test
        when(mPreparedPutCollectionOfObjects.executeAsBlocking()).thenReturn(PowerMockito.mock(PutResults.class));
        when(mPreparedPutObject.executeAsBlocking()).thenReturn(PowerMockito.mock(PutResult.class));

        // Query Operation config
        when(mStorIOSQLite.get()).thenReturn(mPreparedGetBuilder);
        when(mPreparedGetBuilder.listOfObjects(any(Class.class))).thenReturn(mPreparedGetListOfObjectsBuilder);
        when(mPreparedGetListOfObjectsBuilder.withQuery(any(Query.class))).thenReturn(mPreparedGetListOfObjectsCompleteBuilder);
        when(mPreparedGetListOfObjectsCompleteBuilder.prepare()).thenReturn(mPreparedGetListOfObjects);
        // Default mock. Subclasses should override this instruction on each test
        when(mPreparedGetListOfObjects.asRxObservable()).thenReturn(Observable.empty());
    }
}
