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
package com.athila.mvpweather.interactor.usecase.city;

import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.repository.city.CityRepository;
import com.athila.mvpweather.interactor.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class AddCities extends UseCase {

    private CityRepository mCityRepository;
    // Clients must set this before executing the usecase
    private List<City> mCitiesToBeAdded;

    @Inject
    public AddCities(CityRepository cityRepository) {
        mCityRepository = cityRepository;
    }

    public void setCitiesToBeAdded(List<City> citiesToBeAdded) {
        mCitiesToBeAdded = citiesToBeAdded;
    }

    @Override
    public Observable buildUseCaseObservable() {
        return mCityRepository.insertCities(mCitiesToBeAdded);
    }
}
