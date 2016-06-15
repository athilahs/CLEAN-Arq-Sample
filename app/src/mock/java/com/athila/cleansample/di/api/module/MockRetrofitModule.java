package com.athila.cleansample.di.api.module;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by athila on 15/06/16.
 */

@Module
public class MockRetrofitModule {

    @Provides
    NetworkBehavior provideMockNetworkBehavior() {
        // TODO: you can customize this through its public methods
        return NetworkBehavior.create();
    }

    @Provides
    MockRetrofit providesMockRetrofit(NetworkBehavior networkBehavior, Retrofit retrofit) {
        return new MockRetrofit.Builder(retrofit)
                .networkBehavior(networkBehavior)
                .build();
    }
}
