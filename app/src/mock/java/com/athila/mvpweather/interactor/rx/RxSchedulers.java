package com.athila.mvpweather.interactor.rx;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by athila on 15/03/16.
 */
public class RxSchedulers {
    public static <T> Observable.Transformer<T, T> applyDefaultSchedulers() {
        // Mock operations should execute on current thread
        return applyImmediateSchedulers();
    }

    public static <T> Observable.Transformer<T, T> applyImmediateSchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.immediate())
                        .observeOn(Schedulers.immediate());
            }
        };
    }
}
