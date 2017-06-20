package com.athila.cleansample.presentation.forecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.athila.cleansample.R;
import com.athila.cleansample.data.model.City;
import com.athila.cleansample.di.component.DaggerForecastComponent;
import com.athila.cleansample.di.component.ForecastComponent;
import com.athila.cleansample.di.module.presentation.ForecastPresenterModule;
import com.athila.cleansample.infrastructure.CleanSampleApp;
import com.athila.cleansample.presentation.BaseActivity;
import com.athila.cleansample.presentation.citieslist.CitiesListActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ForecastActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener, ForecastContract.OnProgressRequestListener,
    ForecastContract.OnCitiesLoadedListener {

  @BindView(R.id.forecast_screen_app_bar_spinner_cities)
  Spinner mCitySpinner;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.forecast_screen_app_bar_button_refresh)
  ImageView mRefreshButton;

  @BindView(R.id.forecast_screen_app_bar_progress)
  ContentLoadingProgressBar mProgressBar;

  private Unbinder mUnbinder;

  private CitiesSpinnerAdapter mCitiesAdapter;
  private ForecastContract.OnCitySelectedListener mOnCitySelectedListener;

  private ForecastComponent mForecastComponent;

  @Inject
  ForecastPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forecast);
    mUnbinder = ButterKnife.bind(this);

    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
      //noinspection ConstantConditions
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    setListeners();
    init();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    mUnbinder.unbind();
    mForecastComponent = null;
  }

  @Override
  public void onAttachFragment(Fragment fragment) {
    if (fragment instanceof ForecastContract.View) {
      initializeInjector((ForecastContract.View) fragment);
    }
  }

  private void initializeInjector(ForecastContract.View forecastView) {
    // initialize injector
    mForecastComponent = DaggerForecastComponent.builder()
        .applicationComponent(((CleanSampleApp) (getApplication())).getApplicationComponent())
        .forecastPresenterModule(new ForecastPresenterModule(forecastView))
        .build();
    mForecastComponent.inject(this);
  }

  private void setListeners() {
    mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
        City selectedCity = (City) adapter.getItemAtPosition(position);
        if (mOnCitySelectedListener != null) {
          mOnCitySelectedListener.onCitySelected(selectedCity);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0) {
      }
    });

    mRefreshButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mOnCitySelectedListener.onCitySelected((City) mCitySpinner.getSelectedItem());
      }
    });
  }

  private void init() {
    // just for initialization
    List<City> emptyCities = new ArrayList<>();
    mCitiesAdapter = new CitiesSpinnerAdapter(this, emptyCities);
    mCitySpinner.setAdapter(mCitiesAdapter);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_cities) {
      startActivity(new Intent(this, CitiesListActivity.class));
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onCitiesLoaded(List<City> cities) {
    mCitiesAdapter.setCities(cities);
    mCitiesAdapter.notifyDataSetChanged();
  }

  @Override
  public void showProgress() {
    mRefreshButton.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress() {
    mProgressBar.setVisibility(View.GONE);
    mRefreshButton.setVisibility(View.VISIBLE);
  }

  void setOnCitySelectedListener(ForecastContract.OnCitySelectedListener onCitySelectedListener) {
    mOnCitySelectedListener = onCitySelectedListener;
  }
}
