package com.athila.cleansample.interactor.usecase;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

/**
 * Created by athila on 07/06/16.
 */

public class UseCaseTest {

  private UseCase mUseCase;

  @Before
  public void setupUseCase() {
    // We will mock different behaviors fo buildUseCaseObservable method. So, for other methods, call teh real ones.
    mUseCase = Mockito.mock(UseCase.class, CALLS_REAL_METHODS);
  }

  @Test
  public void testExecuteOperationWithEmptyObservable() {
    List<Integer> values = new ArrayList<>();
    values.add(1);
    values.add(2);
    when(mUseCase.buildUseCaseObservable(Matchers.any())).thenReturn(Observable.empty());

    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
    // TestSubscriber is synchronous and does not work with transformer. Pass null.
    mUseCase.execute(testSubscriber, null);
    testSubscriber.assertNoValues();
    testSubscriber.assertCompleted();
  }

  @Test
  public void testExecuteOperationWithSequenceOfEvents() {
    List<Integer> values = new ArrayList<>();
    values.add(1);
    values.add(2);
    values.add(3);
    when(mUseCase.buildUseCaseObservable(Matchers.any())).thenReturn(Observable.from(values));

    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
    // TestSubscriber is synchronous and does not work with transformer. Pass null.
    mUseCase.execute(testSubscriber, null);
    testSubscriber.assertReceivedOnNext(values);
    testSubscriber.assertCompleted();
  }

  @Test
  public void testExecuteOperationWithError() {
    Exception mockException = new Exception("Mock error");
    when(mUseCase.buildUseCaseObservable(Matchers.any())).thenReturn(Observable.error(mockException));

    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
    // TestSubscriber is synchronous and does not work with transformer. Pass null.
    mUseCase.execute(testSubscriber, null);
    testSubscriber.assertError(mockException);
  }
}
