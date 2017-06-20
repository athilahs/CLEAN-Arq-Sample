package com.athila.cleansample.presentation.citieslist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.athila.cleansample.R;
import com.athila.cleansample.data.model.City;
import com.athila.cleansample.interactor.validator.CoordinateValidator;
import com.athila.cleansample.interactor.validator.EmptyValidator;
import com.athila.cleansample.interactor.validator.ValidationException;
import com.athila.cleansample.presentation.BaseFragment;
import com.athila.cleansample.presentation.customviews.ValidatableEditText;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CitiesListFragment extends BaseFragment
    implements CitiesListContract.View, CitiesListContract.CityManagementController, AbsListView.MultiChoiceModeListener {

  private static final int PLACE_PICKER_REQUEST_NEW_CITY = 1;
  private static final int PLACE_PICKER_REQUEST_EDIT_CITY = 2;

  @BindView(R.id.cities_list_content)
  View mContent;
  @BindView(R.id.cities_list_empty_view)
  View mEmptyView;
  @BindView(R.id.cities_list_screen_cities_list)
  ListView mCitiesList;

  Unbinder mUnbinder;

  private CitiesListAdapter mCitiesAdapter;

  private CitiesListContract.Presenter mPresenter;

  private CitiesListContract.PermissionChecker mPermissionChecker;

  private List<City> mSelectedCities;
  private MenuItem mEditMenuItem;

  private View mRootView;

  public CitiesListFragment() {
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (context instanceof CitiesListActivity) {
      ((CitiesListActivity) context).setCityManagementController(this);
    }

    if (context instanceof CitiesListContract.PermissionChecker) {
      mPermissionChecker = (CitiesListContract.PermissionChecker) context;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.fragment_cities_list, container, false);
    mUnbinder = ButterKnife.bind(this, mRootView);

    return mRootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    init();
    setListeners();
  }

  private void setListeners() {
    mCitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedCities.add(mCitiesAdapter.getItem(position));
        mPermissionChecker.openPermissionFlowIfNeeded();
      }
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mRootView = null;
    mPresenter.stop();

    mUnbinder.unbind();
  }

  private void init() {
    mCitiesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    mCitiesList.setMultiChoiceModeListener(this);

    List<City> emptyCities = new ArrayList<>();
    mCitiesAdapter = new CitiesListAdapter(getActivity(), emptyCities);
    mCitiesList.setAdapter(mCitiesAdapter);

    mSelectedCities = new ArrayList<>();

    mPresenter.start();
  }

  @Override
  public void setPresenter(CitiesListContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public void showCitiesList(List<City> citiesList) {
    if (mCitiesAdapter == null) {
      showEmptyView();
    } else {
      mContent.setVisibility(View.VISIBLE);
      mEmptyView.setVisibility(View.GONE);
      mCitiesAdapter.setCities(citiesList);
      mCitiesAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void showEmptyView() {
    // just in case...
    mCitiesAdapter.setCities(Collections.<City>emptyList());

    mContent.setVisibility(View.GONE);
    mEmptyView.setVisibility(View.VISIBLE);
  }

  @Override
  public void onCityEditionFinished(City updatedCity) {
    if (mRootView != null && isAdded()) {
      Snackbar.make(mRootView, R.string.message_city_edited, Snackbar.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  public void onCitiesDeleted(int citiesDeleted) {
    if (mRootView != null && isAdded()) {
      Snackbar.make(mRootView, getResources().getString(R.string.message_cities_deleted, String.valueOf(citiesDeleted)),
          Snackbar.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  public void onCityAdded(City createdCity) {
    Toast.makeText(getActivity(), "City added: " + createdCity.getName(), Toast.LENGTH_LONG)
        .show();
  }

  @Override
  public void openPlacePicker() {
    City editingCity = null;
    if (mSelectedCities != null && mSelectedCities.size() > 0) {
      // In edition mode, mSelectedCities is expected to have only one entry
      editingCity = mSelectedCities.get(0);
    }

    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    int requestCode = PLACE_PICKER_REQUEST_NEW_CITY;
    if (editingCity != null) {
      LatLng southwest = new LatLng(editingCity.getLatitude() - 0.0001, editingCity.getLongitude() - 0.0001);
      LatLng northeast = new LatLng(editingCity.getLatitude() + 0.0001, editingCity.getLongitude() + 0.0001);
      builder.setLatLngBounds(new LatLngBounds(southwest, northeast));
      requestCode = PLACE_PICKER_REQUEST_EDIT_CITY;
    }

    try {
      startActivityForResult(builder.build(getActivity()), requestCode);
    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
      if (mRootView != null) {
        // TODO: notify user?
        openCityInputDialog();
      }
    }
  }

  @Override
  public void openCityInputDialog() {
    City auxCity = null;
    if (mSelectedCities != null && mSelectedCities.size() > 0) {
      // In edition mode, mSelectedCities is expected to have only one entry
      auxCity = mSelectedCities.get(0);
      // Clear it right now to avoid future problems
      mSelectedCities.clear();
    }
    final City editingCity = auxCity;

    @SuppressLint("InflateParams")
    View dialogLayout = LayoutInflater.from(getActivity())
        .inflate(R.layout.dialog_add_city, null);
    final ValidatableEditText cityName =
        ((ValidatableEditText) dialogLayout.findViewById(R.id.dialog_addCity_editText_city_name));
    final ValidatableEditText cityLatitude =
        ((ValidatableEditText) dialogLayout.findViewById(R.id.dialog_addCity_editText_city_latitude));
    final ValidatableEditText cityLongitude =
        ((ValidatableEditText) dialogLayout.findViewById(R.id.dialog_addCity_editText_city_longitude));

    cityName.setValidator(new EmptyValidator());
    cityLatitude.setValidator(new CoordinateValidator());
    cityLongitude.setValidator(new CoordinateValidator());

    if (editingCity != null) {
      cityName.setText(editingCity.getName());
      cityLatitude.setText(String.valueOf(editingCity.getLatitude()));
      cityLongitude.setText(String.valueOf(editingCity.getLongitude()));
    }

    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(dialogLayout)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // Do nothing here because we override this button later to change the close behaviour.
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .create();

    dialog.show();
    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            try {
              cityName.validate();
              cityLatitude.validate();
              cityLongitude.validate();
            } catch (ValidationException e) {
              // do not proceed
              return;
            }
            if (editingCity == null) {
              // creating new city
              City newCity = new City(cityName.getText()
                  .toString(), Double.parseDouble(cityLatitude.getText()
                  .toString()), Double.parseDouble(cityLongitude.getText()
                  .toString()));

              mPresenter.addCity(newCity);
            } else {
              editingCity.setName(cityName.getText()
                  .toString());
              editingCity.setLatitude(Double.parseDouble(cityLatitude.getText()
                  .toString()));
              editingCity.setLongitude(Double.parseDouble(cityLongitude.getText()
                  .toString()));

              mPresenter.updateCity(editingCity);
            }

            dialog.dismiss();
          }
        });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case PLACE_PICKER_REQUEST_NEW_CITY:
        if (resultCode == Activity.RESULT_OK) {
          Place place = PlacePicker.getPlace(getActivity(), data);
          City newCity = new City(place.getName()
              .toString(), place.getLatLng().latitude, place.getLatLng().longitude);

          mPresenter.addCity(newCity);
        }
        break;

      case PLACE_PICKER_REQUEST_EDIT_CITY:
        if (resultCode == Activity.RESULT_OK) {
          Place place = PlacePicker.getPlace(getActivity(), data);
          City editingCity = mSelectedCities.get(0);
          if (editingCity == null) {
            handleBasicError(new Exception());
            break;
          }
          editingCity.setName(place.getName()
              .toString());
          editingCity.setLatitude(place.getLatLng().latitude);
          editingCity.setLongitude(place.getLatLng().longitude);

          mPresenter.updateCity(editingCity);
        }
        // reset the selected cities
        mSelectedCities.clear();
        break;
    }
  }

  // For contextual action mode
  @Override
  public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    MenuInflater inflater = mode.getMenuInflater();
    inflater.inflate(R.menu.menu_cities_list, menu);
    mEditMenuItem = menu.findItem(R.id.menu_cities_edit);
    return true;
  }

  @Override
  public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    return false;
  }

  @Override
  public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_cities_delete:
        mPresenter.deleteCities(mSelectedCities);
        mode.finish();
        return true;
      case R.id.menu_cities_edit:
        if (mSelectedCities != null && !mSelectedCities.isEmpty()) {
          mPermissionChecker.openPermissionFlowIfNeeded();
          return true;
        }
        mode.finish();
      default:
        return false;
    }
  }

  @Override
  public void onDestroyActionMode(ActionMode mode) {
    mSelectedCities.clear();

    getActivity().setTitle(R.string.title_activity_cities_list);
  }

  @Override
  public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
    if (checked) {
      mSelectedCities.add(mCitiesAdapter.getItem(position));
    } else {
      mSelectedCities.remove(mCitiesAdapter.getItem(position));
    }

    if (mSelectedCities.size() > 1) {
      mEditMenuItem.setVisible(false);
    } else {
      mEditMenuItem.setVisible(true);
    }

    if (isAdded()) {
      mode.setTitle(
          getResources().getQuantityString(R.plurals.cities_selected_count, mSelectedCities.size(), mSelectedCities.size()));
    }
  }
}
