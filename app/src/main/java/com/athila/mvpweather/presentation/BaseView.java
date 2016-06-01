package com.athila.mvpweather.presentation;

/**
 * Created by athila on 17/03/16.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
