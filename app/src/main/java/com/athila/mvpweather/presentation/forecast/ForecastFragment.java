package com.athila.mvpweather.presentation.forecast;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.athila.mvpweather.R;
import com.athila.mvpweather.data.model.City;
import com.athila.mvpweather.data.model.DataPoint;
import com.athila.mvpweather.data.model.Forecast;
import com.athila.mvpweather.presentation.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.adapter.rxjava.HttpException;

public class ForecastFragment extends BaseFragment implements ForecastContract.View,
        ForecastContract.OnCitySelectedListener {

    // UI references.
    @Bind(R.id.weather_screen_content)
    View mScreenContent;
    @Bind(R.id.weather_screen_empty_view)
    View mEmptyView;
    @Bind(R.id.weather_screen_recyclerView_forecast)
    RecyclerView mForecastList;
    @Bind(R.id.weather_screen_textView_current_conditions_summary)
    TextView mCurrentConditionsSummary;
    @Bind(R.id.weather_screen_imageView_current_conditions_icon)
    ImageView mCurrentConditionsIcon;
    @Bind(R.id.weather_screen_textView_current_conditions_temperature)
    TextView mCurrentConditionsTemperature;

    private ForecastAdapter mForecastAdapter;

    private ForecastContract.Presenter mPresenter;

    private ForecastContract.OnCitiesLoadedListener mOnCitiesLoadedListener;
    private ForecastContract.OnProgressRequestListener mOnProgressRequestListener;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // test against a concrete class. Put the setOnCitySelectedListener method on an interface? Overkill?
        if (context instanceof ForecastActivity) {
            ((ForecastActivity)context).setOnCitySelectedListener(this);
        }

        if (context instanceof ForecastContract.OnCitiesLoadedListener) {
            mOnCitiesLoadedListener = (ForecastContract.OnCitiesLoadedListener) context;
        }

        if (context instanceof ForecastContract.OnProgressRequestListener) {
            mOnProgressRequestListener = (ForecastContract.OnProgressRequestListener) context;
        }
        // No exception thrown. Remember to check against null before notify any listener
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // put this call in onActivityCreated since the callback for the getCities operation is handled by activity
        mPresenter.getCities();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, root);

        init();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        mPresenter.stop();
    }


    private void init() {
        mForecastList.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter(getActivity());
        mForecastList.setAdapter(mForecastAdapter);
        mForecastList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setPresenter(@NonNull ForecastContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgress() {
        if (mOnProgressRequestListener != null) {
            mOnProgressRequestListener.showProgress();
        }
    }

    @Override
    public void hideProgress() {
        if (mOnProgressRequestListener != null) {
            mOnProgressRequestListener.hideProgress();
        }
    }

    @Override
    public void handleForecastError(HttpException error) {
        View fragmentView = getView();
        if (fragmentView != null) {
            Snackbar errorMessage = Snackbar.make(fragmentView, R.string.error_generic_forecast, Snackbar.LENGTH_LONG);
            switch (error.code()) {
                // TODO: what are the API errors? Remember: generic errors (like no connection) are already handled at base class
                case -1:
                    errorMessage.setText("Some error message");
            }
            errorMessage.show();
        }
    }

    @Override
    public void setForecast(@NonNull Forecast forecast) {
        if (isAdded()) {
            mEmptyView.setVisibility(View.GONE);
            mScreenContent.setVisibility(View.VISIBLE);
            DataPoint currentConditions = forecast.getCurrentConditions();
            mCurrentConditionsIcon.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(), ForecastUtils.getWeatherIcon(currentConditions.getIcon())));
            mCurrentConditionsSummary.setText(currentConditions.getSummary());
            mCurrentConditionsTemperature.setText(getString(R.string.temperature, String.valueOf(currentConditions.getTemperature())));

            mForecastAdapter.setForecast(forecast);
            mForecastAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mScreenContent.setVisibility(View.GONE);
    }

    @Override
    public void onCitySelected(City selectedCity) {
        mPresenter.getForecast(selectedCity);
    }

    @Override
    public void onCitiesLoaded(List<City> cities) {
        if (mOnCitiesLoadedListener != null) {
            mOnCitiesLoadedListener.onCitiesLoaded(cities);
        }
    }

    @Override
    public void handleGetCitiesError() {
        if (isAdded()) {
            Activity activity = getActivity();
            Toast.makeText(activity, R.string.error_generic, Toast.LENGTH_LONG).show();
            // without the cities, we cannot continue
            activity.finish();
        }
    }

    @Override
    public void handleGenericErrors(Throwable error) {
        // from superclass
        handleBasicErrors(error);
    }
}
