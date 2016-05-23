package com.athila.mvpweather.infrastructure.rx;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by athila on 15/03/16.
 */
public class WWSchedulers {
    public static Scheduler getIoScheduler() {
        return Schedulers.io() ;
    }
    public static Scheduler getMainScheduler() {
        return AndroidSchedulers.mainThread() ;
    }
}
