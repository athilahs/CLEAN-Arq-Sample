package com.athila.cleansample.data.datasources.api;

import com.athila.cleansample.di.api.component.DaggerMockRetrofitComponent;
import com.athila.cleansample.di.api.module.MockRetrofitModule;
import com.athila.cleansample.di.module.NetworkModule;

import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by athila on 15/06/16.
 */

public abstract class MockAPI<T> {
    // Subclasses must build the delegate
    protected BehaviorDelegate<T> mDelegate;

    private MockRetrofit mMockRetrofit;

    protected MockAPI() {
        mMockRetrofit = DaggerMockRetrofitComponent.builder()
                .networkModule(new NetworkModule())
                .mockRetrofitModule(new MockRetrofitModule())
                .build()
                .mockRetrofit();
    }

    protected void mockApi(Class<T> clazz) {
        mDelegate = mMockRetrofit.create(clazz);
    }

    public NetworkBehavior getNetworkBehavior() {
        return mMockRetrofit.networkBehavior();
    }
}
