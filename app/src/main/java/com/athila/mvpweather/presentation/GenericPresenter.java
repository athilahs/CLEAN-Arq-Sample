package com.athila.mvpweather.presentation;

/**
 * Created by athila on 14/03/16.
 */
public interface GenericPresenter<V> {
    void attachView(V view);
    void detachView();
}
