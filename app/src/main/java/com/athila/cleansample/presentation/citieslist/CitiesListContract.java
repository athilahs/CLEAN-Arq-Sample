package com.athila.cleansample.presentation.citieslist;

import com.athila.cleansample.data.model.City;
import com.athila.cleansample.presentation.BasePresenterContract;
import com.athila.cleansample.presentation.BaseViewContract;

import java.util.List;

/**
 * Created by athila on 05/03/16.
 */
public interface CitiesListContract {
    interface View extends BaseViewContract<CitiesListContract.Presenter> {
        // No specific error handling. DB operations errors could be handled as generic errors
        void showCitiesList(List<City> citiesList);
        void showEmptyView();
        void onCityEditionFinished(City updatedCity);
        void onCitiesDeleted(int citiesDeleted);
        void onCityAdded(City createdCity);

        // Need loading? Since this view will show only data from local storage (at least for now),
        // we won't put loading in the contract
    }

    // Add button is on Activity but is handled by fragment
    interface CityManagementController {
        void openPlacePicker();
        void openCityInputDialog();
    }

    interface PermissionChecker {
        void openPermissionFlowIfNeeded();
    }

    interface Presenter extends BasePresenterContract {
        void getCities();
        void updateCity(City updatedCity);
        void deleteCities(List<City> citiesToDelete);
        void addCity(City cityToAdd);
    }
}
