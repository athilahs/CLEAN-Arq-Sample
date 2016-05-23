package com.athila.mvpweather.infrastructure.validator;

/**
 * Created by athila on 10/07/15.
 */
public interface Validator<T> {
    boolean isValid(T target);
}
