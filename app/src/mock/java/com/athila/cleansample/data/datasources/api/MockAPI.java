package com.athila.cleansample.data.datasources.api;

import retrofit2.mock.BehaviorDelegate;

/**
 * Created by athila on 15/06/16.
 */

public abstract class MockAPI<T> {
    // Subclasses must build the delegate
    protected BehaviorDelegate<T> mDelegate;

    // Expose delegate, so tests are able to customize network behavior
    public BehaviorDelegate<T> getDelegate() {
        return mDelegate;
    }
}
