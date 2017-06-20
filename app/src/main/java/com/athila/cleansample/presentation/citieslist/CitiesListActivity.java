package com.athila.cleansample.presentation.citieslist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.athila.cleansample.R;
import com.athila.cleansample.di.component.CitiesListComponent;
import com.athila.cleansample.di.component.DaggerCitiesListComponent;
import com.athila.cleansample.di.module.presentation.CitiesListPresenterModule;
import com.athila.cleansample.infrastructure.CleanSampleApp;
import com.athila.cleansample.presentation.BaseActivity;
import javax.inject.Inject;

public class CitiesListActivity extends BaseActivity implements CitiesListContract.PermissionChecker {

  private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

  private CitiesListContract.CityManagementController mCityManagementController;

  @BindView(R.id.cities_list_fab)
  FloatingActionButton mFab;

  @BindView(R.id.cities_list_screen_parent_layout)
  View mParentLayout;

  private Unbinder mUnbinder;

  private CitiesListComponent mCitiesListComponent;

  @Inject
  CitiesListPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cities_list);

    mUnbinder = ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //noinspection ConstantConditions
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setTitle(R.string.title_activity_cities_list);
    setListeners();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    mUnbinder.unbind();
    mCitiesListComponent = null;
  }

  @Override
  public void onAttachFragment(Fragment fragment) {
    if (fragment instanceof CitiesListContract.View) {
      initializeInjector((CitiesListContract.View) fragment);
    }
  }

  private void initializeInjector(CitiesListContract.View citiesListView) {
    // initialize injector
    mCitiesListComponent = DaggerCitiesListComponent.builder()
        .applicationComponent(((CleanSampleApp) (getApplication())).getApplicationComponent())
        .citiesListPresenterModule(new CitiesListPresenterModule(citiesListView))
        .build();
    mCitiesListComponent.inject(this);
  }

  void setCityManagementController(CitiesListContract.CityManagementController cityManagementController) {
    mCityManagementController = cityManagementController;
  }

  private void setListeners() {
    mFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openPermissionFlowIfNeeded();
      }
    });
  }

  @Override
  public void openPermissionFlowIfNeeded() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

        // Explain the user why we need the permission
        Snackbar explanation = Snackbar.make(mParentLayout, R.string.permission_explanation, Snackbar.LENGTH_INDEFINITE);
        explanation.setAction(R.string.ok, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ActivityCompat.requestPermissions(CitiesListActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                PERMISSIONS_REQUEST_FINE_LOCATION);
          }
        });
        explanation.show();
      } else {
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
            PERMISSIONS_REQUEST_FINE_LOCATION);
      }
    } else {
      if (mCityManagementController != null) {
        mCityManagementController.openPlacePicker();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    switch (requestCode) {
      case PERMISSIONS_REQUEST_FINE_LOCATION:
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          if (mCityManagementController != null) {
            mCityManagementController.openPlacePicker();
          }
        } else {

          // permission denied, open an ugly City picker
          if (mCityManagementController != null) {
            mCityManagementController.openCityInputDialog();
          }
        }
    }
  }
}
