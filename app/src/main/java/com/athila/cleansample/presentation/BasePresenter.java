package com.athila.cleansample.presentation;

import android.support.annotation.VisibleForTesting;

/**
 * Created by athila on 14/03/16.
 */
public abstract class BasePresenter {

  @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
  public boolean handleBasicError(BaseViewContract view, Throwable error) {
    return view != null && view instanceof BasicErrorsHandlerView && ((BasicErrorsHandlerView) view).handleBasicError(error);
  }
}
