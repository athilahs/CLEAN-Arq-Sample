package com.athila.cleansample.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by athila on 31/05/16.
 */

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MockApiNoConnectionError {
}
