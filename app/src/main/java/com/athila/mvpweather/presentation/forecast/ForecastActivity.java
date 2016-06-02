package com.athila.mvpweather.presentation.forecast;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.athila.mvpweather.R;
import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.di.component.DaggerForecastComponent;
import com.athila.mvpweather.di.component.ForecastComponent;
import com.athila.mvpweather.di.module.presentation.ForecastPresenterModule;
import com.athila.mvpweather.infrastructure.MvpWeatherApp;
import com.athila.mvpweather.presentation.citieslist.CitiesListActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForecastActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ForecastContract.OnProgressRequestListener,
        ForecastContract.OnCitiesLoadedListener {

    @Bind(R.id.forecast_screen_app_bar_spinner_cities)
    Spinner mCitySpinner;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.forecast_screen_app_bar_button_refresh)
    ImageView mRefreshButton;

    @Bind(R.id.forecast_screen_app_bar_progress)
    ContentLoadingProgressBar mProgressBar;

    private CitiesSpinnerAdapter mCitiesAdapter;
    private ForecastContract.OnCitySelectedListener mOnCitySelectedListener;

    private ForecastComponent mForecastComponent;

    @Inject
    ForecastPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        mForecastComponent = null;

        
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ForecastContract.View) {
            initializeInjector((ForecastContract.View)fragment);
        }
    }

    private void initializeInjector(ForecastContract.View forecastView) {
        // initialize injector
        mForecastComponent = DaggerForecastComponent.builder()
                .applicationComponent(((MvpWeatherApp)(getApplication())).getApplicationComponent())
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
            public void onNothingSelected(AdapterView<?> arg0) {}
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
