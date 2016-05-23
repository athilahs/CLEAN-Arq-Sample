package com.athila.mvpweather.infrastructure.rx;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by athila on 15/03/16.
 */
public class WWSchedulers {
    public static Scheduler getIoScheduler() {
        return Schedulers.immediate() ;
    }

    public static Scheduler getMainScheduler() {
        return Schedulers.immediate() ;
    }
}
