package com.athila.cleansample.presentation.citieslist;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.infrastructure.CleanSampleLog;
import com.athila.cleansample.interactor.rx.DefaultSubscriber;
import com.athila.cleansample.interactor.usecase.city.AddCities;
import com.athila.cleansample.interactor.usecase.city.DeleteCities;
import com.athila.cleansample.interactor.usecase.city.GetCities;
import com.athila.cleansample.interactor.usecase.city.UpdateCity;
import com.athila.cleansample.presentation.BasePresenter;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by athila on 14/03/16.
 */
public class CitiesListPresenter extends BasePresenter implements CitiesListContract.Presenter {

  private CitiesListContract.View mView;
  private GetCities mGetCities;
  private UpdateCity mUpdateCity;
  private DeleteCities mDeleteCities;
  private AddCities mAddCities;

  @Inject
  CitiesListPresenter(CitiesListContract.View view, GetCities getCities, UpdateCity updateCity, DeleteCities deleteCities,
      AddCities addCities) {
    mView = view;
    mGetCities = getCities;
    mUpdateCity = updateCity;
    mDeleteCities = deleteCities;
    mAddCities = addCities;
  }

  @Override
  public void stop() {
    CleanSampleLog.debug("CitiesListPresenter::stop");
    mView = null;
    if (mGetCities != null) {
      mGetCities.unsubscribe();
      mGetCities = null;
    }
    if (mUpdateCity != null) {
      mUpdateCity.unsubscribe();
      mUpdateCity = null;
    }
    if (mDeleteCities != null) {
      mDeleteCities.unsubscribe();
      mDeleteCities = null;
    }
    if (mAddCities != null) {
      mAddCities.unsubscribe();
      mAddCities = null;
    }
  }

  @Inject
  @Override
  public void attachView() {
    mView.setPresenter(this);
  }

  @Override
  public void start() {
    getCities();
  }

  @Override
  public void getCities() {
    if (mView == null) {
      return;
    }

    if (mGetCities == null) {
      handleBasicError(mView, new Exception("GetCities usecase is null. Cannot execute requested operation."));
      return;
    }
    mGetCities.unsubscribe();

    mGetCities.execute(new GetCitiesSubscriber(), null);
  }

  @Override
  public void updateCity(final City updatedCity) {
    if (mView == null) {
      return;
    }

    if (mUpdateCity == null) {
      handleBasicError(mView, new Exception("UpdateCity usecase is null. Cannot execute requested operation."));
      return;
    }
    mUpdateCity.unsubscribe();
    mUpdateCity.execute(new UpdateCitySubscriber(updatedCity), UpdateCity.UpdateCityParams.forCity(updatedCity));
  }

  @Override
  public void deleteCities(List<City> citiesToDelete) {
    if (mView == null) {
      return;
    }

    if (mDeleteCities == null) {
      handleBasicError(mView, new Exception("DeleteCities usecase is null. Cannot execute requested operation."));
      return;
    }
    mDeleteCities.unsubscribe();
    mDeleteCities.execute(new DeleteCitiesSubscriber(), DeleteCities.DeleteCitiesParams.forCities(citiesToDelete));
  }

  @Override
  public void addCity(final City cityToAdd) {
    if (mView == null) {
      return;
    }

    if (mAddCities == null) {
      handleBasicError(mView, new Exception("AddCities usecase is null. Cannot execute requested operation."));
      return;
    }
    mAddCities.unsubscribe();

    List<City> city = new ArrayList<>();
    city.add(cityToAdd);
    mAddCities.execute(new AddCitySubscriber(cityToAdd), AddCities.AddCitiesParams.forCities(city));
  }

  @RxLogSubscriber
  private class GetCitiesSubscriber extends DefaultSubscriber<List<City>> {
    @Override
    public void onError(Throwable e) {
      CleanSampleLog.error("Error on CitiesListPresenter::getCities: ", e);
      if (mView != null) {
        handleBasicError(mView, e);
      }
    }

    @Override
    public void onNext(List<City> cities) {
      CleanSampleLog.debug("onNext called in CitiesListPresenter with " + cities.size() + " cities");
      if (mView != null) {
        if (cities.isEmpty()) {
          mView.showEmptyView();
        } else {
          mView.showCitiesList(cities);
        }
      }
    }

    @Override
    public void onCompleted() {
      super.onCompleted();
    }
  }

  private class UpdateCitySubscriber extends DefaultSubscriber<PutResult> {
    private City mUpdatedCity;

    UpdateCitySubscriber(City updatedCity) {
      mUpdatedCity = updatedCity;
    }

    @Override
    public void onError(Throwable e) {
      CleanSampleLog.error("Error on CitiesListPresenter::updateCity: ", e);
      if (mView != null) {
        handleBasicError(mView, e);
      }
    }

    @Override
    public void onNext(PutResult result) {
      CleanSampleLog.debug("onNext called on CitiesListPresenter::updateCity. city: " + mUpdatedCity.getName());
      if (mView != null && result.wasUpdated()) {
        mView.onCityEditionFinished(mUpdatedCity);
      }
    }
  }

  private class DeleteCitiesSubscriber extends DefaultSubscriber<DeleteResults<City>> {
    @Override
    public void onError(Throwable e) {
      CleanSampleLog.error("Error on CitiesListPresenter::deleteCities: ", e);
      if (mView != null) {
        handleBasicError(mView, e);
      }
    }

    @Override
    public void onNext(DeleteResults<City> result) {
      CleanSampleLog.debug("onNext called on CitiesListPresenter::deleteCities.");
      if (mView != null) {
        mView.onCitiesDeleted(result.results()
            .size());
      }
    }
  }

  private class AddCitySubscriber extends DefaultSubscriber<PutResults<City>> {
    private City mCityToAdd;

    AddCitySubscriber(City cityToAdd) {
      mCityToAdd = cityToAdd;
    }

    @Override
    public void onError(Throwable e) {
      CleanSampleLog.error("Error on CitiesListPresenter::addCity: ", e);
      if (mView != null) {
        handleBasicError(mView, e);
      }
    }

    @Override
    public void onNext(PutResults<City> result) {
      CleanSampleLog.debug("onNext called on CitiesListPresenter::addCity. city: " + mCityToAdd.getName());
      if (mView != null) {
        mView.onCityAdded(mCityToAdd);
      }
    }
  }
}
