/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.athila.cleansample.interactor.usecase.city;

import android.support.annotation.VisibleForTesting;
import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.repository.city.CityRepository;
import com.athila.cleansample.interactor.usecase.UseCase;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AddCities extends UseCase<PutResults<City>, AddCities.AddCitiesParams> {

  private CityRepository mCityRepository;

  @Inject
  public AddCities(CityRepository cityRepository) {
    mCityRepository = cityRepository;
  }

  @Override
  public Observable<PutResults<City>> buildUseCaseObservable(AddCitiesParams params) {
    return mCityRepository.insertCities(params.cities);
  }

  public static final class AddCitiesParams {
    private List<City> cities;

    private AddCitiesParams(List<City> cities) {
      this.cities = cities;
    }

    @VisibleForTesting
    public List<City> getCities() {
      return cities;
    }

    public static AddCitiesParams forCities(List<City> cities) {
      return new AddCitiesParams(cities);
    }
  }
}
