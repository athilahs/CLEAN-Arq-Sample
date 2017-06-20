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

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.athila.cleansample.data.model.City;
import com.athila.cleansample.data.repository.city.CityRepository;
import com.athila.cleansample.interactor.usecase.UseCase;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import javax.inject.Inject;
import rx.Observable;

public class UpdateCity extends UseCase<PutResult, UpdateCity.UpdateCityParams> {

  private CityRepository mCityRepository;

  @Inject
  public UpdateCity(CityRepository cityRepository) {
    mCityRepository = cityRepository;
  }

  @Override
  public Observable buildUseCaseObservable(@NonNull UpdateCityParams params) {
    return mCityRepository.updateCity(params.city);
  }

  public static final class UpdateCityParams {
    private City city;

    private UpdateCityParams(City city) {
      this.city = city;
    }

    @VisibleForTesting
    public City getCity() {
      return city;
    }

    public static UpdateCityParams forCity(City city) {
      return new UpdateCityParams(city);
    }
  }
}
